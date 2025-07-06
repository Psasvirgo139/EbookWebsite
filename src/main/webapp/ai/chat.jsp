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
    <title>AI Chat - EbookWebsite</title>
    <link rel="stylesheet" href="${ctx}/assets/css/style.css" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <style>
        .chat-container { max-width: 700px; margin: 40px auto; background: #fff; border-radius: 16px; box-shadow: 0 2px 16px rgba(0,0,0,0.08); padding: 32px 24px; }
        .chat-history { max-height: 350px; overflow-y: auto; margin-bottom: 24px; }
        .chat-bubble { padding: 12px 18px; border-radius: 16px; margin-bottom: 10px; display: inline-block; }
        .chat-user { background: #e0e7ff; color: #333; align-self: flex-end; }
        .chat-ai { background: #f3f0ff; color: #6a5acd; align-self: flex-start; }
        .chat-meta { font-size: 0.85em; color: #888; margin-bottom: 2px; }
    </style>
</head>
<body>
<%@ include file="../common/header.jsp" %>
<main>
    <div class="chat-container">
        <h2 class="text-center mb-4">🤖 AI Chat</h2>
        <div class="chat-history d-flex flex-column mb-3" id="chatHistory">
            <c:forEach var="msg" items="${chatHistory}">
                <div class="chat-bubble ${msg.type eq 'user' ? 'chat-user' : 'chat-ai'}">
                    <div class="chat-meta">${msg.type eq 'user' ? '👤 Bạn' : '🤖 AI'}</div>
                    <div>
                        <c:choose>
                            <c:when test="${msg.type eq 'user'}">${msg.message}</c:when>
                            <c:otherwise>${msg.response}</c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </c:forEach>
        </div>
        <form method="post" action="${ctx}/ai-chat" class="d-flex gap-2">
            <input type="text" name="userMessage" class="form-control" placeholder="Nhập câu hỏi..." autocomplete="off" required style="flex:1;">
            <button type="submit" class="btn btn-primary">Gửi</button>
        </form>
        <c:if test="${not empty error}">
            <div class="alert alert-danger mt-3">${error}</div>
        </c:if>
        <c:if test="${not empty success}">
            <div class="alert alert-success mt-3">${success}</div>
        </c:if>
    </div>
</main>
<%@ include file="../common/footer.jsp" %>
</body>
</html> 