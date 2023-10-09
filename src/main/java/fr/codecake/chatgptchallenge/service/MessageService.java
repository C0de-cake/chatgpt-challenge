package fr.codecake.chatgptchallenge.service;

import fr.codecake.chatgptchallenge.domain.Message;
import fr.codecake.chatgptchallenge.repository.MessageRepository;
import fr.codecake.chatgptchallenge.service.dto.MessageDTO;
import fr.codecake.chatgptchallenge.service.mapper.MessageMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Message}.
 */
@Service
@Transactional
public class MessageService {

    private final Logger log = LoggerFactory.getLogger(MessageService.class);

    private final MessageRepository messageRepository;

    private final MessageMapper messageMapper;

    public MessageService(MessageRepository messageRepository, MessageMapper messageMapper) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
    }

    /**
     * Save a message.
     *
     * @param messageDTO the entity to save.
     * @return the persisted entity.
     */
    public MessageDTO save(MessageDTO messageDTO) {
        log.debug("Request to save Message : {}", messageDTO);
        Message message = messageMapper.toEntity(messageDTO);
        message = messageRepository.save(message);
        return messageMapper.toDto(message);
    }

    /**
     * Update a message.
     *
     * @param messageDTO the entity to save.
     * @return the persisted entity.
     */
    public MessageDTO update(MessageDTO messageDTO) {
        log.debug("Request to update Message : {}", messageDTO);
        Message message = messageMapper.toEntity(messageDTO);
        message = messageRepository.save(message);
        return messageMapper.toDto(message);
    }

    /**
     * Partially update a message.
     *
     * @param messageDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MessageDTO> partialUpdate(MessageDTO messageDTO) {
        log.debug("Request to partially update Message : {}", messageDTO);

        return messageRepository
            .findById(messageDTO.getId())
            .map(existingMessage -> {
                messageMapper.partialUpdate(existingMessage, messageDTO);

                return existingMessage;
            })
            .map(messageRepository::save)
            .map(messageMapper::toDto);
    }

    /**
     * Get all the messages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MessageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Messages");
        return messageRepository.findAll(pageable).map(messageMapper::toDto);
    }

    /**
     * Get one message by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MessageDTO> findOne(Long id) {
        log.debug("Request to get Message : {}", id);
        return messageRepository.findById(id).map(messageMapper::toDto);
    }

    /**
     * Delete the message by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Message : {}", id);
        messageRepository.deleteById(id);
    }

    /**
     * Delete all messages by conversation id.
     *
     * @param conversationPublicId the id of the conversation.
     */
    public void deleteByConversationPublicId(UUID conversationPublicId) {
        log.debug("Request to delete Message by conversation publicId : {}", conversationPublicId);
        messageRepository.deleteAllByConversation_PublicId(conversationPublicId);
    }

    public void deleteByConversationId(Long conversationId) {
        log.debug("Request to delete Message by conversation publicId : {}", conversationId);
        messageRepository.deleteAllByConversation_Id(conversationId);
    }

    public List<MessageDTO> saveAll(List<MessageDTO> messagesDTO) {
        log.debug("Request to save list of messages : {}", messagesDTO);

        List<Message> messagesToPersist = messageMapper.toEntityWithConversation(messagesDTO);
        List<Message> messages = messageRepository.saveAllAndFlush(messagesToPersist);

        return messageMapper.toDtoWithConversation(messages);
    }

    public List<MessageDTO> getAllMessagesByConversationId(Long id) {
        log.debug("Request to get list of messages by conversation id : {}", id);
        return messageRepository.findAllByConversation_Id(id)
            .stream()
            .map(messageMapper::toDto)
            .collect(Collectors.toList());
    }

    public List<Message> mapListToEntity(List<MessageDTO> messagesDTO) {
        return messageMapper.toEntity(messagesDTO);
    }
}
