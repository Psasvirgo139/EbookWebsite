<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Truyện mới nhất | EbookWebsite</title>
    
    <!-- Core CSS -->
    <link rel="stylesheet" href="${ctx}/assets/css/style.css" />
    
    <!-- Preconnect fonts -->
    <link rel="preconnect" href="https://fonts.gstatic.com" />
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    
    <meta name="description" content="Danh sách truyện mới nhất được cập nhật hàng ngày." />
    <link rel="icon" href="${ctx}/favicon.ico" />
    
    <style>
        .latest-container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }
        
        .page-header {
            text-align: center;
            margin-bottom: 40px;
            padding: 30px 0;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 15px;
        }
        
        .page-title {
            font-size: 2.5em;
            margin-bottom: 10px;
            font-weight: bold;
        }
        
        .page-subtitle {
            font-size: 1.2em;
            opacity: 0.9;
        }
        
        .stats-info {
            background: rgba(255,255,255,0.1);
            padding: 15px;
            border-radius: 10px;
            margin-top: 20px;
            display: inline-block;
        }
        
        .books-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
            gap: 25px;
            margin-bottom: 40px;
        }
        
        .book-card {
            background: white;
            border-radius: 15px;
            box-shadow: 0 8px 25px rgba(0,0,0,0.1);
            overflow: hidden;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
            border: 1px solid #e0e0e0;
        }
        
        .book-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 15px 35px rgba(0,0,0,0.15);
        }
        
        .book-cover {
            width: 100%;
            height: 200px;
            object-fit: cover;
            background: linear-gradient(45deg, #f0f0f0, #e0e0e0);
        }
        
        .book-info {
            padding: 20px;
        }
        
        .book-title {
            font-size: 1.3em;
            font-weight: bold;
            margin-bottom: 10px;
            color: #333;
            line-height: 1.4;
        }
        
        .book-title a {
            color: inherit;
            text-decoration: none;
        }
        
        .book-title a:hover {
            color: #667eea;
        }
        
        .book-description {
            color: #666;
            font-size: 0.95em;
            line-height: 1.5;
            margin-bottom: 15px;
            display: -webkit-box;
            -webkit-line-clamp: 3;
            -webkit-box-orient: vertical;
            overflow: hidden;
        }
        
        .book-meta {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 15px;
            font-size: 0.9em;
            color: #888;
        }
        
        .book-genre {
            background: #667eea;
            color: white;
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 0.8em;
            font-weight: 500;
        }
        
        .book-stats {
            display: flex;
            gap: 15px;
            font-size: 0.85em;
        }
        
        .book-actions {
            display: flex;
            gap: 10px;
        }
        
        .read-btn {
            background: linear-gradient(45deg, #667eea, #764ba2);
            color: white;
            padding: 10px 20px;
            border-radius: 25px;
            text-decoration: none;
            font-weight: 600;
            font-size: 0.9em;
            transition: all 0.3s ease;
            flex: 1;
            text-align: center;
        }
        
        .read-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }
        
        .no-books {
            text-align: center;
            padding: 60px 20px;
            color: #666;
        }
        
        .no-books-icon {
            font-size: 4em;
            margin-bottom: 20px;
            opacity: 0.5;
        }
        
        .pagination {
            display: flex;
            justify-content: center;
            gap: 10px;
            margin-top: 40px;
        }
        
        .pagination a {
            padding: 10px 15px;
            border: 1px solid #ddd;
            border-radius: 5px;
            text-decoration: none;
            color: #333;
            transition: all 0.3s ease;
        }
        
        .pagination a:hover {
            background: #667eea;
            color: white;
            border-color: #667eea;
        }
        
        .pagination .current {
            background: #667eea;
            color: white;
            border-color: #667eea;
        }
        
        .back-link {
            display: inline-block;
            margin-bottom: 20px;
            padding: 10px 20px;
            background: #f8f9fa;
            color: #333;
            text-decoration: none;
            border-radius: 25px;
            transition: all 0.3s ease;
        }
        
        .back-link:hover {
            background: #667eea;
            color: white;
        }
        
        @media (max-width: 768px) {
            .books-grid {
                grid-template-columns: 1fr;
                gap: 20px;
            }
            
            .page-title {
                font-size: 2em;
            }
            
            .latest-container {
                padding: 15px;
            }
        }
    </style>
</head>
<body>
    <!-- Skip-link for Accessibility -->
    <a href="#main" class="skip-link">Bỏ qua và tới nội dung chính</a>

    <%@ include file="/common/header.jsp" %>

    <main id="main">
        <div class="latest-container">
            <!-- Back Link -->
            <a href="${ctx}/" class="back-link">← Quay về trang chủ</a>
            
            <!-- Page Header -->
            <div class="page-header">
                <h1 class="page-title">📚 Truyện mới nhất</h1>
                <p class="page-subtitle">Khám phá những tác phẩm mới được thêm vào thư viện</p>

            </div>

            <!-- Books Grid -->
            <c:choose>
                <c:when test="${not empty latestBooks}">
                    <div class="books-grid">
                        <c:forEach var="book" items="${latestBooks}" varStatus="status">
                            <div class="book-card">
                                <img class="book-cover" 
                                     src="${not empty book.coverUrl ? book.coverUrl : ctx.concat('/assets/img/book_covers/cover1.jpg')}" 
                                     alt="${book.title}" 
                                     onerror="this.src='${ctx}/assets/img/book_covers/cover1.jpg'" />
                                
                                <div class="book-info">
                                    <h3 class="book-title">
                                        <a href="${ctx}/book/detail?id=${book.id}">${book.title}</a>
                                    </h3>
                                    
                                    <p class="book-description">
                                        ${not empty book.description ? book.description : 'Chưa có mô tả cho truyện này.'}
                                    </p>
                                    
                                    <div class="book-meta">
                                        <span class="book-genre">${not empty book.releaseType ? book.releaseType : 'Chưa phân loại'}</span>
                                        <div class="book-stats">
                                            <span>👁️ ${book.viewCount} lượt xem</span>
                                            <span>⏰ ${book.timeAgo}</span>
                                        </div>
                                    </div>
                                    
                                    <div class="book-actions">
                                        <a href="${ctx}/book/detail?id=${book.id}" class="read-btn">
                                            📖 Đọc ngay
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:when>
                
                <c:otherwise>
                    <div class="no-books">
                        <div class="no-books-icon">📚</div>
                        <h2>Chưa có truyện nào</h2>
                        <p>Hiện tại chưa có truyện nào trong thư viện. Hãy quay lại sau!</p>
                        <a href="${ctx}/" class="read-btn" style="display: inline-block; margin-top: 20px;">
                            ← Quay về trang chủ
                        </a>
                    </div>
                </c:otherwise>
            </c:choose>

            <!-- Pagination Controls -->
            <c:if test="${not empty latestBooks}">
                <div class="pagination">
                    <c:if test="${limit > 20}">
                        <a href="${ctx}/latest?limit=${limit - 20}">← Ít hơn</a>
                    </c:if>
                    
                    <span class="current">Hiển thị ${totalBooks} truyện</span>
                    
                    <c:if test="${totalBooks >= limit}">
                        <a href="${ctx}/latest?limit=${limit + 20}">Nhiều hơn →</a>
                    </c:if>
                </div>
            </c:if>
        </div>
    </main>

    <%@ include file="/common/footer.jsp" %>
</body>
</html> 