<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ include file="/common/header.jsp" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${ebook.title} - Chương ${currentChapter}</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        /* === Modern Tech Design System - Enhanced Contrast === */
        :root {
            --primary: #4361ee;
            --primary-dark: #3a56d4;
            --secondary: #7209b7;
            --accent: #06d6a0;
            --dark: #0f172a;
            --darker: #0a101f;
            --light: #f8f9ff;
            --text-primary: #ffffff;
            --text-secondary: #e2e8f0;
            --gray: #94a3b8;
            --gradient: linear-gradient(135deg, var(--primary), var(--secondary));
            --card-bg: rgba(23, 30, 51, 0.9);
            --card-border: rgba(255, 255, 255, 0.15);
            --shadow: 0 8px 32px rgba(0, 0, 0, 0.5);
            --glow: 0 0 20px rgba(67, 97, 238, 0.5);
        }
        
        body {
            background: linear-gradient(135deg, var(--darker), var(--dark));
            color: var(--text-primary);
            font-family: 'Segoe UI', 'PingFang SC', 'Microsoft YaHei', sans-serif;
            min-height: 100vh;
            background-attachment: fixed;
            line-height: 1.7;
        }
        
        .container {
            max-width: 900px;
            padding: 2rem 1.5rem;
            margin: 0 auto;
        }
        
        /* === Typography === */
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
            border-bottom: 2px solid rgba(255, 255, 255, 0.15);
            display: flex;
            align-items: center;
        }
        
        h4 i {
            margin-right: 10px;
            color: var(--accent);
        }
        
        h5, h6 {
            font-weight: 700;
            color: var(--text-primary);
        }
        
        /* === Navigation Buttons === */
        .chapter-nav {
            display: flex;
            justify-content: space-between;
            margin: 2rem 0;
        }
        
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
        
        .btn-outline-primary {
            background: transparent;
            border: 2px solid var(--primary);
            color: #a7c3ff;
        }
        
        .btn-outline-primary:hover {
            background: rgba(67, 97, 238, 0.15);
            transform: translateY(-3px);
            color: var(--primary);
            box-shadow: 0 5px 15px rgba(67, 97, 238, 0.25);
        }
        
        .btn-primary {
            background: var(--primary);
            color: white;
        }
        
        .btn-primary:hover {
            background: var(--primary-dark);
            transform: translateY(-3px);
            box-shadow: 0 7px 25px rgba(67, 97, 238, 0.5);
        }
        
        .btn-warning {
            background: #f59e0b;
            color: #0a0a0a;
        }
        
        .btn-warning:hover {
            background: #d97706;
            transform: translateY(-3px);
            box-shadow: 0 7px 25px rgba(245, 158, 11, 0.5);
        }
        
        /* === Chapter Content === */
        .chapter-content {
            background: var(--card-bg);
            backdrop-filter: blur(12px);
            border: 1px solid var(--card-border);
            border-radius: 16px;
            padding: 2.5rem;
            margin: 2rem 0;
            box-shadow: var(--shadow);
            line-height: 1.85;
            font-size: 1.15rem;
            color: var(--text-secondary);
            animation: fadeIn 0.7s ease forwards;
        }
        
        .chapter-content p {
            margin-bottom: 1.5rem;
            text-align: justify;
        }
        
        /* === Premium Unlock Section === */
        .unlock-section {
            margin: 3rem 0;
        }
        
        .card {
            background: var(--card-bg);
            backdrop-filter: blur(12px);
            border: 1px solid var(--card-border);
            border-radius: 16px;
            box-shadow: var(--shadow);
            transition: all 0.3s ease;
            overflow: hidden;
            height: 100%;
            animation: fadeIn 0.7s ease forwards;
        }
        
        .card:hover {
            transform: translateY(-5px);
            box-shadow: var(--shadow), var(--glow);
        }
        
        .card-body {
            padding: 1.8rem;
        }
        
        .card-title {
            font-size: 1.25rem;
            font-weight: 700;
            margin-bottom: 1rem;
            color: var(--accent);
            display: flex;
            align-items: center;
            gap: 10px;
        }
        
        /* === Chapter List === */
        .chapter-list-container {
            margin: 3rem 0;
        }
        
        .volume-title {
            display: flex;
            align-items: center;
            gap: 12px;
            margin-top: 2rem;
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
        
        .pagination {
            display: flex;
            flex-wrap: wrap;
            gap: 8px;
            margin: 1rem 0 2rem;
            padding: 0;
            list-style: none;
        }
        
        .page-item {
            margin: 0;
        }
        
        .page-link {
            background: rgba(255, 255, 255, 0.08);
            border: 1px solid rgba(255, 255, 255, 0.12);
            border-radius: 12px !important;
            color: var(--text-primary);
            padding: 0.8rem 1.2rem;
            transition: all 0.3s ease;
            display: flex;
            align-items: center;
            gap: 6px;
            text-decoration: none;
        }
        
        .page-link:hover {
            background: rgba(67, 97, 238, 0.15);
            border-color: var(--primary);
            transform: translateY(-3px);
            box-shadow: 0 5px 15px rgba(67, 97, 238, 0.25);
        }
        
        .page-item.active .page-link {
            background: rgba(67, 97, 238, 0.25);
            border-color: var(--primary);
            color: var(--primary);
            box-shadow: 0 0 15px rgba(67, 97, 238, 0.3);
        }
        
        .badge {
            font-weight: 600;
            padding: 0.4em 0.8em;
            border-radius: 50px;
            font-size: 0.85rem;
            letter-spacing: 0.5px;
            margin-left: 0.5rem;
        }
        
        .badge-success {
            background: rgba(6, 214, 160, 0.25);
            border: 1px solid var(--accent);
            color: #d4fdf0;
        }
        
        .badge-warning {
            background: rgba(245, 158, 11, 0.25);
            border: 1px solid #f59e0b;
            color: #fef3c7;
        }
        
        /* === Alerts === */
        .alert {
            border-radius: 12px;
            padding: 1.2rem 1.5rem;
            margin: 1.5rem 0;
            border: 1px solid transparent;
        }
        
        .alert-success {
            background: rgba(6, 214, 160, 0.15);
            border-color: rgba(6, 214, 160, 0.3);
            color: #d4fdf0;
        }
        
        .alert-danger {
            background: rgba(220, 53, 69, 0.15);
            border-color: rgba(220, 53, 69, 0.3);
            color: #fecaca;
        }
        
        .alert-warning {
            background: rgba(245, 158, 11, 0.15);
            border-color: rgba(245, 158, 11, 0.3);
            color: #fef3c7;
        }
        
        .alert i {
            margin-right: 10px;
        }
        
        .btn-close {
            filter: invert(1);
        }
        
        /* === Utility Classes === */
        .text-center {
            text-align: center;
        }
        
        .mt-3 {
            margin-top: 1rem !important;
        }
        
        .mt-4 {
            margin-top: 1.5rem !important;
        }
        
        .mt-5 {
            margin-top: 3rem !important;
        }
        
        .mb-3 {
            margin-bottom: 1rem !important;
        }
        
        .d-flex {
            display: flex;
        }
        
        .justify-content-between {
            justify-content: space-between;
        }
        
        .ms-auto {
            margin-left: auto;
        }
        
        /* === Animation === */
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(20px); }
            to { opacity: 1; transform: translateY(0); }
        }
        
        /* === Responsive Design === */
        @media (max-width: 768px) {
            .container {
                padding: 1.5rem 1rem;
            }
            
            h2 {
                font-size: 1.8rem;
            }
            
            h4 {
                font-size: 1.3rem;
            }
            
            .chapter-content {
                padding: 1.5rem;
                font-size: 1.05rem;
            }
            
            .btn {
                padding: 0.7rem 1.5rem;
                font-size: 0.95rem;
            }
            
            .page-link {
                padding: 0.6rem 0.9rem;
                font-size: 0.9rem;
            }
            
            .card-body {
                padding: 1.2rem;
            }
        }
    </style>
</head>
<body>
<div class="container py-4">
    <h2>${ebook.title}</h2>
    <h4><i class="fas fa-book-open"></i> Chương ${currentChapter}</h4>

    <!-- Hiển thị thông báo -->
    <c:if test="${not empty successMessage}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="fas fa-check-circle"></i> ${successMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>
    
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="fas fa-exclamation-triangle"></i> ${errorMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <!-- Chapter Navigation -->
    <div class="d-flex justify-content-between mb-3">
        <c:if test="${prevChapter != null}">
            <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/book/read?bookId=${ebook.id}&chapterId=${prevChapter}">
                <i class="fas fa-arrow-left"></i> Ch ${prevChapter}
            </a>
        </c:if>
        <c:if test="${nextChapter != null}">
            <a class="btn btn-outline-primary ms-auto" href="${pageContext.request.contextPath}/book/read?bookId=${ebook.id}&chapterId=${nextChapter}">
                Ch ${nextChapter} <i class="fas fa-arrow-right"></i>
            </a>
        </c:if>
    </div>

    <c:choose>
        <c:when test="${hasAccess}">
            <div class="chapter-content">
                <p style="white-space: pre-line;">${chapter.content}</p>
            </div>
        </c:when>
        <c:when test="${needUnlock}">
            <!-- UI cho chapter premium cần unlock -->
            <div class="unlock-section">
                <div class="alert alert-warning">
                    <h5><i class="fas fa-lock"></i> Chapter Premium</h5>
                    <p><strong>Bạn chưa mở khóa chapter này!</strong></p>
                    <p>Chapter này yêu cầu mở khóa để đọc nội dung.</p>
                </div>
                
                <div class="row mt-3">
                    <div class="col-md-6">
                        <div class="card">
                            <div class="card-body text-center">
                                <h6 class="card-title"><i class="fas fa-coins"></i> Mở khóa với Coin</h6>
                                <p class="card-text">
                                    <strong>Phí: ${unlockCost} coins</strong><br>
                                    <small>Bạn hiện có: ${userCoins} coins</small>
                                </p>
                                <c:choose>
                                    <c:when test="${userCoins >= unlockCost}">
                                        <form method="post" action="${pageContext.request.contextPath}/book/read" 
                                              onsubmit="this.querySelector('button').innerHTML='<i class=&quot;fas fa-spinner fa-spin&quot;></i> Đang mở khóa...'; this.querySelector('button').disabled=true;">
                                            <input type="hidden" name="action" value="unlock">
                                            <input type="hidden" name="bookId" value="${ebook.id}">
                                            <input type="hidden" name="chapterNum" value="${currentChapter}">
                                            <input type="hidden" name="chapterId" value="${chapter.id}">
                                            <button type="submit" class="btn btn-primary">
                                                <i class="fas fa-unlock"></i> Mở khóa (${unlockCost} coins)
                                            </button>
                                        </form>
                                        <small class="text-muted mt-2">
                                            Sau khi mở khóa, bạn sẽ có ${userCoins - unlockCost} coins
                                        </small>
                                    </c:when>
                                    <c:otherwise>
                                        <button class="btn btn-secondary" disabled>
                                            <i class="fas fa-coins"></i> Không đủ coin
                                        </button>
                                        <small class="text-muted d-block mt-2">
                                            Bạn cần thêm ${unlockCost - userCoins} coins
                                        </small>
                                        <a href="${pageContext.request.contextPath}/coin/payment" 
                                           class="btn btn-sm btn-outline-primary mt-2">
                                            <i class="fas fa-wallet"></i> Nạp coin
                                        </a>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="card">
                            <div class="card-body text-center">
                                <h6 class="card-title"><i class="fas fa-crown"></i> Trở thành Premium User</h6>
                                <p class="card-text">
                                    <strong>10.000 VND/tháng</strong><br>
                                    <small>Xem tất cả chapter premium</small>
                                </p>
                                <a href="${pageContext.request.contextPath}/coin/payment" class="btn btn-warning">
                                    <i class="fas fa-crown"></i> Nâng cấp Premium
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="alert alert-danger mt-3">
                <h5><i class="fas fa-exclamation-triangle"></i> Không có quyền truy cập</h5>
                <p>Bạn cần đăng nhập để xem chapter này.</p>
                <a href="${pageContext.request.contextPath}/user/login.jsp" class="btn btn-primary">
                    <i class="fas fa-sign-in-alt"></i> Đăng nhập
                </a>
            </div>
        </c:otherwise>
    </c:choose>

    <!-- Chapter List -->
    <div class="chapter-list-container">
        <h5><i class="fas fa-list-ol"></i> Danh sách chương</h5>
        <!-- Nếu có volumes -->
        <c:if test="${not empty volumes and fn:length(volumes) > 1}">
            <c:forEach var="vol" items="${volumes}">
                <div class="volume-title">
                    <div class="volume-icon">
                        <i class="fas fa-book"></i>
                    </div>
                    <h6>Tập ${vol.number}: ${vol.title}</h6>
                </div>
                <ul class="pagination">
                    <c:forEach var="ch" items="${chapters}">
                        <c:if test="${ch.volumeID == vol.id}">
                            <li class="page-item ${ch.number == currentChapter ? 'active' : ''}">
                                <a class="page-link" href="${pageContext.request.contextPath}/book/read?bookId=${ebook.id}&chapterId=${ch.number}">
                                    Ch ${ch.number}
                                    <c:choose>
                                        <c:when test="${ch.accessLevel == 'free' || ch.accessLevel == 'public'}">
                                            <span class="badge badge-success">Free</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge badge-warning">Premium</span>
                                            <!-- Hiển thị trạng thái unlock cho chapter premium -->
                                            <c:if test="${user != null && ch.accessLevel == 'premium'}">
                                                <!-- Kiểm tra chapter đã unlock chưa -->
                                                <c:set var="isUnlocked" value="false" />
                                                <c:forEach var="unlockedCh" items="${userUnlockedChapters}">
                                                    <c:if test="${unlockedCh.chapterId == ch.id}">
                                                        <c:set var="isUnlocked" value="true" />
                                                    </c:if>
                                                </c:forEach>
                                                <c:choose>
                                                    <c:when test="${isUnlocked}">
                                                        <i class="fas fa-unlock text-success ms-1" title="Đã mở khóa"></i>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <i class="fas fa-lock text-warning ms-1" title="Chưa mở khóa"></i>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:if>
                                        </c:otherwise>
                                    </c:choose>
                                </a>
                            </li>
                        </c:if>
                    </c:forEach>
                </ul>
            </c:forEach>
        </c:if>
        <!-- Nếu không có volume hoặc chỉ 1 volume -->
        <c:if test="${empty volumes or fn:length(volumes) <= 1}">
            <ul class="pagination">
                <c:forEach var="ch" items="${chapters}">
                    <li class="page-item ${ch.number == currentChapter ? 'active' : ''}">
                        <a class="page-link" href="${pageContext.request.contextPath}/book/read?bookId=${ebook.id}&chapterId=${ch.number}">
                            Ch ${ch.number}
                            <c:choose>
                                <c:when test="${ch.accessLevel == 'free' || ch.accessLevel == 'public'}">
                                    <span class="badge badge-success">Free</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge badge-warning">Premium</span>
                                    <!-- Hiển thị trạng thái unlock cho chapter premium -->
                                    <c:if test="${user != null && ch.accessLevel == 'premium'}">
                                        <!-- Kiểm tra chapter đã unlock chưa -->
                                        <c:set var="isUnlocked" value="false" />
                                        <c:forEach var="unlockedCh" items="${userUnlockedChapters}">
                                            <c:if test="${unlockedCh.chapterId == ch.id}">
                                                <c:set var="isUnlocked" value="true" />
                                            </c:if>
                                        </c:forEach>
                                        <c:choose>
                                            <c:when test="${isUnlocked}">
                                                <i class="fas fa-unlock text-success ms-1" title="Đã mở khóa"></i>
                                            </c:when>
                                            <c:otherwise>
                                                <i class="fas fa-lock text-warning ms-1" title="Chưa mở khóa"></i>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:if>
                                </c:otherwise>
                            </c:choose>
                        </a>
                    </li>
                </c:forEach>
            </ul>
        </c:if>
    </div>

    <!-- Chapter Navigation Bottom -->
    <div class="d-flex justify-content-between mt-4">
        <c:if test="${prevChapter != null}">
            <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/book/read?bookId=${ebook.id}&chapterId=${prevChapter}">
                <i class="fas fa-arrow-left"></i> Ch ${prevChapter}
            </a>
        </c:if>
        <c:if test="${nextChapter != null}">
            <a class="btn btn-outline-primary ms-auto" href="${pageContext.request.contextPath}/book/read?bookId=${ebook.id}&chapterId=${nextChapter}">
                Ch ${nextChapter} <i class="fas fa-arrow-right"></i>
            </a>
        </c:if>
    </div>

    <!-- Bình luận về chương -->
    <div class="mt-5">
        <c:set var="bookId" value="${ebook.id}" />
        <jsp:include page="comments-chapter.jsp" />
    </div>
</div>

<%@ include file="/common/footer.jsp" %>
</body>
</html>