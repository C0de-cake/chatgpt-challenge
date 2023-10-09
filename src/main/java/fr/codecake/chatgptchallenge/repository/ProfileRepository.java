package fr.codecake.chatgptchallenge.repository;

import fr.codecake.chatgptchallenge.domain.Profile;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Profile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findOneByUserLogin(String login);
}
