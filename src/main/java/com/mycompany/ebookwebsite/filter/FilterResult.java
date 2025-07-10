package com.mycompany.ebookwebsite.filter;

public class FilterResult {

    private boolean passed;
    private String reason;
    private String message;

    // Factory method - Passed
    public static FilterResult passed() {
        return new FilterResult(true, null, null);
    }

    // Factory method - Rejected với reason cũng là message
    public static FilterResult rejected(String reason) {
        return new FilterResult(false, reason, reason);
    }

    // Constructor đầy đủ
    public FilterResult(boolean passed, String reason, String message) {
        this.passed = passed;
        this.reason = reason;
        this.message = message;
    }

    public boolean isPassed() {
        return passed;
    }

    public String getReason() {
        return reason;
    }

    public String getMessage() {
        return message;
    }
} 