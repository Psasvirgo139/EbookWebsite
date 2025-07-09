<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Trang cá nhân | EBook Website</title>
        <!-- Google Fonts -->
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
        <!-- Font Awesome Icons -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <style>
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }
            
            body {
                font-family: 'Inter', sans-serif;
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                min-height: 100vh;
                color: #333;
                line-height: 1.6;
            }
            
            .profile-container {
                max-width: 1200px;
                margin: 0 auto;
                padding: 20px;
            }
            
            /* Header Section */
            .profile-header {
                background: rgba(255, 255, 255, 0.95);
                backdrop-filter: blur(10px);
                border-radius: 20px;
                padding: 40px;
                margin-bottom: 30px;
                box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
                position: relative;
                overflow: hidden;
            }
            
            .profile-header::before {
                content: '';
                position: absolute;
                top: 0;
                left: 0;
                right: 0;
                height: 4px;
                background: linear-gradient(90deg, #667eea, #764ba2, #f093fb);
            }
            
            .header-content {
                display: flex;
                align-items: center;
                gap: 30px;
                flex-wrap: wrap;
            }
            
            .avatar-section {
                text-align: center;
                flex-shrink: 0;
            }
            
            .avatar {
                width: 120px;
                height: 120px;
                border-radius: 50%;
                object-fit: cover;
                border: 4px solid #fff;
                box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
                transition: transform 0.3s ease;
            }
            
            .avatar:hover {
                transform: scale(1.05);
            }
            
            .user-info {
                flex: 1;
                min-width: 300px;
            }
            
            .username {
                font-size: 2.5rem;
                font-weight: 800;
                color: #2d3748;
                margin-bottom: 8px;
                background: linear-gradient(135deg, #667eea, #764ba2);
                -webkit-background-clip: text;
                -webkit-text-fill-color: transparent;
                background-clip: text;
            }
            
            .role-badge {
                display: inline-block;
                background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
                color: white;
                padding: 6px 16px;
                border-radius: 20px;
                font-size: 0.9rem;
                font-weight: 600;
                text-transform: uppercase;
                letter-spacing: 0.5px;
                margin-bottom: 20px;
            }
            
            .user-stats {
                display: flex;
                gap: 30px;
                margin-bottom: 25px;
                flex-wrap: wrap;
            }
            
            .stat-item {
                text-align: center;
            }
            
            .stat-number {
                font-size: 1.8rem;
                font-weight: 700;
                color: #667eea;
                display: block;
            }
            
            .stat-label {
                font-size: 0.9rem;
                color: #718096;
                font-weight: 500;
            }
            
            .action-buttons {
                display: flex;
                gap: 15px;
                flex-wrap: wrap;
            }
            
            .btn {
                padding: 12px 24px;
                border: none;
                border-radius: 12px;
                font-size: 0.95rem;
                font-weight: 600;
                cursor: pointer;
                transition: all 0.3s ease;
                text-decoration: none;
                display: inline-flex;
                align-items: center;
                gap: 8px;
                text-align: center;
            }
            
            .btn-primary {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
            }
            
            .btn-primary:hover {
                transform: translateY(-2px);
                box-shadow: 0 10px 25px rgba(102, 126, 234, 0.4);
            }
            
            .btn-secondary {
                background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
                color: white;
            }
            
            .btn-secondary:hover {
                transform: translateY(-2px);
                box-shadow: 0 10px 25px rgba(79, 172, 254, 0.4);
            }
            
            .btn-danger {
                background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
                color: white;
            }
            
            .btn-danger:hover {
                transform: translateY(-2px);
                box-shadow: 0 10px 25px rgba(250, 112, 154, 0.4);
            }
            
            /* Main Content */
            .profile-content {
                display: grid;
                grid-template-columns: 1fr 2fr;
                gap: 30px;
                margin-bottom: 30px;
            }
            
            /* Personal Info Card */
            .personal-info-card {
                background: rgba(255, 255, 255, 0.95);
                backdrop-filter: blur(10px);
                border-radius: 20px;
                padding: 30px;
                box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
                height: fit-content;
            }
            
            .card-title {
                font-size: 1.4rem;
                font-weight: 700;
                color: #2d3748;
                margin-bottom: 20px;
                display: flex;
                align-items: center;
                gap: 10px;
            }
            
            .card-title i {
                color: #667eea;
            }
            
            .info-list {
                list-style: none;
            }
            
            .info-item {
                display: flex;
                align-items: center;
                padding: 12px 0;
                border-bottom: 1px solid #e2e8f0;
            }
            
            .info-item:last-child {
                border-bottom: none;
            }
            
            .info-icon {
                width: 40px;
                height: 40px;
                border-radius: 10px;
                background: linear-gradient(135deg, #667eea, #764ba2);
                color: white;
                display: flex;
                align-items: center;
                justify-content: center;
                margin-right: 15px;
                flex-shrink: 0;
            }
            
            .info-content {
                flex: 1;
            }
            
            .info-label {
                font-size: 0.85rem;
                color: #718096;
                font-weight: 500;
                margin-bottom: 2px;
            }
            
            .info-value {
                font-size: 1rem;
                color: #2d3748;
                font-weight: 600;
            }
            
            .info-value.empty {
                color: #a0aec0;
                font-style: italic;
            }
            
            /* Books Section */
            .books-section {
                background: rgba(255, 255, 255, 0.95);
                backdrop-filter: blur(10px);
                border-radius: 20px;
                padding: 30px;
                box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
            }
            
            .books-grid {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
                gap: 20px;
                margin-top: 20px;
            }
            
            .book-card {
                background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
                border-radius: 15px;
                padding: 20px;
                color: white;
                position: relative;
                overflow: hidden;
                transition: transform 0.3s ease;
            }
            
            .book-card:hover {
                transform: translateY(-5px);
            }
            
            .book-card::before {
                content: '';
                position: absolute;
                top: 0;
                left: 0;
                right: 0;
                bottom: 0;
                background: linear-gradient(135deg, rgba(255,255,255,0.1) 0%, rgba(255,255,255,0.05) 100%);
                pointer-events: none;
            }
            
            .book-title {
                font-size: 1.1rem;
                font-weight: 700;
                margin-bottom: 8px;
                position: relative;
                z-index: 1;
            }
            
            .book-chapter {
                font-size: 0.9rem;
                opacity: 0.9;
                position: relative;
                z-index: 1;
            }
            
            .book-progress {
                margin-top: 15px;
                position: relative;
                z-index: 1;
            }
            
            .progress-bar {
                width: 100%;
                height: 6px;
                background: rgba(255, 255, 255, 0.3);
                border-radius: 3px;
                overflow: hidden;
            }
            
            .progress-fill {
                height: 100%;
                background: rgba(255, 255, 255, 0.8);
                border-radius: 3px;
                transition: width 0.3s ease;
            }
            
            /* Favorites Section */
            .favorites-section {
                background: rgba(255, 255, 255, 0.95);
                backdrop-filter: blur(10px);
                border-radius: 20px;
                padding: 30px;
                box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
            }
            
            .favorites-grid {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
                gap: 15px;
                margin-top: 20px;
            }
            
            .favorite-item {
                background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
                border-radius: 12px;
                padding: 15px;
                color: white;
                text-align: center;
                transition: transform 0.3s ease;
                cursor: pointer;
            }
            
            .favorite-item:hover {
                transform: translateY(-3px);
            }
            
            .favorite-title {
                font-weight: 600;
                font-size: 0.95rem;
            }
            
            /* Message */
            .message {
                background: rgba(255, 255, 255, 0.95);
                backdrop-filter: blur(10px);
                border-radius: 15px;
                padding: 20px;
                margin-bottom: 20px;
                font-weight: 500;
                box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            }
            
            .message.success {
                border-left: 4px solid #48bb78;
                color: #2f855a;
            }
            
            .message.error {
                border-left: 4px solid #f56565;
                color: #c53030;
            }
            
            /* Responsive */
            @media (max-width: 1024px) {
                .profile-content {
                    grid-template-columns: 1fr;
                }
            }
            
            @media (max-width: 768px) {
                .profile-container {
                    padding: 10px;
                }
                
                .profile-header {
                    padding: 30px 20px;
                }
                
                .header-content {
                    flex-direction: column;
                    text-align: center;
                }
                
                .username {
                    font-size: 2rem;
                }
                
                .user-stats {
                    justify-content: center;
                }
                
                .action-buttons {
                    justify-content: center;
                }
                
                .books-grid {
                    grid-template-columns: 1fr;
                }
                
                .favorites-grid {
                    grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
                }
            }
            
            @media (max-width: 480px) {
                .user-stats {
                    gap: 20px;
                }
                
                .action-buttons {
                    flex-direction: column;
                }
                
                .btn {
                    width: 100%;
                    justify-content: center;
                }
            }
        </style>
    </head>
    <body>
        <div class="profile-container">
            <!-- Hiển thị thông báo -->
            <c:if test="${not empty message}">
                <div class="message ${messageType == 'error' ? 'error' : 'success'}">
                    <i class="fas ${messageType == 'error' ? 'fa-exclamation-circle' : 'fa-check-circle'}"></i>
                    ${message}
                </div>
            </c:if>
            
            <!-- Profile Header -->
            <div class="profile-header">
                <div class="header-content">
                    <div class="avatar-section">
                        <img class="avatar"
                             src="${empty user.avatarUrl ? 'https://i.imgur.com/Vz8s1cC.png' : user.avatarUrl}"
                             alt="Avatar"/>
                    </div>
                    
                    <div class="user-info">
                        <h1 class="username"><c:out value="${user.username}"/></h1>
                        <div class="role-badge">
                            <i class="fas fa-crown"></i>
                            <c:out value="${user.role}"/>
                        </div>
                        
                        <div class="user-stats">
                            <div class="stat-item">
                                <span class="stat-number">${userCoins != null ? userCoins : 0}</span>
                                <span class="stat-label">💰 Coins</span>
                            </div>
                            <div class="stat-item">
                                <span class="stat-number">12</span>
                                <span class="stat-label">Truyện đang đọc</span>
                            </div>
                            <div class="stat-item">
                                <span class="stat-number">8</span>
                                <span class="stat-label">Truyện yêu thích</span>
                            </div>
                            <div class="stat-item">
                                <span class="stat-number">156</span>
                                <span class="stat-label">Chương đã đọc</span>
                            </div>
                            <div class="stat-item">
                                <span class="stat-number">45</span>
                                <span class="stat-label">Ngày tham gia</span>
                            </div>
                        </div>
                        
                        <div class="action-buttons">
                            <a href="${pageContext.request.contextPath}/book?action=upload" class="btn btn-primary" style="
                                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                                margin-right: 10px;
                            ">
                                <i class="fas fa-upload"></i>
                                📤 Upload Truyện
                            </a>
                            <a href="${pageContext.request.contextPath}/edit-profile" class="btn btn-primary">
                                <i class="fas fa-edit"></i>
                                Chỉnh sửa thông tin
                            </a>
                            <a href="${pageContext.request.contextPath}/change-password" class="btn btn-secondary">
                                <i class="fas fa-key"></i>
                                Đổi mật khẩu
                            </a>
                            <form action="${pageContext.request.contextPath}/logout" method="post" style="display: inline;">
                                <button class="btn btn-danger" type="submit">
                                    <i class="fas fa-sign-out-alt"></i>
                                    Đăng xuất
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- Main Content -->
            <div class="profile-content">
                <!-- Personal Information -->
                <div class="personal-info-card">
                    <h2 class="card-title">
                        <i class="fas fa-user"></i>
                        Thông tin cá nhân
                    </h2>
                    
                    <ul class="info-list">
                        <li class="info-item">
                            <div class="info-icon">
                                <i class="fas fa-envelope"></i>
                            </div>
                            <div class="info-content">
                                <div class="info-label">Email</div>
                                <div class="info-value"><c:out value="${user.email}"/></div>
                            </div>
                        </li>
                        
                        <li class="info-item">
                            <div class="info-icon">
                                <i class="fas fa-coins"></i>
                            </div>
                            <div class="info-content">
                                <div class="info-label">Số dư Coins</div>
                                <div class="info-value">
                                    <span style="color: #ffd700; font-weight: 700; font-size: 1.1rem;">
                                        💰 ${userCoins != null ? userCoins : 0} Coins
                                    </span>
                                </div>
                            </div>
                        </li>
                        
                        <li class="info-item">
                            <div class="info-icon">
                                <i class="fas fa-shield-alt"></i>
                            </div>
                            <div class="info-content">
                                <div class="info-label">Trạng thái</div>
                                <div class="info-value">
                                    <span style="color: #48bb78; font-weight: 600;">
                                        <i class="fas fa-circle" style="font-size: 0.5rem; margin-right: 5px;"></i>
                                        <c:out value="${user.status}"/>
                                    </span>
                                </div>
                            </div>
                        </li>
                        
                        <c:if test="${not empty userInfor}">
                            <li class="info-item">
                                <div class="info-icon">
                                    <i class="fas fa-venus-mars"></i>
                                </div>
                                <div class="info-content">
                                    <div class="info-label">Giới tính</div>
                                    <div class="info-value">
                                        <c:out value="${empty userInfor.gender ? 'Chưa cập nhật' : userInfor.gender}"/>
                                    </div>
                                </div>
                            </li>
                            
                            <li class="info-item">
                                <div class="info-icon">
                                    <i class="fas fa-birthday-cake"></i>
                                </div>
                                <div class="info-content">
                                    <div class="info-label">Ngày sinh</div>
                                    <div class="info-value">
                                        <c:out value="${empty birthDayStr ? 'Chưa cập nhật' : birthDayStr}"/>
                                    </div>
                                </div>
                            </li>
                            
                            <li class="info-item">
                                <div class="info-icon">
                                    <i class="fas fa-phone"></i>
                                </div>
                                <div class="info-content">
                                    <div class="info-label">Điện thoại</div>
                                    <div class="info-value">
                                        <c:out value="${empty userInfor.phone ? 'Chưa cập nhật' : userInfor.phone}"/>
                                    </div>
                                </div>
                            </li>
                            
                            <li class="info-item">
                                <div class="info-icon">
                                    <i class="fas fa-map-marker-alt"></i>
                                </div>
                                <div class="info-content">
                                    <div class="info-label">Địa chỉ</div>
                                    <div class="info-value">
                                        <c:out value="${empty userInfor.address ? 'Chưa cập nhật' : userInfor.address}"/>
                                    </div>
                                </div>
                            </li>
                            
                            <li class="info-item">
                                <div class="info-icon">
                                    <i class="fas fa-quote-left"></i>
                                </div>
                                <div class="info-content">
                                    <div class="info-label">Giới thiệu</div>
                                    <div class="info-value">
                                        <c:out value="${empty userInfor.introduction ? 'Chưa cập nhật' : userInfor.introduction}"/>
                                    </div>
                                </div>
                            </li>
                        </c:if>
                        
                        <li class="info-item">
                            <div class="info-icon">
                                <i class="fas fa-calendar-alt"></i>
                            </div>
                            <div class="info-content">
                                <div class="info-label">Ngày tham gia</div>
                                <div class="info-value"><c:out value="${createdAtStr}"/></div>
                            </div>
                        </li>
                    </ul>
                </div>
                
                <!-- Books Section -->
                <div class="books-section">
                    <h2 class="card-title">
                        <i class="fas fa-book-open"></i>
                        Truyện đang đọc
                    </h2>
                    
                    <div class="books-grid">
                        <div class="book-card">
                            <div class="book-title">Thần Thoại Bắc Âu</div>
                            <div class="book-chapter">Chương 12 - Khởi đầu mới</div>
                            <div class="book-progress">
                                <div class="progress-bar">
                                    <div class="progress-fill" style="width: 75%;"></div>
                                </div>
                            </div>
                        </div>
                        
                        <div class="book-card">
                            <div class="book-title">Ma Đạo Tổ Sư</div>
                            <div class="book-chapter">Chương 45 - Bí mật cổ xưa</div>
                            <div class="book-progress">
                                <div class="progress-bar">
                                    <div class="progress-fill" style="width: 60%;"></div>
                                </div>
                            </div>
                        </div>
                        
                        <div class="book-card">
                            <div class="book-title">One Piece</div>
                            <div class="book-chapter">Chương 1090 - Hành trình vĩ đại</div>
                            <div class="book-progress">
                                <div class="progress-bar">
                                    <div class="progress-fill" style="width: 90%;"></div>
                                </div>
                            </div>
                        </div>
                        
                        <div class="book-card">
                            <div class="book-title">Hệ Thống Tu Tiên</div>
                            <div class="book-chapter">Chương 8 - Tu luyện bắt đầu</div>
                            <div class="book-progress">
                                <div class="progress-bar">
                                    <div class="progress-fill" style="width: 30%;"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- Favorites Section -->
            <div class="favorites-section">
                <h2 class="card-title">
                    <i class="fas fa-heart"></i>
                    Truyện yêu thích
                </h2>
                
                <div class="favorites-grid">
                    <div class="favorite-item">
                        <div class="favorite-title">Ma Đạo Tổ Sư</div>
                    </div>
                    <div class="favorite-item">
                        <div class="favorite-title">One Piece</div>
                    </div>
                    <div class="favorite-item">
                        <div class="favorite-title">Người Ở Bên Kia</div>
                    </div>
                    <div class="favorite-item">
                        <div class="favorite-title">Phế Vật Dòng Bá Tước</div>
                    </div>
                    <div class="favorite-item">
                        <div class="favorite-title">Thần Thoại Bắc Âu</div>
                    </div>
                    <div class="favorite-item">
                        <div class="favorite-title">Hệ Thống Tu Tiên</div>
                    </div>
                </div>
            </div>
        </div>
        
        <script>
            // Auto-hide messages after 5 seconds
            setTimeout(function() {
                const messages = document.querySelectorAll('.message');
                messages.forEach(function(msg) {
                    msg.style.opacity = '0';
                    msg.style.transition = 'opacity 0.3s ease';
                    setTimeout(function() {
                        msg.style.display = 'none';
                    }, 300);
                });
            }, 5000);
            
            // Add hover effects to book cards
            document.querySelectorAll('.book-card').forEach(function(card) {
                card.addEventListener('mouseenter', function() {
                    this.style.transform = 'translateY(-8px) scale(1.02)';
                });
                
                card.addEventListener('mouseleave', function() {
                    this.style.transform = 'translateY(0) scale(1)';
                });
            });
            
            // Add click effects to favorite items
            document.querySelectorAll('.favorite-item').forEach(function(item) {
                item.addEventListener('click', function() {
                    this.style.transform = 'scale(0.95)';
                    setTimeout(() => {
                        this.style.transform = 'translateY(-3px)';
                    }, 150);
                });
            });
        </script>
    </body>
</html>
