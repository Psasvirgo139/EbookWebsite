<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Sửa thông tin sách</title>
    <style>
        body { font-family: 'Inter', Arial, sans-serif; background: #f4f6fb; color: #222; }
        .container { max-width: 600px; margin: 32px auto; background: #fff; border-radius: 12px; box-shadow: 0 2px 8px rgba(60,50,100,0.07); padding: 32px; }
        h1 { color: #3e2f92; margin-bottom: 24px; }
        label { display: block; margin-top: 16px; }
        input, select { width: 100%; padding: 8px; margin-top: 4px; border-radius: 6px; border: 1px solid #ccc; }
        button { margin-top: 24px; background: #3e2f92; color: #fff; border: none; border-radius: 6px; padding: 10px 24px; cursor: pointer; }
        button:hover { background: #6346e6; }
        .error { color: #e24370; margin-top: 12px; }
    </style>
</head>
<body>
<div class="container">
    <h1>Sửa thông tin sách</h1>
    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>
    <form method="post" action="${pageContext.request.contextPath}/admin/books">
        <input type="hidden" name="action" value="update"/>
        <input type="hidden" name="bookId" value="${editBook.id}"/>
        <label>Tên sách:
            <input type="text" name="title" value="${editBook.title}" required/>
        </label>
        <label>Thể loại:
            <input type="text" name="releaseType" value="${editBook.releaseType}" required/>
        </label>
        <label>Trạng thái:
            <select name="status">
                <option value="approved" ${editBook.status == 'approved' ? 'selected' : ''}>Đã duyệt</option>
                <option value="pending" ${editBook.status == 'pending' ? 'selected' : ''}>Chờ duyệt</option>
                <option value="deleted" ${editBook.status == 'deleted' ? 'selected' : ''}>Đã xóa</option>
            </select>
        </label>
        <!-- Thêm các trường khác nếu cần -->
        <button type="submit">Cập nhật</button>
    </form>
</div>
</body>
</html> 