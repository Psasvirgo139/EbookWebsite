<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Đăng nhập - Ebook Website</title>
    <meta charset="UTF-8">
    <style>
        /* Đơn giản hóa giao diện cho dễ hiểu, bạn có thể trang trí lại */
        body { font-family: Arial; background: #f5f5f5; }
        .login-form {
            width: 360px;
            margin: 60px auto;
            background: #fff;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 0 10px #bbb;
        }
        .login-form h2 { margin-bottom: 24px; }
        .error-msg { color: #e74c3c; margin-bottom: 10px; }
        input[type="text"], input[type="password"] {
            width: 100%; padding: 10px; margin: 8px 0 18px 0; border-radius: 5px; border: 1px solid #ccc;
        }
        button { padding: 10px 20px; background: #2980b9; color: white; border: none; border-radius: 5px; }
        button:hover { background: #3498db; }
    </style>
</head>
<body>
<div class="login-form">
    <h2>Đăng nhập</h2>
    <%-- Hiển thị thông báo lỗi nếu có --%>
    <c:if test="${not empty error}">
        <div class="error-msg">${error}</div>
    </c:if>
    <form action="login" method="post">
        <label for="usernameOrEmail">Tên đăng nhập hoặc Email:</label>
        <input type="text" name="usernameOrEmail" id="usernameOrEmail" 
               value="${username != null ? username : ''}" required />

        <label for="password">Mật khẩu:</label>
        <input type="password" name="password" id="password" required />

        <button type="submit">Đăng nhập</button>
    </form>
</div>

<%-- Nếu không có JSTL, có thể thay bằng code dưới đây để hiển thị lỗi:
<%
    String error = (String) request.getAttribute("error");
    if (error != null) { out.print("<div class='error-msg'>" + error + "</div>"); }
%>
--%>
</body>
</html>
