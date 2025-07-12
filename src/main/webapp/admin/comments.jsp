<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản lý bình luận - Admin</title>
    <style>
        body { font-family: 'Inter', Arial, sans-serif; background: #f4f6fb; color: #222; }
        .container { max-width: 1100px; margin: 32px auto; background: #fff; border-radius: 12px; box-shadow: 0 2px 8px rgba(60,50,100,0.07); padding: 32px; }
        h1 { color: #3e2f92; margin-bottom: 24px; }
        table { width: 100%; border-collapse: collapse; background: #fff; border-radius: 12px; overflow: hidden; box-shadow: 0 2px 8px rgba(60,50,100,0.07); }
        th, td { padding: 14px 18px; text-align: left; }
        th { background: #f3f2fe; color: #3e2f92; font-weight: 600; }
        tr:not(:last-child) { border-bottom: 1px solid #eee; }
        .actions button { background: #e24370; color: #fff; border: none; border-radius: 6px; padding: 6px 14px; cursor: pointer; margin-right: 6px; }
        .actions button:hover { background: #c62828; }
    </style>
</head>
<body>
<div class="container">
    <h1>Quản lý bình luận</h1>
    <table>
        <tr>
            <th>ID</th>
            <th>User</th>
            <th>Sách</th>
            <th>Chương</th>
            <th>Nội dung</th>
            <th>Lý do báo cáo</th>
            <th>Ngày tạo</th>
            <th>Hành động</th>
        </tr>
        <c:forEach var="c" items="${comments}">
            <tr>
                <td>${c.id}</td>
                <td>${c.username}</td>
                <td>${c.ebookTitle}</td>
                <td><c:out value="${c.chapterId}" default="-"/></td>
                <td>${c.content}</td>
                <td>
                    <c:set var="reason" value="" />
                    <c:set var="found" value="false" />
                    <c:forEach var="r" items="${pageContext.request.request.getAttribute('reports')}">
                        <c:if test="${!found && r.commentId == c.id}">
                            <c:set var="reason" value="${r.reason}" />
                            <c:set var="found" value="true" />
                        </c:if>
                    </c:forEach>
                    <c:out value="${reason}" default="-"/>
                </td>
                <td>${c.createdAt}</td>
                <td class="actions">
                    <form method="post" action="comments" style="display:inline;">
                        <input type="hidden" name="commentId" value="${c.id}" />
                        <button type="submit" name="action" value="delete">Xóa</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html> 