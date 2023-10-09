package fr.codecake.chatgptchallenge.service.mapper;


import org.junit.jupiter.api.BeforeEach;

class ConversationMapperTest {

    private ConversationMapper conversationMapper;

    @BeforeEach
    public void setUp() {
        conversationMapper = new ConversationMapperImpl();
    }
}
