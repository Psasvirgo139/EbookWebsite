<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Trang cá nhân | EBook Website</title>
        <style>
            body{
                font-family:Arial,sans-serif;
                background:#f3f7fb;
                margin:0;
            }
            .profile-container{
                background:#fff;
                max-width:430px;
                margin:60px auto 0;
                box-shadow:0 4px 18px #0002;
                border-radius:12px;
                padding:32px 26px;
            }
            .avatar{
                display:block;
                margin:0 auto 14px;
                border-radius:50%;
                width:92px;
                height:92px;
                object-fit:cover;
                background:#eef2fa;
            }
            h2{
                text-align:center;
                color:#365bc6;
                margin-top:6px;
                margin-bottom:15px;
            }
            .info{
                margin:18px 0 10px;
            }
            .info label{
                color:#5477a5;
                font-weight:bold;
                display:inline-block;
                width:110px;
            }
            .info span{
                color:#222;
            }
            .logout-btn{
                display:block;
                width:100%;
                padding:11px;
                margin-top:16px;
                background:#d54343;
                color:#fff;
                border:none;
                border-radius:6px;
                font-size:1.09rem;
                font-weight:600;
                cursor:pointer;
                transition:background .15s;
            }
            .logout-btn:hover{
                background:#a82222;
            }
        </style>
    </head>
    <body>
        <div class="profile-container">
            <img class="avatar"
                 src="${empty user.avatarUrl ? 'https://i.imgur.com/Vz8s1cC.png' : user.avatarUrl}"
                 alt="Avatar"/>

            <h2><c:out value="${user.username}"/></h2>

            <div class="info">
                <label>Email:</label>       <span><c:out value="${user.email}"/></span><br>
                <label>Quyền:</label>       <span><c:out value="${user.role}"/></span><br>
                <label>Trạng thái:</label>  <span><c:out value="${user.status}"/></span><br>

                <c:if test="${not empty userInfor}">
                    <label>Giới tính:</label> <span><c:out value="${userInfor.gender}"/></span><br>
                    <label>Sinh nhật:</label> <span>
                        <c:choose>
                            <c:when test="${not empty userInfor.birthDay}">
                                <c:out value="${birthDayStr}"/>

                            </c:when>
                            <c:otherwise>-</c:otherwise>
                        </c:choose>
                    </span><br>
                    <label>Điện thoại:</label> <span><c:out value="${userInfor.phone}"/></span><br>
                    <label>Địa chỉ:</label>    <span><c:out value="${userInfor.address}"/></span><br>
                    <label>Giới thiệu:</label> <span><c:out value="${userInfor.introduction}"/></span><br>
                </c:if>

                <label>Ngày tạo:</label> <span>
                    <c:choose>
                        <c:when test="${not empty user.createdAt}">
                            <label>Ngày tạo:</label> <span>
                                <c:out value="${createdAtStr}"/>
                            </span>

                        </c:when>
                        <c:otherwise>-</c:otherwise>
                    </c:choose>
                </span>
            </div>

            <form action="${pageContext.request.contextPath}/logout" method="post">
                <button class="logout-btn" type="submit">Đăng xuất</button>
            </form>
        </div>
    </body>
</html>
