<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/common/header.jsp" %>

<!-- Import Google Fonts (Inter) nếu chưa có trong header.jsp -->
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap" rel="stylesheet">

<style>
    body {
        font-family: 'Inter', Arial, sans-serif;
        background: #f5f5f5;
    }
    .register-form {
        width: 400px;
        margin: 60px auto;
        background: #fff;
        padding: 30px;
        border-radius: 10px;
        box-shadow: 0 0 10px #bbb;
    }
    .register-form h2 {
        margin-bottom: 24px;
        text-align: center;
    }
    .error-msg {
        color: #e74c3c;
        margin-bottom: 12px;
        text-align: center;
    }
    .success-msg {
        color: #27ae60;
        margin-bottom: 12px;
        text-align: center;
    }
    label {
        font-weight: bold;
    }
    input[type="text"], input[type="email"], input[type="password"] {
        width: 100%;
        padding: 10px;
        margin: 6px 0 16px;
        border: 1px solid #ccc;
        border-radius: 5px;
        font-family: 'Inter', Arial, sans-serif;
    }
    button {
        width: 100%;
        padding: 10px;
        background: #27ae60;
        color: #fff;
        border: none;
        border-radius: 5px;
        font-size: 1rem;
        font-family: 'Inter', Arial, sans-serif;
        transition: background 0.2s;
    }
    button:hover {
        background: #2ecc71;
    }
</style>

<div class="register-form">
    <h2>Đăng ký tài khoản</h2>
    <c:if test="${not empty error}">
        <div class="error-msg">${error}</div>
    </c:if>
    <c:if test="${not empty success}">
        <div class="success-msg">${success}</div>
    </c:if>
    <form action="${pageContext.request.contextPath}/register" method="post">
        <label for="username">Tên đăng nhập:</label>
        <input type="text" id="username" name="username" required />

        <label for="email">Email:</label>
        <input type="email" id="email" name="email" required />

        <label for="password">Mật khẩu:</label>
        <input type="password" id="password" name="password" required />

        <label for="confirmPassword">Nhập lại mật khẩu:</label>
        <input type="password" id="confirmPassword" name="confirmPassword" required />

        <button type="submit">Đăng ký</button>
    </form>
    <div style="text-align:center; margin-top: 14px;">
        Đã có tài khoản? <a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
    </div>
</div>

<%@ include file="/common/footer.jsp" %>
