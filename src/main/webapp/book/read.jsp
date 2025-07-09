<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String ctx = request.getContextPath();
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8" />
    <title>${ebook.title} | Đọc truyện online</title>
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <meta name="description" content="Đọc truyện ${ebook.title} - ${ebook.description}" />
    <link rel="stylesheet" href="${ctx}/assets/css/style.css" />
    <link rel="icon" href="${ctx}/favicon.svg" type="image/svg+xml" />
</head>
<body>

<%@ include file="/common/header.jsp" %>

<main id="main">
    <div class="container">
        <a href="${ctx}/" class="back-link">← Quay lại trang chủ</a>

        <c:choose>
            <c:when test="${not empty ebook}">
                <h1>${ebook.title}</h1>
                <p>Trạng thái: ${ebook.status} | Lượt xem: ${ebook.viewCount}
                    <c:if test="${not empty ebook.createdAt}"> | Ngày tạo: ${ebook.createdAt}</c:if>
                </p>

                <c:if test="${not empty ebook.summary}">
                    <h3>Tóm tắt AI</h3>
                    <p>${ebook.summary}</p>
                </c:if>

                <c:if test="${not empty ebook.description}">
                    <h3>Mô tả</h3>
                    <p>${ebook.description}</p>
                </c:if>

                <!-- Hiển thị toàn văn nếu không có chương -->
                <c:if test="${not empty bookContent}">
                    <h3>Nội dung</h3>
                    <div>${bookContent}</div>
                </c:if>

                <!-- Nếu có chương -->
                <c:if test="${not empty chapter}">
                    <h2>Chương ${currentChapter}: ${chapter.title}</h2>

                    <div>
                        <c:if test="${prevChapter != null}">
                            <a class="btn" href="${ctx}/read?id=${ebook.id}&chapter=${prevChapter}">← Ch ${prevChapter}</a>
                        </c:if>
                        <c:if test="${nextChapter != null}">
                            <a class="btn" href="${ctx}/read?id=${ebook.id}&chapter=${nextChapter}">Ch ${nextChapter} →</a>
                        </c:if>
                    </div>

                    <c:choose>
                        <c:when test="${hasAccess}">
                            <hr>
                            <pre>${chapter.content}</pre>
                        </c:when>
                        <c:otherwise>
                            <div>
                                Bạn cần mua chapter này hoặc đăng ký gói Premium để đọc.
                            </div>
                        </c:otherwise>
                    </c:choose>
                </c:if>

                <!-- Danh sách chương -->
                <div>
                    <h4>Danh sách chương</h4>

                    <c:if test="${not empty volumes and fn:length(volumes) > 1}">
                        <c:forEach var="vol" items="${volumes}">
                            <h5>Tập ${vol.number}: ${vol.title}</h5>
                            <ul>
                                <c:forEach var="ch" items="${chapters}">
                                    <c:if test="${ch.volumeID == vol.id}">
                                        <li>
                                            <a href="${ctx}/read?id=${ebook.id}&chapter=${ch.number}">Ch ${ch.number}</a>
                                            <c:choose>
                                                <c:when test="${ch.accessLevel == 'free' || ch.accessLevel == 'public'}">
                                                    <span>[Free]</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span>[$]</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </li>
                                    </c:if>
                                </c:forEach>
                            </ul>
                        </c:forEach>
                    </c:if>

                    <c:if test="${empty volumes or fn:length(volumes) <= 1}">
                        <ul>
                            <c:forEach var="ch" items="${chapters}">
                                <li>
                                    <a href="${ctx}/read?id=${ebook.id}&chapter=${ch.number}">Ch ${ch.number}</a>
                                    <c:choose>
                                        <c:when test="${ch.accessLevel == 'free' || ch.accessLevel == 'public'}">
                                            <span>[Free]</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span>[$]</span>
                                        </c:otherwise>
                                    </c:choose>
                                </li>
                            </c:forEach>
                        </ul>
                    </c:if>
                </div>

                <div>
                    <a href="${ctx}/" class="btn">← Trang chủ</a>
                    <a href="${ctx}/book?action=list" class="btn">📚 Danh sách truyện</a>
                </div>
            </c:when>

            <c:otherwise>
                <div>
                    <h2>❌ Không tìm thấy sách</h2>
                    <p>Sách bạn đang tìm kiếm không tồn tại hoặc đã bị xóa.</p>
                    <a href="${ctx}/" class="btn">Quay lại trang chủ</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</main>

<%@ include file="/common/footer.jsp" %>
<script src="${ctx}/assets/js/app.js" defer></script>
</body>
</html>
