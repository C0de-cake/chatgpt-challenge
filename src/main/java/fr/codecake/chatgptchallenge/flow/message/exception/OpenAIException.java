package fr.codecake.chatgptchallenge.flow.message.exception;

public class OpenAIException extends RuntimeException {

    public OpenAIException(String message) {
        super(message);
    }
}
