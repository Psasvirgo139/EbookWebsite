<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Reset Password - EbookWebsite</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&family=Inter:wght@400;600&family=Roboto:wght@400;500&display=swap" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(120deg, #e0f7fa 0%, #f3e7ff 100%);
            font-family: 'Poppins', 'Inter', 'Roboto', Arial, sans-serif;
            margin: 0;
            min-height: 100vh;
            color: #222;
        }
        .reset-main {
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .reset-card {
            background: #fff;
            border-radius: 24px;
            box-shadow: 0 8px 32px rgba(124,131,253,0.10);
            max-width: 370px;
            width: 100%;
            padding: 2.5rem 2rem 2rem 2rem;
            animation: fadeIn 0.8s cubic-bezier(.4,0,.2,1);
        }
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(30px);}
            to { opacity: 1; transform: translateY(0);}
        }
        .reset-logo {
            text-align: center;
            margin-bottom: 1.2rem;
        }
        .reset-logo i {
            font-size: 2.1rem;
            color: #6ee7b7;
            margin-right: 0.4rem;
        }
        .reset-logo span {
            font-size: 1.3rem;
            font-weight: 700;
            color: #4f8cff;
            letter-spacing: 1px;
        }
        .reset-title {
            font-size: 1.5rem;
            font-weight: 700;
            color: #4f8cff;
            margin-bottom: 0.5rem;
            text-align: center;
        }
        .reset-subtitle {
            font-size: 1.01rem;
            color: #6a7ba2;
            text-align: center;
            margin-bottom: 1.5rem;
            font-weight: 500;
        }
        .input-group {
            position: relative;
            margin-bottom: 1.2rem;
        }
        .input-group input {
            width: 100%;
            padding: 1.05rem 1rem 1.05rem 2.7rem;
            border: 1.5px solid #e0e7ff;
            border-radius: 16px;
            font-size: 1rem;
            background: #f8faff;
            outline: none;
            transition: border 0.18s, box-shadow 0.18s;
            box-shadow: 0 1px 4px #e0e7ff33;
        }
        .input-group input:focus {
            border-color: #6ee7b7;
            box-shadow: 0 2px 8px #6ee7b733;
        }
        .input-group .input-icon {
            position: absolute;
            left: 1rem;
            top: 50%;
            transform: translateY(-50%);
            color: #b3b8e0;
            font-size: 1.1rem;
        }
        .input-group .toggle-password {
            position: absolute;
            right: 1rem;
            top: 50%;
            transform: translateY(-50%);
            color: #b3b8e0;
            font-size: 1.1rem;
            cursor: pointer;
            transition: color 0.15s;
        }
        .input-group .toggle-password:hover {
            color: #4f8cff;
        }
        .reset-btn {
            width: 100%;
            background: linear-gradient(120deg, #6ee7b7 0%, #4f8cff 100%);
            color: #fff;
            font-weight: 700;
            font-size: 1.1rem;
            border: none;
            border-radius: 16px;
            padding: 1rem 0;
            margin-bottom: 1.1rem;
            box-shadow: 0 2px 12px #4f8cff22;
            transition: background 0.18s, box-shadow 0.18s, transform 0.12s;
            cursor: pointer;
        }
        .reset-btn:hover {
            background: linear-gradient(120deg, #4f8cff 0%, #6ee7b7 100%);
            box-shadow: 0 4px 24px #4f8cff33;
            transform: translateY(-2px) scale(1.01);
        }
        .reset-links {
            text-align: center;
            margin-top: 1.2rem;
            font-size: 0.99rem;
            color: #6a7ba2;
        }
        .reset-links a {
            color: #6ee7b7;
            font-weight: 600;
            text-decoration: none;
            transition: color 0.15s;
        }
        .reset-links a:hover {
            color: #4f8cff;
            text-decoration: underline;
        }
        .reset-alert, .reset-success, .info {
            margin-bottom: 1.1rem;
            border-radius: 12px;
            padding: 0.8rem 1rem;
            font-size: 1rem;
            text-align: center;
        }
        .reset-alert {
            background: #ffeaea;
            color: #c0392b;
            border: 1px solid #e74c3c;
        }
        .reset-success {
            background: #eafaf1;
            color: #27ae60;
            border: 1px solid #2ecc71;
        }
        .info {
            background: #eaf1fa;
            color: #2980b9;
            border: 1px solid #3498db;
        }
    </style>
</head>
<body>
<div class="reset-main">
    <div class="reset-card">
        <div class="reset-logo">
            <i class="fas fa-book-open"></i><span>EbookWebsite</span>
        </div>
        <div class="reset-title">Reset Password</div>
        <div class="reset-subtitle">Enter the code sent to your email and set a new password</div>
        <c:if test="${not empty error}">
            <div class="reset-alert"><i class="fas fa-exclamation-triangle"></i> ${error}</div>
        </c:if>
        <c:if test="${not empty success}">
            <div class="reset-success"><i class="fas fa-check-circle"></i> ${success}</div>
        </c:if>
        <form method="post" action="reset-password">
            <input type="hidden" name="email" value="${email}">
            <div class="input-group">
                <span class="input-icon"><i class="fas fa-key"></i></span>
                <input type="text" id="otp" name="otp" placeholder="Reset Code (OTP)" required>
            </div>
            <div class="input-group">
                <span class="input-icon"><i class="fas fa-lock"></i></span>
                <input type="password" id="password" name="password" placeholder="New Password" required>
                <span class="toggle-password" onclick="togglePassword('password', this)"><i class="fas fa-eye"></i></span>
            </div>
            <div class="input-group">
                <span class="input-icon"><i class="fas fa-lock"></i></span>
                <input type="password" id="confirm" name="confirm" placeholder="Confirm Password" required>
                <span class="toggle-password" onclick="togglePassword('confirm', this)"><i class="fas fa-eye"></i></span>
            </div>
            <button type="submit" class="reset-btn">Reset Password</button>
        </form>
        <div class="reset-links">
            <a href="login.jsp">Back to Login</a>
        </div>
    </div>
</div>
<script>
function togglePassword(id, el) {
    var pwd = document.getElementById(id);
    var icon = el.querySelector('i');
    if (pwd.type === 'password') {
        pwd.type = 'text';
        icon.classList.remove('fa-eye');
        icon.classList.add('fa-eye-slash');
    } else {
        pwd.type = 'password';
        icon.classList.remove('fa-eye-slash');
        icon.classList.add('fa-eye');
    }
}
</script>
</body>
</html> 