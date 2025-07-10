package com.mycompany.ebookwebsite.model;

import java.util.List;

/**
 * 📊 Question Context Model
 * 
 * Lưu trữ thông tin ngữ cảnh của câu hỏi để hỗ trợ query transformation
 */
public class QuestionContext {
    
    public enum QuestionType {
        BOOK_SEARCH,
        CHARACTER_QUESTION,
        PLOT_QUESTION,
        RECOMMENDATION,
        CONTENT_INQUIRY
    }
    
    private String originalQuestion;
    private QuestionType questionType;
    private String intent;
    private List<String> keywords;
    private double confidence;
    
    public QuestionContext(String originalQuestion) {
        this.originalQuestion = originalQuestion;
        this.questionType = QuestionType.CONTENT_INQUIRY; // Default
        this.intent = "Tìm kiếm thông tin";
        this.keywords = List.of();
        this.confidence = 0.5;
    }
    
    // Getters
    public String getOriginalQuestion() {
        return originalQuestion;
    }
    
    public QuestionType getQuestionType() {
        return questionType;
    }
    
    public String getIntent() {
        return intent;
    }
    
    public List<String> getKeywords() {
        return keywords;
    }
    
    public double getConfidence() {
        return confidence;
    }
    
    // Setters
    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }
    
    public void setIntent(String intent) {
        this.intent = intent;
    }
    
    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }
    
    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }
    
    @Override
    public String toString() {
        return String.format("QuestionContext{type=%s, intent='%s', keywords=%s, confidence=%.2f}",
            questionType, intent, keywords, confidence);
    }
} 