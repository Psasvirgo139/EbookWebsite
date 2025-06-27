<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Đặt lại mật khẩu | Scroll</title>
    
    <!-- Import Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Inter', Arial, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
        }
        
        .container {
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
            padding: 40px;
            width: 100%;
            max-width: 450px;
            text-align: center;
        }
        
        .logo {
            font-size: 2rem;
            font-weight: 700;
            color: #3e2f92;
            margin-bottom: 10px;
        }
        
        .subtitle {
            color: #666;
            margin-bottom: 30px;
            font-size: 0.95rem;
        }
        
        .form-group {
            margin-bottom: 20px;
            text-align: left;
        }
        
        label {
            display: block;
            margin-bottom: 8px;
            font-weight: 500;
            color: #333;
            font-size: 0.9rem;
        }
        
        input[type="password"] {
            width: 100%;
            padding: 12px 16px;
            border: 2px solid #e1e5e9;
            border-radius: 10px;
            font-size: 1rem;
            font-family: inherit;
            transition: border-color 0.3s ease;
        }
        
        input[type="password"]:focus {
            outline: none;
            border-color: #3e2f92;
        }
        
        .password-requirements {
            background: #f8f9fa;
            border: 1px solid #e9ecef;
            border-radius: 8px;
            padding: 12px;
            margin-bottom: 20px;
            font-size: 0.85rem;
            color: #666;
            text-align: left;
        }
        
        .password-requirements h4 {
            margin-bottom: 8px;
            color: #333;
            font-size: 0.9rem;
        }
        
        .password-requirements ul {
            list-style: none;
            padding-left: 0;
        }
        
        .password-requirements li {
            margin-bottom: 4px;
            padding-left: 16px;
            position: relative;
        }
        
        .password-requirements li:before {
            content: "•";
            position: absolute;
            left: 0;
            color: #3e2f92;
        }
        
        .btn {
            width: 100%;
            padding: 14px;
            background: #3e2f92;
            color: white;
            border: none;
            border-radius: 10px;
            font-size: 1rem;
            font-weight: 600;
            cursor: pointer;
            transition: background 0.3s ease;
            margin-top: 10px;
        }
        
        .btn:hover {
            background: #2a1f6b;
        }
        
        .btn:disabled {
            background: #ccc;
            cursor: not-allowed;
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
        
        .message.info {
            background: #ebf8ff;
            color: #2b6cb0;
            border: 1px solid #bee3f8;
        }
        
        .back-link {
            margin-top: 20px;
            display: block;
            color: #3e2f92;
            text-decoration: none;
            font-weight: 500;
            transition: color 0.3s ease;
        }
        
        .back-link:hover {
            color: #2a1f6b;
        }
        
        .icon {
            font-size: 3rem;
            margin-bottom: 20px;
            display: block;
        }
        
        .password-strength {
            margin-top: 8px;
            font-size: 0.8rem;
            color: #666;
        }
        
        .strength-weak { color: #e53e3e; }
        .strength-medium { color: #d69e2e; }
        .strength-strong { color: #38a169; }
        
        @media (max-width: 480px) {
            .container {
                padding: 30px 20px;
                margin: 10px;
            }
            
            .logo {
                font-size: 1.5rem;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <span class="icon">🔑</span>
        <h1 class="logo">Đặt lại mật khẩu</h1>
        <p class="subtitle">Nhập mật khẩu mới cho tài khoản của bạn</p>
        
        <!-- Hiển thị thông báo -->
        <c:if test="${not empty message}">
            <div class="message ${messageType}">
                ${message}
            </div>
        </c:if>
        
        <!-- Password requirements -->
        <div class="password-requirements">
            <h4>Yêu cầu mật khẩu:</h4>
            <ul>
                <li>Ít nhất 6 ký tự</li>
                <li>Nên có chữ hoa, chữ thường và số</li>
                <li>Không nên sử dụng thông tin cá nhân</li>
            </ul>
        </div>
        
        <form action="${pageContext.request.contextPath}/reset-password" method="post" id="resetForm">
            <input type="hidden" name="token" value="${param.token}" />
            
            <div class="form-group">
                <label for="password">Mật khẩu mới:</label>
                <input 
                    type="password" 
                    id="password" 
                    name="password" 
                    placeholder="Nhập mật khẩu mới"
                    required 
                    minlength="6"
                />
                <div class="password-strength" id="passwordStrength"></div>
            </div>
            
            <div class="form-group">
                <label for="confirmPassword">Nhập lại mật khẩu:</label>
                <input 
                    type="password" 
                    id="confirmPassword" 
                    name="confirmPassword" 
                    placeholder="Nhập lại mật khẩu mới"
                    required 
                />
            </div>
            
            <button type="submit" class="btn" id="submitBtn">
                Đặt lại mật khẩu
            </button>
        </form>
        
        <a href="${pageContext.request.contextPath}/login" class="back-link">
            ← Quay lại trang đăng nhập
        </a>
    </div>
    
    <script>
        const passwordInput = document.getElementById('password');
        const confirmPasswordInput = document.getElementById('confirmPassword');
        const passwordStrength = document.getElementById('passwordStrength');
        const submitBtn = document.getElementById('submitBtn');
        
        // Check password strength
        function checkPasswordStrength(password) {
            let strength = 0;
            let feedback = '';
            
            if (password.length >= 6) strength++;
            if (password.match(/[a-z]/)) strength++;
            if (password.match(/[A-Z]/)) strength++;
            if (password.match(/[0-9]/)) strength++;
            if (password.match(/[^a-zA-Z0-9]/)) strength++;
            
            if (strength < 2) {
                feedback = '<span class="strength-weak">Mật khẩu yếu</span>';
            } else if (strength < 4) {
                feedback = '<span class="strength-medium">Mật khẩu trung bình</span>';
            } else {
                feedback = '<span class="strength-strong">Mật khẩu mạnh</span>';
            }
            
            passwordStrength.innerHTML = feedback;
        }
        
        // Check if passwords match
        function checkPasswordsMatch() {
            const password = passwordInput.value;
            const confirmPassword = confirmPasswordInput.value;
            
            if (confirmPassword && password !== confirmPassword) {
                confirmPasswordInput.style.borderColor = '#e53e3e';
                submitBtn.disabled = true;
            } else {
                confirmPasswordInput.style.borderColor = '#e1e5e9';
                submitBtn.disabled = false;
            }
        }
        
        // Event listeners
        passwordInput.addEventListener('input', function() {
            checkPasswordStrength(this.value);
            checkPasswordsMatch();
        });
        
        confirmPasswordInput.addEventListener('input', checkPasswordsMatch);
        
        // Form submission
        document.getElementById('resetForm').addEventListener('submit', function(e) {
            const password = passwordInput.value;
            const confirmPassword = confirmPasswordInput.value;
            
            if (password !== confirmPassword) {
                e.preventDefault();
                alert('Mật khẩu nhập lại không khớp!');
                return;
            }
            
            submitBtn.disabled = true;
            submitBtn.textContent = 'Đang xử lý...';
        });
        
        // Auto-hide success messages after 5 seconds
        setTimeout(function() {
            const successMsg = document.querySelector('.message.success');
            if (successMsg) {
                successMsg.style.opacity = '0';
                setTimeout(function() {
                    successMsg.style.display = 'none';
                }, 300);
            }
        }, 5000);
    </script>
</body>
</html>
