package fr.codecake.chatgptchallenge.flow.message.dto.gpt.query;

import java.io.Serializable;


public class GPTMessageQueryDTO implements Serializable {
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
