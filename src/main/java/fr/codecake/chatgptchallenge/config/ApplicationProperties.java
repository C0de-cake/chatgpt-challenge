package fr.codecake.chatgptchallenge.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Chatgpt Challenge.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
    private final OpenAI openAI = new ApplicationProperties.OpenAI();

    public static class OpenAI {

        public String key = "";

        public String url = "";

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public OpenAI getOpenAI() {
        return openAI;
    }

    // jhipster-needle-application-properties-property
    // jhipster-needle-application-properties-property-getter
    // jhipster-needle-application-properties-property-class
}
