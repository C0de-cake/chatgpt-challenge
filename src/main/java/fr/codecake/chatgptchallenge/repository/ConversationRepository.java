package fr.codecake.chatgptchallenge.repository;

import fr.codecake.chatgptchallenge.domain.Conversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for the Conversation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Optional<Conversation> findOneByPublicIdAndProfileId(UUID publicId, Long profileId);

    Page<Conversation> findAllByProfileId(Long profileId, Pageable pageable);
}
