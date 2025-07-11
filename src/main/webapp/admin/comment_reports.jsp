<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản lý báo cáo bình luận - Admin</title>
    <style>
        body { font-family: 'Inter', Arial, sans-serif; background: #f4f6fb; color: #222; }
        .container { max-width: 1100px; margin: 32px auto; background: #fff; border-radius: 12px; box-shadow: 0 2px 8px rgba(60,50,100,0.07); padding: 32px; }
        h1 { color: #3e2f92; margin-bottom: 24px; }
        table { width: 100%; border-collapse: collapse; background: #fff; border-radius: 12px; overflow: hidden; box-shadow: 0 2px 8px rgba(60,50,100,0.07); }
        th, td { padding: 14px 18px; text-align: left; }
        th { background: #f3f2fe; color: #3e2f92; font-weight: 600; }
        tr:not(:last-child) { border-bottom: 1px solid #eee; }
        .status-pending { color: #e24370; font-weight: bold; }
        .status-resolved { color: #3e2f92; font-weight: bold; }
    </style>
</head>
<body>
<div class="container">
    <h1>Quản lý báo cáo bình luận</h1>
    <table>
        <tr>
            <th>ID</th>
            <th>Bình luận</th>
            <th>Người báo cáo</th>
            <th>Lý do</th>
            <th>Ngày báo cáo</th>
            <th>Trạng thái</th>
        </tr>
        <c:forEach var="r" items="${reports}">
            <tr>
                <td>${r.id}</td>
                <td>
                    <b>${r.commentUsername}</b>: ${r.commentContent}
                </td>
                <td>${r.reporterUsername}</td>
                <td>${r.reason}</td>
                <td>${r.reportedAt}</td>
                <td><span class="status-${r.status}">${r.status}</span></td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html> 