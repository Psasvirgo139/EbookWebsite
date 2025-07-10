package com.mycompany.ebookwebsite.service;

import com.mycompany.ebookwebsite.model.Chapter;
import com.mycompany.ebookwebsite.utils.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * AI Service tự động chia chương truyện bằng OpenAI GPT-3.5-turbo
 */
public class ChapterSplittingAIService {
    private final String apiKey;
    private final HttpClient httpClient;
    private final String model;

    public ChapterSplittingAIService() {
        this.apiKey = Utils.getEnv("OPENAI_API_KEY");
        this.httpClient = HttpClient.newHttpClient();
        this.model = "gpt-3.5-turbo";
    }

    /**
     * Chia chương tự động bằng AI, trả về danh sách Chapter (có title, content)
     */
    public List<Chapter> splitChapters(String storyContent) {
        // Giới hạn độ dài gửi lên OpenAI (ví dụ 10.000 ký tự)
        String limitedContent = storyContent;
        if (storyContent.length() > 10000) {
            limitedContent = storyContent.substring(0, 10000);
            System.out.println("[Cảnh báo] Nội dung truyện quá dài, chỉ gửi 10.000 ký tự đầu tiên cho AI để chia chương.");
        }
        String prompt = buildPrompt(limitedContent);
        String aiResult = callOpenAI(prompt);
        System.out.println("AI raw result: " + aiResult); // debug
        return parseChapters(aiResult);
    }

   private String buildPrompt(String storyContent) {
    return "Bạn là AI chuyên biên tập truyện dài. Hãy phân chia nội dung truyện dưới đây thành các chương hoàn chỉnh.\n\n"
         + "YÊU CẦU RẤT QUAN TRỌNG:\n"
         + "- KHÔNG tóm tắt, KHÔNG viết lại, KHÔNG thay đổi từ ngữ gốc.\n"
         + "- GIỮ NGUYÊN 100% nội dung gốc của truyện.\n"
         + "- CHỈ chia truyện thành các chương hợp lý (theo mốc nội dung, cao trào, ngắt cảnh).\n"
         + "- Nếu nội dung gốc đã có tiêu đề chương, hãy GIỮ LẠI NGUYÊN VẸN và dùng làm ranh giới chương.\n"
         + "- Nếu không có tiêu đề, hãy tự đặt tiêu đề ngắn gọn cho từng chương và thêm vào.\n\n"
         + "Định dạng đầu ra phải theo mẫu:\n"
         + "Chương 1: [Tiêu đề chương]\n"
         + "[Nội dung chương 1 - GIỮ NGUYÊN không chỉnh sửa]\n\n"
         + "Chương 2: [Tiêu đề chương]\n"
         + "[Nội dung chương 2 - GIỮ NGUYÊN không chỉnh sửa]\n"
         + "...\n\n"
         + "Đây là nội dung truyện cần xử lý:\n"
         + storyContent;
}

    private String callOpenAI(String prompt) {
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("model", model);
            requestBody.put("max_tokens", 2048);
            requestBody.put("temperature", 0.5);

            JSONArray messages = new JSONArray();
            JSONObject systemMessage = new JSONObject();
            systemMessage.put("role", "system");
            systemMessage.put("content", "Bạn là AI chuyên chia chương truyện.");
            messages.put(systemMessage);

            JSONObject userMessage = new JSONObject();
            userMessage.put("role", "user");
            userMessage.put("content", prompt);
            messages.put(userMessage);

            requestBody.put("messages", messages);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JSONObject responseJson = new JSONObject(response.body());
                JSONArray choices = responseJson.getJSONArray("choices");
                if (choices.length() > 0) {
                    return choices.getJSONObject(0)
                            .getJSONObject("message")
                            .getString("content")
                            .trim();
                }
            } else {
                System.err.println("OpenAI API error: " + response.statusCode() + " - " + response.body());
            }
        } catch (Exception e) {
            System.err.println("Error calling OpenAI: " + e.getMessage());
        }
        return "";
    }

    /**
     * Parse kết quả AI trả về thành danh sách Chapter (title, content)
     */
    private List<Chapter> parseChapters(String aiResult) {
        List<Chapter> chapters = new ArrayList<>();
        if (aiResult == null || aiResult.isEmpty()) return chapters;
        // Regex: Chương 1: [Tiêu đề]\n[Nội dung]\nChương 2: ...
        Pattern chapterPattern = Pattern.compile("Chương\\s*(\\d+):?\\s*(.*?)\\n(.*?)(?=(\\nChương\\s*\\d+:|$))", Pattern.DOTALL | Pattern.UNICODE_CASE);
        Matcher matcher = chapterPattern.matcher(aiResult);
        while (matcher.find()) {
            String title = matcher.group(2).trim();
            String content = matcher.group(3).trim();
            Chapter chapter = new Chapter();
            chapter.setTitle(title.isEmpty() ? ("Chương " + matcher.group(1)) : title);
            chapter.setContentUrl(content);
            chapters.add(chapter);
        }
        return chapters;
    }
} 