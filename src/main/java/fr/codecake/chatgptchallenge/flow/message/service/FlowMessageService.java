package fr.codecake.chatgptchallenge.flow.message.service;

import fr.codecake.chatgptchallenge.domain.enumeration.Owner;
import fr.codecake.chatgptchallenge.flow.message.service.dto.FlowConversationDTO;
import fr.codecake.chatgptchallenge.flow.message.service.dto.FlowMessageQueryDTO;
import fr.codecake.chatgptchallenge.flow.message.service.dto.FlowMessageResponseDTO;
import fr.codecake.chatgptchallenge.domain.enumeration.GPTRole;
import fr.codecake.chatgptchallenge.flow.message.service.dto.gpt.response.GPTChatCompResponseDTO;
import fr.codecake.chatgptchallenge.flow.message.service.dto.gpt.response.GPTChoiceResponseDTO;
import fr.codecake.chatgptchallenge.flow.message.service.exception.ConversationNotExistException;
import fr.codecake.chatgptchallenge.flow.message.service.exception.OpenAIException;
import fr.codecake.chatgptchallenge.service.ConnectedUserService;
import fr.codecake.chatgptchallenge.service.ConversationService;
import fr.codecake.chatgptchallenge.service.MessageService;
import fr.codecake.chatgptchallenge.service.dto.ConversationDTO;
import fr.codecake.chatgptchallenge.service.dto.MessageDTO;
import fr.codecake.chatgptchallenge.service.dto.ProfileDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static java.lang.String.format;

@Service
public class FlowMessageService {

    private final GPTService gptService;
    private final ConversationService conversationService;
    private final ConnectedUserService connectedUserService;
    private final MessageService messageService;

    public FlowMessageService(GPTService gptService,
                              ConversationService conversationService,
                              ConnectedUserService connectedUserService,
                              MessageService messageService) {
        this.gptService = gptService;
        this.conversationService = conversationService;
        this.connectedUserService = connectedUserService;
        this.messageService = messageService;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public FlowMessageResponseDTO sendMessage(FlowMessageQueryDTO flowMessageQueryDTO) throws OpenAIException, ConversationNotExistException {
        ProfileDTO profileConnectedUser = connectedUserService.getProfileConnectedUser();

        FlowMessageResponseDTO flowMessageResponseDTO = new FlowMessageResponseDTO();
        if (flowMessageQueryDTO.getNewConversation()) {
            handleNewMessage(flowMessageQueryDTO, flowMessageResponseDTO, new ConversationDTO(), profileConnectedUser);
        } else {
            ConversationDTO existingConversation =
                conversationService.findOneByPublicIdAndProfileId(
                        flowMessageQueryDTO.getConversationPublicId(), profileConnectedUser.getId())
                    .orElseThrow(() -> new ConversationNotExistException(format("Conversation with the following public id and profile id doesn't exist %s, %s ",
                        flowMessageQueryDTO.getConversationPublicId(), profileConnectedUser.getId())));

            handleNewMessage(flowMessageQueryDTO, flowMessageResponseDTO, existingConversation, profileConnectedUser);
        }
        return flowMessageResponseDTO;
    }

    private void handleNewMessage(FlowMessageQueryDTO flowMessageQueryDTO,
                                  FlowMessageResponseDTO flowMessageResponseDTO,
                                  ConversationDTO conversationDTO, ProfileDTO profileDTO) {
        if (conversationDTO.getId() == null) {
            conversationDTO.setName(UUID.randomUUID().toString().substring(0, 6));
            conversationDTO.setProfile(profileDTO);
            conversationDTO = conversationService.save(conversationDTO);
        }

        MessageDTO messageFromUser = createNewMessageForDB(flowMessageQueryDTO.getContent(),
            conversationDTO, Owner.USER);

        final Long conversationId = conversationDTO.getId();

        List<MessageDTO> allPreviousMessages = messageService.getAllMessagesByConversationId(conversationId);

        GPTChatCompResponseDTO gptChatCompResponseDTO = gptService.sendMessage(flowMessageQueryDTO.getContent(), allPreviousMessages)
            .orElseThrow(() ->
                new OpenAIException(format("Something went wrong when calling the GPT API" +
                    " for the conversation %s and the message %s", conversationId, flowMessageQueryDTO)));

        MessageDTO newMessagesFromGPT = gptChatCompResponseDTO.getChoices()
            .stream()
            .map(mapChoiceToMessageDTO(conversationDTO))
            .findFirst().orElseThrow(() -> new OpenAIException(format("No message present in the DTO from GPT" +
                " for the conversation %s and the message %s", conversationId, flowMessageQueryDTO)));

        conversationDTO.getMessages().add(messageFromUser);
        conversationDTO.getMessages().add(newMessagesFromGPT);

        conversationDTO = conversationService.saveWithMessages(conversationDTO);

        flowMessageResponseDTO.setContent(newMessagesFromGPT.getContent());
        if(flowMessageQueryDTO.getNewConversation()) {
            FlowConversationDTO flowConversationDTO = new FlowConversationDTO();
            flowConversationDTO.setName(conversationDTO.getName());
            flowConversationDTO.setPublicId(conversationDTO.getPublicId());
            flowConversationDTO.setCreatedDate(conversationDTO.getCreatedDate());
            flowMessageResponseDTO.setConversation(flowConversationDTO);
        } else {
            flowMessageResponseDTO.setConversationPublicId(conversationDTO.getPublicId());
        }
    }

    private static MessageDTO createNewMessageForDB(String content,
                                                    ConversationDTO savedConversationDTO, Owner owner) {
        MessageDTO newMessage = new MessageDTO();
        newMessage.setOwner(owner);
        newMessage.setContent(content);
        newMessage.setConversation(savedConversationDTO);
        return newMessage;
    }

    private Function<GPTChoiceResponseDTO, MessageDTO> mapChoiceToMessageDTO(ConversationDTO conversationDTO) {
        return choice -> {
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setContent(choice.getMessage().getContent());
            Owner owner = mapGPTRoleToOwner(choice.getMessage().getRole());
            messageDTO.setConversation(conversationDTO);
            messageDTO.setOwner(owner);
            return messageDTO;
        };
    }

    private Owner mapGPTRoleToOwner(String role) {
        if (role.equals(GPTRole.USER.getValue())) {
            return Owner.USER;
        } else if (role.equals(GPTRole.SYSTEM.getValue()) ||
            role.equals(GPTRole.ASSISTANT.getValue())) {
            return Owner.GPT;
        } else {
            return null;
        }
    }
}
