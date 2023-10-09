package fr.codecake.chatgptchallenge.flow.message.dto;

import fr.codecake.chatgptchallenge.service.dto.MessageDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link fr.codecake.chatgptchallenge.domain.Conversation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConversationFlowDTO implements Serializable {

    private Long id;

    private String name;

    private List<MessageFlowDTO> messages = new ArrayList<>();
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MessageFlowDTO> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageFlowDTO> messages) {
        this.messages = messages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConversationFlowDTO)) {
            return false;
        }

        ConversationFlowDTO conversationDTO = (ConversationFlowDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, conversationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConversationDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
