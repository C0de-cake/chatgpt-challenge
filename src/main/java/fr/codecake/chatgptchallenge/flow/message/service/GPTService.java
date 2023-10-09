package fr.codecake.chatgptchallenge.flow.message.service;


import fr.codecake.chatgptchallenge.flow.message.service.dto.gpt.enums.GPTModel;
import fr.codecake.chatgptchallenge.domain.enumeration.GPTRole;
import fr.codecake.chatgptchallenge.flow.message.service.dto.gpt.query.GPTConversationQueryDTO;
import fr.codecake.chatgptchallenge.flow.message.service.dto.gpt.query.GPTMessageQueryDTO;
import fr.codecake.chatgptchallenge.flow.message.service.dto.gpt.response.GPTChatCompResponseDTO;
import fr.codecake.chatgptchallenge.flow.message.service.exception.OpenAIException;
import fr.codecake.chatgptchallenge.service.dto.MessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;

@Service
public class GPTService {

    private final Logger log = LoggerFactory.getLogger(GPTService.class);

    @Value("${application.openai.url}")
    private String baseUrl;

    @Value("${application.openai.key}")
    private String openAIKey;

    private final RestTemplate restTemplate;

    public GPTService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<GPTChatCompResponseDTO> sendMessage(String content, List<MessageDTO> messages) {
        List<GPTMessageQueryDTO> messagesToSend = new ArrayList<>();
        GPTMessageQueryDTO messageFromSystem = new GPTMessageQueryDTO();
        messageFromSystem.setContent("you are a helpful assistant, you should answer with Markdown format");
        messageFromSystem.setRole(GPTRole.SYSTEM.getValue());

        List<GPTMessageQueryDTO> collect = messages.stream().map(messageFromDB -> {
            GPTMessageQueryDTO messageToGPT = new GPTMessageQueryDTO();
            messageToGPT.setContent(messageFromDB.getContent());
            messageToGPT.setRole(messageFromDB.getOwner().getGptRole().getValue());
            return messageToGPT;
        }).toList();

        GPTMessageQueryDTO message = new GPTMessageQueryDTO();
        message.setContent(content);
        message.setRole(GPTRole.USER.getValue());

        messagesToSend.add(messageFromSystem);
        messagesToSend.addAll(collect);
        messagesToSend.add(message);

        GPTConversationQueryDTO gptConversationDTO = new GPTConversationQueryDTO();
        gptConversationDTO.setMessages(messagesToSend);
        gptConversationDTO.setModel(GPTModel.GPT_3_5_TURBO.getName());
        gptConversationDTO.setN(1);
        gptConversationDTO.setMaxTokens(1000);
        gptConversationDTO.setUser(UUID.randomUUID().toString());

        try {
            return callAPI(gptConversationDTO);
        } catch (Exception e) {
            log.error("Error when calling the API", e);
            return Optional.empty();
        }
    }

    public Optional<GPTChatCompResponseDTO> callAPI(GPTConversationQueryDTO gptConversationDTO) throws OpenAIException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAIKey);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl + "/chat/completions");

        HttpEntity<GPTConversationQueryDTO> requestEntity = new HttpEntity<>(gptConversationDTO, headers);
        ResponseEntity<GPTChatCompResponseDTO> response = restTemplate
            .postForEntity(builder.toUriString(), requestEntity, GPTChatCompResponseDTO.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return Optional.ofNullable(response.getBody());
        } else {
            throw new OpenAIException(format("Something went wrong when calling OpenAI API %s"
                , gptConversationDTO.toString()));
        }
    }
}
