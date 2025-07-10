package com.mycompany.ebookwebsite.service;

import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/**
 * 🤖 AI Assistant Interface for LangChain4j AiServices
 * 
 * Defines the contract for AI chat functionality
 */
public interface Assistant {
    
    /**
     * 💬 Chat with user message
     * @param userMessage User's question or message
     * @return AI response
     */
    String chat(@UserMessage String userMessage);
    
    /**
     * 💬 Chat with context-aware response
     * @param userMessage User's question
     * @param context Additional context information
     * @return AI response with context
     */
    String chatWithContext(@UserMessage String userMessage, @V("context") String context);
    
    /**
     * 📚 Chat with book-specific knowledge
     * @param userMessage User's question
     * @param bookId Book ID for context
     * @return AI response with book knowledge
     */
    String chatAboutBook(@V("bookId") String bookId, @UserMessage String userMessage);
    
    /**
     * 🎯 Get recommendations
     * @param userMessage User's request
     * @return Book recommendations
     */
    String getRecommendations(@UserMessage String userMessage);
    
    /**
     * 📖 Analyze book content
     * @param bookContent Book content to analyze
     * @return Analysis result
     */
    String analyzeBookContent(@UserMessage String bookContent);
    
    /**
     * 🔍 Search books with advanced query
     * @param searchQuery Search query
     * @return Search results
     */
    String searchBooks(@UserMessage String searchQuery);
} 