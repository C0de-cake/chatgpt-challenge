package fr.codecake.chatgptchallenge.service.mapper;

import fr.codecake.chatgptchallenge.domain.Conversation;
import fr.codecake.chatgptchallenge.service.dto.ConversationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Conversation} and its DTO {@link ConversationDTO}.
 */
@Mapper(componentModel = "spring", uses = {MessageMapper.class})
public interface ConversationMapper extends EntityMapper<ConversationDTO, Conversation> {

    @Named("without-messages")
    @Mapping(target = "messages", source = "messages", ignore = true)
    Conversation toEntityWithoutMessages(ConversationDTO conversationDTO);

    @Named("with-messages")
    @Mapping(target = "messages", source = "messages", qualifiedByName = "without-conversation")
    Conversation toEntityWithMessages(ConversationDTO conversationDTO);
}
