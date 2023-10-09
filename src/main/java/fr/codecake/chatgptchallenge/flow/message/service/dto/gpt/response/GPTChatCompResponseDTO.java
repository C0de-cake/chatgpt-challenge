package fr.codecake.chatgptchallenge.flow.message.service.dto.gpt.response;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GPTChatCompResponseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String object;
    private Long created;
    private String model;

    @JsonProperty("choices")
    private List<GPTChoiceResponseDTO> choices;

    private GPTUsageResponseDTO usage;

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

    public List<GPTChoiceResponseDTO> getChoices() {
        return choices;
    }

    public void setChoices(List<GPTChoiceResponseDTO> choices) {
        this.choices = choices;
    }

    public GPTUsageResponseDTO getUsage() {
        return usage;
    }

    public void setUsage(GPTUsageResponseDTO usage) {
        this.usage = usage;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}

