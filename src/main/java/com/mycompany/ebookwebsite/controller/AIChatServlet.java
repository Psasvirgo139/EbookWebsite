package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.service.SimpleEnhancedAIChatService;
import com.mycompany.ebookwebsite.model.ChatMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 🚀 AI CHAT SERVLET
 * 
 * ========================================
 * 📋 TÁC DỤNG CHÍNH:
 * ========================================
 * 
 * 1. 🌐 **AI Chat Web Interface**
 *    - HTTP endpoint cho AI chat functionality
 *    - Session-based chat history management
 *    - RESTful API cho AI interactions
 *    - Web UI integration với chat services
 * 
 * 2. 💬 **Chat Session Management**
 *    - Session-based conversation tracking
 *    - Chat history persistence trong session
 *    - Multi-turn conversation support
 *    - User message và AI response pairing
 * 
 * 3. 🔄 **Request Processing**
 *    - GET: Display chat interface
 *    - POST: Process user messages
 *    - Input validation và sanitization
 *    - Error handling và user feedback
 * 
 * 4. 🎯 **Integration Layer**
 *    - SimpleEnhancedAIChatService integration
 *    - JSP view rendering
 *    - Session attribute management
 *    - Request/response handling
 * 
 * ========================================
 * 🔧 FEATURES:
 * ========================================
 * 
 * ✅ HTTP GET/POST support
 * ✅ Session-based chat history
 * ✅ Input validation
 * ✅ Error handling
 * ✅ JSP view integration
 * ✅ AI service integration
 * ✅ Message persistence
 * ✅ User feedback
 * ✅ Session management
 * ✅ Request forwarding
 * 
 * ========================================
 * 🎯 SỬ DỤNG:
 * ========================================
 * 
 * - URL: /ai-chat
 * - GET: Load chat interface
 * - POST: Send message to AI
 * - Session-based conversation
 * - Web UI cho AI interactions
 * 
 * ========================================
 * 🏗️ ARCHITECTURE:
 * ========================================
 * 
 * AIChatServlet (Web Controller)
 *     ├── SimpleEnhancedAIChatService (AI Engine)
 *     ├── HttpSession (State Management)
 *     ├── ChatMessage (Message Model)
 *     ├── chat.jsp (View Layer)
 *     └── Request/Response (HTTP Layer)
 * 
 * ========================================
 * 🔄 WORKFLOW:
 * ========================================
 * 
 * GET Request → Load Chat History → Render Chat Interface
 *     ↓                                          ↓
 * POST Request → Validate Input → AI Processing → Update Session → Render Response
 */

@WebServlet(name = "AIChatServlet", urlPatterns = {"/ai/chat"})
public class AIChatServlet extends HttpServlet {
    private final SimpleEnhancedAIChatService aiService = new SimpleEnhancedAIChatService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check authentication
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/user/login.jsp");
            return;
        }
        
        List<ChatMessage> chatHistory = (List<ChatMessage>) session.getAttribute("chatHistory");
        if (chatHistory == null) chatHistory = new ArrayList<>();
        request.setAttribute("chatHistory", chatHistory);
        request.getRequestDispatcher("/ai/chat.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check authentication  
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/user/login.jsp");
            return;
        }
        
        List<ChatMessage> chatHistory = (List<ChatMessage>) session.getAttribute("chatHistory");
        if (chatHistory == null) chatHistory = new ArrayList<>();
        String userMessage = request.getParameter("userMessage");
        if (userMessage == null || userMessage.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập nội dung câu hỏi!");
            request.setAttribute("chatHistory", chatHistory);
            request.getRequestDispatcher("/ai/chat.jsp").forward(request, response);
            return;
        }
        
        try {
            // Lưu user message
            chatHistory.add(new ChatMessage(1, session.getId(), userMessage, null, "user", null));
            // Gọi AI
            String aiReply = aiService.processEnhancedChat(1, session.getId(), userMessage, null);
            chatHistory.add(new ChatMessage(1, session.getId(), null, aiReply, "ai", null));
            session.setAttribute("chatHistory", chatHistory);
            request.setAttribute("chatHistory", chatHistory);
            request.setAttribute("success", "Đã gửi câu hỏi cho AI!");
        } catch (Exception e) {
            request.setAttribute("error", "Có lỗi xảy ra khi xử lý AI: " + e.getMessage());
            request.setAttribute("chatHistory", chatHistory);
        }
        
        request.getRequestDispatcher("/ai/chat.jsp").forward(request, response);
    }
} 