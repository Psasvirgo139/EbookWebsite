<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/common/header.jspf" %>

<div class="container py-4">
    <!-- ======= Thông tin sách ======= -->
    <h2 class="mb-3">${ebook.title}</h2>
    <img src="${ebook.coverUrl}" alt="cover" class="img-fluid mb-3" style="max-height:300px;">

    <div class="mb-4">
        <p><strong>Loại phát hành:</strong> ${ebook.releaseType}</p>
        <p><strong>Ngôn ngữ:</strong> ${ebook.language}</p>
        <p><strong>Trạng thái:</strong> ${ebook.status}</p>
        <p><strong>Ngày tạo:</strong> <fmt:formatDate value="${ebookCreatedDate}" pattern="dd/MM/yyyy"/></p>
        <p><strong>Tác giả:</strong>
            <c:forEach var="a" items="${authors}" varStatus="loop">
                ${a.name}<c:if test="${!loop.last}">, </c:if>
            </c:forEach>
        </p>
        <p><strong>Thể loại:</strong>
            <c:forEach var="t" items="${tags}" varStatus="loop">
                <span class="badge bg-secondary">${t.name}</span><c:if test="${!loop.last}"> </c:if>
            </c:forEach>
        </p>
        <p><strong>Mô tả:</strong> ${ebook.description}</p>
        <p><strong>Lượt xem:</strong> ${ebook.viewCount}</p>
        <a href="read?id=${ebook.id}" class="btn btn-success mt-2">Đọc ngay</a>
    </div>

    <!-- Form thêm bình luận -->
    <div class="mt-4">
        <h5>Thêm bình luận</h5>
        <form action="${pageContext.request.contextPath}/add-comment" method="post">
            <input type="hidden" name="ebookId" value="${ebook.id}" />
            <div class="mb-3">
                <textarea name="content" class="form-control" rows="3" placeholder="Viết bình luận..."></textarea>
            </div>
            <button type="submit" class="btn btn-primary">Gửi bình luận</button>
        </form>
    </div>

    <!-- ======= Danh sách bình luận ======= -->
    <div class="mt-5">
        <h4 class="mb-3">Bình luận</h4>

        <c:if test="${empty comments}">
            <p class="text-muted fst-italic">Chưa có bình luận nào.</p>
        </c:if>

        <c:if test="${not empty comments}">
            <ul class="list-group">
                <c:forEach var="comment" items="${comments}">
                    <li class="list-group-item">
                        <div class="fw-bold mb-1">Người dùng ID: ${comment.userID}</div>
                        <div>${comment.content}</div>
                        <small class="text-muted">Lúc: ${comment.createdAt}</small>
                    </li>
                </c:forEach>
            </ul>
        </c:if>
    </div>

    <!-- ======= Danh sách chương / volume ======= -->
    <div class="mt-4">
        <h4>Danh sách chương</h4>

        <!-- Trường hợp có nhiều volume -->
        <c:if test="${isMultiVolume}">
            <c:forEach var="vol" items="${volumes}">
                <h5 class="mt-3">Tập ${vol.number}: ${vol.title}</h5>
                <ul class="list-group list-group-horizontal flex-wrap">
                    <c:forEach var="ch" items="${chapters}">
                        <c:if test="${ch.volumeID == vol.id}">
                            <li class="list-group-item p-2">
                                <a href="read?id=${ebook.id}&chapter=${ch.number}">
                                    Ch ${ch.number}
                                </a>
                                <c:choose>
                                    <c:when test="${ch.accessLevel == 'free' || ch.accessLevel == 'public'}">
                                        <span class="badge bg-success ms-1">Miễn phí</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-warning text-dark ms-1">Trả phí</span>
                                    </c:otherwise>
                                </c:choose>
                            </li>
                        </c:if>
                    </c:forEach>
                </ul>
            </c:forEach>
        </c:if>

        <!-- Trường hợp chỉ có 1 volume hoặc không có volume -->
        <c:if test="${not isMultiVolume}">
            <ul class="list-group list-group-horizontal flex-wrap">
                <c:forEach var="ch" items="${chapters}">
                    <li class="list-group-item p-2">
                        <a href="read?id=${ebook.id}&chapter=${ch.number}">
                            Ch ${ch.number}
                        </a>
                        <c:choose>
                            <c:when test="${ch.accessLevel == 'free' || ch.accessLevel == 'public'}">
                                <span class="badge bg-success ms-1">Miễn phí</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-warning text-dark ms-1">Trả phí</span>
                            </c:otherwise>
                        </c:choose>
                    </li>
                </c:forEach>
            </ul>
        </c:if>
    </div>
</div>

<%@ include file="/common/footer.jspf" %>
