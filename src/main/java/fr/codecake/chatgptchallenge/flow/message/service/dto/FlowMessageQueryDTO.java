package fr.codecake.chatgptchallenge.flow.message.service.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class FlowMessageQueryDTO implements Serializable {

    @NotNull
    private String content;

    @NotNull
    private Boolean isNewConversation;

    private Long conversationPublicId;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getNewConversation() {
        return isNewConversation;
    }

    public void setNewConversation(Boolean newConversation) {
        isNewConversation = newConversation;
    }

    public Long getConversationPublicId() {
        return conversationPublicId;
    }

    public void setConversationPublicId(Long conversationPublicId) {
        this.conversationPublicId = conversationPublicId;
    }
}
