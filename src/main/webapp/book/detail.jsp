<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/common/header.jsp" %>

<style>
    /* === Modern Tech Design System - Enhanced Contrast === */
    :root {
        --primary: #4361ee;
        --primary-dark: #3a56d4;
        --secondary: #7209b7;
        --accent: #06d6a0;
        --dark: #0f172a; /* Darker background */
        --darker: #0a101f; /* Even darker for better contrast */
        --light: #f8f9ff;
        --text-primary: #ffffff; /* Pure white for better contrast */
        --text-secondary: #e2e8f0;
        --gray: #94a3b8;
        --gradient: linear-gradient(135deg, var(--primary), var(--secondary));
        --card-bg: rgba(23, 30, 51, 0.9); /* Darker card background */
        --card-border: rgba(255, 255, 255, 0.15); /* Lighter border for contrast */
        --shadow: 0 8px 32px rgba(0, 0, 0, 0.5);
        --glow: 0 0 20px rgba(67, 97, 238, 0.5);
    }
    
    body {
        background: linear-gradient(135deg, var(--darker), var(--dark));
        color: var(--text-primary); /* Use high-contrast text color */
        font-family: 'Segoe UI', 'PingFang SC', 'Microsoft YaHei', sans-serif;
        min-height: 100vh;
        background-attachment: fixed;
        line-height: 1.7; /* Improved line spacing */
    }
    
    .container {
        max-width: 1200px;
        padding: 2rem 1rem; /* Better padding */
    }
    
    /* === Typography with Enhanced Contrast === */
    h1, h2, h3, h4, h5, h6 {
        font-weight: 700;
        letter-spacing: -0.015em;
        color: var(--text-primary);
    }
    
    h2 {
        font-size: 2.3rem;
        background: linear-gradient(to right, #f72585, #4361ee);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        margin-bottom: 1.5rem;
        position: relative;
        display: inline-block;
    }
    
    h2:after {
        content: '';
        position: absolute;
        bottom: -8px;
        left: 0;
        height: 4px;
        width: 80px;
        background: var(--accent);
        border-radius: 10px;
    }
    
    h4 {
        font-size: 1.5rem;
        margin-bottom: 1.5rem;
        padding-bottom: 0.75rem;
        border-bottom: 2px solid rgba(255, 255, 255, 0.15); /* Lighter border */
        display: flex;
        align-items: center;
    }
    
    h4 i {
        margin-right: 10px;
        color: var(--accent);
    }
    
    /* === Cards & Containers with Better Contrast === */
    .card {
        background: var(--card-bg);
        backdrop-filter: blur(12px);
        border: 1px solid var(--card-border);
        border-radius: 16px;
        box-shadow: var(--shadow);
        transition: all 0.3s ease;
        overflow: hidden;
    }
    
    .card:hover {
        transform: translateY(-5px);
        box-shadow: var(--shadow), var(--glow);
    }
    
    /* === Cover Image with Contrast Improvements === */
    .cover-container {
        position: relative;
        overflow: hidden;
        border-radius: 16px;
        box-shadow: 0 12px 30px rgba(0, 0, 0, 0.4);
        transition: all 0.4s ease;
        height: 100%;
        display: flex;
        align-items: center;
        background: linear-gradient(45deg, #1a1a2e, #16213e);
    }
    
    .cover-container:hover {
        transform: scale(1.02);
        box-shadow: 0 15px 40px rgba(114, 9, 183, 0.4);
    }
    
    .cover-container img {
        border-radius: 16px;
        width: 100%;
        object-fit: cover;
        transition: transform 0.5s ease;
    }
    
    .cover-container:hover img {
        transform: scale(1.05);
    }
    
    /* === Badges with Better Contrast === */
    .badge {
        font-weight: 600;
        padding: 0.6em 1.2em;
        border-radius: 50px;
        font-size: 0.9rem;
        letter-spacing: 0.5px;
        margin-right: 0.5rem;
        margin-bottom: 0.5rem;
        display: inline-flex;
        align-items: center;
        gap: 6px;
    }
    
    .badge-primary {
        background: rgba(67, 97, 238, 0.25);
        border: 1px solid var(--primary);
        color: #dbe4ff; /* Lighter text for contrast */
    }
    
    .badge-info {
        background: rgba(6, 214, 160, 0.25);
        border: 1px solid var(--accent);
        color: #d4fdf0; /* Lighter text for contrast */
    }
    
    .badge-success {
        background: rgba(6, 214, 160, 0.25);
        border: 1px solid var(--accent);
        color: #d4fdf0; /* Lighter text for contrast */
    }
    
    .badge-secondary {
        background: rgba(114, 9, 183, 0.25);
        border: 1px solid var(--secondary);
        color: #e9d4ff; /* Lighter text for contrast */
        transition: all 0.3s ease;
    }
    
    .badge-secondary:hover {
        background: rgba(114, 9, 183, 0.35);
        transform: translateY(-2px);
    }
    
    /* === Action Buttons with Enhanced Visibility === */
    .btn {
        border-radius: 50px;
        font-weight: 600;
        padding: 0.8rem 1.8rem;
        transition: all 0.3s ease;
        border: none;
        display: inline-flex;
        align-items: center;
        gap: 8px;
        font-size: 1rem;
    }
    
    .btn-lg {
        padding: 1.1rem 2.5rem;
        font-size: 1.15rem;
    }
    
    .btn-success {
        background: var(--accent);
        color: #0a1e18; /* Darker text for contrast */
        box-shadow: 0 4px 20px rgba(6, 214, 160, 0.4);
    }
    
    .btn-success:hover {
        background: #05b890;
        transform: translateY(-3px);
        box-shadow: 0 7px 25px rgba(6, 214, 160, 0.5);
    }
    
    .btn-outline-danger {
        background: transparent;
        border: 2px solid #ff6b6b;
        color: #ff9e9e; /* Lighter for better visibility */
    }
    
    .btn-outline-danger:hover {
        background: rgba(255, 107, 107, 0.15);
        transform: translateY(-3px);
        color: #ff6b6b;
    }
    
    .btn-outline-primary {
        background: transparent;
        border: 2px solid var(--primary);
        color: #a7c3ff; /* Lighter for better visibility */
    }
    
    .btn-outline-primary:hover {
        background: rgba(67, 97, 238, 0.15);
        transform: translateY(-3px);
        color: var(--primary);
    }
    
    .btn-outline-secondary {
        background: transparent;
        border: 2px solid #94a3b8;
        color: #cbd5e1; /* Lighter for better visibility */
    }
    
    .btn-outline-secondary:hover {
        background: rgba(148, 163, 184, 0.15);
        transform: translateY(-3px);
        color: #94a3b8;
    }
    
    /* === AI Summary with Enhanced Contrast === */
    .ai-summary {
        background: rgba(6, 214, 160, 0.15);
        border-left: 4px solid var(--accent);
        border-radius: 0 12px 12px 0;
        position: relative;
        overflow: hidden;
        color: var(--text-secondary);
    }
    
    .ai-summary:before {
        content: "";
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: linear-gradient(45deg, 
            rgba(6, 214, 160, 0.08) 0%, 
            rgba(6, 214, 160, 0.04) 100%);
        z-index: 0;
    }
    
    .ai-summary h6 {
        color: var(--accent);
        font-weight: 700;
        display: flex;
        align-items: center;
        gap: 10px;
    }
    
    /* === Chapter List with Better Contrast === */
    .chapter-list {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
        gap: 14px;
        margin-top: 1.2rem;
    }
    
    .chapter-item {
        background: rgba(255, 255, 255, 0.08);
        border: 1px solid rgba(255, 255, 255, 0.12);
        border-radius: 12px;
        padding: 1.2rem 1rem;
        text-align: center;
        transition: all 0.3s ease;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        height: 100%;
    }
    
    .chapter-item:hover {
        background: rgba(67, 97, 238, 0.15);
        border-color: var(--primary);
        transform: translateY(-3px);
        box-shadow: 0 5px 15px rgba(67, 97, 238, 0.25);
    }
    
    .chapter-item a {
        color: var(--text-primary);
        font-weight: 600;
        text-decoration: none;
        display: block;
        width: 100%;
        font-size: 1.05rem;
    }
    
    .chapter-badge {
        font-size: 0.8rem;
        padding: 0.3rem 0.7rem;
        border-radius: 50px;
        margin-top: 0.7rem;
    }
    
    /* === Volume Styles === */
    .volume-title {
        display: flex;
        align-items: center;
        gap: 12px;
        margin-top: 2.2rem;
        margin-bottom: 1.2rem;
        padding-bottom: 0.85rem;
        border-bottom: 2px solid rgba(255, 255, 255, 0.15);
    }
    
    .volume-icon {
        background: var(--gradient);
        width: 42px;
        height: 42px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 1.25rem;
    }
    
    /* === Comments Section with Enhanced Contrast === */
    .comments-list {
        display: grid;
        gap: 1.7rem;
        margin-top: 1.7rem;
    }
    
    .comment-item {
        background: rgba(255, 255, 255, 0.06);
        border-radius: 16px;
        padding: 1.7rem;
        border: 1px solid rgba(255, 255, 255, 0.1);
        transition: all 0.3s ease;
    }
    
    .comment-item:hover {
        background: rgba(255, 255, 255, 0.08);
        border-color: rgba(67, 97, 238, 0.35);
    }
    
    .comment-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 1.2rem;
    }
    
    .comment-author {
        font-weight: 600;
        color: var(--accent);
        font-size: 1.1rem;
    }
    
    .comment-date {
        color: var(--gray);
        font-size: 0.95rem;
    }
    
    .comment-content {
        line-height: 1.75;
        color: var(--text-secondary);
        font-size: 1.05rem;
    }
    
    .comment-actions .btn {
        padding: 0.5rem 1.2rem;
        font-size: 0.95rem;
    }
    
    /* === Responsive Adjustments === */
    @media (max-width: 768px) {
        .chapter-list {
            grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
        }
        
        .btn-lg {
            width: 100%;
            margin-bottom: 0.7rem;
        }
        
        .action-group {
            display: flex;
            flex-wrap: wrap;
            gap: 0.7rem;
        }
        
        .action-group .btn {
            flex: 1 1 auto;
            text-align: center;
            justify-content: center;
        }
        
        h2 {
            font-size: 2rem;
        }
        
        .container {
            padding: 1.5rem 1rem;
        }
    }
    
    /* === Utility Classes === */
    .mt-5 {
        margin-top: 3.5rem !important;
    }
    
    .mb-3 {
        margin-bottom: 1.2rem !important;
    }
    
    .py-4 {
        padding-top: 2.5rem !important;
        padding-bottom: 2.5rem !important;
    }
    
    .text-muted {
        color: var(--gray) !important;
    }
    
    .flex-wrap {
        flex-wrap: wrap;
    }
    
    /* === Animation Enhancements === */
    @keyframes fadeIn {
        from { opacity: 0; transform: translateY(20px); }
        to { opacity: 1; transform: translateY(0); }
    }
    
    .card, .chapter-item, .comment-item {
        animation: fadeIn 0.7s ease forwards;
    }
    
    /* === Additional Contrast Improvements === */
    .book-description p {
        color: var(--text-secondary) !important;
        font-size: 1.1rem;
        line-height: 1.85;
    }
    
    .alert {
        color: var(--text-secondary);
    }
    
    .alert-danger {
        background: rgba(220, 53, 69, 0.2);
        border-color: rgba(220, 53, 69, 0.5);
    }
    
    .alert-secondary {
        background: rgba(108, 117, 125, 0.2);
        border-color: rgba(108, 117, 125, 0.5);
    }
    
    .small {
        font-size: 0.95rem;
        color: var(--gray);
    }
    
    .text-primary {
        color: #a7c3ff !important; /* Lighter for better visibility */
    }
    </style>

<div class="container py-4">
    <!-- Book Detail Card -->
    <section id="book-detail" class="mb-5">
        <div class="book-detail-wrapper" style="display: flex; gap: 32px; flex-wrap: wrap; align-items: flex-start; max-width: 1000px; margin: 0 auto;">
            <!-- Left: Cover -->
            <div class="book-cover" style="flex: 0 0 220px; max-width: 220px; display: flex; align-items: center; justify-content: center;">
                <div class="cover-container w-100">
                    <c:choose>
                        <c:when test="${not empty ebook.coverUrl}">
                            <img src="${pageContext.request.contextPath}/${ebook.coverUrl}" alt="cover" class="img-fluid" style="width: 100%; border-radius: 12px;">
                        </c:when>
                        <c:otherwise>
                            <div class="d-flex flex-column align-items-center justify-content-center w-100 py-5">
                                <span class="display-1 text-secondary">📚</span>
                                <span class="text-muted mt-2">Không có ảnh bìa</span>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            <!-- Right: Info -->
            <div class="book-info" style="flex: 1 1 320px; min-width: 260px;">
                <h1 style="margin-top:0;">${ebook.title}</h1>
                <div class="book-tags" style="margin-bottom: 16px;">
                    <c:forEach var="t" items="${tags}">
                        <span class="badge badge-secondary me-2 mb-2">${t.name}</span>
                    </c:forEach>
                </div>
                <ul class="book-meta" style="list-style:none;padding:0;margin:0 0 18px 0;">
                    <li style="margin-bottom:8px;"><span class="me-2">📖</span> <b>Tác giả:</b> <c:forEach var="a" items="${authors}"><span class="text-primary me-2">${a.name}</span></c:forEach></li>
                    <li style="margin-bottom:8px;"><span class="me-2">📌</span> <b>Tình trạng:</b> ${ebook.status}</li>
                    <li style="margin-bottom:8px;"><span class="me-2">🔄</span> <b>Cập nhật:</b> <fmt:formatDate value="${ebookCreatedDate}" pattern="dd-MM-yyyy"/></li>
                    <li style="margin-bottom:8px;"><span class="me-2">👁️</span> <b>Lượt xem:</b> ${ebook.viewCount}</li>
                    <!-- Add more meta as needed -->
                </ul>
                <div class="book-actions" style="display:flex;flex-wrap:wrap;gap:12px 16px;margin-bottom:18px;">
                    <a href="${pageContext.request.contextPath}/book/read?bookId=${ebook.id}&chapterId=1" class="btn btn-success btn-lg"><i class="fas fa-book-open me-2"></i>Đọc từ đầu</a>
                    <c:if test="${sessionScope.user != null}">
                        <form method="post" action="${pageContext.request.contextPath}/favorites" style="display:inline;">
                            <input type="hidden" name="action" value="add"/>
                            <input type="hidden" name="ebookId" value="${ebook.id}"/>
                            <input type="hidden" name="redirectUrl" value="${pageContext.request.contextPath}/book/detail?id=${ebook.id}"/>
                            <c:choose>
                                <c:when test="${isFavorite}">
                                    <button type="submit" class="btn btn-danger" disabled><i class="fas fa-heart me-2"></i>Đã yêu thích</button>
                                </c:when>
                                <c:otherwise>
                                    <button type="submit" class="btn btn-outline-danger"><i class="far fa-heart me-2"></i>Theo dõi</button>
                                </c:otherwise>
                            </c:choose>
                        </form>
                    </c:if>
                    <a href="#" class="btn btn-outline-secondary"><i class="fas fa-exclamation-triangle me-2"></i>Báo lỗi</a>
                    <a href="#" class="btn btn-outline-primary"><i class="fas fa-gift me-2"></i>Tặng quà</a>
                    <a href="#" class="btn btn-outline-primary"><i class="fas fa-fire me-2"></i>Đề cử</a>
                </div>
                <!-- AI SUMMARY (optional, can be below actions or in description) -->
                <c:if test="${not empty ebook.summary}">
                    <div class="ai-summary p-3 mb-3">
                        <h6 class="mb-2"><i class="fas fa-robot"></i> Tóm tắt AI</h6>
                        <p class="mb-0">${ebook.summary}</p>
                    </div>
                </c:if>
            </div>
        </div>
        <!-- Description full width -->
        <div class="book-description mt-4" style="max-width:900px;margin:auto;">
            <h2 style="font-size:1.3em;margin-bottom:10px;">Giới thiệu</h2>
            <p>${ebook.description}</p>
        </div>
    </section>

    <!-- Comments Section -->
    <section id="book-comments" class="mt-5" style="max-width:1200px; margin:0 auto 48px auto; padding-left:16px; padding-right:16px;">
        <h4><i class="fas fa-comments"></i> Bình luận về sách</h4>
        <c:if test="${not empty ebook and not empty ebook.id}">
            <c:set var="bookId" value="${ebook.id}" />
            <jsp:include page="comments-book.jsp" />
        </c:if>
        <c:if test="${empty ebook or empty ebook.id}">
            <div class="alert alert-danger">Không tìm thấy sách hoặc ID không hợp lệ!</div>
        </c:if>
    </section>

    <!-- Chapter Comments -->
    <section id="chapter-comments" class="mt-5" style="max-width:1200px; margin:0 auto 48px auto; padding-left:16px; padding-right:16px;">
        <h4><i class="fas fa-comment-dots"></i> Bình luận từ các chương</h4>
        <c:if test="${empty aggregatedComments}">
            <div class="alert alert-secondary mt-3">
                <i class="fas fa-info-circle me-2"></i>Chưa có bình luận nào từ các chương.
            </div>
        </c:if>
        <c:if test="${not empty aggregatedComments}">
            <div class="comments-list">
                <c:forEach var="comment" items="${aggregatedComments}">
                    <div class="comment-item">
                        <div class="comment-header">
                            <span class="comment-author">User ${comment.userID}</span>
                            <span class="comment-date">${comment.createdAt}</span>
                        </div>
                        <div class="comment-content">
                            ${comment.content}
                        </div>
                        <div class="comment-actions mt-3">
                            <form method="post" style="display: inline;">
                                <input type="hidden" name="commentId" value="${comment.id}">
                                <button type="submit" class="btn btn-sm btn-outline-primary">
                                    <i class="fas fa-heart me-1"></i> ${comment.likeCount}
                                </button>
                            </form>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:if>
    </section>

    <!-- Chapter List -->
    <section id="chapter-list" class="mt-5" style="max-width:1200px; margin:0 auto 48px auto; padding-left:16px; padding-right:16px;">
        <h4><i class="fas fa-list-ol"></i> Danh sách chương</h4>
        <div class="chapter-list">
            <c:forEach var="ch" items="${chapters}">
                <div class="chapter-item">
                    <a href="${pageContext.request.contextPath}/book/read?bookId=${ebook.id}&chapterId=${ch.number}">
                        Chương ${ch.number}
                    </a>
                    <c:choose>
                        <c:when test="${ch.accessLevel == 'free' || ch.accessLevel == 'public'}">
                            <span class="badge bg-success chapter-badge">Miễn phí</span>
                        </c:when>
                        <c:otherwise>
                            <span class="badge bg-warning text-dark chapter-badge">Trả phí</span>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:forEach>
        </div>
    </section>
</div>

<%@ include file="/common/footer.jsp" %>

<script>
document.addEventListener('DOMContentLoaded', function() {
    // Add subtle animations to interactive elements
    const interactiveElements = document.querySelectorAll('.btn, .badge, .chapter-item');
    interactiveElements.forEach(el => {
        el.addEventListener('mouseenter', () => {
            el.style.transform = 'translateY(-3px)';
        });
        el.addEventListener('mouseleave', () => {
            el.style.transform = 'translateY(0)';
        });
    });
    
    // Add parallax effect to cover image
    const cover = document.querySelector('.cover-container');
    if(cover) {
        window.addEventListener('scroll', () => {
            const scrollPosition = window.pageYOffset;
            cover.style.transform = `translateY(${scrollPosition * 0.1}px)`;
        });
    }
});
</script>