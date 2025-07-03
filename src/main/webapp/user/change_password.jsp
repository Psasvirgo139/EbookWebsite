<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Đổi mật khẩu | EBook Website</title>
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
            .change-password-container {
                max-width: 500px;
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
            .form-group input {
                width: 100%;
                padding: 12px 16px;
                border: 2px solid #e1e5e9;
                border-radius: 10px;
                font-size: 1rem;
                font-family: inherit;
                transition: border-color 0.3s ease;
                box-sizing: border-box;
            }
            .form-group input:focus {
                outline: none;
                border-color: #3e2f92;
            }
            .password-requirements {
                background: #f8f9fa;
                border-radius: 8px;
                padding: 12px;
                margin-bottom: 20px;
                font-size: 0.85rem;
                color: #666;
            }
            .password-requirements h4 {
                margin: 0 0 8px 0;
                color: #3e2f92;
                font-size: 0.9rem;
            }
            .password-requirements ul {
                margin: 0;
                padding-left: 20px;
            }
            .password-requirements li {
                margin-bottom: 4px;
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
                width: 100%;
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
            .button-group {
                display: flex;
                gap: 12px;
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
            .password-strength {
                margin-top: 8px;
                font-size: 0.8rem;
            }
            .strength-weak {
                color: #e53e3e;
            }
            .strength-medium {
                color: #d69e2e;
            }
            .strength-strong {
                color: #38a169;
            }
            @media (max-width: 768px) {
                .change-password-container {
                    padding: 20px;
                    margin: 10px;
                }
                .button-group {
                    flex-direction: column;
                }
            }
        </style>
    </head>
    <body>
        <div class="change-password-container">
            <div class="header">
                <h1>Đổi mật khẩu</h1>
                <p>Bảo mật tài khoản của bạn</p>
            </div>

            <!-- Hiển thị thông báo -->
            <c:if test="${not empty message}">
                <div class="message ${messageType == 'error' ? 'error' : 'success'}">
                    ${message}
                </div>
            </c:if>

            <div class="password-requirements">
                <h4>Yêu cầu mật khẩu:</h4>
                <ul>
                    <li>Ít nhất 6 ký tự</li>
                    <li>Nên có cả chữ hoa, chữ thường và số</li>
                    <li>Không nên sử dụng thông tin cá nhân</li>
                </ul>
            </div>

            <form action="${pageContext.request.contextPath}/change-password" method="post" id="changePasswordForm">
                <div class="form-group">
                    <label for="current_password">Mật khẩu hiện tại:</label>
                    <input type="password" id="current_password" name="current_password" 
                           placeholder="Nhập mật khẩu hiện tại" required>
                </div>

                <div class="form-group">
                    <label for="new_password">Mật khẩu mới:</label>
                    <input type="password" id="new_password" name="new_password" 
                           placeholder="Nhập mật khẩu mới" required>
                    <div class="password-strength" id="passwordStrength"></div>
                </div>

                <div class="form-group">
                    <label for="confirm_password">Xác nhận mật khẩu:</label>
                    <input type="password" id="confirm_password" name="confirm_password" 
                           placeholder="Nhập lại mật khẩu mới" required>
                </div>

                <div class="button-group">
                    <button type="submit" class="btn btn-primary">Đổi mật khẩu</button>
                    <a href="${pageContext.request.contextPath}/profile" class="btn btn-secondary">Hủy</a>
                </div>
            </form>
        </div>

        <script>
            // Kiểm tra độ mạnh mật khẩu
            function checkPasswordStrength(password) {
                const strengthDiv = document.getElementById('passwordStrength');
                let strength = 0;
                let feedback = '';

                if (password.length >= 6) strength++;
                if (password.match(/[a-z]/)) strength++;
                if (password.match(/[A-Z]/)) strength++;
                if (password.match(/[0-9]/)) strength++;
                if (password.match(/[^a-zA-Z0-9]/)) strength++;

                if (password.length < 6) {
                    feedback = '<span class="strength-weak">Quá ngắn</span>';
                } else if (strength < 3) {
                    feedback = '<span class="strength-weak">Yếu</span>';
                } else if (strength < 4) {
                    feedback = '<span class="strength-medium">Trung bình</span>';
                } else {
                    feedback = '<span class="strength-strong">Mạnh</span>';
                }

                strengthDiv.innerHTML = feedback;
            }

            // Validate form trước khi submit
            document.getElementById('changePasswordForm').addEventListener('submit', function(e) {
                const currentPassword = document.getElementById('current_password').value;
                const newPassword = document.getElementById('new_password').value;
                const confirmPassword = document.getElementById('confirm_password').value;

                if (newPassword.length < 6) {
                    alert('Mật khẩu mới phải có ít nhất 6 ký tự!');
                    e.preventDefault();
                    return;
                }

                if (newPassword !== confirmPassword) {
                    alert('Mật khẩu xác nhận không khớp!');
                    e.preventDefault();
                    return;
                }

                if (newPassword === currentPassword) {
                    alert('Mật khẩu mới không được trùng với mật khẩu hiện tại!');
                    e.preventDefault();
                    return;
                }
            });

            // Kiểm tra độ mạnh mật khẩu real-time
            document.getElementById('new_password').addEventListener('input', function() {
                checkPasswordStrength(this.value);
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