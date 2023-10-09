package fr.codecake.chatgptchallenge.domain.enumeration;

public enum GPTRole {
    SYSTEM("system"),
    USER("user"),
    ASSISTANT("assistant");

    final String value;

    GPTRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
