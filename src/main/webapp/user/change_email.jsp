<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/common/header.jsp" %>

<div class="login-form">
    <meta charset="UTF-8" />
    <h2>Đổi email</h2>
    <c:if test="${not empty error}">
        <div class="error-msg">${error}</div>
    </c:if>
    <c:if test="${not empty message}">
        <div class="success-msg">${message}</div>
    </c:if>
    <form action="${pageContext.request.contextPath}/change-email" method="post">
        <label for="newEmail">Email mới:</label>
        <input type="email" id="newEmail" name="newEmail" required />
        <label for="password">Mật khẩu hiện tại:</label>
        <input type="password" id="password" name="password" required />
        <button type="submit">Gửi xác nhận đổi email</button>
    </form>
    <div class="register-link">
        <a href="${pageContext.request.contextPath}/profile">Quay lại trang cá nhân</a>
    </div>
</div>

<%@ include file="/common/footer.jsp" %>

<style>
.success-msg {
    color: #27ae60;
    margin-bottom: 12px;
    text-align: center;
}
</style> 