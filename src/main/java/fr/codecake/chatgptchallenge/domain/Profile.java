package fr.codecake.chatgptchallenge.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.codecake.chatgptchallenge.domain.enumeration.UserSubscription;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Profile.
 */
@Entity
@Table(name = "profile")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Profile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profileSequenceGenerator")
    @SequenceGenerator(name = "profileSequenceGenerator", sequenceName = "profile_sequence", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription")
    private UserSubscription subscription;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profile")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "messages", "profile" }, allowSetters = true)
    private Set<Conversation> conversations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Profile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserSubscription getSubscription() {
        return this.subscription;
    }

    public Profile subscription(UserSubscription subscription) {
        this.setSubscription(subscription);
        return this;
    }

    public void setSubscription(UserSubscription subscription) {
        this.subscription = subscription;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Profile user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Conversation> getConversations() {
        return this.conversations;
    }

    public void setConversations(Set<Conversation> conversations) {
        if (this.conversations != null) {
            this.conversations.forEach(i -> i.setProfile(null));
        }
        if (conversations != null) {
            conversations.forEach(i -> i.setProfile(this));
        }
        this.conversations = conversations;
    }

    public Profile conversations(Set<Conversation> conversations) {
        this.setConversations(conversations);
        return this;
    }

    public Profile addConversation(Conversation conversation) {
        this.conversations.add(conversation);
        conversation.setProfile(this);
        return this;
    }

    public Profile removeConversation(Conversation conversation) {
        this.conversations.remove(conversation);
        conversation.setProfile(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Profile)) {
            return false;
        }
        return id != null && id.equals(((Profile) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Profile{" +
            "id=" + getId() +
            ", subscription='" + getSubscription() + "'" +
            "}";
    }
}
