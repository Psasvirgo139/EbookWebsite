<%-- 
    Document   : bookList
    Created on : Jul 3, 2025, 1:23:40 PM
    Author     : Admin
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Danh sách truyện | Đọc truyện online</title>

    <!-- Core CSS -->
    <link rel="stylesheet" href="${ctx}/assets/css/style.css" />

    <!-- Preconnect fonts -->
    <link rel="preconnect" href="https://fonts.gstatic.com" />
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <meta name="description" content="Danh sách truyện đầy đủ - Khám phá kho truyện khổng lồ" />
    <link rel="icon" href="${ctx}/favicon.svg" type="image/svg+xml" />
    <link rel="alternate icon" href="${ctx}/favicon.svg" />
    
    <style>
        .books-container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }
        .books-header {
            text-align: center;
            margin-bottom: 30px;
        }
        .books-title {
            font-size: 2.5rem;
            color: var(--primary);
            margin-bottom: 10px;
        }
        .books-subtitle {
            color: var(--text-secondary);
            font-size: 1.1rem;
        }
        
        /* Search and Filter Section */
        .search-filter-section {
            background: var(--bg-secondary);
            padding: 20px;
            border-radius: 12px;
            margin-bottom: 30px;
            box-shadow: var(--shadow);
        }
        .search-filter-row {
            display: flex;
            gap: 15px;
            align-items: center;
            flex-wrap: wrap;
            justify-content: space-between;
        }
        .search-form {
            display: flex;
            gap: 10px;
            flex: 1;
            min-width: 300px;
        }
        .search-input {
            flex: 1;
            padding: 10px 15px;
            border: 1px solid #ddd;
            border-radius: 8px;
            font-size: 1rem;
        }
        .search-btn {
            background: var(--accent);
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 8px;
            cursor: pointer;
            font-size: 1rem;
            transition: background 0.3s ease;
        }
        .search-btn:hover {
            background: #6e4efe;
        }
        .filter-controls {
            display: flex;
            gap: 15px;
            align-items: center;
            flex-wrap: wrap;
        }
        .filter-select {
            padding: 8px 12px;
            border: 1px solid #ddd;
            border-radius: 6px;
            font-size: 0.9rem;
        }
        
        /* Results Info */
        .results-info {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
            padding: 15px;
            background: #f8f9fa;
            border-radius: 8px;
        }
        .results-count {
            font-weight: 500;
            color: var(--primary);
        }
        .clear-search {
            color: var(--accent);
            text-decoration: none;
            font-size: 0.9rem;
        }
        .clear-search:hover {
            text-decoration: underline;
        }
        
        .books-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
            gap: 24px;
            margin-bottom: 40px;
        }
        .book-item {
            background: var(--bg-secondary);
            border-radius: 12px;
            overflow: hidden;
            box-shadow: var(--shadow);
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }
        .book-item:hover {
            transform: translateY(-4px);
            box-shadow: 0 8px 25px rgba(0,0,0,0.15);
        }
        .book-cover {
            width: 100%;
            height: 200px;
            object-fit: cover;
            background: #f4f4f8;
        }
        .book-info {
            padding: 16px;
        }
        .book-title {
            font-size: 1.2rem;
            font-weight: 700;
            color: var(--primary);
            margin-bottom: 8px;
            line-height: 1.3;
        }
        .book-description {
            color: var(--text-secondary);
            font-size: 0.9rem;
            line-height: 1.4;
            margin-bottom: 12px;
            display: -webkit-box;
            -webkit-line-clamp: 3;
            -webkit-box-orient: vertical;
            overflow: hidden;
        }
        .book-meta {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 12px;
            font-size: 0.85rem;
            color: var(--text-secondary);
        }
        .book-status {
            padding: 4px 8px;
            border-radius: 12px;
            font-size: 0.8rem;
            font-weight: 500;
        }
        .status-ongoing {
            background: #e3f2fd;
            color: #1976d2;
        }
        .status-completed {
            background: #e8f5e8;
            color: #388e3c;
        }
        .book-genre {
            background: #f0f0f0;
            padding: 2px 8px;
            border-radius: 10px;
            font-size: 0.8rem;
            color: #666;
        }
        .book-actions {
            display: flex;
            gap: 8px;
        }
        .btn-read {
            background: var(--accent);
            color: white;
            padding: 8px 16px;
            border: none;
            border-radius: 6px;
            font-size: 0.9rem;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            transition: background 0.3s ease;
        }
        .btn-read:hover {
            background: #6e4efe;
        }
        .btn-view {
            background: #f8f9fa;
            color: var(--text);
            padding: 8px 16px;
            border: 1px solid #ddd;
            border-radius: 6px;
            font-size: 0.9rem;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            transition: all 0.3s ease;
        }
        .btn-view:hover {
            background: #e9ecef;
        }
        
        /* Pagination */
        .pagination {
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 8px;
            margin-top: 40px;
        }
        .pagination a, .pagination span {
            padding: 8px 12px;
            border: 1px solid #ddd;
            border-radius: 6px;
            text-decoration: none;
            color: var(--text);
            transition: all 0.3s ease;
        }
        .pagination a:hover {
            background: var(--accent);
            color: white;
            border-color: var(--accent);
        }
        .pagination .current {
            background: var(--accent);
            color: white;
            border-color: var(--accent);
        }
        .pagination .disabled {
            background: #f8f9fa;
            color: #999;
            pointer-events: none;
        }
        
        .error {
            background: #fee;
            color: #c33;
            padding: 15px;
            border-radius: 5px;
            margin: 20px 0;
            text-align: center;
        }
        .no-books {
            text-align: center;
            padding: 60px 20px;
            color: var(--text-secondary);
        }
        .no-books h3 {
            color: var(--primary);
            margin-bottom: 10px;
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
    </style>
</head>
<body>
    <!-- Skip-link for Accessibility -->
    <a href="#main" class="skip-link">Bỏ qua và tới nội dung chính</a>

    <%@ include file="/common/header.jsp" %>

    <main id="main">
        <div class="books-container">
            <a href="${ctx}/" class="back-link">← Quay lại trang chủ</a>
            
            <div class="books-header">
                <h1 class="books-title">📚 Danh sách truyện</h1>
                <p class="books-subtitle">Khám phá kho truyện đầy đủ với nhiều thể loại đa dạng</p>
                
                <!-- Upload Button for logged in users -->
                <c:if test="${not empty sessionScope.user}">
                    <div class="upload-section" style="margin-top: 20px;">
                        <a href="${ctx}/book?action=upload" class="upload-btn-main" style="
                            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                            color: white;
                            padding: 12px 24px;
                            border-radius: 25px;
                            font-weight: 600;
                            text-decoration: none;
                            display: inline-flex;
                            align-items: center;
                            gap: 8px;
                            box-shadow: 0 8px 20px rgba(102, 126, 234, 0.3);
                            transition: all 0.3s ease;
                            font-size: 1.1rem;
                        ">
                            📤 Upload Truyện Mới
                        </a>
                    </div>
                </c:if>
            </div>
            
            <!-- Search and Filter Section -->
            <div class="search-filter-section">
                <div class="search-filter-row">
                    <form class="search-form" method="GET" action="${ctx}/books">
                        <input type="text" 
                               name="search" 
                               class="search-input" 
                               placeholder="Tìm kiếm truyện theo tên hoặc mô tả..." 
                               value="${searchKeyword}">
                        <button type="submit" class="search-btn">🔍 Tìm kiếm</button>
                    </form>
                    
                    <div class="filter-controls">
                        <select class="filter-select" onchange="filterByGenre(this.value)">
                            <option value="all">Tất cả thể loại</option>
                            <c:forEach var="genre" items="${genres}">
                                <option value="${genre}" 
                                        <c:if test="${selectedGenre eq genre}">selected</c:if>>
                                    ${genre}
                                </option>
                            </c:forEach>
                        </select>
                        
                        <select class="filter-select" onchange="sortBooks(this.value)">
                            <option value="">Sắp xếp</option>
                            <option value="title" <c:if test="${sortBy eq 'title'}">selected</c:if>>A-Z</option>
                            <option value="newest" <c:if test="${sortBy eq 'newest'}">selected</c:if>>Mới nhất</option>
                            <option value="oldest" <c:if test="${sortBy eq 'oldest'}">selected</c:if>>Cũ nhất</option>
                        </select>
                    </div>
                </div>
            </div>
            
            <!-- Results Info -->
            <c:if test="${totalBooks > 0}">
                <div class="results-info">
                    <span class="results-count">
                        Hiển thị ${startBook}-${endBook} trong tổng số ${totalBooks} truyện
                        <c:if test="${not empty searchKeyword}">
                            cho từ khóa "<strong>${searchKeyword}</strong>"
                        </c:if>
                        <c:if test="${not empty selectedGenre}">
                            thuộc thể loại "<strong>${selectedGenre}</strong>"
                        </c:if>
                    </span>
                    <c:if test="${not empty searchKeyword or not empty selectedGenre}">
                        <a href="${ctx}/books" class="clear-search">✕ Xóa bộ lọc</a>
                    </c:if>
                </div>
            </c:if>
            
            <!-- Error Display -->
            <c:if test="${not empty error}">
                <div class="error">
                    <h3>❌ Lỗi</h3>
                    <p>${error}</p>
                </div>
            </c:if>
            
            <!-- Books Grid -->
            <div class="books-grid">
                <c:choose>
                    <c:when test="${empty bookList}">
                        <div class="no-books">
                            <h3>📚 Không tìm thấy truyện nào</h3>
                            <p>
                                <c:choose>
                                    <c:when test="${not empty searchKeyword}">
                                        Không có truyện nào phù hợp với từ khóa "<strong>${searchKeyword}</strong>".
                                    </c:when>
                                    <c:when test="${not empty selectedGenre}">
                                        Không có truyện nào thuộc thể loại "<strong>${selectedGenre}</strong>".
                                    </c:when>
                                    <c:otherwise>
                                        Hiện tại chưa có truyện nào trong hệ thống.
                                    </c:otherwise>
                                </c:choose>
                            </p>
                            <c:if test="${not empty searchKeyword or not empty selectedGenre}">
                                <a href="${ctx}/books" class="btn-read">Xem tất cả truyện</a>
                            </c:if>
                            
                            <!-- Upload suggestion for logged in users -->
                            <c:if test="${not empty sessionScope.user and empty searchKeyword and empty selectedGenre}">
                                <div class="upload-suggestion">
                                    <h4>📤 Hãy là người đầu tiên!</h4>
                                    <p>Chia sẻ truyện hay của bạn với cộng đồng</p>
                                    <a href="${ctx}/book?action=upload" class="upload-btn-main" style="
                                        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                                        color: white;
                                        padding: 12px 24px;
                                        border-radius: 25px;
                                        font-weight: 600;
                                        text-decoration: none;
                                        display: inline-flex;
                                        align-items: center;
                                        gap: 8px;
                                        box-shadow: 0 8px 20px rgba(102, 126, 234, 0.3);
                                        transition: all 0.3s ease;
                                        font-size: 1.1rem;
                                    ">
                                        📤 Upload Truyện Đầu Tiên
                                    </a>
                                </div>
                            </c:if>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="book" items="${bookList}">
                            <div class="book-item">
                                <img src="${not empty book.coverUrl ? book.coverUrl : 'https://via.placeholder.com/280x200?text=No+Cover'}" 
                                     alt="${book.title}" 
                                     class="book-cover"
                                     onerror="this.src='https://via.placeholder.com/280x200?text=No+Cover'">
                                <div class="book-info">
                                    <h3 class="book-title">${fn:escapeXml(book.title)}</h3>
                                    <p class="book-description">
                                        ${not empty book.description ? fn:escapeXml(book.description) : 'Không có mô tả'}
                                    </p>
                                    
                                    <c:if test="${not empty book.summary}">
                                        <div class="book-summary" style="margin-bottom: 12px; padding: 8px; background: #f0f8ff; border-radius: 6px; font-size: 0.85rem;">
                                            <span style="color:#0066cc; font-weight:500;">🤖 AI tóm tắt:</span> 
                                            ${fn:escapeXml(book.summary)}
                                        </div>
                                    </c:if>
                                    
                                    <div class="book-meta">
                                        <span class="book-status status-${book.status eq 'Hoàn thành' ? 'completed' : 'ongoing'}">
                                            ${not empty book.status ? book.status : 'Đang ra'}
                                        </span>
                                        <c:if test="${not empty book.genre}">
                                            <span class="book-genre">${book.genre}</span>
                                        </c:if>
                                    </div>
                                    
                                    <div class="book-actions">
                                        <a href="${ctx}/book/read?id=${book.id}" class="btn-read">📖 Đọc ngay</a>
                                        <a href="${ctx}/book?action=view&id=${book.id}" class="btn-view">👁️ Chi tiết</a>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>
            
            <!-- Pagination -->
            <c:if test="${totalPages > 1}">
                <div class="pagination">
                    <!-- Previous Page -->
                    <c:choose>
                        <c:when test="${currentPage > 1}">
                            <a href="?page=${currentPage - 1}&search=${searchKeyword}&genre=${selectedGenre}&sortBy=${sortBy}">‹ Trước</a>
                        </c:when>
                        <c:otherwise>
                            <span class="disabled">‹ Trước</span>
                        </c:otherwise>
                    </c:choose>
                    
                    <!-- Page Numbers -->
                    <c:set var="startPage" value="${currentPage - 2}" />
                    <c:set var="endPage" value="${currentPage + 2}" />
                    
                    <c:if test="${startPage < 1}">
                        <c:set var="startPage" value="1" />
                    </c:if>
                    <c:if test="${endPage > totalPages}">
                        <c:set var="endPage" value="${totalPages}" />
                    </c:if>
                    
                    <c:if test="${startPage > 1}">
                        <a href="?page=1&search=${searchKeyword}&genre=${selectedGenre}&sortBy=${sortBy}">1</a>
                        <c:if test="${startPage > 2}">
                            <span>...</span>
                        </c:if>
                    </c:if>
                    
                    <c:forEach var="i" begin="${startPage}" end="${endPage}">
                        <c:choose>
                            <c:when test="${i == currentPage}">
                                <span class="current">${i}</span>
                            </c:when>
                            <c:otherwise>
                                <a href="?page=${i}&search=${searchKeyword}&genre=${selectedGenre}&sortBy=${sortBy}">${i}</a>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                    
                    <c:if test="${endPage < totalPages}">
                        <c:if test="${endPage < totalPages - 1}">
                            <span>...</span>
                        </c:if>
                        <a href="?page=${totalPages}&search=${searchKeyword}&genre=${selectedGenre}&sortBy=${sortBy}">${totalPages}</a>
                    </c:if>
                    
                    <!-- Next Page -->
                    <c:choose>
                        <c:when test="${currentPage < totalPages}">
                            <a href="?page=${currentPage + 1}&search=${searchKeyword}&genre=${selectedGenre}&sortBy=${sortBy}">Sau ›</a>
                        </c:when>
                        <c:otherwise>
                            <span class="disabled">Sau ›</span>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:if>
        </div>
    </main>

    <!-- FOOTER -->
    <%@ include file="/common/footer.jsp" %>

    <!-- Core JS -->
    <script src="${ctx}/assets/js/app.js" defer></script>
    
    <script>
        function filterByGenre(genre) {
            const url = new URL(window.location);
            if (genre === 'all') {
                url.searchParams.delete('genre');
            } else {
                url.searchParams.set('genre', genre);
            }
            url.searchParams.delete('page'); // Reset to first page
            window.location.href = url.toString();
        }
        
        function sortBooks(sortBy) {
            const url = new URL(window.location);
            if (sortBy === '') {
                url.searchParams.delete('sortBy');
            } else {
                url.searchParams.set('sortBy', sortBy);
            }
            url.searchParams.delete('page'); // Reset to first page
            window.location.href = url.toString();
        }
        
        // Auto-focus search input if there's a search query
        document.addEventListener('DOMContentLoaded', function() {
            const searchInput = document.querySelector('.search-input');
            const hasSearch = new URLSearchParams(window.location.search).get('search');
            if (!hasSearch && searchInput) {
                searchInput.focus();
            }
        });
    </script>
</body>
</html>
