package com.mycompany.ebookwebsite.agent;

import java.util.HashMap;
import java.util.Map;

/**
 * Action đại diện cho một hành động mà agent hoặc AI yêu cầu thực thi,
 * thường là lời gọi một "tool" với tham số (args).
 * Ví dụ: kiểm duyệt nội dung, phân tích, gợi ý, terminate...
 */
public class Action {
    private String toolName;           // Tên tool/hành động (vd: "moderate", "recommend", "terminate")
    private Map<String, Object> args;  // Tham số cho tool/hành động (vd: { "text": "...", "lang": "vi" })

    // Constructor mặc định
    public Action() {
        this.args = new HashMap<>();
    }

    // Constructor đầy đủ
    public Action(String toolName, Map<String, Object> args) {
        this.toolName = toolName;
        this.args = args != null ? args : new HashMap<>();
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public Map<String, Object> getArgs() {
        return args;
    }

    public void setArgs(Map<String, Object> args) {
        this.args = args;
    }
} 