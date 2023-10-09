package fr.codecake.chatgptchallenge.flow.message.service;

import fr.codecake.chatgptchallenge.domain.enumeration.Owner;
import fr.codecake.chatgptchallenge.flow.message.dto.FlowMessageQueryDTO;
import fr.codecake.chatgptchallenge.flow.message.dto.FlowMessageResponseDTO;
import fr.codecake.chatgptchallenge.flow.message.dto.gpt.enums.GPTRole;
import fr.codecake.chatgptchallenge.flow.message.dto.gpt.response.GPTChatCompResponseDTO;
import fr.codecake.chatgptchallenge.flow.message.dto.gpt.response.GPTChoiceResponseDTO;
import fr.codecake.chatgptchallenge.flow.message.exception.OpenAIException;
import fr.codecake.chatgptchallenge.service.ConversationService;
import fr.codecake.chatgptchallenge.service.MessageService;
import fr.codecake.chatgptchallenge.service.dto.ConversationDTO;
import fr.codecake.chatgptchallenge.service.dto.MessageDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class FlowMessageService {

    private final GPTService gptService;
    private final ConversationService conversationService;

    public FlowMessageService(GPTService gptService,
                              ConversationService conversationService) {
        this.gptService = gptService;
        this.conversationService = conversationService;
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public FlowMessageResponseDTO sendMessage(FlowMessageQueryDTO flowMessageQueryDTO) throws OpenAIException {
        FlowMessageResponseDTO flowMessageResponseDTO = new FlowMessageResponseDTO();
        if (flowMessageQueryDTO.getNewConversation()) {
            ConversationDTO unsavedConversationDTO = new ConversationDTO();
            unsavedConversationDTO.setName("Conversation-1");
            final ConversationDTO savedConversationDTO = conversationService.save(unsavedConversationDTO);

            MessageDTO messageFromUserDTO = new MessageDTO();
            messageFromUserDTO.setOwner(Owner.USER);
            messageFromUserDTO.setContent(flowMessageQueryDTO.getContent());
            messageFromUserDTO.setConversation(savedConversationDTO);

            GPTChatCompResponseDTO gptChatCompResponseDTO = gptService.sendMessage(flowMessageQueryDTO.getContent())
                .orElseThrow(() ->
                    new OpenAIException(format("Something went wrong when calling the GPT API" +
                        " for the conversation %s and the message %s", savedConversationDTO.getId(), flowMessageQueryDTO)));
            List<MessageDTO> newMessagesDTO = gptChatCompResponseDTO.getChoices()
                .stream()
                .map(mapChoiceToMessageDTO(savedConversationDTO))
                .collect(Collectors.toList());

            newMessagesDTO.add(messageFromUserDTO);

            savedConversationDTO.setMessages(newMessagesDTO);

            ConversationDTO saveConversation = conversationService.saveWithMessages(savedConversationDTO);

            flowMessageResponseDTO.setContent(newMessagesDTO.stream().findFirst().get().getContent());
            flowMessageResponseDTO.setConversationPublicId(saveConversation.getId());
        } else {
            Optional<ConversationDTO> conversationPresent = conversationService.findOne(flowMessageQueryDTO.getConversationPublicId());
            // updateConversation()
        }
        return flowMessageResponseDTO;
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
        if (role.equals(GPTRole.USER.name())) {
            return Owner.USER;
        } else if (role.equals(GPTRole.SYSTEM.name()) ||
            role.equals(GPTRole.ASSISTANT.name())) {
            return Owner.GPT;
        } else {
            return null;
        }
    }
}
