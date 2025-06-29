<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Chỉnh sửa thông tin | EBook Website</title>
        <!-- Google Fonts Inter -->
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap&subset=vietnamese" rel="stylesheet">
        <style>
            body {
                font-family: 'Inter', Arial, sans-serif;
                background: #f3f7fb;
                margin: 0;
                padding: 20px;
            }
            .edit-container {
                max-width: 600px;
                margin: 0 auto;
                background: #fff;
                border-radius: 18px;
                box-shadow: 0 4px 24px #0001;
                padding: 32px;
            }
            .header {
                text-align: center;
                margin-bottom: 30px;
            }
            .header h1 {
                color: #3e2f92;
                font-size: 1.8rem;
                margin-bottom: 8px;
            }
            .header p {
                color: #666;
                margin: 0;
            }
            .form-group {
                margin-bottom: 20px;
            }
            .form-group label {
                display: block;
                margin-bottom: 8px;
                font-weight: 600;
                color: #333;
                font-size: 0.9rem;
            }
            .form-group input,
            .form-group select,
            .form-group textarea {
                width: 100%;
                padding: 12px 16px;
                border: 2px solid #e1e5e9;
                border-radius: 10px;
                font-size: 1rem;
                font-family: inherit;
                transition: border-color 0.3s ease;
                box-sizing: border-box;
            }
            .form-group input:focus,
            .form-group select:focus,
            .form-group textarea:focus {
                outline: none;
                border-color: #3e2f92;
            }
            .form-group textarea {
                resize: vertical;
                min-height: 100px;
            }
            .form-row {
                display: grid;
                grid-template-columns: 1fr 1fr;
                gap: 15px;
            }
            .avatar-preview {
                text-align: center;
                margin-bottom: 20px;
            }
            .avatar-preview img {
                width: 100px;
                height: 100px;
                border-radius: 50%;
                object-fit: cover;
                border: 3px solid #e1e5e9;
                margin-bottom: 10px;
            }
            .btn {
                padding: 14px 24px;
                border: none;
                border-radius: 10px;
                font-size: 1rem;
                font-weight: 600;
                cursor: pointer;
                transition: all 0.3s ease;
                text-decoration: none;
                display: inline-block;
                text-align: center;
            }
            .btn-primary {
                background: #3e2f92;
                color: white;
            }
            .btn-primary:hover {
                background: #2a1f6b;
            }
            .btn-secondary {
                background: #6c757d;
                color: white;
            }
            .btn-secondary:hover {
                background: #545b62;
            }
            .btn-danger {
                background: #e24370;
                color: white;
            }
            .btn-danger:hover {
                background: #a82222;
            }
            .button-group {
                display: flex;
                gap: 12px;
                justify-content: center;
                margin-top: 30px;
            }
            .message {
                padding: 12px 16px;
                border-radius: 10px;
                margin-bottom: 20px;
                font-size: 0.9rem;
            }
            .message.error {
                background: #fee;
                color: #c53030;
                border: 1px solid #fed7d7;
            }
            .message.success {
                background: #f0fff4;
                color: #2f855a;
                border: 1px solid #c6f6d5;
            }
            .readonly-field {
                background: #f8f9fa;
                color: #6c757d;
            }
            @media (max-width: 768px) {
                .edit-container {
                    padding: 20px;
                    margin: 10px;
                }
                .form-row {
                    grid-template-columns: 1fr;
                }
                .button-group {
                    flex-direction: column;
                }
            }
        </style>
    </head>
    <body>
        <div class="edit-container">
            <div class="header">
                <h1>Chỉnh sửa thông tin cá nhân</h1>
                <p>Cập nhật thông tin của bạn</p>
            </div>

            <!-- Hiển thị thông báo -->
            <c:if test="${not empty param.message}">
                <div class="message ${param.type == 'error' ? 'error' : 'success'}">
                    ${param.message}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/edit-profile" method="post" id="editForm">
                <!-- Avatar Preview -->
                <div class="avatar-preview">
                    <img id="avatarPreview" 
                         src="${empty user.avatarUrl ? 'https://i.imgur.com/Vz8s1cC.png' : user.avatarUrl}"
                         alt="Avatar Preview"/>
                </div>

                <!-- Thông tin cơ bản -->
                <div class="form-group">
                    <label for="username">Tên đăng nhập:</label>
                    <input type="text" id="username" name="username" 
                           value="${user.username}" readonly class="readonly-field">
                </div>

                <div class="form-group">
                    <label for="email">Email:</label>
                    <input type="email" id="email" name="email" 
                           value="${user.email}" readonly class="readonly-field">
                </div>

                <div class="form-group">
                    <label for="avatar_url">Avatar URL:</label>
                    <input type="url" id="avatar_url" name="avatar_url" 
                           value="${user.avatarUrl}" 
                           placeholder="https://example.com/avatar.jpg"
                           onchange="updateAvatarPreview(this.value)">
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="phone">Số điện thoại:</label>
                        <input type="tel" id="phone" name="phone" 
                               value="${not empty userInfor.phone ? userInfor.phone : ''}" 
                               placeholder="0123456789"
                               maxlength="20">
                    </div>

                    <div class="form-group">
                        <label for="gender">Giới tính:</label>
                        <select id="gender" name="gender">
                            <option value="">Chọn giới tính</option>
                            <option value="Nam" ${not empty userInfor.gender and userInfor.gender == 'Nam' ? 'selected' : ''}>Nam</option>
                            <option value="Nữ" ${not empty userInfor.gender and userInfor.gender == 'Nữ' ? 'selected' : ''}>Nữ</option>
                            <option value="Khác" ${not empty userInfor.gender and userInfor.gender == 'Khác' ? 'selected' : ''}>Khác</option>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <label for="birthday">Ngày sinh:</label>
                    <input type="date" id="birthday" name="birthday" 
                           value="${not empty userInfor.birthDay ? userInfor.birthDay : ''}">
                </div>

                <div class="form-group">
                    <label for="address">Địa chỉ:</label>
                    <input type="text" id="address" name="address" 
                           value="${not empty userInfor.address ? userInfor.address : ''}" 
                           placeholder="Nhập địa chỉ của bạn"
                           maxlength="200">
                </div>

                <div class="form-group">
                    <label for="introduction">Giới thiệu bản thân:</label>
                    <textarea id="introduction" name="introduction" 
                              placeholder="Viết một vài dòng giới thiệu về bản thân..."
                              maxlength="500">${not empty userInfor.introduction ? userInfor.introduction : ''}</textarea>
                    <small style="color: #666; font-size: 0.8rem;">
                        Còn <span id="charCount">500</span> ký tự
                    </small>
                </div>

                <div class="button-group">
                    <button type="submit" class="btn btn-primary">Lưu thay đổi</button>
                    <a href="${pageContext.request.contextPath}/profile" class="btn btn-secondary">Hủy</a>
                </div>
            </form>
        </div>

        <script>
            // Cập nhật avatar preview
            function updateAvatarPreview(url) {
                const preview = document.getElementById('avatarPreview');
                if (url && url.trim() !== '') {
                    preview.src = url;
                } else {
                    preview.src = 'https://i.imgur.com/Vz8s1cC.png';
                }
            }

            // Đếm ký tự trong textarea
            const introduction = document.getElementById('introduction');
            const charCount = document.getElementById('charCount');
            
            function updateCharCount() {
                const remaining = 500 - introduction.value.length;
                charCount.textContent = remaining;
                if (remaining < 50) {
                    charCount.style.color = '#e24370';
                } else {
                    charCount.style.color = '#666';
                }
            }
            
            introduction.addEventListener('input', updateCharCount);
            // Khởi tạo đếm ký tự khi trang load
            document.addEventListener('DOMContentLoaded', function() {
                updateCharCount();
            });

            // Auto-hide messages after 5 seconds
            setTimeout(function() {
                const messages = document.querySelectorAll('.message');
                messages.forEach(function(msg) {
                    msg.style.opacity = '0';
                    setTimeout(function() {
                        msg.style.display = 'none';
                    }, 300);
                });
            }, 5000);
        </script>
    </body>
</html> 