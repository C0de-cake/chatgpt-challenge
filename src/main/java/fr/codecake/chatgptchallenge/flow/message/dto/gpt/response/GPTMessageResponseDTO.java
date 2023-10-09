package fr.codecake.chatgptchallenge.flow.message.dto.gpt.response;

import fr.codecake.chatgptchallenge.flow.message.dto.gpt.enums.GPTRole;

import java.io.Serializable;

public class GPTMessageResponseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String role;
    private String content;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
