package fr.codecake.chatgptchallenge.flow.message.service.dto.gpt.response;

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
