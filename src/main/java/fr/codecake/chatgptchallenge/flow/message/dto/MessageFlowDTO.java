package fr.codecake.chatgptchallenge.flow.message.dto;

import fr.codecake.chatgptchallenge.domain.enumeration.Owner;
import fr.codecake.chatgptchallenge.service.dto.ConversationDTO;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.codecake.chatgptchallenge.domain.Message} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MessageFlowDTO implements Serializable {

    private Long id;

    private String content;

    private Owner owner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MessageFlowDTO)) {
            return false;
        }

        MessageFlowDTO messageDTO = (MessageFlowDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, messageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MessageDTO{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", owner='" + getOwner() + "'" +
            "}";
    }
}
