package fr.codecake.chatgptchallenge.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ExternalCallConfiguration {

    public ExternalCallConfiguration() {
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        return objectMapper;
    }
}
