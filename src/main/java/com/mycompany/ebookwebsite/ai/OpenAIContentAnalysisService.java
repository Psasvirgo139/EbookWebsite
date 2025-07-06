package com.mycompany.ebookwebsite.ai;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mycompany.ebookwebsite.utils.Utils;

/**
 * 🤖 OpenAI Content Analysis Service
 * 
 * Chuyên phân tích nội dung sách và tạo phản hồi tự nhiên, thông minh
 */
public class OpenAIContentAnalysisService {
    
    private final String apiKey;
    private final HttpClient httpClient;
    private final String model;
    
    public OpenAIContentAnalysisService() {
        this.apiKey = Utils.getEnv("OPENAI_API_KEY");
        this.httpClient = HttpClient.newHttpClient();
        this.model = Utils.getEnv("CONTENT_ANALYSIS_MODEL");
        
        System.out.println("🤖 OpenAI Content Analysis Service initialized");
        if (apiKey == null || apiKey.equals("test-api-key-for-demo")) {
            System.out.println("⚠️ Using demo mode - Set OPENAI_API_KEY in .env for full functionality");
        }
    }
    
    /**
     * 📚 Analyze book content and generate natural response
     */
    public String analyzeBookContent(String content, String userQuestion, List<String> bookTitles) {
        try {
            if (isApiKeyValid()) {
                return callOpenAIAPI(content, userQuestion, bookTitles);
            } else {
                return generateDemoResponse(content, userQuestion, bookTitles);
            }
        } catch (Exception e) {
            System.err.println("Error in content analysis: " + e.getMessage());
            return generateFallbackResponse(userQuestion, bookTitles);
        }
    }
    
    /**
     * 🌐 Call OpenAI API for content analysis
     */
    private String callOpenAIAPI(String content, String userQuestion, List<String> bookTitles) throws Exception {
        String prompt = buildPrompt(content, userQuestion, bookTitles);
        
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", model != null ? model : "gpt-3.5-turbo");
        requestBody.put("max_tokens", 1000);
        requestBody.put("temperature", 0.7);
        
        JSONArray messages = new JSONArray();
        
        // System message
        JSONObject systemMessage = new JSONObject();
        systemMessage.put("role", "system");
        systemMessage.put("content", buildSystemPrompt());
        messages.put(systemMessage);
        
        // User message
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
            return generateFallbackResponse(userQuestion, bookTitles);
        }
        
        return generateFallbackResponse(userQuestion, bookTitles);
    }
    
    /**
     * 📝 Build system prompt for natural conversation
     */
    private String buildSystemPrompt() {
        return "Bạn là một người thủ thư thân thiện, hiểu biết sâu sắc về văn học và rất giỏi kể chuyện.\n\n" +
               "NHIỆM VỤ:\n" +
               "- Trả lời câu hỏi về nội dung sách dựa trên thông tin được cung cấp\n" +
               "- Giao tiếp tự nhiên, dẻo miệng như đang trò chuyện với bạn bè\n" +
               "- Sử dụng emoji phù hợp để tạo không khí thân thiện\n" +
               "- Có cảm xúc, nhiệt tình khi nói về sách\n" +
               "- Dẫn dắt cuộc trò chuyện tiếp tục\n\n" +
               "PHONG CÁCH:\n" +
               "- Không máy móc, không sử dụng ngôn ngữ formal\n" +
               "- Kể chuyện sinh động, có hồn\n" +
               "- Chia sẻ cảm xúc về các nhân vật và tình huống\n" +
               "- Đặt câu hỏi để khuyến khích người dùng tham gia\n\n" +
               "QUY TẮC:\n" +
               "- CHỈ sử dụng thông tin từ nội dung sách được cung cấp\n" +
               "- KHÔNG bịa thêm thông tin không có trong sách\n" +
               "- Nếu không có thông tin, hãy thừa nhận và gợi ý câu hỏi khác";
    }
    
    /**
     * 📖 Build content analysis prompt
     */
    private String buildPrompt(String content, String userQuestion, List<String> bookTitles) {
        return String.format(
            "NỘI DUNG SÁCH: %s\n\n" +
            "TÊN SÁCH: %s\n\n" +
            "CÂU HỎI: %s\n\n" +
            "Hãy trả lời tự nhiên, thân thiện với emoji. Chỉ dùng thông tin từ nội dung sách.",
            content.length() > 8000 ? content.substring(0, 8000) + "..." : content, 
            String.join(", ", bookTitles), 
            userQuestion
        );
    }
    
    /**
     * 🎭 Generate demo response when no API key
     */
    private String generateDemoResponse(String content, String userQuestion, List<String> bookTitles) {
        String bookTitle = bookTitles.isEmpty() ? "cuốn sách này" : bookTitles.get(0);
        
        // Simple keyword-based response generation
        String lowerQuestion = userQuestion.toLowerCase();
        
        if (lowerQuestion.contains("nhân vật") || lowerQuestion.contains("character")) {
            return String.format("Ồ, bạn hỏi về nhân vật trong %s à! 😊\n\n" +
                "Từ nội dung tôi đọc được, có những nhân vật rất thú vị đấy!\n" +
                "Mỗi nhân vật đều có tính cách và vai trò riêng trong câu chuyện.\n\n" +
                "Bạn có muốn tôi kể cụ thể về nhân vật nào không? Hay bạn tò mò về mối quan hệ giữa các nhân vật? 🤔✨", 
                bookTitle);
        }
        
        if (lowerQuestion.contains("cốt truyện") || lowerQuestion.contains("nội dung") || lowerQuestion.contains("kể")) {
            return String.format("Wow, %s thật sự là một câu chuyện hấp dẫn! 📚✨\n\n" +
                "Từ những gì tôi đọc được, đây là một tác phẩm với cốt truyện thú vị,\n" +
                "có những tình tiết được xây dựng khéo léo và ý nghĩa sâu sắc.\n\n" +
                "Bạn muốn tôi kể về phần nào cụ thể? Phần mở đầu, cao trào hay kết thúc?\n" +
                "Tôi sẵn sàng chia sẻ những điều thú vị! 🎭📖", 
                bookTitle);
        }
        
        if (lowerQuestion.contains("thích") || lowerQuestion.contains("hay") || lowerQuestion.contains("đánh giá")) {
            return String.format("Tôi nghĩ %s là một cuốn sách rất đáng đọc! 🌟\n\n" +
                "Phong cách viết thú vị, nội dung có chiều sâu, và cách tác giả\n" +
                "xây dựng câu chuyện thực sự cuốn hút người đọc.\n\n" +
                "Bạn đã đọc chưa? Cảm giác của bạn về cuốn sách này thế nào?\n" +
                "Tôi rất muốn biết ý kiến của bạn! 😊📚", 
                bookTitle);
        }
        
        // Default response
        return String.format("Câu hỏi hay đấy! 🤔 Về %s, tôi có thể chia sẻ nhiều điều thú vị.\n\n" +
            "Từ nội dung tôi đã đọc, đây là một tác phẩm có giá trị và ý nghĩa.\n" +
            "Tác giả đã khéo léo xây dựng câu chuyện với những chi tiết thú vị.\n\n" +
            "Bạn có thể hỏi cụ thể hơn về nhân vật, cốt truyện, hay ý nghĩa của tác phẩm không?\n" +
            "Tôi sẽ chia sẻ những hiểu biết của mình! 📖✨", 
            bookTitle);
    }
    
    /**
     * 🔄 Generate fallback response on error
     */
    private String generateFallbackResponse(String userQuestion, List<String> bookTitles) {
        String bookTitle = bookTitles.isEmpty() ? "cuốn sách này" : bookTitles.get(0);
        
        return String.format("Xin lỗi, hiện tại tôi gặp chút khó khăn trong việc phân tích nội dung %s một cách chi tiết. 😔\n\n" +
            "Nhưng tôi vẫn có thể giúp bạn tìm hiểu về cuốn sách này!\n" +
            "Hãy thử hỏi tôi về:\n" +
            "• Thông tin cơ bản về sách 📚\n" +
            "• Thể loại và phong cách ✨\n" +
            "• Gợi ý sách tương tự 💡\n\n" +
            "Hoặc bạn có thể thử lại câu hỏi này sau nhé! 🔄", 
            bookTitle);
    }
    
    /**
     * 🔑 Check if API key is valid
     */
    private boolean isApiKeyValid() {
        return apiKey != null && 
               !apiKey.trim().isEmpty() && 
               !apiKey.equals("test-api-key-for-demo") &&
               !apiKey.equals("your-openai-api-key-here");
    }
} 