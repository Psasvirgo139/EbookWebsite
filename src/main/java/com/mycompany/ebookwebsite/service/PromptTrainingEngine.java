package com.mycompany.ebookwebsite.service;

import com.mycompany.ebookwebsite.dao.EbookDAO;
import com.mycompany.ebookwebsite.dao.AuthorDAO;
import com.mycompany.ebookwebsite.dao.TagDAO;
import com.mycompany.ebookwebsite.model.Ebook;
import com.mycompany.ebookwebsite.model.Author;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Prompt Training Engine - Quy tắc trả lời của AI
 * Đảm bảo AI chỉ trả lời dựa trên dữ liệu database, không bịa
 */
public class PromptTrainingEngine {
    
    private final EbookDAO ebookDAO;
    private final AuthorDAO authorDAO;
    private final TagDAO tagDAO;
    
    // AI Response Rules
    private static final String[] FORBIDDEN_RESPONSES = {
        "có thể", "có lẽ", "thường thì", "theo như tôi biết", 
        "dựa trên kinh nghiệm", "nói chung", "hầu hết"
    };
    
    private static final String DATABASE_ONLY_PREFIX = "Dựa trên dữ liệu trong hệ thống của chúng ta: ";
    private static final String NOT_FOUND_RESPONSE = "Tôi không tìm thấy thông tin này trong cơ sở dữ liệu của hệ thống. ";
    
    public PromptTrainingEngine() {
        this.ebookDAO = new EbookDAO();
        this.authorDAO = new AuthorDAO();
        this.tagDAO = new TagDAO();
    }
    
    /**
     * CORE METHOD: Xử lý và validate response từ AI
     */
    public String processAndValidateResponse(String userQuestion, String rawResponse, int userId) {
        try {
            // 1. Validate response không chứa forbidden words
            String validatedResponse = validateResponseContent(rawResponse);
            
            // 2. Extract entities từ user question và response
            Set<String> mentionedBooks = extractBookTitles(userQuestion + " " + validatedResponse);
            Set<String> mentionedAuthors = extractAuthors(userQuestion + " " + validatedResponse);
            
            // 3. Verify tất cả entities có tồn tại trong database
            ValidationResult validation = verifyEntitiesInDatabase(mentionedBooks, mentionedAuthors);
            
            // 4. Rewrite response nếu cần
            if (!validation.isValid()) {
                return rewriteResponseWithDatabaseData(userQuestion, validation, userId);
            }
            
            // 5. Add database disclaimer
            return addDatabaseDisclaimer(validatedResponse);
            
        } catch (Exception e) {
            e.printStackTrace();
            return generateSafeErrorResponse(userQuestion);
        }
    }
    
    /**
     * Validate response content - loại bỏ từ ngữ không chắc chắn
     */
    private String validateResponseContent(String response) {
        String validated = response;
        
        // Remove forbidden uncertain phrases
        for (String forbidden : FORBIDDEN_RESPONSES) {
            validated = validated.replaceAll("(?i)" + Pattern.quote(forbidden), "");
        }
        
        // Remove hedging words
        validated = validated.replaceAll("(?i)\\b(might|maybe|perhaps|possibly|probably)\\b", "");
        
        // Clean up extra spaces
        validated = validated.replaceAll("\\s+", " ").trim();
        
        return validated;
    }
    
    /**
     * Extract book titles từ text
     */
    private Set<String> extractBookTitles(String text) {
        Set<String> bookTitles = new HashSet<>();
        
        // Patterns để tìm book titles
        String[] patterns = {
            "sách \"([^\"]+)\"",
            "cuốn \"([^\"]+)\"", 
            "truyện \"([^\"]+)\"",
            "tác phẩm \"([^\"]+)\"",
            "sách ([A-ZÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴÈÉẸẺẼÊỀẾỆỂỄÌÍỊỈĨÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠÙÚỤỦŨƯỪỨỰỬỮỲÝỴỶỸ][\\w\\s]{2,30})",
            "\"([^\"]{3,50})\""
        };
        
        for (String pattern : patterns) {
            Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(text);
            while (m.find()) {
                String title = m.group(1).trim();
                if (title.length() > 2 && title.length() < 100) {
                    bookTitles.add(title);
                }
            }
        }
        
        return bookTitles;
    }
    
    /**
     * Extract author names từ text
     */
    private Set<String> extractAuthors(String text) {
        Set<String> authors = new HashSet<>();
        
        String[] patterns = {
            "tác giả ([A-ZÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴÈÉẸẺẼÊỀẾỆỂỄÌÍỊỈĨÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠÙÚỤỦŨƯỪỨỰỬỮỲÝỴỶỸ][\\w\\s]{2,30})",
            "của ([A-ZÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴÈÉẸẺẼÊỀẾỆỂỄÌÍỊỈĨÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠÙÚỤỦŨƯỪỨỰỬỮỲÝỴỶỸ][\\w\\s]{2,30})",
            "\\bby ([A-Z][a-zA-Z\\s]{2,30})",
            "([A-ZÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴÈÉẸẺẼÊỀẾỆỂỄÌÍỊỈĨÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠÙÚỤỦŨƯỪỨỰỬỮỲÝỴỶỸ][a-zA-ZÀÁẠẢÃÂầẤẬẨẪĂằẮẶẲẴèéẹẻẽêềếệểễìíịỉĩòóọỏõôồốộổỗơờớợởỡùúụủũưừứựửữỳýỵỷỹ\\s]{2,30})\\s+(viết|sáng tác|tác giả)"
        };
        
        for (String pattern : patterns) {
            Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(text);
            while (m.find()) {
                String author = m.group(1).trim();
                if (author.length() > 2 && author.length() < 50 && !isCommonWord(author)) {
                    authors.add(author);
                }
            }
        }
        
        return authors;
    }
    
    /**
     * Verify entities tồn tại trong database
     */
    private ValidationResult verifyEntitiesInDatabase(Set<String> bookTitles, Set<String> authors) throws SQLException {
        ValidationResult result = new ValidationResult();
        
        // Verify books
        for (String title : bookTitles) {
            List<Ebook> books = ebookDAO.search(title);
            if (books.isEmpty()) {
                result.addInvalidBook(title);
            } else {
                result.addValidBook(title, books);
            }
        }
        
        // Verify authors
        for (String authorName : authors) {
            List<Author> foundAuthors = authorDAO.search(authorName);
            if (foundAuthors.isEmpty()) {
                result.addInvalidAuthor(authorName);
            } else {
                result.addValidAuthor(authorName, foundAuthors);
            }
        }
        
        return result;
    }
    
    /**
     * Rewrite response với chỉ database data
     */
    private String rewriteResponseWithDatabaseData(String userQuestion, ValidationResult validation, int userId) throws SQLException {
        StringBuilder response = new StringBuilder();
        
        // Analyze user question intent
        String intent = analyzeQuestionIntent(userQuestion);
        
        switch (intent) {
            case "SEARCH_BOOK":
                return handleBookSearchRewrite(userQuestion, validation);
            case "SEARCH_AUTHOR":
                return handleAuthorSearchRewrite(userQuestion, validation);
            case "RECOMMENDATION":
                return handleRecommendationRewrite(userId, validation);
            default:
                return handleGeneralRewrite(userQuestion, validation);
        }
    }
    
    private String handleBookSearchRewrite(String question, ValidationResult validation) throws SQLException {
        StringBuilder response = new StringBuilder();
        response.append(DATABASE_ONLY_PREFIX);
        
        if (!validation.getValidBooks().isEmpty()) {
            response.append("Tôi tìm thấy các sách sau:\n");
            for (Map.Entry<String, List<Ebook>> entry : validation.getValidBooks().entrySet()) {
                List<Ebook> books = entry.getValue();
                for (Ebook book : books) {
                    response.append(String.format("📚 %s - %s (%s)\n", 
                        book.getTitle(), book.getDescription(), book.getLanguage()));
                }
            }
        } else {
            response.append(NOT_FOUND_RESPONSE);
            response.append("Bạn có thể thử tìm kiếm với từ khóa khác hoặc duyệt theo thể loại.");
        }
        
        return response.toString();
    }
    
    private String handleAuthorSearchRewrite(String question, ValidationResult validation) throws SQLException {
        StringBuilder response = new StringBuilder();
        response.append(DATABASE_ONLY_PREFIX);
        
        if (!validation.getValidAuthors().isEmpty()) {
            response.append("Tôi tìm thấy các tác giả sau trong hệ thống:\n");
            for (Map.Entry<String, List<Author>> entry : validation.getValidAuthors().entrySet()) {
                String authorName = entry.getKey();
                List<Ebook> authorBooks = ebookDAO.search(authorName);
                response.append(String.format("✍️ %s - có %d cuốn sách\n", 
                    authorName, authorBooks.size()));
            }
        } else {
            response.append(NOT_FOUND_RESPONSE);
            response.append("Tác giả này chưa có sách trong hệ thống của chúng tôi.");
        }
        
        return response.toString();
    }
    
    private String handleRecommendationRewrite(int userId, ValidationResult validation) throws SQLException {
        // Lấy tất cả sách - sử dụng method có sẵn với limit lớn
        List<Ebook> allBooks = ebookDAO.getBooksByPage(0, 1000);
        if (allBooks.isEmpty()) {
            return NOT_FOUND_RESPONSE + "Hệ thống chưa có sách nào để đề xuất.";
        }
        
        StringBuilder response = new StringBuilder();
        response.append(DATABASE_ONLY_PREFIX);
        response.append("Dựa trên sách có trong hệ thống, tôi đề xuất:\n");
        
        // Simple recommendation: latest or random books
        List<Ebook> recommendations = allBooks.stream()
            .limit(5)
            .collect(Collectors.toList());
            
        for (Ebook book : recommendations) {
            response.append(String.format("📖 %s - %s (%s)\n", 
                book.getTitle(), book.getDescription(), book.getLanguage()));
        }
        
        return response.toString();
    }
    
    private String handleGeneralRewrite(String question, ValidationResult validation) {
        StringBuilder response = new StringBuilder();
        response.append(DATABASE_ONLY_PREFIX);
        response.append("Tôi chỉ có thể trả lời về các sách, tác giả và thông tin có trong cơ sở dữ liệu hệ thống. ");
        
        if (!validation.getInvalidBooks().isEmpty()) {
            response.append("Những sách bạn đề cập: ");
            response.append(String.join(", ", validation.getInvalidBooks()));
            response.append(" không có trong hệ thống của chúng tôi. ");
        }
        
        response.append("Bạn có thể hỏi tôi về sách có sẵn hoặc yêu cầu đề xuất.");
        
        return response.toString();
    }
    
    private String addDatabaseDisclaimer(String response) {
        if (!response.startsWith(DATABASE_ONLY_PREFIX)) {
            return DATABASE_ONLY_PREFIX + response;
        }
        return response;
    }
    
    private String generateSafeErrorResponse(String question) {
        return NOT_FOUND_RESPONSE + "Có lỗi khi xử lý câu hỏi. Vui lòng thử lại với câu hỏi đơn giản hơn.";
    }
    
    // Helper methods
    private String analyzeQuestionIntent(String question) {
        String lower = question.toLowerCase();
        if (lower.contains("tìm") && (lower.contains("sách") || lower.contains("truyện"))) {
            return "SEARCH_BOOK";
        }
        if (lower.contains("tác giả") || lower.contains("của ai")) {
            return "SEARCH_AUTHOR";
        }
        if (lower.contains("đề xuất") || lower.contains("gợi ý") || lower.contains("recommend")) {
            return "RECOMMENDATION";
        }
        return "GENERAL";
    }
    
    private boolean isCommonWord(String word) {
        String[] commonWords = {"tôi", "bạn", "chúng", "tôi", "này", "đó", "với", "và", "or", "the", "a", "an"};
        String lowerWord = word.toLowerCase();
        for (String common : commonWords) {
            if (lowerWord.equals(common)) return true;
        }
        return false;
    }
    
    // Inner class for validation results
    public static class ValidationResult {
        private Map<String, List<Ebook>> validBooks = new HashMap<>();
        private Map<String, List<Author>> validAuthors = new HashMap<>();
        private Set<String> invalidBooks = new HashSet<>();
        private Set<String> invalidAuthors = new HashSet<>();
        
        public boolean isValid() {
            return invalidBooks.isEmpty() && invalidAuthors.isEmpty();
        }
        
        public void addValidBook(String title, List<Ebook> books) {
            validBooks.put(title, books);
        }
        
        public void addValidAuthor(String name, List<Author> authors) {
            validAuthors.put(name, authors);
        }
        
        public void addInvalidBook(String title) {
            invalidBooks.add(title);
        }
        
        public void addInvalidAuthor(String name) {
            invalidAuthors.add(name);
        }
        
        // Getters
        public Map<String, List<Ebook>> getValidBooks() { return validBooks; }
        public Map<String, List<Author>> getValidAuthors() { return validAuthors; }
        public Set<String> getInvalidBooks() { return invalidBooks; }
        public Set<String> getInvalidAuthors() { return invalidAuthors; }
    }
} 