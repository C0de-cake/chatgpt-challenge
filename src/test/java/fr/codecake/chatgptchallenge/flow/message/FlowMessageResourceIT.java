package fr.codecake.chatgptchallenge.flow.message;

import fr.codecake.chatgptchallenge.IntegrationTest;
import fr.codecake.chatgptchallenge.domain.Conversation;
import fr.codecake.chatgptchallenge.domain.Message;
import fr.codecake.chatgptchallenge.domain.enumeration.Owner;
import fr.codecake.chatgptchallenge.flow.message.dto.FlowMessageDTO;
import fr.codecake.chatgptchallenge.flow.message.dto.gpt.enums.GPTRole;
import fr.codecake.chatgptchallenge.flow.message.dto.gpt.response.GPTChatCompletion;
import fr.codecake.chatgptchallenge.flow.message.dto.gpt.response.GPTChoice;
import fr.codecake.chatgptchallenge.flow.message.dto.gpt.response.GPTMessage;
import fr.codecake.chatgptchallenge.flow.message.dto.gpt.response.GPTUsage;
import fr.codecake.chatgptchallenge.repository.ConversationRepository;
import fr.codecake.chatgptchallenge.repository.MessageRepository;
import fr.codecake.chatgptchallenge.web.rest.TestUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.codecake.chatgptchallenge.test.util.OAuth2TestUtil.TEST_USER_LOGIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the {@link FlowMessageResourceIT} REST controller.
 */
@AutoConfigureMockMvc
@WithMockUser(value = TEST_USER_LOGIN)
@IntegrationTest
class FlowMessageResourceIT {

    private static final String DEFAULT_CONTENT_FROM_USER = "AAAAAAAAAA";

    private static final String DEFAULT_CONTENT_FROM_GPT = "CCCCCCCCC";

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMessageMockMvc;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private MessageRepository messageRepository;

    @MockBean
    private RestTemplate restTemplate;

    private FlowMessageDTO flowMessageDTO;

    private MockRestServiceServer mockServer;

    private ObjectMapper mapper = new ObjectMapper();

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
        flowMessageDTO = new FlowMessageDTO();
    }

    private void prepareGPTAPICallMock() throws URISyntaxException, JsonProcessingException {
        GPTUsage usage = new GPTUsage();
        usage.setPromptTokens(9);
        usage.setCompletionTokens(12);
        usage.setTotalTokens(21);

        GPTMessage gptMessage = new GPTMessage();
        gptMessage.setRole(GPTRole.ASSISTANT);
        gptMessage.setContent("Hello there, how may I assist you today?");

        GPTChoice choice = new GPTChoice();
        choice.setIndex(0);
        choice.setMessage(gptMessage);
        choice.setFinishReason("stop");

        List<GPTChoice> choices = List.of(choice);

        GPTChatCompletion chatCompletion = new GPTChatCompletion();
        chatCompletion.setId("chatcmpl-123");
        chatCompletion.setObject("chat.completion");
        chatCompletion.setCreated(1677652288L);
        chatCompletion.setChoices(choices);
        chatCompletion.setUsage(usage);

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://localhost:8080/employee/E001")))
            .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
            .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(chatCompletion))
            );
    }

    @Test
    public void shouldHandleMessageForANewConversation() throws Exception {
        prepareGPTAPICallMock();

        List<Conversation> noConversationsPresent = conversationRepository.findAll();
        assertThat(noConversationsPresent).isEmpty();

        restMessageMockMvc
            .perform(
                MockMvcRequestBuilders.post("/api/flow/message")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flowMessageDTO))
            )
            .andExpect(status().isOk());

        mockServer.verify();

        List<Conversation> allConversations = conversationRepository.findAll();
        assertThat(allConversations.size()).isEqualTo(1);

        Optional<Conversation> conversationCreated = allConversations.stream().findFirst();
        assertThat(conversationCreated).isPresent();

        Conversation conversation = conversationCreated.get();
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
        prepareGPTAPICallMock();

        String newMessageContent = "Can you explain what's spring boot?";

        Message message = new Message();
        message.setContent("Hello, I want to know what's JHipster?");
        message.setOwner(Owner.USER);
        messageRepository.save(message);

        Conversation conversation = new Conversation();
        conversation.setName("test conversation");
        conversation.addMessage(message);
        message.setConversation(conversation);

        conversationRepository.save(conversation);

        List<Conversation> conversationsPresent = conversationRepository.findAll();
        assertThat(conversationsPresent.size()).isEqualTo(1);

        Optional<Conversation> conversationAlreadyPresent = conversationsPresent.stream().findFirst();
        assertThat(conversationAlreadyPresent).isPresent();

        int onlyOneMessageShouldBePresent = conversationAlreadyPresent.get().getMessages()
            .stream()
            .filter(messageToVerify -> messageToVerify.getOwner().equals(Owner.USER))
            .collect(Collectors.toSet()).size();

        assertThat(onlyOneMessageShouldBePresent).isEqualTo(1);

        flowMessageDTO.setNewConversation(false);
        flowMessageDTO.setConversationPublicId("lol");
        flowMessageDTO.setContent(newMessageContent);

        restMessageMockMvc
            .perform(
                MockMvcRequestBuilders.post("/api/flow/message")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flowMessageDTO))
            )
            .andExpect(status().isOk());

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
            .filteredOn(messageToVerify -> messageToVerify.getId().equals(message.getId())).isNotEmpty();
        assertThat(messagesFromUser)
            .filteredOn(messageToVerify -> messageToVerify.getContent().equals(newMessageContent)).isNotEmpty();
    }

    @Test
    public void shouldAnswer400WhenRequiredFieldAreNotPresent() throws Exception {
        flowMessageDTO.setNewConversation(false);
        flowMessageDTO.setConversationPublicId("lol");
        flowMessageDTO.setContent(null);

        restMessageMockMvc
            .perform(
                MockMvcRequestBuilders.post("/api/flow/message")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flowMessageDTO))
            )
            .andExpect(status().isBadRequest());
    }
}
