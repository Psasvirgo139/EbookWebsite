<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản lý sách - Admin</title>
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
    <h1>Quản lý sách</h1>
    <table>
        <tr>
            <th>ID</th>
            <th>Tên sách</th>
            <th>Thể loại</th>
            <th>Ngày đăng</th>
            <th>Trạng thái</th>
            <th>Lượt xem</th>
            <th>Hành động</th>
        </tr>
        <c:forEach var="book" items="${books}">
            <tr>
                <td>${book.id}</td>
                <td>${book.title}</td>
                <td>${book.releaseType}</td>
                <td>${book.createdAt}</td>
                <td>${book.status}</td>
                <td>${book.viewCount}</td>
                <td class="actions">
                    <a href="${pageContext.request.contextPath}/admin/books?editId=${book.id}" style="background:#3e2f92;color:#fff;border:none;border-radius:6px;padding:6px 14px;cursor:pointer;margin-right:6px;text-decoration:none;">Sửa</a>
                    <form method="post" action="books" style="display:inline;">
                        <input type="hidden" name="bookId" value="${book.id}" />
                        <button type="submit" name="action" value="delete">Xóa</button>
                    </form>
                    <!-- Có thể bổ sung các nút duyệt, xem chi tiết... -->
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html> 