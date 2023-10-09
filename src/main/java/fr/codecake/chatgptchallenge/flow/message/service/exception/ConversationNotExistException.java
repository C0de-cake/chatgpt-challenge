package fr.codecake.chatgptchallenge.flow.message.service.exception;

public class ConversationNotExistException extends RuntimeException {

    public ConversationNotExistException(String message) {
        super(message);
    }
}
