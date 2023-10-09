package fr.codecake.chatgptchallenge.flow.message.service.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.UUID;

public class FlowMessageResponseDTO implements Serializable {

    @NotNull
    public String content;

    public UUID conversationPublicId;

    public FlowConversationDTO conversation;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UUID getConversationPublicId() {
        return conversationPublicId;
    }

    public void setConversationPublicId(UUID conversationPublicId) {
        this.conversationPublicId = conversationPublicId;
    }

    public FlowConversationDTO getConversation() {
        return conversation;
    }

    public void setConversation(FlowConversationDTO conversationDTO) {
        this.conversation = conversationDTO;
    }
}
