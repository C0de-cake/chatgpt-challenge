package fr.codecake.chatgptchallenge.service.mapper;

import fr.codecake.chatgptchallenge.domain.Conversation;
import fr.codecake.chatgptchallenge.domain.Profile;
import fr.codecake.chatgptchallenge.service.dto.ConversationDTO;
import fr.codecake.chatgptchallenge.service.dto.ProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Conversation} and its DTO {@link ConversationDTO}.
 */
@Mapper(componentModel = "spring", uses = {MessageMapper.class, UserMapper.class})
public interface ConversationMapper extends EntityMapper<ConversationDTO, Conversation> {
    @Mapping(target = "profile", source = "profile", qualifiedByName = "profileId")
    @Mapping(target = "profile.user", source = "profile.user", ignore = true)
    ConversationDTO toDto(Conversation s);

    @Named("profileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProfileDTO toDtoProfileId(Profile profile);

    @Named("without-messages")
    @Mapping(target = "messages", source = "messages", ignore = true)
    Conversation toEntityWithoutMessages(ConversationDTO conversationDTO);

    @Named("with-messages")
    @Mapping(target = "messages", source = "messages", qualifiedByName = "without-conversation")
    Conversation toEntityWithMessages(ConversationDTO conversationDTO);

    @Override
    @Mapping(target = "profile.user", source = "profile.user", ignore = true)
    Conversation toEntity(ConversationDTO dto);
}
