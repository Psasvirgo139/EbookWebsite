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
    <link rel="icon" href="${ctx}/favicon.svg" type="image/svg+xml" />
    <link rel="icon" href="${ctx}/favicon.svg" type="image/x-icon" />
    <link rel="shortcut icon" href="${ctx}/favicon.svg" />
</head>
<body>
    <!-- Skip-link for Accessibility -->
    <a href="#main" class="skip-link">Bỏ qua và tới nội dung chính</a>

    <%@ include file="/common/header.jsp" %>

    <main id="main">
        <!-- HERO SECTION -->
        <section class="hero" id="home">
            <h1>
                Khám phá kho truyện <span class="highlight">khổng lồ</span> &amp;
                <span class="highlight">chất lượng</span>
            </h1>
            <p>Hơn 20 thể loại, hàng nghìn chương mới mỗi ngày.</p>
            <div class="hero-buttons">
                <a href="#storiesGrid" class="cta-btn">Bắt đầu đọc ngay</a>
                
                <!-- Upload Button for logged in users -->
                <c:if test="${not empty sessionScope.user}">
                    <a href="${ctx}/book?action=upload" class="cta-btn upload-btn" style="
                        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                        margin-left: 15px;
                        box-shadow: 0 8px 20px rgba(102, 126, 234, 0.3);
                    ">
                        📤 Upload Truyện Mới
                    </a>
                </c:if>
            </div>
        </section>

        <!-- STORIES FEATURED -->
        <section class="stories-list" aria-labelledby="featured-heading">
            <h2 id="featured-heading">Truyện nổi bật</h2>
            <div class="stories-grid" id="storiesGrid" aria-live="polite"></div>
            <div class="skeleton-loader" id="skeletonLoader"></div>
            
            <!-- VIEW ALL STORIES BUTTON -->
            <div class="view-all-section" style="text-align: center; margin-top: 30px; margin-bottom: 40px;">
                <a href="${ctx}/book-list" class="view-all-btn">
                    📚 Xem tất cả truyện
                </a>
            </div>
        </section>

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
