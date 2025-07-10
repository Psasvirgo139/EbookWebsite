<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String ctx = request.getContextPath();
    boolean editMode = request.getAttribute("editMode") != null && (Boolean)request.getAttribute("editMode");
    com.mycompany.ebookwebsite.model.Ebook book = (com.mycompany.ebookwebsite.model.Ebook)request.getAttribute("book");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8" />
    <title><%= editMode ? "Sửa sách" : "Thêm sách mới" %></title>
    <link rel="stylesheet" href="${ctx}/assets/css/style.css" />
</head>
<body>
<%@ include file="/common/header.jsp" %>
<main>
    <div class="container">
        <h1><%= editMode ? "✏️ Sửa sách" : "➕ Thêm sách mới" %></h1>
        <c:if test="${not empty errors}">
            <ul class="error">
                <c:forEach var="err" items="${errors}">
                    <li>${err}</li>
                </c:forEach>
            </ul>
        </c:if>
        <form method="post" action="${ctx}/book/upload">
            <c:if test="${editMode}">
                <input type="hidden" name="id" value="${book.id}" />
            </c:if>
            <div>
                <label>Tiêu đề:</label>
                <input type="text" name="title" value="${book.title}" required maxlength="255" />
            </div>
            <div>
                <label>Mô tả:</label>
                <textarea name="description" maxlength="1000">${book.description}</textarea>
            </div>
            <div>
                <label>Thể loại:</label>
                <input type="text" name="releaseType" value="${book.releaseType}" required />
            </div>
            <div>
                <label>Ngôn ngữ:</label>
                <input type="text" name="language" value="${book.language}" required />
            </div>
            <div>
                <label>Trạng thái:</label>
                <input type="text" name="status" value="${book.status}" required />
            </div>
            <div>
                <label>Hiển thị:</label>
                <input type="text" name="visibility" value="${book.visibility}" required />
            </div>
            <div>
                <label>Uploader ID:</label>
                <input type="number" name="uploaderId" value="${book.uploaderId}" />
            </div>
            <div>
                <label>Lượt xem:</label>
                <input type="number" name="viewCount" value="${book.viewCount}" min="0" />
            </div>
            <div style="margin-top:16px;">
                <button type="submit" class="btn btn-primary"><%= editMode ? "Cập nhật" : "Tạo mới" %></button>
                <a href="${ctx}/book-list" class="btn btn-secondary">Hủy</a>
            </div>
        </form>
    </div>
</main>
<%@ include file="/common/footer.jsp" %>
</body>
</html> 