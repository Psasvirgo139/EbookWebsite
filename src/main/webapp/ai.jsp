<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>🤖 AI Hub - EbookWebsite</title>
    <link rel="stylesheet" href="${ctx}/assets/css/style.css" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <style>
        .ai-hub-container { max-width: 1000px; margin: 40px auto; padding: 20px; }
        .ai-feature-card { 
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white; border: none; border-radius: 16px; 
            padding: 30px; margin-bottom: 20px; 
            transition: transform 0.3s ease, box-shadow 0.3s ease;
            text-decoration: none; display: block;
        }
        .ai-feature-card:hover { 
            transform: translateY(-5px); 
            box-shadow: 0 10px 30px rgba(0,0,0,0.2);
            color: white; text-decoration: none;
        }
        .ai-feature-icon { font-size: 3rem; margin-bottom: 15px; }
        .ai-feature-title { font-size: 1.5rem; font-weight: bold; margin-bottom: 10px; }
        .ai-feature-desc { opacity: 0.9; line-height: 1.6; }
        .ai-stats { background: rgba(255,255,255,0.1); padding: 15px; border-radius: 8px; margin-top: 15px; }
    </style>
</head>
<body>
<%@ include file="/common/header.jsp" %>

<main>
    <div class="ai-hub-container">
        <div class="text-center mb-5">
            <h1 class="display-4">🤖 AI Hub</h1>
            <p class="lead">Trải nghiệm các tính năng AI thông minh cho việc đọc sách</p>
        </div>

        <div class="row">
            <!-- AI Chat -->
            <div class="col-md-6">
                <a href="${ctx}/ai/chat" class="ai-feature-card">
                    <div class="ai-feature-icon">💬</div>
                    <div class="ai-feature-title">AI Chat</div>
                    <div class="ai-feature-desc">
                        Trò chuyện thông minh với AI về sách, tác giả và các chủ đề văn học. 
                        AI sẽ ghi nhớ cuộc trò chuyện và đưa ra gợi ý phù hợp.
                    </div>
                    <div class="ai-stats">
                        ✨ Context-aware • 🧠 Memory-enabled • 📚 Book-focused
                    </div>
                </a>
            </div>



            <!-- AI Recommendations -->
            <div class="col-md-6">
                <a href="${ctx}/ai/recommendations" class="ai-feature-card" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">
                    <div class="ai-feature-icon">📖</div>
                    <div class="ai-feature-title">AI Recommendations</div>
                    <div class="ai-feature-desc">
                        Hệ thống gợi ý đa dạng với các mode: Smart AI, theo thể loại, tìm kiếm thông minh. 
                        Kết hợp nhiều AI service để đưa ra gợi ý tốt nhất.
                    </div>
                    <div class="ai-stats">
                        🔍 Multi-mode • 🎲 Various types • 🌟 Best suggestions
                    </div>
                </a>
            </div>

            <!-- AI Features Coming Soon -->
            <div class="col-md-6">
                <div class="ai-feature-card" style="background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%); opacity: 0.7;">
                    <div class="ai-feature-icon">🚀</div>
                    <div class="ai-feature-title">Tính năng sắp ra mắt</div>
                    <div class="ai-feature-desc">
                        • 📝 AI Content Analysis<br>
                        • 🎭 Character Analysis<br>
                        • 📊 Reading Analytics<br>
                        • 🗣️ Voice AI Assistant
                    </div>
                    <div class="ai-stats">
                        🔜 Coming Soon • 💡 Innovative • 🎉 Exciting
                    </div>
                </div>
            </div>
        </div>

        <div class="text-center mt-5">
            <div class="alert alert-info">
                <h5>🤖 Về AI của chúng tôi</h5>
                <p class="mb-0">
                    Hệ thống AI sử dụng công nghệ tiên tiến như LangChain4j, OpenAI GPT-3.5-turbo, 
                    và RAG (Retrieval-Augmented Generation) để mang đến trải nghiệm đọc sách thông minh nhất.
                </p>
            </div>
        </div>
    </div>
</main>

<%@ include file="/common/footer.jsp" %>
</body>
</html>
