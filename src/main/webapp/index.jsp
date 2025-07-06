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

        <!-- AI FEATURES SECTION -->
        <section class="ai-features" aria-labelledby="ai-heading" style="padding: 60px 0; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white;">
            <div class="container" style="max-width: 1200px; margin: 0 auto; padding: 0 20px;">
                <h2 id="ai-heading" style="text-align: center; margin-bottom: 50px; font-size: 2.5rem; font-weight: 700;">
                    🤖 Tính năng AI thông minh
                </h2>
                <p style="text-align: center; margin-bottom: 40px; font-size: 1.2rem; opacity: 0.9;">
                    Khám phá những tính năng AI tiên tiến giúp bạn đọc truyện thông minh hơn
                </p>
                
                <div class="ai-grid" style="display: grid; grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); gap: 30px; margin-top: 40px;">
                    
                    <!-- AI Chat -->
                    <div class="ai-card" style="background: rgba(255,255,255,0.1); backdrop-filter: blur(10px); border-radius: 20px; padding: 30px; text-align: center; transition: all 0.3s ease; border: 1px solid rgba(255,255,255,0.2);">
                        <div class="ai-icon" style="font-size: 3rem; margin-bottom: 20px;">💬</div>
                        <h3 style="margin-bottom: 15px; font-size: 1.4rem;">AI Chat Trợ Lý</h3>
                        <p style="opacity: 0.8; margin-bottom: 25px; line-height: 1.6;">
                            Trò chuyện với AI để nhận tư vấn sách, tìm kiếm thông tin về tác giả và thể loại
                        </p>
                        <a href="${ctx}/ai/chat" class="ai-btn" style="
                            display: inline-block; 
                            padding: 12px 24px; 
                            background: rgba(255,255,255,0.2); 
                            border: 2px solid rgba(255,255,255,0.3);
                            border-radius: 25px; 
                            color: white; 
                            text-decoration: none; 
                            font-weight: 600;
                            transition: all 0.3s ease;
                        ">
                            Bắt đầu chat
                        </a>
                    </div>

                    <!-- AI Recommendations -->
                    <div class="ai-card" style="background: rgba(255,255,255,0.1); backdrop-filter: blur(10px); border-radius: 20px; padding: 30px; text-align: center; transition: all 0.3s ease; border: 1px solid rgba(255,255,255,0.2);">
                        <div class="ai-icon" style="font-size: 3rem; margin-bottom: 20px;">🎯</div>
                        <h3 style="margin-bottom: 15px; font-size: 1.4rem;">AI Đề Xuất Thông Minh</h3>
                        <p style="opacity: 0.8; margin-bottom: 25px; line-height: 1.6;">
                            Nhận đề xuất sách cá nhân hóa dựa trên sở thích và lịch sử đọc của bạn
                        </p>
                        <a href="${ctx}/ai/recommendations" class="ai-btn" style="
                            display: inline-block; 
                            padding: 12px 24px; 
                            background: rgba(255,255,255,0.2); 
                            border: 2px solid rgba(255,255,255,0.3);
                            border-radius: 25px; 
                            color: white; 
                            text-decoration: none; 
                            font-weight: 600;
                            transition: all 0.3s ease;
                        ">
                            Khám phá ngay
                        </a>
                    </div>

                    <!-- Smart Recommendations -->
                    <div class="ai-card" style="background: rgba(255,255,255,0.1); backdrop-filter: blur(10px); border-radius: 20px; padding: 30px; text-align: center; transition: all 0.3s ease; border: 1px solid rgba(255,255,255,0.2);">
                        <div class="ai-icon" style="font-size: 3rem; margin-bottom: 20px;">🚀</div>
                        <h3 style="margin-bottom: 15px; font-size: 1.4rem;">Smart AI Nâng Cao</h3>
                        <p style="opacity: 0.8; margin-bottom: 25px; line-height: 1.6;">
                            Hệ thống AI tiên tiến với bộ lọc thông minh theo tâm trạng và độ phức tạp
                        </p>
                        <a href="${ctx}/ai/smart-recommendations" class="ai-btn" style="
                            display: inline-block; 
                            padding: 12px 24px; 
                            background: rgba(255,255,255,0.2); 
                            border: 2px solid rgba(255,255,255,0.3);
                            border-radius: 25px; 
                            color: white; 
                            text-decoration: none; 
                            font-weight: 600;
                            transition: all 0.3s ease;
                        ">
                            Trải nghiệm
                        </a>
                    </div>

                    <!-- AI Upload (for logged in users) -->
                    <c:if test="${not empty sessionScope.user}">
                        <div class="ai-card" style="background: rgba(255,255,255,0.1); backdrop-filter: blur(10px); border-radius: 20px; padding: 30px; text-align: center; transition: all 0.3s ease; border: 1px solid rgba(255,255,255,0.2);">
                            <div class="ai-icon" style="font-size: 3rem; margin-bottom: 20px;">📤</div>
                            <h3 style="margin-bottom: 15px; font-size: 1.4rem;">Upload Truyện Thông Minh</h3>
                            <p style="opacity: 0.8; margin-bottom: 25px; line-height: 1.6;">
                                Upload truyện với 2 chế độ: Thủ công hoặc AI tự động phân tích nội dung
                            </p>
                            <a href="${ctx}/book?action=upload" class="ai-btn" style="
                                display: inline-block; 
                                padding: 12px 24px; 
                                background: rgba(255,255,255,0.2); 
                                border: 2px solid rgba(255,255,255,0.3);
                                border-radius: 25px; 
                                color: white; 
                                text-decoration: none; 
                                font-weight: 600;
                                transition: all 0.3s ease;
                            ">
                                Upload ngay
                            </a>
                        </div>
                    </c:if>

                    <!-- AI Book Upload with Content Moderation -->
                    <c:if test="${not empty sessionScope.user}">
                        <div class="ai-card" style="background: rgba(255,255,255,0.1); backdrop-filter: blur(10px); border-radius: 20px; padding: 30px; text-align: center; transition: all 0.3s ease; border: 1px solid rgba(255,255,255,0.2);">
                            <div class="ai-icon" style="font-size: 3rem; margin-bottom: 20px;">🤖</div>
                            <h3 style="margin-bottom: 15px; font-size: 1.4rem;">AI Upload với Kiểm Duyệt</h3>
                            <p style="opacity: 0.8; margin-bottom: 25px; line-height: 1.6;">
                                Upload sách với AI kiểm duyệt nội dung và trích xuất metadata tự động
                            </p>
                            <a href="${ctx}/ai-upload/upload-form.jsp" class="ai-btn" style="
                                display: inline-block; 
                                padding: 12px 24px; 
                                background: rgba(255,255,255,0.2); 
                                border: 2px solid rgba(255,255,255,0.3);
                                border-radius: 25px; 
                                color: white; 
                                text-decoration: none; 
                                font-weight: 600;
                                transition: all 0.3s ease;
                            ">
                                AI Upload
                            </a>
                        </div>
                    </c:if>

                    <!-- Prompt Training (for logged in users) -->
                    <c:if test="${not empty sessionScope.user}">
                        <div class="ai-card" style="background: rgba(255,255,255,0.1); backdrop-filter: blur(10px); border-radius: 20px; padding: 30px; text-align: center; transition: all 0.3s ease; border: 1px solid rgba(255,255,255,0.2);">
                            <div class="ai-icon" style="font-size: 3rem; margin-bottom: 20px;">🧠</div>
                            <h3 style="margin-bottom: 15px; font-size: 1.4rem;">AI Prompt Training</h3>
                            <p style="opacity: 0.8; margin-bottom: 25px; line-height: 1.6;">
                                Huấn luyện và tối ưu hóa AI để phù hợp với sở thích cá nhân của bạn
                            </p>
                            <a href="${ctx}/ai/prompt-training" class="ai-btn" style="
                                display: inline-block; 
                                padding: 12px 24px; 
                                background: rgba(255,255,255,0.2); 
                                border: 2px solid rgba(255,255,255,0.3);
                                border-radius: 25px; 
                                color: white; 
                                text-decoration: none; 
                                font-weight: 600;
                                transition: all 0.3s ease;
                            ">
                                Huấn luyện AI
                            </a>
                        </div>
                    </c:if>

                    <!-- Internal AI Chat -->
                    <div class="ai-card" style="background: rgba(255,255,255,0.1); backdrop-filter: blur(10px); border-radius: 20px; padding: 30px; text-align: center; transition: all 0.3s ease; border: 1px solid rgba(255,255,255,0.2);">
                        <div class="ai-icon" style="font-size: 3rem; margin-bottom: 20px;">🔒</div>
                        <h3 style="margin-bottom: 15px; font-size: 1.4rem;">AI Chat Nội Bộ</h3>
                        <p style="opacity: 0.8; margin-bottom: 25px; line-height: 1.6;">
                            Chat AI hoàn toàn nội bộ, bảo mật tuyệt đối với dữ liệu chỉ từ cơ sở dữ liệu
                        </p>
                        <a href="${ctx}/ai/internal-chat" class="ai-btn" style="
                            display: inline-block; 
                            padding: 12px 24px; 
                            background: rgba(255,255,255,0.2); 
                            border: 2px solid rgba(255,255,255,0.3);
                            border-radius: 25px; 
                            color: white; 
                            text-decoration: none; 
                            font-weight: 600;
                            transition: all 0.3s ease;
                        ">
                            Chat an toàn
                        </a>
                    </div>

                </div>
            </div>

            <!-- AI Features Enhancement Effects -->
            <style>
                .ai-card:hover {
                    transform: translateY(-10px);
                    background: rgba(255,255,255,0.2) !important;
                    box-shadow: 0 20px 40px rgba(0,0,0,0.2);
                }
                
                .ai-btn:hover {
                    background: rgba(255,255,255,0.3) !important;
                    border-color: rgba(255,255,255,0.5) !important;
                    transform: translateY(-2px);
                    box-shadow: 0 10px 25px rgba(0,0,0,0.2);
                }
                
                .ai-icon {
                    animation: float 3s ease-in-out infinite;
                }
                
                @keyframes float {
                    0%, 100% { transform: translateY(0px); }
                    50% { transform: translateY(-10px); }
                }
                
                @media (max-width: 768px) {
                    .ai-grid {
                        grid-template-columns: 1fr !important;
                        gap: 20px !important;
                    }
                    
                    .ai-features h2 {
                        font-size: 2rem !important;
                    }
                }
            </style>
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
