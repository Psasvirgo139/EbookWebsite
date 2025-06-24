<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/common/header.jspf" %>

<div class="container py-4">
    <!-- ======= Thông tin sách ======= -->
    <h2 class="mb-3">${ebook.title}</h2>
    <img src="${ebook.coverUrl}" alt="cover" class="img-fluid mb-3" style="max-height:300px;">

    <div class="mb-4">
        <p><strong>Ngôn ngữ:</strong> ${ebook.language}</p>
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
                        <div class="fw-bold mb-1">Người dùng ID: ${comment.userId}</div>
                        <div>${comment.content}</div>
                        <small class="text-muted">Lúc: 
                            <fmt:formatDate value="${comment.createdAt}" pattern="dd/MM/yyyy HH:mm" />
                        </small>
                    </li>
                </c:forEach>
            </ul>
        </c:if>
    </div>
</div>

<%@ include file="/common/footer.jspf" %>
