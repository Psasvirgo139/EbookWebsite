<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8" />
    <title>Xác nhận xóa sách</title>
    <link rel="stylesheet" href="${ctx}/assets/css/style.css" />
</head>
<body>
<%@ include file="/common/header.jsp" %>
<main>
    <div class="container">
        <h1>🗑️ Xác nhận xóa sách</h1>
        <c:if test="${not empty book}">
            <div><strong>ID:</strong> ${book.id}</div>
            <div><strong>Tiêu đề:</strong> ${book.title}</div>
            <div><strong>Mô tả:</strong> ${book.description}</div>
            <form method="post" action="${ctx}/book">
                <input type="hidden" name="action" value="delete" />
                <input type="hidden" name="id" value="${book.id}" />
                <button type="submit" class="btn btn-danger">Xác nhận xóa</button>
                <a href="${ctx}/book?action=list" class="btn btn-secondary">Hủy</a>
            </form>
        </c:if>
    </div>
</main>
<%@ include file="/common/footer.jsp" %>
</body>
</html> 