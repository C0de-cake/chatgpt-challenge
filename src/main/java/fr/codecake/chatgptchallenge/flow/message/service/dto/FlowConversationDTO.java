package fr.codecake.chatgptchallenge.flow.message.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class FlowConversationDTO implements Serializable {

    private String name;

    private UUID publicId;

    private Instant createdDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getPublicId() {
        return publicId;
    }

    public void setPublicId(UUID publicId) {
        this.publicId = publicId;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }
}
