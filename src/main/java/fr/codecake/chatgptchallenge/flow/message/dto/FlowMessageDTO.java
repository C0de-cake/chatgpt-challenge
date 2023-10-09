package fr.codecake.chatgptchallenge.flow.message.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class FlowMessageDTO implements Serializable {

    @NotNull
    public String content;

    @NotNull
    public Boolean isNewConversation;

    public String conversationPublicId;

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

    public String getConversationPublicId() {
        return conversationPublicId;
    }

    public void setConversationPublicId(String conversationPublicId) {
        this.conversationPublicId = conversationPublicId;
    }
}
