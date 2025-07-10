package com.mycompany.ebookwebsite.filter;

import java.util.regex.Pattern;

public class RegexContentFilter {
    private static final Pattern pattern = Pattern.compile("(rên rỉ|cởi.*quần áo|dục vọng|mơn trớn)", Pattern.CASE_INSENSITIVE);

    public static FilterResult check(String content) {
        if (content == null || content.isEmpty()) return FilterResult.passed();
        if (pattern.matcher(content).find()) {
            return FilterResult.rejected("Nội dung chứa từ khóa nhạy cảm.");
        }
        return FilterResult.passed();
    }
} 