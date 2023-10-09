package fr.codecake.chatgptchallenge.service.mapper;

import fr.codecake.chatgptchallenge.domain.Conversation;
import fr.codecake.chatgptchallenge.service.dto.ConversationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Conversation} and its DTO {@link ConversationDTO}.
 */
@Mapper(componentModel = "spring", uses = {MessageMapper.class})
public interface ConversationMapper extends EntityMapper<ConversationDTO, Conversation> {

    @Named("with-messages")
    @Mapping(target = "messages", source = "messages")
    Conversation toEntityWithMessages(ConversationDTO conversationDTO);
}
