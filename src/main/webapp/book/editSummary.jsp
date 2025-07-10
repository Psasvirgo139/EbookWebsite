<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.mycompany.ebookwebsite.model.Ebook" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css" />
<script src="${pageContext.request.contextPath}/assets/js/app.js" defer></script>

<div class="logo-bar">
    <img src="${pageContext.request.contextPath}/img/logo.png" alt="Logo">
</div>

<%
    Ebook book = (Ebook) request.getAttribute("book");
%>
<!DOCTYPE html>
<html>
    <head>
        <title>Sửa tóm tắt sách</title>
    </head>
    <body>
        <div class="container sakura-bg">
            <h2 class="form-title">
                <span style="color:#e08bca;">🌸🤖</span> Sửa tóm tắt nội dung (AI)
            </h2>
            <form id="editSummaryForm"
                  action="${pageContext.request.contextPath}/book/detail?action=editSummary"
                  method="post"
                  class="form-book card-shadow"
                  style="max-width:550px;margin:0 auto;background:#fff;border-radius:18px;padding:28px 34px;">
                <input type="hidden" name="id" value="${book.id}" />

                <div class="form-row">
                    <label for="summary" style="color:#bc4073;"><b>Tóm tắt nội dung:</b></label>
                    <textarea name="summary" id="summary" rows="8" required
                              class="sakura-input"
                              style="width:100%;padding:14px;border-radius:10px;border:1.5px solid #f8b5d2;background:#fff9fa;font-size:1em;">
                        ${book.summary}
                    </textarea>
                </div>

                <button type="submit" class="btn btn-add sakura-btn" style="margin-top:16px;">Lưu tóm tắt</button>
                <c:if test="${not empty filterError}">
                    <div class="alert-error sakura-alert">${filterError}</div>
                </c:if>
            </form>
            <div class="action-links" style="margin-top:22px;text-align:center;">
                <a href="${pageContext.request.contextPath}/book/detail?id=${book.id}" class="btn sakura-btn">Quay lại chi tiết sách</a>
            </div>
        </div>
    </body>
</html>
