<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="login" class="com.mycompany.ebookwebsite.bean.LoginBean" scope="request"/>
<jsp:setProperty name="login" property="*"/>

<%@ include file="/common/header.jsp" %>

<!-- Import Google Fonts (nên để, hỗ trợ tốt tiếng Việt, hoặc bỏ nếu đã có ở header.jsp) -->
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap" rel="stylesheet">

<style>
    body{
        font-family: 'Inter', Arial, sans-serif;
        background: #f5f5f5;
    }
    .login-form{
        width: 360px;
        margin: 60px auto;
        background: #fff;
        padding: 30px;
        border-radius: 10px;
        box-shadow: 0 0 10px #bbb;
    }
    .login-form h2{
        margin-bottom: 24px;
        text-align: center;
    }
    .error-msg{
        color: #e74c3c;
        margin-bottom: 12px;
        text-align: center;
    }
    label{
        font-weight: bold;
    }
    input[type="text"], input[type="password"]{
        width: 100%;
        padding: 10px;
        margin: 6px 0 16px;
        border: 1px solid #ccc;
        border-radius: 5px;
        font-family: 'Inter', Arial, sans-serif;
    }
    button{
        width: 100%;
        padding: 10px;
        background: #2980b9;
        color: #fff;
        border: none;
        border-radius: 5px;
        font-size: 1rem;
        font-family: 'Inter', Arial, sans-serif;
        transition: background 0.2s;
    }
    button:hover{
        background: #3498db;
    }
    .forgot-password{
        text-align: center;
        margin-top: 15px;
    }
    .forgot-password a{
        color: #2980b9;
        text-decoration: none;
        font-size: 0.9rem;
    }
    .forgot-password a:hover{
        text-decoration: underline;
    }
    .register-link{
        text-align: center;
        margin-top: 20px;
        padding-top: 20px;
        border-top: 1px solid #eee;
    }
    .register-link a{
        color: #2980b9;
        text-decoration: none;
        font-weight: 600;
    }
    .register-link a:hover{
        text-decoration: underline;
    }
</style>

<div class="login-form">
    <meta charset="UTF-8" />
    <h2>Đăng nhập</h2>
    <!-- Hiển thị lỗi nếu có -->
    <c:if test="${not empty login.error}">
        <div class="error-msg">${login.error}</div>
    </c:if>
    <form action="${pageContext.request.contextPath}/login" method="post">
        <label for="usernameOrEmail">Tên đăng nhập hoặc Email:</label>
        <input type="text" id="usernameOrEmail" name="usernameOrEmail"
               value="${login.usernameOrEmail != null ? login.usernameOrEmail : ''}" required />
        <label for="password">Mật khẩu:</label>
        <input type="password" id="password" name="password" required />
        <button type="submit">Đăng nhập</button>
    </form>
    
    <!-- Link quên mật khẩu -->
    <div class="forgot-password">
        <a href="${pageContext.request.contextPath}/forgot-password">Quên mật khẩu?</a>
    </div>
    
    <!-- Link đăng ký -->
    <div class="register-link">
        Chưa có tài khoản? <a href="${pageContext.request.contextPath}/register">Đăng ký ngay</a>
    </div>
</div>

<%@ include file="/common/footer.jsp" %>
