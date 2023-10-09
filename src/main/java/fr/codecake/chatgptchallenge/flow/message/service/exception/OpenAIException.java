package fr.codecake.chatgptchallenge.flow.message.service.exception;

public class OpenAIException extends RuntimeException {

    public OpenAIException(String message) {
        super(message);
    }
}
