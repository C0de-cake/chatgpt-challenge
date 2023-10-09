package fr.codecake.chatgptchallenge.flow.message.service;


import fr.codecake.chatgptchallenge.flow.message.service.dto.gpt.enums.GPTModel;
import fr.codecake.chatgptchallenge.flow.message.service.dto.gpt.enums.GPTRole;
import fr.codecake.chatgptchallenge.flow.message.service.dto.gpt.query.GPTConversationQueryDTO;
import fr.codecake.chatgptchallenge.flow.message.service.dto.gpt.query.GPTMessageQueryDTO;
import fr.codecake.chatgptchallenge.flow.message.service.dto.gpt.response.GPTChatCompResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public Optional<GPTChatCompResponseDTO> sendMessage(String content) {
        GPTMessageQueryDTO message = new GPTMessageQueryDTO();
        message.setContent(content);
        message.setRole(GPTRole.USER.name().toLowerCase());

        GPTConversationQueryDTO gptConversationDTO = new GPTConversationQueryDTO();
        gptConversationDTO.setMessages(List.of(message));
        gptConversationDTO.setModel(GPTModel.GPT_3_5_TURBO.getName());
        gptConversationDTO.setN(1);
        gptConversationDTO.setMaxTokens(1000);
        gptConversationDTO.setUser(UUID.randomUUID().toString());

        return callAPI(gptConversationDTO);
    }

    public Optional<GPTChatCompResponseDTO> callAPI(GPTConversationQueryDTO gptConversationDTO) {
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
            return Optional.empty();
        }
    }
}
