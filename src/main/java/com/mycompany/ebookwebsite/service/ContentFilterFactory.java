package com.mycompany.ebookwebsite.service;

/**
 * Factory tạo instance service kiểm duyệt phù hợp theo cấu hình (rule-based hoặc AI).
 */
public class ContentFilterFactory {
    /**
     * Trả về service kiểm duyệt phù hợp (ưu tiên AI nếu ENV cho phép)
     */
    public static OpenAIContentFilterService getContentFilterService() {
        // Demo: luôn trả về AI agent. Có thể kiểm tra biến môi trường để chọn
        return new OpenAIContentFilterService();
        // Nếu muốn dùng rule: return new RuleBasedContentFilterService();
    }
} 