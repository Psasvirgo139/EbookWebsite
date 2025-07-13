<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản lý người dùng - Admin</title>
    <style>
        body { font-family: 'Inter', Arial, sans-serif; background: #f4f6fb; color: #222; }
        .container { max-width: 1100px; margin: 32px auto; background: #fff; border-radius: 12px; box-shadow: 0 2px 8px rgba(60,50,100,0.07); padding: 32px; }
        h1 { color: #3e2f92; margin-bottom: 24px; }
        table { width: 100%; border-collapse: collapse; background: #fff; border-radius: 12px; overflow: hidden; box-shadow: 0 2px 8px rgba(60,50,100,0.07); }
        th, td { padding: 14px 18px; text-align: left; }
        th { background: #f3f2fe; color: #3e2f92; font-weight: 600; }
        tr:not(:last-child) { border-bottom: 1px solid #eee; }
        .actions a, .actions button { background: #3e2f92; color: #fff; border: none; border-radius: 6px; padding: 6px 14px; cursor: pointer; margin-right: 6px; text-decoration: none; }
        .actions button { background: #e24370; }
        .actions a:hover { background: #6346e6; }
        .actions button:hover { background: #c62828; }
    </style>
</head>
<body>
<div class="container">
    <h1>Quản lý người dùng</h1>
    <table>
        <tr>
            <th>ID</th>
            <th>Tên đăng nhập</th>
            <th>Email</th>
            <th>Vai trò</th>
            <th>Trạng thái</th>
            <th>Ngày tạo</th>
            <th>Đăng nhập cuối</th>
            <th>Hành động</th>
        </tr>
        <c:forEach var="user" items="${users}">
            <tr>
                <td>${user.id}</td>
                <td>${user.username}</td>
                <td>${user.email}</td>
                <td>${user.role}</td>
                <td>${user.status}</td>
                <td>${user.createdAt}</td>
                <td>${user.lastLogin}</td>
                <td class="actions">
                    <a href="${pageContext.request.contextPath}/admin/users?editId=${user.id}">Sửa</a>
                    <form method="post" action="users" style="display:inline;">
                        <input type="hidden" name="userId" value="${user.id}" />
                        <button type="submit" name="action" value="delete">Xóa</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html> 