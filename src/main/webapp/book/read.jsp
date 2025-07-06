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
    <title>${book.title} | Đọc truyện online</title>

    <!-- Core CSS -->
    <link rel="stylesheet" href="${ctx}/assets/css/style.css" />

    <!-- Preconnect fonts -->
    <link rel="preconnect" href="https://fonts.gstatic.com" />
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <meta name="description" content="Đọc truyện ${book.title} - ${book.description}" />
    <link rel="icon" href="${ctx}/favicon.svg" type="image/svg+xml" />
    <link rel="alternate icon" href="${ctx}/favicon.svg" />
    
    <style>
        .book-reader {
            max-width: 800px;
            margin: 0 auto;
            padding: 20px;
            background: var(--bg-secondary);
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        
        .book-header {
            text-align: center;
            margin-bottom: 30px;
            padding-bottom: 20px;
            border-bottom: 2px solid var(--accent);
        }
        
        .book-title {
            font-size: 2.5rem;
            color: var(--text-primary);
            margin-bottom: 10px;
        }
        
        .book-meta {
            color: var(--text-secondary);
            font-size: 1.1rem;
        }
        
        .book-summary {
            background: var(--bg-primary);
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 30px;
            border-left: 4px solid var(--accent);
        }
        
        .summary-title {
            font-size: 1.3rem;
            color: var(--accent);
            margin-bottom: 15px;
            font-weight: bold;
        }
        
        .summary-content {
            line-height: 1.6;
            color: var(--text-primary);
        }
        
        .book-content {
            background: var(--bg-primary);
            padding: 30px;
            border-radius: 8px;
            line-height: 1.8;
            font-size: 1.1rem;
            color: var(--text-primary);
            white-space: pre-wrap;
            max-height: 600px;
            overflow-y: auto;
        }
        
        .book-actions {
            display: flex;
            justify-content: space-between;
            margin-top: 20px;
            padding-top: 20px;
            border-top: 1px solid var(--border);
        }
        
        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 1rem;
            text-decoration: none;
            display: inline-block;
            transition: all 0.3s ease;
        }
        
        .btn-primary {
            background: var(--accent);
            color: white;
        }
        
        .btn-secondary {
            background: var(--bg-secondary);
            color: var(--text-primary);
            border: 1px solid var(--border);
        }
        
        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0,0,0,0.2);
        }
        
        .back-link {
            color: var(--accent);
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 5px;
            margin-bottom: 20px;
        }
        
        .back-link:hover {
            text-decoration: underline;
        }
        
        .loading {
            text-align: center;
            padding: 40px;
            color: var(--text-secondary);
        }
        
        .error {
            background: #fee;
            color: #c33;
            padding: 15px;
            border-radius: 5px;
            margin: 20px 0;
        }
    </style>
</head>
<body>
    <!-- Skip-link for Accessibility -->
    <a href="#main" class="skip-link">Bỏ qua và tới nội dung chính</a>

    <%@ include file="/common/header.jsp" %>

    <main id="main">
        <div class="container">
            <a href="${ctx}/" class="back-link">← Quay lại trang chủ</a>
            
            <c:choose>
                <c:when test="${not empty book}">
                    <div class="book-reader">
                        <div class="book-header">
                            <h1 class="book-title">${book.title}</h1>
                            <div class="book-meta">
                                <span>Trạng thái: ${book.status}</span> | 
                                <span>Lượt xem: ${book.viewCount}</span>
                                <c:if test="${not empty book.createdAt}">
                                    | <span>Ngày tạo: ${book.createdAt}</span>
                                </c:if>
                            </div>
                        </div>
                        
                        <c:if test="${not empty book.summary}">
                            <div class="book-summary">
                                <div class="summary-title">🤖 Tóm tắt AI</div>
                                <div class="summary-content">${book.summary}</div>
                            </div>
                        </c:if>
                        <button class="btn btn-primary" onclick="document.querySelector('.book-summary').scrollIntoView({behavior: 'smooth'});">Xem tóm tắt AI</button>
                        
                        <c:if test="${not empty book.description}">
                            <div class="book-summary">
                                <div class="summary-title">📖 Mô tả</div>
                                <div class="summary-content">${book.description}</div>
                            </div>
                        </c:if>
                        
                        <div class="book-content">
                            <c:choose>
                                <c:when test="${not empty bookContent}">
                                    ${bookContent}
                                </c:when>
                                <c:otherwise>
                                    <div class="loading">
                                        <p>📚 Nội dung sách đang được tải...</p>
                                        <p>Nếu không thấy nội dung, có thể file sách chưa được upload hoặc có lỗi.</p>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        
                        <div class="book-actions">
                            <a href="${ctx}/" class="btn btn-secondary">← Trang chủ</a>
                            <a href="${ctx}/book?action=list" class="btn btn-primary">📚 Danh sách truyện</a>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="error">
                        <h2>❌ Không tìm thấy sách</h2>
                        <p>Sách bạn đang tìm kiếm không tồn tại hoặc đã bị xóa.</p>
                        <a href="${ctx}/" class="btn btn-primary">Quay lại trang chủ</a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </main>

    <!-- FOOTER -->
    <%@ include file="/common/footer.jsp" %>

    <!-- Core JS -->
    <script src="${ctx}/assets/js/app.js" defer></script>
</body>
</html> 