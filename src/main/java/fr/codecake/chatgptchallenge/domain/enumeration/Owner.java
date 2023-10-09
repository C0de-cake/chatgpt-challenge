package fr.codecake.chatgptchallenge.domain.enumeration;

/**
 * The Owner enumeration.
 */
public enum Owner {
    USER(GPTRole.USER),
    GPT(GPTRole.SYSTEM);

    private GPTRole gptRole;

    Owner(GPTRole gptRole) {
        this.gptRole = gptRole;
    }

    public GPTRole getGptRole() {
        return gptRole;
    }
}
