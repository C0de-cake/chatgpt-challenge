package fr.codecake.chatgptchallenge.flow.message.service;


import fr.codecake.chatgptchallenge.flow.message.dto.gpt.query.GPTConversationDTO;
import fr.codecake.chatgptchallenge.flow.message.dto.gpt.response.GPTChatCompletion;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GPTService {

    private final RestTemplate restTemplate;

    public GPTService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public GPTChatCompletion sendMessage(GPTConversationDTO gptConversationDTO) {
        return new GPTChatCompletion();
    }
}
