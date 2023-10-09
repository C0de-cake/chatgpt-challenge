package fr.codecake.chatgptchallenge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ExternalCallConfiguration {

    public ExternalCallConfiguration() {
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
