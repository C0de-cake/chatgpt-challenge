package fr.codecake.chatgptchallenge.flow.message.dto.gpt.query;

import fr.codecake.chatgptchallenge.flow.message.dto.gpt.enums.GPTRole;

import java.io.Serializable;


public class GPTMessageDTO implements Serializable {
    private GPTRole role;
    private String content;

    public GPTRole getRole() {
        return role;
    }

    public void setRole(GPTRole role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
