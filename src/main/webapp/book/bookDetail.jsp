<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8" />
    <title>Chi tiết sách</title>
    <link rel="stylesheet" href="${ctx}/assets/css/style.css" />
</head>
<body>
<%@ include file="/common/header.jsp" %>
<main>
    <div class="container">
        <h1>📖 Chi tiết Sách</h1>
        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>
        <c:if test="${not empty book}">
            <div><strong>ID:</strong> ${book.id}</div>
            <div><strong>Tiêu đề:</strong> ${book.title}</div>
            <div><strong>Mô tả:</strong> ${book.description}</div>
            <div><strong>Thể loại:</strong> ${book.releaseType}</div>
            <div><strong>Ngôn ngữ:</strong> ${book.language}</div>
            <div><strong>Trạng thái:</strong> ${book.status}</div>
            <div><strong>Lượt xem:</strong> ${book.viewCount}</div>
            <div><strong>Uploader:</strong> ${book.uploaderId}</div>
            <div style="margin-top:16px;">
                <a href="${ctx}/book?action=edit&id=${book.id}" class="btn btn-sm">✏️ Sửa</a>
                <a href="${ctx}/book?action=delete&id=${book.id}" class="btn btn-sm btn-danger">🗑️ Xóa</a>
                <a href="${ctx}/book?action=list" class="btn btn-sm">← Quay lại danh sách</a>
            </div>
        </c:if>
    </div>
</main>
<%@ include file="/common/footer.jsp" %>
</body>
</html> 