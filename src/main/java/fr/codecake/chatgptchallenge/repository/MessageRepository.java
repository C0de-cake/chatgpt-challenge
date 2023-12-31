package fr.codecake.chatgptchallenge.repository;

import fr.codecake.chatgptchallenge.domain.Message;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA repository for the Message entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByConversation_Id(Long id);

    void deleteAllByConversation_PublicId(UUID publicId);

    void deleteAllByConversation_Id(Long id);
}
