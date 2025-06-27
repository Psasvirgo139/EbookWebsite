<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Quên mật khẩu | Scroll</title>
    
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
        
        input[type="email"] {
            width: 100%;
            padding: 12px 16px;
            border: 2px solid #e1e5e9;
            border-radius: 10px;
            font-size: 1rem;
            font-family: inherit;
            transition: border-color 0.3s ease;
        }
        
        input[type="email"]:focus {
            outline: none;
            border-color: #3e2f92;
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
        <span class="icon">🔐</span>
        <h1 class="logo">Quên mật khẩu</h1>
        <p class="subtitle">Nhập email của bạn để nhận link đặt lại mật khẩu</p>
        
        <!-- Hiển thị thông báo -->
        <c:if test="${not empty message}">
            <div class="message ${messageType}">
                ${message}
            </div>
        </c:if>
        
        <form action="${pageContext.request.contextPath}/forgot-password" method="post" id="forgotForm">
            <div class="form-group">
                <label for="email">Email đã đăng ký:</label>
                <input 
                    type="email" 
                    id="email" 
                    name="email" 
                    placeholder="example@email.com"
                    value="${param.email}"
                    required 
                />
            </div>
            
            <button type="submit" class="btn" id="submitBtn">
                Gửi yêu cầu đặt lại mật khẩu
            </button>
        </form>
        
        <a href="${pageContext.request.contextPath}/login" class="back-link">
            ← Quay lại trang đăng nhập
        </a>
    </div>
    
    <script>
        // Disable form submission if already submitted
        document.getElementById('forgotForm').addEventListener('submit', function(e) {
            const submitBtn = document.getElementById('submitBtn');
            submitBtn.disabled = true;
            submitBtn.textContent = 'Đang gửi...';
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
