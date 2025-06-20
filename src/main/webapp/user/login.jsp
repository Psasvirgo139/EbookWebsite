<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:useBean id="login" class="com.mycompany.ebookwebsite.bean.LoginBean" scope="request"/>
<jsp:setProperty name="login" property="*"/>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Đăng nhập · Ebook Website</title>
        <style>
            body{
                font-family:Arial;
                background:#f5f5f5;
            }
            .login-form{
                width:360px;
                margin:60px auto;
                background:#fff;
                padding:30px;
                border-radius:10px;
                box-shadow:0 0 10px #bbb;
            }
            .login-form h2{
                margin-bottom:24px;
                text-align:center
            }
            .error-msg{
                color:#e74c3c;
                margin-bottom:12px;
                text-align:center
            }
            label{
                font-weight:bold
            }
            input[type="text"],input[type="password"]{
                width:100%;
                padding:10px;
                margin:6px 0 16px;
                border:1px solid #ccc;
                border-radius:5px;
            }
            button{
                width:100%;
                padding:10px;
                background:#2980b9;
                color:#fff;
                border:none;
                border-radius:5px;
                font-size:1rem
            }
            button:hover{
                background:#3498db
            }
        </style>
    </head>
    <body>
        <div class="login-form">
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
        </div>
    </body>
</html>
