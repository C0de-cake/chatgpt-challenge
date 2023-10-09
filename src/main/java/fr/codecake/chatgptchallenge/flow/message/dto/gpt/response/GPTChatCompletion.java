package fr.codecake.chatgptchallenge.flow.message.dto.gpt.response;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GPTChatCompletion implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String object;
    private Long created;

    @JsonProperty("choices")
    private List<GPTChoice> choices;

    private GPTUsage usage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public List<GPTChoice> getChoices() {
        return choices;
    }

    public void setChoices(List<GPTChoice> choices) {
        this.choices = choices;
    }

    public GPTUsage getUsage() {
        return usage;
    }

    public void setUsage(GPTUsage usage) {
        this.usage = usage;
    }
}

