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
    <title>scroll | Đọc truyện online</title>

    <!-- Core CSS -->
    <link rel="stylesheet" href="${ctx}/assets/css/style.css" />

    <!-- Preconnect fonts -->
    <link rel="preconnect" href="https://fonts.gstatic.com" />
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <meta name="description" content="Scroll – Kho truyện lớn, cập nhật nhanh, đọc mượt trên mọi thiết bị." />
    <link rel="icon" href="${ctx}/favicon.ico" />
</head>
<body>
    <!-- Skip-link for Accessibility -->
    <a href="#main" class="skip-link">Bỏ qua và tới nội dung chính</a>

    <%@ include file="/common/header.jsp" %>

    <!-- Success Messages -->
    <c:if test="${param.success != null}">
        <div id="successMessage" style="
            position: fixed; 
            top: 80px; 
            right: 20px; 
            background: linear-gradient(135deg, #28a745, #34ce57); 
            color: white; 
            padding: 20px 25px; 
            border-radius: 12px; 
            box-shadow: 0 8px 25px rgba(40, 167, 69, 0.3); 
            z-index: 1000; 
            max-width: 400px;
            animation: slideInRight 0.5s ease-out;
        ">
            <div style="display: flex; align-items: center; gap: 10px;">
                <span style="font-size: 24px;">✅</span>
                <div>
                    <div style="font-weight: bold; margin-bottom: 5px;">Upload thành công!</div>
                    <div style="font-size: 14px; opacity: 0.9;">
                        <c:choose>
                            <c:when test="${param.success == 'smart_upload_completed'}">
                                Sách "<strong>${param.bookTitle}</strong>" đã được tạo từ file "${param.originalFile}" với AI processing hoàn tất.
                            </c:when>
                            <c:when test="${param.success == 'title_override_upload_completed'}">
                                Sách "<strong>${param.bookTitle}</strong>" đã được upload và xử lý bằng AI thành công.
                            </c:when>
                            <c:when test="${param.success == 'ai_upload_completed'}">
                                Sách "<strong>${param.bookTitle}</strong>" đã được upload và phân tích bằng AI hoàn tất.
                            </c:when>
                            <c:when test="${param.success == 'book_created'}">
                                Sách "<strong>${param.bookTitle}</strong>" đã được tạo thành công.
                            </c:when>
                            <c:otherwise>
                                Sách của bạn đã được xử lý thành công!
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <button onclick="document.getElementById('successMessage').style.display='none'" 
                        style="background: none; border: none; color: white; font-size: 20px; cursor: pointer; margin-left: 10px;">×</button>
            </div>
        </div>
        <style>
            @keyframes slideInRight {
                from {
                    transform: translateX(100%);
                    opacity: 0;
                }
                to {
                    transform: translateX(0);
                    opacity: 1;
                }
            }
        </style>
        <script>
            // Auto hide success message after 8 seconds
            setTimeout(function() {
                var msg = document.getElementById('successMessage');
                if (msg) {
                    msg.style.animation = 'slideInRight 0.5s ease-out reverse';
                    setTimeout(function() {
                        msg.style.display = 'none';
                    }, 500);
                }
            }, 8000);
        </script>
    </c:if>

    <main id="main">
        <!-- HERO SECTION -->
        <section class="hero" id="home">
            <h1>
                Khám phá kho truyện <span class="highlight">khổng lồ</span> &amp;
                <span class="highlight">chất lượng</span>
            </h1>
            <p>Hơn 20 thể loại, hàng nghìn chương mới mỗi ngày.</p>
            <div style="display: flex; gap: 15px; justify-content: center; flex-wrap: wrap; margin-top: 25px;">
                <a href="#storiesGrid" class="cta-btn">Bắt đầu đọc ngay</a>
                <c:if test="${not empty sessionScope.user}">
                    <a href="${ctx}/book/upload" class="cta-btn" style="background: linear-gradient(45deg, #28a745, #34ce57); box-shadow: 0 4px 15px rgba(40, 167, 69, 0.3);">
                        📚 Upload Sách của bạn
                    </a>
                </c:if>
            </div>
        </section>

        <!-- STORIES FEATURED -->
        <section class="stories-list" aria-labelledby="featured-heading">
            <h2 id="featured-heading">Truyện nổi bật</h2>
            <div class="stories-grid" id="storiesGrid" aria-live="polite">
                <c:forEach var="book" items="${featuredBooks}">
                    <div class="story-card">
                        <img class="story-thumb" src="${book.coverUrl}" alt="${book.title}" />
                        <div class="story-content">
                            <a class="story-title" href="${pageContext.request.contextPath}/book/detail?id=${book.id}">${book.title}</a>
                            <div class="story-desc">${book.description}</div>
                            <div class="story-info"><span>Lượt xem: ${book.viewCount}</span></div>
                            <a class="read-btn" href="${pageContext.request.contextPath}/book/detail?id=${book.id}">Đọc</a>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </section>

        <!-- UPLOAD CALL-TO-ACTION (for logged-in users) -->
        <c:if test="${not empty sessionScope.user}">
            <section class="upload-cta" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 60px 20px; margin: 40px 0; text-align: center; border-radius: 20px;">
                <h2 style="margin-bottom: 20px; font-size: 2.2em;">🚀 Chia sẻ tác phẩm của bạn!</h2>
                <p style="font-size: 1.2em; margin-bottom: 30px; opacity: 0.9;">
                    Upload sách, truyện, tiểu thuyết của bạn và tiếp cận hàng nghìn độc giả. 
                    Hệ thống AI sẽ tự động phân tích và tối ưu nội dung.
                </p>
                <div style="display: flex; gap: 20px; justify-content: center; flex-wrap: wrap;">
                    <a href="${ctx}/book/upload" 
                       style="background: rgba(255,255,255,0.2); color: white; padding: 15px 30px; text-decoration: none; border-radius: 25px; font-weight: 600; border: 2px solid rgba(255,255,255,0.3); transition: all 0.3s ease;"
                       onmouseover="this.style.background='rgba(255,255,255,0.3)'; this.style.transform='translateY(-2px)'"
                       onmouseout="this.style.background='rgba(255,255,255,0.2)'; this.style.transform='translateY(0)'">
                        📚 Upload Sách Ngay
                    </a>
                    <a href="${ctx}/ai.jsp" 
                       style="background: transparent; color: white; padding: 15px 30px; text-decoration: none; border-radius: 25px; font-weight: 600; border: 2px solid rgba(255,255,255,0.5); transition: all 0.3s ease;"
                       onmouseover="this.style.background='rgba(255,255,255,0.1)'; this.style.transform='translateY(-2px)'"
                       onmouseout="this.style.background='transparent'; this.style.transform='translateY(0)'">
                        🤖 Khám phá AI Tools
                    </a>
                </div>
                <div style="margin-top: 25px; font-size: 0.9em; opacity: 0.8;">
                    ✨ Hỗ trợ format: PDF, DOCX, TXT | 🤖 AI tự động phân tích nội dung | 💰 Kiếm coin từ độc giả
                </div>
            </section>
        </c:if>

        <!-- STORIES UPDATE -->
        <section class="stories-update" id="latest" aria-labelledby="update-heading">
            <div class="update-header">
                <h2 id="update-heading">Truyện mới cập nhật</h2>
                <button class="filter-btn" id="filterUpdate" aria-label="Lọc truyện mới"></button>
            </div>
            <div class="stories-update-grid" id="updateGrid" aria-live="polite"></div>
            <div class="skeleton-loader" id="updateSkeleton"></div>
        </section>
    </main>

    <!-- FOOTER -->
    <%@ include file="/common/footer.jsp" %>

    <!-- DYNAMIC MODALS -->
    <dialog class="modal" id="modalAuth" aria-modal="true">
        <form method="dialog" class="modal-content" id="authForm" novalidate>
            <button class="close" id="closeModal" aria-label="Đóng">×</button>
            <h3 id="modalTitle">Đăng nhập</h3>
            <input type="text" id="authUser" placeholder="Tên đăng nhập" required />
            <input type="password" id="authPass" placeholder="Mật khẩu" required />
            <button type="submit" id="submitAuth">Đăng nhập</button>
            <p class="switch-auth">
                Chưa có tài khoản?
                <a href="#" id="switchToRegister">Đăng ký</a>
            </p>
        </form>
    </dialog>

    <!-- Core JS -->
    <script src="${ctx}/assets/js/app.js" defer></script>

    <!-- Quick enhancement: đóng menu mobile khi chọn link -->
    <script>
      document.querySelectorAll('.main-nav a').forEach((l) =>
        l.addEventListener('click', () => {
          document.getElementById('mainNav').classList.remove('show-mobile');
        })
      );
    </script>
</body>
</html>
