package fr.codecake.chatgptchallenge.flow.message;

import fr.codecake.chatgptchallenge.IntegrationTest;
import fr.codecake.chatgptchallenge.domain.Conversation;
import fr.codecake.chatgptchallenge.domain.Message;
import fr.codecake.chatgptchallenge.domain.enumeration.Owner;
import fr.codecake.chatgptchallenge.flow.message.service.dto.FlowMessageQueryDTO;
import fr.codecake.chatgptchallenge.flow.message.service.dto.gpt.enums.GPTModel;
import fr.codecake.chatgptchallenge.flow.message.service.dto.gpt.enums.GPTRole;
import fr.codecake.chatgptchallenge.flow.message.service.dto.gpt.response.GPTChatCompResponseDTO;
import fr.codecake.chatgptchallenge.flow.message.service.dto.gpt.response.GPTChoiceResponseDTO;
import fr.codecake.chatgptchallenge.flow.message.service.dto.gpt.response.GPTMessageResponseDTO;
import fr.codecake.chatgptchallenge.flow.message.service.dto.gpt.response.GPTUsageResponseDTO;
import fr.codecake.chatgptchallenge.repository.ConversationRepository;
import fr.codecake.chatgptchallenge.repository.MessageRepository;
import fr.codecake.chatgptchallenge.web.rest.TestUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

import static fr.codecake.chatgptchallenge.test.util.OAuth2TestUtil.TEST_USER_LOGIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the {@link FlowMessageResourceIT} REST controller.
 */
@AutoConfigureMockMvc
@WithMockUser(value = TEST_USER_LOGIN)
@IntegrationTest
class FlowMessageResourceIT {

    private static final String DEFAULT_CONTENT_FROM_USER = "Hello, I want to know what's JHipster?";

    private static final String DEFAULT_CONTENT_FROM_GPT = "JHipster is an open-source development platform that allows " +
        "developers to quickly generate, develop, and deploy modern web applications and microservices. " +
        "It combines popular frameworks and tools such as Spring Boot, Angular, React, and Vue.js to provide" +
        " a full-stack development experience.\\n\\nJHipster provides a command-line interface (CLI) " +
        "that generates a project structure, including the backend and frontend components, database " +
        "configuration, security setup, and more. It follows best practices and conventions to ensure " +
        "a scalable and maintainable application architecture.\\n\\nWith JHipster, developers can " +
        "easily create CRUD (Create, Read, Update, Delete) operations, handle authentication and " +
        "authorization, manage database migrations, and integrate with various technologies like " +
        "caching, messaging, and search engines. It also supports the creation of microservices " +
        "and provides tools for deployment and continuous integration.\\n\\nOverall, JHipster " +
        "aims to simplify and accelerate the development process by providing a pre-configured, " +
        "opinionated setup that allows developers to focus on building business logic rather " +
        "than spending time on repetitive tasks.";

    private static final String DEFAULT_CONTENT_EXISTING_CONVERSATION_FROM_GPT = "Spring Boot is a framework that simplifies the" +
        " development of Java applications by providing" +
        " a streamlined and opinionated approach to building," +
        " configuring, and deploying applications.";

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMessageMockMvc;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private RestTemplate restTemplate;

    private FlowMessageQueryDTO flowMessageQueryDTO;

    private MockRestServiceServer mockServer;

    private ObjectMapper mapper = new ObjectMapper();

    private final String baseUrl;

    FlowMessageResourceIT(@Value("${application.openai.url}") String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @BeforeEach
    public void setup() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        messageRepository.deleteAll();
        conversationRepository.deleteAll();
    }

    /**
     * Create a User.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which has a required relationship to the User entity.
     */
    public static Message createEntity(EntityManager em) {
        return new Message();
    }

    /**
     * Setups the database with one user.
     */
    public static Message initMessageUser(MessageRepository messageRepository, EntityManager em) {
        return new Message();
    }

    @BeforeEach
    public void initTest() {
        flowMessageQueryDTO = new FlowMessageQueryDTO();
        flowMessageQueryDTO.setContent(DEFAULT_CONTENT_FROM_USER);
        flowMessageQueryDTO.setNewConversation(true);
        flowMessageQueryDTO.setConversationPublicId(null);
    }

    private void fakeGPTAPICall(String content) throws URISyntaxException, JsonProcessingException {
        GPTUsageResponseDTO usage = new GPTUsageResponseDTO();
        usage.setPromptTokens(19);
        usage.setCompletionTokens(203);
        usage.setTotalTokens(222);

        GPTMessageResponseDTO gptMessage = new GPTMessageResponseDTO();
        gptMessage.setRole(GPTRole.ASSISTANT.name().toLowerCase());
        gptMessage.setContent(content);

        GPTChoiceResponseDTO choice = new GPTChoiceResponseDTO();
        choice.setIndex(0);
        choice.setMessage(gptMessage);
        choice.setFinishReason("stop");

        List<GPTChoiceResponseDTO> choices = List.of(choice);

        GPTChatCompResponseDTO chatCompletion = new GPTChatCompResponseDTO();
        chatCompletion.setId("chatcmpl-123");
        chatCompletion.setObject("chat.completion");
        chatCompletion.setCreated(1677652288L);
        chatCompletion.setChoices(choices);
        chatCompletion.setUsage(usage);
        chatCompletion.setModel(GPTModel.GPT_3_5_TURBO_0613.getName());

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(this.baseUrl + "/chat/completions")))
            .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
            .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(chatCompletion))
            );
    }

    @Test
    @Transactional
    public void shouldHandleMessageForANewConversation() throws Exception {
        fakeGPTAPICall(DEFAULT_CONTENT_FROM_GPT);

        List<Conversation> noConversationsPresent = conversationRepository.findAll();
        assertThat(noConversationsPresent).isEmpty();

        restMessageMockMvc
            .perform(
                MockMvcRequestBuilders.post("/api/flow/message")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flowMessageQueryDTO))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT_FROM_GPT))
            .andExpect(jsonPath("$.conversationPublicId").exists());

        mockServer.verify();

        List<Conversation> allConversations = conversationRepository.findAll();
        assertThat(allConversations.size()).isEqualTo(1);

        Optional<Conversation> conversationCreated = allConversations.stream().findFirst();
        assertThat(conversationCreated).isPresent();

        Conversation conversation = conversationCreated.get();
        assertThat(conversation.getPublicId()).isNotNull();
        assertThat(conversation.getName()).isNotNull();
        assertThat(conversation.getMessages().size()).isEqualTo(2);

        Optional<Message> messageFromUser = conversation.getMessages()
            .stream().filter(message -> message.getOwner().equals(Owner.USER)).findFirst();
        Optional<Message> messageFromGPT = conversation.getMessages()
            .stream().filter(message -> message.getOwner().equals(Owner.GPT)).findFirst();

        assertThat(messageFromUser).isPresent();
        assertThat(messageFromUser.get().getContent()).isEqualTo(DEFAULT_CONTENT_FROM_USER);
        assertThat(messageFromUser.get().getId()).isNotNull();

        assertThat(messageFromGPT).isPresent();
        assertThat(messageFromGPT.get().getContent()).isEqualTo(DEFAULT_CONTENT_FROM_GPT);
        assertThat(messageFromGPT.get().getId()).isNotNull();
    }

    @Test
    @Transactional
    public void shouldHandleMessageForAnExistingConversation() throws Exception {
        fakeGPTAPICall(DEFAULT_CONTENT_EXISTING_CONVERSATION_FROM_GPT);
        String newMessageContent = "Can you explain what's spring boot in one short sentence ?";

        Message messageFromUser = new Message();
        messageFromUser.setContent("Hello, I want to know what's JHipster?");
        messageFromUser.setOwner(Owner.USER);

        Message messageFromGPT = new Message();
        messageFromGPT.setContent(DEFAULT_CONTENT_FROM_GPT);
        messageFromGPT.setOwner(Owner.GPT);

        messageRepository.saveAll(List.of(messageFromUser, messageFromGPT));

        Conversation conversation = new Conversation();
        conversation.setName(UUID.randomUUID().toString().substring(0, 6));
        conversation.setPublicId(UUID.randomUUID());

        conversation.addMessage(messageFromUser);
        conversation.addMessage(messageFromGPT);

        messageFromUser.setConversation(conversation);
        messageFromGPT.setConversation(conversation);

        conversationRepository.save(conversation);

        List<Conversation> conversationsPresent = conversationRepository.findAll();
        assertThat(conversationsPresent.size()).isEqualTo(1);

        Optional<Conversation> conversationAlreadyPresent = conversationsPresent.stream().findFirst();
        assertThat(conversationAlreadyPresent).isPresent();

        assertThat(conversationAlreadyPresent.get().getMessages().size()).isEqualTo(2);

        flowMessageQueryDTO.setNewConversation(false);
        flowMessageQueryDTO.setConversationPublicId(conversation.getPublicId());
        flowMessageQueryDTO.setContent(newMessageContent);

        restMessageMockMvc
            .perform(
                MockMvcRequestBuilders.post("/api/flow/message")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flowMessageQueryDTO))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT_EXISTING_CONVERSATION_FROM_GPT))
            .andExpect(jsonPath("$.conversationPublicId").value(conversation.getPublicId().toString()));

        mockServer.verify();

        List<Conversation> onlyOneConversationStillPresent = conversationRepository.findAll();
        assertThat(onlyOneConversationStillPresent.size()).isEqualTo(1);

        Optional<Conversation> conversationCreated = onlyOneConversationStillPresent.stream().findFirst();
        assertThat(conversationCreated).isPresent();

        Conversation conversationToVerify = conversationCreated.get();

        Set<Message> messagesFromUser = conversationToVerify.getMessages()
            .stream()
            .filter(messageToVerify -> messageToVerify.getOwner().equals(Owner.USER))
            .collect(Collectors.toSet());

        assertThat(messagesFromUser)
            .filteredOn(messageToVerify -> messageToVerify.getId().equals(messageFromUser.getId())).isNotEmpty();
        assertThat(messagesFromUser)
            .filteredOn(messageToVerify -> messageToVerify.getContent().equals(newMessageContent)).isNotEmpty();

        Set<Message> messagesFromGPT = conversationToVerify.getMessages()
            .stream()
            .filter(messageToVerify -> messageToVerify.getOwner().equals(Owner.GPT))
            .collect(Collectors.toSet());

        assertThat(messagesFromGPT)
            .filteredOn(messageToVerify -> messageToVerify.getId().equals(messageFromGPT.getId())).isNotEmpty();
        assertThat(messagesFromGPT)
            .filteredOn(messageToVerify -> messageToVerify.getContent().equals(DEFAULT_CONTENT_FROM_GPT)).isNotEmpty();
    }

    @Test
    public void shouldAnswer400WhenRequiredFieldAreNotPresent() throws Exception {
        flowMessageQueryDTO.setNewConversation(false);
        flowMessageQueryDTO.setConversationPublicId(UUID.randomUUID());
        flowMessageQueryDTO.setContent(null);

        restMessageMockMvc
            .perform(
                MockMvcRequestBuilders.post("/api/flow/message")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flowMessageQueryDTO))
            )
            .andExpect(status().isBadRequest());
    }
}
