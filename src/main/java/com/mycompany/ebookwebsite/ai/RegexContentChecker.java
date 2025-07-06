package service.ai;

import java.util.regex.Pattern;

public class RegexContentChecker {

    // Bạn có thể cập nhật regex từ cấu hình, hoặc quản lý từ DB
    private static final Pattern TOXIC_WORD_PATTERN = Pattern.compile("(?i)(sex|spam|quảng cáo|bậy|xxx)");

    public String check(String text) {
        if (text != null && TOXIC_WORD_PATTERN.matcher(text).find()) {
            return "{\"status\":\"violation\", \"reason\":\"Phát hiện từ ngữ cấm/toxic.\"}";
        }
        return "OK";
    }
}
