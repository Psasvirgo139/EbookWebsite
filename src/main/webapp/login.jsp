<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Login - EbookWebsite</title>
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
        .login-main {
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .login-box {
            display: flex;
            background: #fff;
            border-radius: 28px;
            box-shadow: 0 8px 32px rgba(124,131,253,0.10);
            overflow: hidden;
            max-width: 950px;
            width: 100%;
            min-height: 540px;
            animation: fadeIn 0.8s cubic-bezier(.4,0,.2,1);
        }
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(30px);}
            to { opacity: 1; transform: translateY(0);}
        }
        .login-illustration {
            flex: 1 1 380px;
            background: linear-gradient(120deg, #e0f7fa 0%, #f3e7ff 100%);
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            padding: 2.5rem 1.5rem 2.5rem 2.5rem;
            position: relative;
        }
        .login-illustration img {
            max-width: 320px;
            width: 100%;
            height: auto;
            border-radius: 18px;
            box-shadow: 0 4px 24px #7c83fd22;
            animation: float 3s ease-in-out infinite;
        }
        @keyframes float {
            0%, 100% { transform: translateY(0);}
            50% { transform: translateY(-16px);}
        }
        .login-quote {
            margin-top: 2.2rem;
            font-size: 1.08rem;
            color: #6a7ba2;
            text-align: center;
            font-style: italic;
            font-weight: 500;
            letter-spacing: 0.01em;
        }
        .login-form-section {
            flex: 1 1 420px;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 2.5rem 2.2rem;
            background: #fff;
        }
        .login-form {
            width: 100%;
            max-width: 350px;
        }
        .login-logo {
            text-align: center;
            margin-bottom: 1.2rem;
        }
        .login-logo i {
            font-size: 2.1rem;
            color: #6ee7b7;
            margin-right: 0.4rem;
        }
        .login-logo span {
            font-size: 1.45rem;
            font-weight: 700;
            color: #4f8cff;
            letter-spacing: 1px;
        }
        .login-title {
            font-size: 2rem;
            font-weight: 700;
            color: #4f8cff;
            margin-bottom: 0.5rem;
            text-align: center;
            letter-spacing: -1px;
        }
        .login-subtitle {
            font-size: 1.08rem;
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
        .form-check {
            display: flex;
            align-items: center;
            margin-bottom: 1.2rem;
        }
        .form-check input[type="checkbox"] {
            accent-color: #6ee7b7;
            width: 1.1em;
            height: 1.1em;
            margin-right: 0.5em;
        }
        .login-btn {
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
        .login-btn:hover {
            background: linear-gradient(120deg, #4f8cff 0%, #6ee7b7 100%);
            box-shadow: 0 4px 24px #4f8cff33;
            transform: translateY(-2px) scale(1.01);
        }
        .login-links {
            display: flex;
            justify-content: space-between;
            align-items: center;
            font-size: 0.98rem;
            margin-top: 0.2rem;
        }
        .login-links a {
            color: #4f8cff;
            text-decoration: none;
            font-weight: 500;
            transition: color 0.15s;
        }
        .login-links a:hover {
            color: #222;
            text-decoration: underline;
        }
        .login-signup {
            text-align: center;
            margin-top: 1.2rem;
            font-size: 0.99rem;
            color: #6a7ba2;
        }
        .login-signup a {
            color: #6ee7b7;
            font-weight: 600;
            text-decoration: none;
            transition: color 0.15s;
        }
        .login-signup a:hover {
            color: #4f8cff;
            text-decoration: underline;
        }
        .login-alert {
            margin-bottom: 1.1rem;
            border-radius: 12px;
            padding: 0.8rem 1rem;
            font-size: 1rem;
            background: #ffeaea;
            color: #c0392b;
            border: 1px solid #e74c3c;
            text-align: center;
        }
        @media (max-width: 900px) {
            .login-box { max-width: 98vw; }
        }
        @media (max-width: 700px) {
            .login-box {
                flex-direction: column;
                min-height: unset;
            }
            .login-illustration {
                display: none;
            }
            .login-form-section {
                padding: 2rem 1rem;
            }
        }
    </style>
</head>
<body>
<div class="login-main">
    <div class="login-box">
        <div class="login-illustration">
            <img src="https://undraw.co/api/illustrations/undraw_online_reading_np7n.svg" alt="Reading Illustration"
                 onerror="this.onerror=null;this.src='https://images.unsplash.com/photo-1519681393784-d120267933ba?auto=format&fit=crop&w=400&q=80';">
            <div class="login-quote">“Reading is dreaming with open eyes.”</div>
        </div>
        <div class="login-form-section">
            <form class="login-form" action="${pageContext.request.contextPath}/login" method="post" autocomplete="off">
                <c:if test="${not empty param.redirect}">
                  <input type="hidden" name="redirect" value="${param.redirect}" />
                </c:if>
                <div class="login-logo">
                    <i class="fas fa-book-open"></i><span>EbookWebsite</span>
                </div>
                <div class="login-title">Welcome back 👋</div>
                <div class="login-subtitle">Login to continue your reading journey</div>
                <c:if test="${not empty error}">
                    <div class="login-alert"><i class="fas fa-exclamation-triangle"></i> ${error}</div>
                </c:if>
                <div class="input-group">
                    <span class="input-icon"><i class="fas fa-user"></i></span>
                    <input type="text" id="username" name="username" placeholder="Email or Username" required value="${username}">
                </div>
                <div class="input-group">
                    <span class="input-icon"><i class="fas fa-lock"></i></span>
                    <input type="password" id="password" name="password" placeholder="Password" required>
                    <span class="toggle-password" onclick="togglePassword()"><i class="fas fa-eye"></i></span>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" id="rememberMe" name="rememberMe">
                    <label class="form-check-label" for="rememberMe">Remember Me</label>
                </div>
                <button type="submit" class="login-btn">Login</button>
                <div class="login-links">
                    <a href="forgot-password.jsp">Forgot password?</a>
                </div>
                <div class="login-signup">
                    Don't have an account? <a href="register.jsp">Sign up</a>
                </div>
            </form>
        </div>
    </div>
</div>
<script>
function togglePassword() {
    var pwd = document.getElementById('password');
    var icon = document.querySelector('.toggle-password i');
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