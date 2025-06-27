<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Trang cá nhân | EBook Website</title>
        <!-- Google Fonts Inter -->
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap&subset=vietnamese" rel="stylesheet">
        <style>
            body {
                font-family: 'Inter', Arial, sans-serif;
                background: #f3f7fb;
                margin: 0;
            }
            .profile-page {
                max-width: 1000px;
                margin: 40px auto 0;
                padding: 0 12px 40px;
            }
            .profile-container {
                background: #fff;
                border-radius: 18px;
                box-shadow: 0 4px 24px #0001;
                padding: 38px 32px 32px;
                display: flex;
                gap: 40px;
                align-items: flex-start;
                flex-wrap: wrap;
            }
            .profile-info {
                flex: 1 1 320px;
                min-width: 280px;
            }
            .avatar {
                display: block;
                margin: 0 auto 18px;
                border-radius: 50%;
                width: 110px;
                height: 110px;
                object-fit: cover;
                background: #eef2fa;
                box-shadow: 0 2px 8px #0001;
            }
            .username {
                text-align: center;
                color: #3e2f92;
                font-size: 1.5rem;
                font-weight: 700;
                margin-bottom: 8px;
            }
            .role-badge {
                display: inline-block;
                background: #fbbf24;
                color: #65341c;
                font-size: 0.98rem;
                font-weight: 600;
                border-radius: 8px;
                padding: 2px 12px;
                margin: 0 auto 12px;
                text-align: center;
            }
            .info-list {
                margin: 18px 0 10px;
                padding: 0;
                list-style: none;
            }
            .info-list li {
                margin-bottom: 10px;
                display: flex;
                align-items: center;
                gap: 10px;
                color: #444;
            }
            .info-list label {
                color: #5477a5;
                font-weight: 600;
                min-width: 90px;
            }
            .logout-btn {
                display: block;
                width: 100%;
                padding: 12px;
                margin-top: 18px;
                background: #e24370;
                color: #fff;
                border: none;
                border-radius: 8px;
                font-size: 1.09rem;
                font-weight: 600;
                cursor: pointer;
                transition: background .15s;
            }
            .logout-btn:hover {
                background: #a82222;
            }
            .profile-section {
                flex: 2 1 380px;
                min-width: 320px;
            }
            .section-title {
                font-size: 1.15rem;
                font-weight: 600;
                color: #6346e6;
                margin-bottom: 12px;
                margin-top: 0;
            }
            .reading-list, .fav-list {
                list-style: none;
                padding: 0;
                margin: 0 0 24px 0;
                display: grid;
                grid-template-columns: 1fr 1fr;
                gap: 14px;
            }
            .reading-list li, .fav-list li {
                background: #f3f2fe;
                border-left: 5px solid #3e2f92;
                border-radius: 10px;
                padding: 12px 16px;
                font-size: 1rem;
                color: #222;
                box-shadow: 0 2px 8px #0001;
                transition: box-shadow 0.15s;
            }
            .reading-list li:hover, .fav-list li:hover {
                box-shadow: 0 4px 16px #3e2f9222;
            }
            @media (max-width: 900px) {
                .profile-container { flex-direction: column; gap: 24px; padding: 24px 8px; }
                .profile-section { min-width: 0; }
            }
            @media (max-width: 600px) {
                .reading-list, .fav-list { grid-template-columns: 1fr; }
            }
        </style>
    </head>
    <body>
        <div class="profile-page">
            <div class="profile-container">
                <div class="profile-info">
                    <img class="avatar"
                         src="${empty user.avatarUrl ? 'https://i.imgur.com/Vz8s1cC.png' : user.avatarUrl}"
                         alt="Avatar"/>
                    <div class="username"><c:out value="${user.username}"/></div>
                    <div class="role-badge"><c:out value="${user.role}"/></div>
                    <ul class="info-list">
                        <li><label>Email:</label> <span><c:out value="${user.email}"/></span></li>
                        <li><label>Trạng thái:</label> <span><c:out value="${user.status}"/></span></li>
                        <c:if test="${not empty userInfor}">
                            <li><label>Giới tính:</label> <span><c:out value="${userInfor.gender}"/></span></li>
                            <li><label>Sinh nhật:</label> <span><c:out value="${birthDayStr}"/></span></li>
                            <li><label>Điện thoại:</label> <span><c:out value="${userInfor.phone}"/></span></li>
                            <li><label>Địa chỉ:</label> <span><c:out value="${userInfor.address}"/></span></li>
                            <li><label>Giới thiệu:</label> <span><c:out value="${userInfor.introduction}"/></span></li>
                        </c:if>
                        <li><label>Ngày tạo:</label> <span><c:out value="${createdAtStr}"/></span></li>
                    </ul>
                    <form action="${pageContext.request.contextPath}/logout" method="post">
                        <button class="logout-btn" type="submit">Đăng xuất</button>
                    </form>
                </div>
                <div class="profile-section">
                    <div class="section-title">Truyện đang đọc</div>
                    <ul class="reading-list">
                        <li>Thần Thoại Bắc Âu - Chương 12</li>
                        <li>Ma Đạo Tổ Sư - Chương 45</li>
                        <li>One Piece - Chương 1090</li>
                        <li>Hệ Thống Tu Tiên - Chương 8</li>
                    </ul>
                    <div class="section-title">Truyện yêu thích</div>
                    <ul class="fav-list">
                        <li>Ma Đạo Tổ Sư</li>
                        <li>One Piece</li>
                        <li>Người Ở Bên Kia</li>
                        <li>Phế Vật Dòng Bá Tước</li>
                    </ul>
                </div>
            </div>
        </div>
    </body>
</html>
