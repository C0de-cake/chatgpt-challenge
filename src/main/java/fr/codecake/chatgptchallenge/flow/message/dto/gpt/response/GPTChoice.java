package fr.codecake.chatgptchallenge.flow.message.dto.gpt.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class GPTChoice implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer index;
    private GPTMessage message;
    private String finishReason;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public GPTMessage getMessage() {
        return message;
    }

    public void setMessage(GPTMessage message) {
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
