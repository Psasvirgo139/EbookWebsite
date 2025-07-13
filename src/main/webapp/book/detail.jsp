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
        
        <!-- ===== AI SUMMARY DISPLAY ===== -->
        <c:if test="${not empty ebook.summary}">
            <div class="alert alert-info mt-3" style="border-left: 4px solid #17a2b8;">
                <h6 class="alert-heading">
                    <i class="fas fa-robot text-info"></i> 🤖 Tóm tắt AI
                </h6>
                <p class="mb-0" style="line-height: 1.6;">${ebook.summary}</p>
                <hr class="mt-2 mb-2">
                <small class="text-muted">
                    <i class="fas fa-info-circle"></i> Tóm tắt được tạo tự động bằng AI
                </small>
            </div>
        </c:if>
        
        <p><strong>Lượt xem:</strong> ${ebook.viewCount}</p>
        
        <!-- Action buttons -->
        <div class="mt-3">
            <a href="${pageContext.request.contextPath}/book/read?bookId=${ebook.id}&chapterId=1" class="btn btn-success">📖 Đọc ngay</a>
            
            <!-- Favorite button (only for logged in users) -->
            <c:if test="${sessionScope.user != null}">
                <form method="post" action="${pageContext.request.contextPath}/favorites" style="display:inline;">
                    <input type="hidden" name="action" value="add"/>
                    <input type="hidden" name="ebookId" value="${ebook.id}"/>
                    <input type="hidden" name="redirectUrl" value="${pageContext.request.contextPath}/book/detail?id=${ebook.id}"/>
                    <c:choose>
                        <c:when test="${isFavorite}">
                            <button type="submit" class="btn btn-danger ms-2" disabled>💖 Đã yêu thích</button>
                        </c:when>
                        <c:otherwise>
                            <button type="submit" class="btn btn-outline-danger ms-2">❤️ Yêu thích</button>
                        </c:otherwise>
                    </c:choose>
                </form>
            </c:if>
            
            <!-- Admin actions (if user has permission) -->
            <c:if test="${sessionScope.user != null && (sessionScope.user.role == 'admin' || sessionScope.user.id == ebook.uploaderId)}">
                <a href="${pageContext.request.contextPath}/book/detail?action=editSummary&id=${ebook.id}" class="btn btn-outline-primary btn-sm ms-2">📝 Sửa tóm tắt</a>
                <a href="${pageContext.request.contextPath}/book/detail?action=delete&id=${ebook.id}" class="btn btn-outline-danger btn-sm ms-1">🗑️ Xóa</a>
            </c:if>
            
            <a href="${pageContext.request.contextPath}/book-list" class="btn btn-outline-secondary btn-sm ms-2">← Quay lại danh sách</a>
        </div>
    </div>

    <!-- ======= Bình luận về sách ======= -->
    <div class="mt-5">
        <c:if test="${not empty ebook and not empty ebook.id}">
            <c:set var="bookId" value="${ebook.id}" />
            <jsp:include page="comments-book.jsp" />
        </c:if>
        <c:if test="${empty ebook or empty ebook.id}">
            <div class="alert alert-danger">Không tìm thấy sách hoặc ID không hợp lệ!</div>
        </c:if>
    </div>

    <!-- ======= Bình luận tổng hợp từ các chương ======= -->
    <div class="mt-5">
        <h4 class="mb-3">Bình luận từ các chương</h4>
        <c:if test="${empty aggregatedComments}">
            <p class="text-muted fst-italic">Chưa có bình luận nào từ các chương.</p>
        </c:if>
        <c:if test="${not empty aggregatedComments}">
            <div class="comments-list">
                <c:forEach var="comment" items="${aggregatedComments}">
                    <div class="comment-item">
                        <div class="comment-header">
                            <span class="comment-author">User ${comment.userID}</span>
                            <span class="comment-date">
                                ${comment.createdAt}
                            </span>
                        </div>
                        <div class="comment-content">
                            ${comment.content}
                        </div>
                        <div class="comment-actions">
                            <!-- Like button only (no reply for chapter comments) -->
                            <form method="post" style="display: inline;">
                                <input type="hidden" name="commentId" value="${comment.id}">
                                <button type="submit" class="btn btn-sm btn-outline-primary">
                                    ❤️ ${comment.likeCount}
                                </button>
                            </form>
                        </div>
                    </div>
                </c:forEach>
            </div>
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
                                <a href="${pageContext.request.contextPath}/book/read?bookId=${ebook.id}&chapterId=${ch.number}">
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
                        <a href="${pageContext.request.contextPath}/book/read?bookId=${ebook.id}&chapterId=${ch.number}">
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

<script>
// Check if book is already in favorites when page loads
document.addEventListener('DOMContentLoaded', function() {
    // This would require checking favorite status via AJAX
    // For now, we'll assume it's not favorited initially
});
</script>
