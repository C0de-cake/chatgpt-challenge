package fr.codecake.chatgptchallenge.service.mapper;

import fr.codecake.chatgptchallenge.domain.Profile;
import fr.codecake.chatgptchallenge.domain.User;
import fr.codecake.chatgptchallenge.service.dto.ProfileDTO;
import fr.codecake.chatgptchallenge.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Profile} and its DTO {@link ProfileDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProfileMapper extends EntityMapper<ProfileDTO, Profile> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    ProfileDTO toDto(Profile s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
