package fr.codecake.chatgptchallenge.service;

import fr.codecake.chatgptchallenge.domain.Conversation;
import fr.codecake.chatgptchallenge.domain.Message;
import fr.codecake.chatgptchallenge.repository.ConversationRepository;
import fr.codecake.chatgptchallenge.service.dto.ConversationDTO;
import fr.codecake.chatgptchallenge.service.dto.MessageDTO;
import fr.codecake.chatgptchallenge.service.dto.ProfileDTO;
import fr.codecake.chatgptchallenge.service.mapper.ConversationMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Conversation}.
 */
@Service
@Transactional
public class ConversationService {

    private final Logger log = LoggerFactory.getLogger(ConversationService.class);

    private final ConversationRepository conversationRepository;

    private final ConversationMapper conversationMapper;
    private final MessageService messageService;
    private final ConnectedUserService connectedUserService;

    public ConversationService(ConversationRepository conversationRepository,
                               ConversationMapper conversationMapper,
                               MessageService messageService,
                               ConnectedUserService connectedUserService) {
        this.conversationRepository = conversationRepository;
        this.conversationMapper = conversationMapper;
        this.messageService = messageService;
        this.connectedUserService = connectedUserService;
    }

    /**
     * Save a conversation.
     *
     * @param conversationDTO the entity to save.
     * @return the persisted entity.
     */
    public ConversationDTO save(ConversationDTO conversationDTO) {
        log.debug("Request to save Conversation : {}", conversationDTO);
        if (conversationDTO.getPublicId() == null) {
            conversationDTO.setPublicId(UUID.randomUUID());
        }
        Conversation conversation = conversationMapper.toEntity(conversationDTO);
        conversation = conversationRepository.save(conversation);
        return conversationMapper.toDto(conversation);
    }

    /**
     * Update a conversation.
     *
     * @param conversationDTO the entity to save.
     * @return the persisted entity.
     */
    public ConversationDTO update(ConversationDTO conversationDTO) {
        log.debug("Request to update Conversation : {}", conversationDTO);
        Conversation conversation = conversationMapper.toEntity(conversationDTO);
        conversation = conversationRepository.save(conversation);
        return conversationMapper.toDto(conversation);
    }

    /**
     * Partially update a conversation.
     *
     * @param conversationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ConversationDTO> partialUpdate(ConversationDTO conversationDTO) {
        log.debug("Request to partially update Conversation : {}", conversationDTO);

        return conversationRepository
            .findById(conversationDTO.getId())
            .map(existingConversation -> {
                conversationMapper.partialUpdate(existingConversation, conversationDTO);

                return existingConversation;
            })
            .map(conversationRepository::save)
            .map(conversationMapper::toDto);
    }


    /**
     * Partially update a conversation by public id.
     *
     * @param conversationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ConversationDTO> partialUpdateByPublicId(ConversationDTO conversationDTO) {
        log.debug("Request to partially update Conversation : {}", conversationDTO);

        return conversationRepository
            .findOneByPublicId(conversationDTO.getPublicId())
            .map(existingConversation -> {
                conversationMapper.partialUpdate(existingConversation, conversationDTO);

                return existingConversation;
            })
            .map(conversationRepository::save)
            .map(conversationMapper::toDto);
    }

    /**
     * Get all the conversations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ConversationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Conversations");
        return conversationRepository.findAll(pageable).map(conversationMapper::toDto);
    }

    /**
     * Get one conversation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ConversationDTO> findOne(Long id) {
        log.debug("Request to get Conversation : {}", id);
        return conversationRepository.findById(id).map(conversationMapper::toDto);
    }

    /**
     * Delete the conversation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Conversation : {}", id);
        conversationRepository.deleteById(id);
    }

    public ConversationDTO saveWithMessages(ConversationDTO conversationDTO) {
        Conversation conversationToSave = conversationMapper.toEntityWithoutMessages(conversationDTO);

        List<MessageDTO> messagesDTO = messageService.saveAll(conversationDTO.getMessages());

        List<Message> messages = messageService.mapListToEntity(messagesDTO);
        conversationToSave.getMessages().addAll(messages);
        conversationRepository.save(conversationToSave);
        return conversationDTO;
    }

    @Transactional(readOnly = true)
    public Optional<ConversationDTO> findOneByPublicIdAndProfileId(UUID publicId, Long profileId) {
        log.debug("Request to get Conversation by public id : {}", publicId);
        return conversationRepository.findOneByPublicIdAndProfileId(publicId, profileId).map(conversationMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<ConversationDTO> findAllForConnectedUser(Pageable pageable) throws UsernameNotFoundException {
        ProfileDTO profileConnectedUser = connectedUserService.getProfileConnectedUser();
        log.debug("Request to get Conversations connected user (profile id : {})", profileConnectedUser.getId());
        return conversationRepository.findAllByProfileId(profileConnectedUser.getId(), pageable)
            .map(conversationMapper::toDto);
    }
}
