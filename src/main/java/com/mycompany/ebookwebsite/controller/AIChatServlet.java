package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.ai.SimpleEnhancedAIChatService;
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

@WebServlet(name = "AIChatServlet", urlPatterns = {"/ai-chat"})
public class AIChatServlet extends HttpServlet {
    private final SimpleEnhancedAIChatService aiService = new SimpleEnhancedAIChatService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<ChatMessage> chatHistory = (List<ChatMessage>) session.getAttribute("chatHistory");
        if (chatHistory == null) chatHistory = new ArrayList<>();
        request.setAttribute("chatHistory", chatHistory);
        request.getRequestDispatcher("/ai/chat.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<ChatMessage> chatHistory = (List<ChatMessage>) session.getAttribute("chatHistory");
        if (chatHistory == null) chatHistory = new ArrayList<>();
        String userMessage = request.getParameter("userMessage");
        if (userMessage == null || userMessage.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập nội dung câu hỏi!");
            request.setAttribute("chatHistory", chatHistory);
            request.getRequestDispatcher("/ai/chat.jsp").forward(request, response);
            return;
        }
        // Lưu user message
        chatHistory.add(new ChatMessage(1, session.getId(), userMessage, null, "user", null));
        // Gọi AI
        String aiReply = aiService.processEnhancedChat(1, session.getId(), userMessage, null);
        chatHistory.add(new ChatMessage(1, session.getId(), null, aiReply, "ai", null));
        session.setAttribute("chatHistory", chatHistory);
        request.setAttribute("chatHistory", chatHistory);
        request.setAttribute("success", "Đã gửi câu hỏi cho AI!");
        request.getRequestDispatcher("/ai/chat.jsp").forward(request, response);
    }
} 