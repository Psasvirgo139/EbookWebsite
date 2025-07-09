package com.mycompany.ebookwebsite.agent;

/**
 * Message là class định nghĩa một "tin nhắn" trong hội thoại giữa agent và AI,
 * gồm role (system/user/assistant) và nội dung (content).
 * Dùng chuẩn hóa khi truyền vào LLM hoặc agent, lưu memory hội thoại.
 */
public class Message {
    private String role;      // Vai trò: "system", "user", "assistant"
    private String content;   // Nội dung tin nhắn

    public Message(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public String getRole() {
        return role;
    }

    public String getContent() {
        return content;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setContent(String content) {
        this.content = content;
    }
} 