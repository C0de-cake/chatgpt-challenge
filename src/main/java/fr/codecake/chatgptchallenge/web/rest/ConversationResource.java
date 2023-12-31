package fr.codecake.chatgptchallenge.web.rest;

import fr.codecake.chatgptchallenge.repository.ConversationRepository;
import fr.codecake.chatgptchallenge.security.AuthoritiesConstants;
import fr.codecake.chatgptchallenge.security.SecurityUtils;
import fr.codecake.chatgptchallenge.service.ConversationService;
import fr.codecake.chatgptchallenge.service.dto.ConversationDTO;
import fr.codecake.chatgptchallenge.service.dto.ProfileDTO;
import fr.codecake.chatgptchallenge.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link fr.codecake.chatgptchallenge.domain.Conversation}.
 */
@RestController
@RequestMapping("/api")
public class ConversationResource {

    private final Logger log = LoggerFactory.getLogger(ConversationResource.class);

    private static final String ENTITY_NAME = "conversation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConversationService conversationService;

    private final ConversationRepository conversationRepository;

    public ConversationResource(ConversationService conversationService, ConversationRepository conversationRepository) {
        this.conversationService = conversationService;
        this.conversationRepository = conversationRepository;
    }

    /**
     * {@code POST  /conversations} : Create a new conversation.
     *
     * @param conversationDTO the conversationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new conversationDTO, or with status {@code 400 (Bad Request)} if the conversation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/conversations")
    @PreAuthorize("hasAnyRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<ConversationDTO> createConversation(@RequestBody ConversationDTO conversationDTO) throws URISyntaxException {
        log.debug("REST request to save Conversation : {}", conversationDTO);
        if (conversationDTO.getId() != null) {
            throw new BadRequestAlertException("A new conversation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConversationDTO result = conversationService.save(conversationDTO);
        return ResponseEntity
            .created(new URI("/api/conversations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /conversations/:id} : Updates an existing conversation.
     *
     * @param id the id of the conversationDTO to save.
     * @param conversationDTO the conversationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated conversationDTO,
     * or with status {@code 400 (Bad Request)} if the conversationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the conversationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/conversations/{id}")
    @PreAuthorize("hasAnyRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<ConversationDTO> updateConversation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ConversationDTO conversationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Conversation : {}, {}", id, conversationDTO);
        if (conversationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, conversationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!conversationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ConversationDTO result = conversationService.update(conversationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, conversationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /conversations/:id} : Partial updates given fields of an existing conversation, field will ignore if it is null
     *
     * @param id the id of the conversationDTO to save.
     * @param conversationDTO the conversationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated conversationDTO,
     * or with status {@code 400 (Bad Request)} if the conversationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the conversationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the conversationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/conversations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize("hasAnyRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<ConversationDTO> partialUpdateConversation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ConversationDTO conversationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Conversation partially : {}, {}", id, conversationDTO);
        if (conversationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, conversationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!conversationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ConversationDTO> result = conversationService.partialUpdate(conversationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, conversationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /conversations} : get all the conversations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of conversations in body.
     */
    @GetMapping("/conversations")
    @PreAuthorize("hasAnyRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<List<ConversationDTO>> getAllConversations(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Conversations");
        Page<ConversationDTO> page = conversationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /conversations/:id} : get the "id" conversation.
     *
     * @param id the id of the conversationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the conversationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/conversations/{id}")
    @PreAuthorize("hasAnyRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<ConversationDTO> getConversation(@PathVariable Long id) {
        log.debug("REST request to get Conversation : {}", id);
        Optional<ConversationDTO> conversationDTO = conversationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(conversationDTO);
    }

    /**
     * {@code DELETE  /conversations/:id} : delete the "id" conversation.
     *
     * @param id the id of the conversationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/conversations/{id}")
    @PreAuthorize("hasAnyRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteConversation(@PathVariable Long id) {
        log.debug("REST request to delete Conversation : {}", id);
        conversationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /conversations} : get all the conversations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of conversations in body.
     */
    @GetMapping("/conversations/for-connected-user")
    public ResponseEntity<List<ConversationDTO>> getAllConversationsByProfileId(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Conversations");
        try {
            Page<ConversationDTO> page = conversationService.findAllForConnectedUser(pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (UsernameNotFoundException unfe) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * {@code PATCH  /conversations/for-connected-user/:public-id } : Partial updates given fields of an existing conversation, field will ignore if it is null
     *
     * @param publicId the publicId of the conversationDTO to save.
     * @param conversationDTO the conversationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated conversationDTO,
     * or with status {@code 400 (Bad Request)} if the conversationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the conversationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the conversationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/conversations/for-connected-user/{public-id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ConversationDTO> partialUpdateConversationByPublicIdAndUserLogin(
        @PathVariable(value = "public-id", required = false) final String publicId,
        @RequestBody ConversationDTO conversationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Conversation partially : {}, {}", publicId, conversationDTO);
        if (conversationDTO.getPublicId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(publicId, conversationDTO.getPublicId().toString())) {
            throw new BadRequestAlertException("Invalid public ID", ENTITY_NAME, "idinvalid");
        }

        String currentLogin = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
        if (!conversationRepository
            .existsConversationByPublicIdAndProfileUserLogin(conversationDTO.getPublicId(), currentLogin)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ConversationDTO> result = conversationService.partialUpdateByPublicId(conversationDTO);

        return ResponseUtil.wrapOrNotFound(result);
    }

    /**
     * {@code DELETE  /conversations/for-connected-user/:public-id} : delete the conversation for the given public id.
     *
     * @param publicId the public id of the conversationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/conversations/for-connected-user/{public-id}")
    public ResponseEntity<Void> deleteConversation(@PathVariable(name = "public-id") String publicId) {
        log.debug("REST request to delete Conversation : {}", publicId);
        String currentLogin = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));

        conversationService.deleteByPublicId(publicId, currentLogin);
        return ResponseEntity.noContent().build();
    }

    /**
     * {@code POST  /conversations} : Create a new conversation.
     *
     * @param conversationDTO the conversationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new conversationDTO, or with status {@code 400 (Bad Request)} if the conversation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/conversations/create")
    @PreAuthorize("hasAnyRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<ConversationDTO> createConversationForConnectedUser(@RequestBody ConversationDTO conversationDTO) throws URISyntaxException {
        log.debug("REST request to save Conversation : {}", conversationDTO);
        if (conversationDTO.getId() != null) {
            throw new BadRequestAlertException("A new conversation cannot already have an ID", ENTITY_NAME, "idexists");
        }

        ConversationDTO result = conversationService.saveForConnectedUser(conversationDTO);
        return ResponseEntity
            .created(new URI("/api/conversations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
}
