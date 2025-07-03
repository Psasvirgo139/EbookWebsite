<%@page contentType="text/html;charset=UTF-8" language="java"%>
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
        <a href="#">Quản lý sách</a>
        <a href="#">Quản lý người dùng</a>
        <a href="#">Quản lý tác giả</a>
        <a href="#">Quản lý bình luận</a>
        <a href="#">Thống kê</a>
        <form action="${pageContext.request.contextPath}/logout" method="post" style="margin-top:auto;">
            <button type="submit" class="logout-link" style="width:100%;">Đăng xuất</button>
        </form>
    </nav>
    <main class="main-content">
        <div class="dashboard-title">Chào mừng Admin!</div>
        <div class="stats">
            <div class="stat-card">
                <div class="stat-label">Tổng số sách</div>
                <div class="stat-value">1,234</div>
            </div>
            <div class="stat-card">
                <div class="stat-label">Người dùng</div>
                <div class="stat-value">567</div>
            </div>
            <div class="stat-card">
                <div class="stat-label">Bình luận</div>
                <div class="stat-value">2,890</div>
            </div>
        </div>
        <div class="section-title">Danh sách sách mới nhất</div>
        <table>
            <tr>
                <th>Tên sách</th>
                <th>Tác giả</th>
                <th>Thể loại</th>
                <th>Ngày đăng</th>
                <th>Trạng thái</th>
            </tr>
            <tr>
                <td>Thần Thoại Bắc Âu</td>
                <td>Rick Riordan</td>
                <td>Hành động</td>
                <td>2024-06-25</td>
                <td>Đã duyệt</td>
            </tr>
            <tr>
                <td>Ma Đạo Tổ Sư</td>
                <td>Mặc Hương Đồng Khứu</td>
                <td>Kỳ ảo</td>
                <td>2024-06-24</td>
                <td>Chờ duyệt</td>
            </tr>
            <tr>
                <td>One Piece</td>
                <td>Eiichiro Oda</td>
                <td>Phiêu lưu</td>
                <td>2024-06-23</td>
                <td>Đã duyệt</td>
            </tr>
        </table>
        <div class="section-title">Người dùng mới đăng ký</div>
        <table>
            <tr>
                <th>Tên</th>
                <th>Email</th>
                <th>Ngày đăng ký</th>
                <th>Trạng thái</th>
            </tr>
            <tr>
                <td>Nguyễn Văn A</td>
                <td>vana@gmail.com</td>
                <td>2024-06-25</td>
                <td>Hoạt động</td>
            </tr>
            <tr>
                <td>Trần Thị B</td>
                <td>tranb@gmail.com</td>
                <td>2024-06-24</td>
                <td>Chờ xác thực</td>
            </tr>
        </table>
    </main>
</div>
</body>
</html>
