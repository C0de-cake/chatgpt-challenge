package fr.codecake.chatgptchallenge.service.mapper;

import fr.codecake.chatgptchallenge.domain.Conversation;
import fr.codecake.chatgptchallenge.domain.Message;
import fr.codecake.chatgptchallenge.service.dto.ConversationDTO;
import fr.codecake.chatgptchallenge.service.dto.MessageDTO;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper for the entity {@link Message} and its DTO {@link MessageDTO}.
 */
@Mapper(componentModel = "spring")
public interface MessageMapper extends EntityMapper<MessageDTO, Message> {
    @Mapping(target = "conversation", source = "conversation", qualifiedByName = "conversationId")
    MessageDTO toDto(Message s);

    @Named("dto-with-conversation")
    @Mapping(target = "conversation", source = "conversation", qualifiedByName = "conversationId")
    MessageDTO toDtoWithConversation(Message s);

    @Named("dtos-with-conversation")
    @Mapping(target = "conversation", source = "conversation", qualifiedByName = "conversationId")
    List<MessageDTO> toDtoWithConversation(List<Message> messages);

    @Named("entity-with-conversation")
    @Mapping(target = "conversation", source = "conversation", qualifiedByName = "conversationIdForEntity")
    Message toEntityWithConversation(MessageDTO  messageDTO);

    @Named("entities-with-conversation")
    @IterableMapping(qualifiedByName = "entity-with-conversation")
    @Mapping(target = "conversation", source = "conversation", qualifiedByName = "conversationIdForEntity")
    List<Message> toEntityWithConversation(List<MessageDTO> messagesDTO);

    @Named("without-conversation")
    @Mapping(target = "conversation", source = "conversation", ignore = true)
    Message toEntityWithoutConversation(MessageDTO messageDTO);

    @Named("conversationIdForEntity")
    @Mapping(target = "messages", source = "messages", ignore = true)
    Conversation toEntityConversationId(ConversationDTO conversation);

    @Named("conversationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ConversationDTO toDtoConversationId(Conversation conversation);
}
