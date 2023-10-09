package fr.codecake.chatgptchallenge.service.dto;

import fr.codecake.chatgptchallenge.domain.enumeration.UserSubscription;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.codecake.chatgptchallenge.domain.Profile} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfileDTO implements Serializable {

    private Long id;

    private UserSubscription subscription;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserSubscription getSubscription() {
        return subscription;
    }

    public void setSubscription(UserSubscription subscription) {
        this.subscription = subscription;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfileDTO)) {
            return false;
        }

        ProfileDTO profileDTO = (ProfileDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, profileDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfileDTO{" +
            "id=" + getId() +
            ", subscription='" + getSubscription() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
