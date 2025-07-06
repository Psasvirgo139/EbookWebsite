package com.mycompany.ebookwebsite.service;

import com.mycompany.ebookwebsite.filter.FilterResult;

/**
 * Service kiểm duyệt nội dung dựa vào rule/từ khóa cấm (không dùng AI).
 */
public class RuleBasedContentFilterService {

    private static final String[] FORBIDDEN_KEYWORDS = { "sex", "bạo lực", "cấm", "thù ghét" };

    public FilterResult check(String text) {
        if (text == null || text.isBlank()) {
            return FilterResult.rejected("Nội dung rỗng!");
        }

        String lower = text.toLowerCase();
        for (String keyword : FORBIDDEN_KEYWORDS) {
            if (lower.contains(keyword)) {
                return FilterResult.rejected("Nội dung vi phạm: phát hiện từ cấm '" + keyword + "'");
            }
        }

        return FilterResult.passed(); // Hợp lệ
    }
} 