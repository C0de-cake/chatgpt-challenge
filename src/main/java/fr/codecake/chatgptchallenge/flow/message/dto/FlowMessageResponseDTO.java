package fr.codecake.chatgptchallenge.flow.message.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class FlowMessageResponseDTO implements Serializable {

    @NotNull
    public String content;

    public Long conversationPublicId;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getConversationPublicId() {
        return conversationPublicId;
    }

    public void setConversationPublicId(Long conversationPublicId) {
        this.conversationPublicId = conversationPublicId;
    }
}
