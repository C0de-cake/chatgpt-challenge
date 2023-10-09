package fr.codecake.chatgptchallenge.flow.message.dto.gpt.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class GPTChoiceResponseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer index;
    private GPTMessageResponseDTO message;
    private String finishReason;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public GPTMessageResponseDTO getMessage() {
        return message;
    }

    public void setMessage(GPTMessageResponseDTO message) {
        this.message = message;
    }

    @JsonProperty("finish_reason")
    public String getFinishReason() {
        return finishReason;
    }

    public void setFinishReason(String finishReason) {
        this.finishReason = finishReason;
    }


}
