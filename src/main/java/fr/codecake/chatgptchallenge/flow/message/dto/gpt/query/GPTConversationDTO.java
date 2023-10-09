package fr.codecake.chatgptchallenge.flow.message.dto.gpt.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.codecake.chatgptchallenge.flow.message.dto.gpt.enums.GPTModel;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class GPTConversationDTO implements Serializable {
    private GPTModel model;
    private List<GPTMessageDTO> messages;

    @JsonProperty("temperature")
    private double temperature;

    @JsonProperty("top_p")
    private double topP;

    @JsonProperty("n")
    private int n;

    @JsonProperty("stream")
    private boolean stream;

    @JsonProperty("max_tokens")
    private int maxTokens;

    @JsonProperty("presence_penalty")
    private double presencePenalty;

    @JsonProperty("frequency_penalty")
    private double frequencyPenalty;

    @JsonProperty("logit_bias")
    private Map<String, Double> logitBias;

    @JsonProperty("user")
    private String user;

    // getters and setters
    public GPTModel getModel() {
        return model;
    }

    public void setModel(GPTModel model) {
        this.model = model;
    }

    public List<GPTMessageDTO> getMessages() {
        return messages;
    }

    public void setMessages(List<GPTMessageDTO> messages) {
        this.messages = messages;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getTopP() {
        return topP;
    }

    public void setTopP(double topP) {
        this.topP = topP;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public boolean isStream() {
        return stream;
    }

    public void setStream(boolean stream) {
        this.stream = stream;
    }

    public int getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(int maxTokens) {
        this.maxTokens = maxTokens;
    }

    public double getPresencePenalty() {
        return presencePenalty;
    }

    public void setPresencePenalty(double presencePenalty) {
        this.presencePenalty = presencePenalty;
    }

    public double getFrequencyPenalty() {
        return frequencyPenalty;
    }

    public void setFrequencyPenalty(double frequencyPenalty) {
        this.frequencyPenalty = frequencyPenalty;
    }

    public Map<String, Double> getLogitBias() {
        return logitBias;
    }

    public void setLogitBias(Map<String, Double> logitBias) {
        this.logitBias = logitBias;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
