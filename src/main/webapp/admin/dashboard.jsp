<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard</title>
    <style>
        body {
            margin: 0;
            font-family: 'Inter', Arial, sans-serif;
            background: #f4f6fb;
            color: #222;
        }
        .admin-container {
            display: flex;
            min-height: 100vh;
        }
        .sidebar {
            width: 220px;
            background: #3e2f92;
            color: #fff;
            display: flex;
            flex-direction: column;
            padding: 32px 0 0 0;
        }
        .sidebar h2 {
            text-align: center;
            margin-bottom: 32px;
            font-size: 1.4rem;
            letter-spacing: 1px;
        }
        .sidebar a {
            color: #fff;
            text-decoration: none;
            padding: 14px 32px;
            display: block;
            font-size: 1.05rem;
            border-left: 4px solid transparent;
            transition: background 0.18s, border 0.18s;
        }
        .sidebar a.active, .sidebar a:hover {
            background: #6346e6;
            border-left: 4px solid #fbbf24;
        }
        .sidebar .logout-link {
            color: #fff;
            background: #e24370;
            border-left: 4px solid #e24370;
            margin-top: auto;
            font-weight: 600;
            text-align: center;
        }
        .sidebar .logout-link:hover {
            background: #c62828;
            border-left: 4px solid #fbbf24;
        }
        .main-content {
            flex: 1;
            padding: 36px 48px;
        }
        .dashboard-title {
            font-size: 2rem;
            margin-bottom: 18px;
            color: #3e2f92;
        }
        .stats {
            display: flex;
            gap: 32px;
            margin-bottom: 36px;
        }
        .stat-card {
            background: #fff;
            border-radius: 14px;
            box-shadow: 0 2px 8px rgba(60,50,100,0.07);
            padding: 24px 32px;
            flex: 1;
            display: flex;
            flex-direction: column;
            align-items: flex-start;
        }
        .stat-label {
            color: #888;
            font-size: 1rem;
            margin-bottom: 8px;
        }
        .stat-value {
            font-size: 2.1rem;
            font-weight: 700;
            color: #3e2f92;
        }
        .section-title {
            font-size: 1.2rem;
            font-weight: 600;
            margin: 32px 0 16px;
            color: #6346e6;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            background: #fff;
            border-radius: 12px;
            overflow: hidden;
            box-shadow: 0 2px 8px rgba(60,50,100,0.07);
        }
        th, td {
            padding: 14px 18px;
            text-align: left;
        }
        th {
            background: #f3f2fe;
            color: #3e2f92;
            font-weight: 600;
        }
        tr:not(:last-child) { border-bottom: 1px solid #eee; }
        @media (max-width: 900px) {
            .admin-container { flex-direction: column; }
            .sidebar { width: 100%; flex-direction: row; padding: 0; }
            .sidebar h2 { display: none; }
            .sidebar a { flex: 1; text-align: center; padding: 14px 0; }
            .main-content { padding: 24px 8px; }
            .stats { flex-direction: column; gap: 18px; }
        }
    </style>
</head>
<body>
<div class="admin-container">
    <nav class="sidebar">
        <h2>Quản trị</h2>
        <a href="#" class="active">Dashboard</a>
        <a href="${pageContext.request.contextPath}/admin/books">Quản lý sách</a>
        <a href="${pageContext.request.contextPath}/admin/users">Quản Lý Người Dùng</a>
        <a href="${pageContext.request.contextPath}/admin/comments">Quản lý bình luận</a>
        
        <form action="${pageContext.request.contextPath}/logout" method="post" style="margin-top:auto;">
            <button type="submit" class="logout-link" style="width:100%;">Đăng xuất</button>
        </form>
    </nav>
    <main class="main-content">
        <div class="dashboard-title">Thống kê tài chính</div>
        <div class="stats">
            <div class="stat-card">
                <div class="stat-label">Tổng doanh thu (VNĐ)</div>
                <div class="stat-value">
                    <c:out value="${totalRevenue}"/>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-label">Tổng số giao dịch</div>
                <div class="stat-value">
                    <c:out value="${orderCount}"/>
                </div>
            </div>
        </div>
        <div class="section-title">Danh sách sách mới nhất</div>
        <table>
            <tr>
                <th>Tên sách</th>
                <th>Thể loại</th>
                <th>Ngày đăng</th>
                <th>Trạng thái</th>
            </tr>
            <c:forEach var="book" items="${latestBooks}">
                <tr>
                    <td>${book.title}</td>
                    <td>${book.releaseType}</td>
                    <td>${book.createdAt}</td>
                    <td>${book.status}</td>
                </tr>
            </c:forEach>
        </table>
        <div class="section-title">Người dùng mới đăng ký</div>
        <table>
            <tr>
                <th>Tên</th>
                <th>Email</th>
                <th>Ngày đăng ký</th>
                <th>Trạng thái</th>
            </tr>
            <c:forEach var="user" items="${latestUsers}">
                <tr>
                    <td>${user.username}</td>
                    <td>${user.email}</td>
                    <td>${user.createdAt}</td>
                    <td>${user.status}</td>
                </tr>
            </c:forEach>
        </table>
    </main>
</div>
</body>
</html>
