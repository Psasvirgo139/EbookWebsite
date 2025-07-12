<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- Lấy tham số replyTo từ URL --%>
<c:set var="replyTo" value="${param.replyTo}" />
<c:set var="editId" value="${param.editId}" />

<style>
.reply-item {
    margin-left: 2rem;
    border-left: 2px solid #eee;
    padding-left: 1rem;
    margin-top: 0.5rem;
    background: #fafbfc;
    border-radius: 4px;
}
</style>

<div class="comments-section">
    <h3>Bình luận về sách</h3>
    
    <!-- Form thêm comment mới -->
    <c:if test="${not empty sessionScope.user}">
        <div class="comment-form">
            <form method="post" action="${pageContext.request.contextPath}/add-comment">
                <input type="hidden" name="bookId" value="${ebook.id}">
                <textarea name="content" placeholder="Viết bình luận của bạn..." required></textarea>
                <button type="submit" class="btn btn-primary">Gửi bình luận</button>
            </form>
        </div>
    </c:if>
    
    <!-- Danh sách comment -->
    <div class="comments-list">
        <c:forEach var="comment" items="${bookComments}" varStatus="loopStatus">
            <c:if test="${comment.parentCommentID == null}">
                <div class="comment-item" data-comment-id="${comment.id}">
                    <div class="comment-header">
                        <span class="comment-author">${userMap[comment.userID]}
                            <fmt:formatDate value="${comment.contentDate}" pattern="dd/MM/yyyy HH:mm"/>
                        </span>
                        <!-- Trạng thái đã sửa -->
                        <span class="comment-edited" style="color:#888; font-size:0.9em;" id="edited-flag-${comment.id}"></span>
                        <!-- Trạng thái đã xóa -->
                        <span class="comment-deleted" style="color:#d00; font-size:0.9em;" id="deleted-flag-${comment.id}"></span>
                    </div>
                    <c:choose>
                        <c:when test="${editId == comment.id}">
                            <form method="post" action="${pageContext.request.contextPath}/comment/edit">
                                <input type="hidden" name="commentId" value="${comment.id}">
                                <input type="hidden" name="bookId" value="${ebook.id}">
                                <textarea name="content" required>${comment.content}</textarea>
                                <button type="submit" class="btn btn-sm btn-primary">Lưu</button>
                                <a href="?id=${ebook.id}" class="btn btn-sm btn-secondary">Hủy</a>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <div class="comment-content" id="content-${comment.id}">
                                ${comment.content}
                            </div>
                        </c:otherwise>
                    </c:choose>
                    <div class="comment-actions">
                        <!-- Like button -->
                        <form method="post" action="${pageContext.request.contextPath}/comment/vote" style="display: inline;">
                            <input type="hidden" name="commentId" value="${comment.id}">
                            <input type="hidden" name="type" value="like">
                            <button type="submit" class="btn btn-sm btn-outline-primary like-btn" data-type="like">
                                👍 <span class="like-count" id="like-count-${comment.id}">${likeMap[comment.id] != null ? likeMap[comment.id] : 0}</span>
                            </button>
                        </form>
                        <form method="post" action="${pageContext.request.contextPath}/comment/vote" style="display: inline;">
                            <input type="hidden" name="commentId" value="${comment.id}">
                            <input type="hidden" name="type" value="dislike">
                            <button type="submit" class="btn btn-sm btn-outline-secondary dislike-btn" data-type="dislike">
                                👎 <span class="dislike-count" id="dislike-count-${comment.id}">${dislikeMap[comment.id] != null ? dislikeMap[comment.id] : 0}</span>
                            </button>
                        </form>
                        <!-- Reply button -->
                        <c:if test="${not empty sessionScope.user}">
                            <form method="get" action="">
                                <input type="hidden" name="id" value="${ebook.id}">
                                <input type="hidden" name="replyTo" value="${comment.id}">
                                <button type="submit" class="btn btn-sm btn-outline-secondary reply-btn">Trả lời</button>
                            </form>
                        </c:if>
                        <!-- Sửa/Xóa nếu là chủ sở hữu hoặc admin -->
                        <c:if test="${sessionScope.userId == comment.userID || sessionScope.role == 'admin'}">
                            <form method="get" action="">
                                <input type="hidden" name="id" value="${ebook.id}">
                                <input type="hidden" name="editId" value="${comment.id}">
                                <button type="submit" class="btn btn-sm btn-outline-warning edit-btn">Sửa</button>
                            </form>
                            <form method="post" action="${pageContext.request.contextPath}/comment/delete" style="display: inline;">
                                <input type="hidden" name="commentId" value="${comment.id}">
                                <button type="submit" class="btn btn-sm btn-outline-danger delete-btn" data-comment-id="${comment.id}">Xóa</button>
                            </form>
                        </c:if>
                        <!-- Nút Báo cáo -->
                        <c:if test="${not empty sessionScope.user}">
                            <button type="button" class="btn btn-sm btn-outline-danger report-btn" data-comment-id="${comment.id}">Báo cáo</button>
                        </c:if>
                    </div>
                    <!-- Reply form -->
                    <c:if test="${replyTo == comment.id}">
                        <div class="reply-form" id="reply-form-${comment.id}">
                            <form method="post" action="${pageContext.request.contextPath}/comment/reply">
                                <input type="hidden" name="bookId" value="${ebook.id}">
                                <input type="hidden" name="parentCommentId" value="${comment.id}">
                                <textarea name="content" placeholder="Viết trả lời..." required></textarea>
                                <button type="submit" class="btn btn-sm btn-primary">Gửi trả lời</button>
                            </form>
                        </div>
                    </c:if>
                    <!-- In các reply ngay dưới comment cha -->
                    <div class="replies">
                        <c:forEach var="reply" items="${bookComments}">
                            <c:if test="${reply.parentCommentID == comment.id}">
                                <div class="reply-item">
                                    <div class="comment-header">
                                        <span class="comment-author">${userMap[reply.userID]}
                                            <fmt:formatDate value="${reply.contentDate}" pattern="dd/MM/yyyy HH:mm"/>
                                        </span>
                                        <span class="comment-edited" style="color:#888; font-size:0.9em;" id="edited-flag-${reply.id}"></span>
                                        <span class="comment-deleted" style="color:#d00; font-size:0.9em;" id="deleted-flag-${reply.id}"></span>
                                    </div>
                                    <c:choose>
                                        <c:when test="${editId == reply.id}">
                                            <form method="post" action="${pageContext.request.contextPath}/comment/edit">
                                                <input type="hidden" name="commentId" value="${reply.id}">
                                                <input type="hidden" name="bookId" value="${ebook.id}">
                                                <textarea name="content" required>${reply.content}</textarea>
                                                <button type="submit" class="btn btn-sm btn-primary">Lưu</button>
                                                <a href="?id=${ebook.id}" class="btn btn-sm btn-secondary">Hủy</a>
                                            </form>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="comment-content" id="content-${reply.id}">
                                                ${reply.content}
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                    <div class="comment-actions">
                                        <div class="comment-actions">
                                            <form method="post" action="${pageContext.request.contextPath}/comment/vote" style="display: inline;">
                                                <input type="hidden" name="commentId" value="${reply.id}">
                                                <input type="hidden" name="type" value="like">
                                                <button type="submit" class="btn btn-sm btn-outline-primary like-btn" data-type="like">
                                                    👍 <span class="like-count" id="like-count-${reply.id}">${likeMap[reply.id] != null ? likeMap[reply.id] : 0}</span>
                                                </button>
                                            </form>
                                            <form method="post" action="${pageContext.request.contextPath}/comment/vote" style="display: inline;">
                                                <input type="hidden" name="commentId" value="${reply.id}">
                                                <input type="hidden" name="type" value="dislike">
                                                <button type="submit" class="btn btn-sm btn-outline-secondary dislike-btn" data-type="dislike">
                                                    👎 <span class="dislike-count" id="dislike-count-${reply.id}">${dislikeMap[reply.id] != null ? dislikeMap[reply.id] : 0}</span>
                                                </button>
                                            </form>
                                        <c:if test="${sessionScope.userId == reply.userID || sessionScope.role == 'admin'}">
                                            <form method="get" action="">
                                                <input type="hidden" name="id" value="${ebook.id}">
                                                <input type="hidden" name="editId" value="${reply.id}">
                                                <button type="submit" class="btn btn-sm btn-outline-warning edit-btn">Sửa</button>
                                            </form>
                                            <form method="post" action="${pageContext.request.contextPath}/comment/delete" style="display: inline;">
                                                <input type="hidden" name="commentId" value="${reply.id}">
                                                <button type="submit" class="btn btn-sm btn-outline-danger delete-btn" data-comment-id="${reply.id}">Xóa</button>
                                            </form>
                                        </c:if>
                                    </div>
                                </div>
                            </c:if>
                        </c:forEach>
                    </div>
                </div>
            </c:if>
        </c:forEach>
    </div>
</div>

<!-- Modal xác nhận xóa -->
<div class="modal" id="deleteModal" tabindex="-1" style="display:none; position:fixed; top:0; left:0; width:100vw; height:100vh; background:rgba(0,0,0,0.3); z-index:9999; align-items:center; justify-content:center;">
    <div style="background:#fff; padding:2rem; border-radius:10px; min-width:300px; max-width:90vw;">
        <h5>Bạn chắc chắn muốn xóa bình luận này?</h5>
        <div class="d-flex justify-content-end mt-3">
            <button class="btn btn-secondary" id="cancelDeleteBtn">Hủy</button>
            <button class="btn btn-danger ms-2" id="confirmDeleteBtn">Xóa</button>
        </div>
    </div>
</div>

<!-- Modal báo cáo bình luận -->
<div class="modal" id="reportModal" tabindex="-1" style="display:none; position:fixed; top:0; left:0; width:100vw; height:100vh; background:rgba(0,0,0,0.3); z-index:9999; align-items:center; justify-content:center;">
    <div style="background:#fff; padding:2rem; border-radius:10px; min-width:300px; max-width:90vw;">
        <h5>Báo cáo bình luận</h5>
        <form id="reportForm" method="post" action="${pageContext.request.contextPath}/comment/report">
            <input type="hidden" name="commentId" id="reportCommentId" />
            <label>Lý do báo cáo:</label>
            <textarea name="reason" id="reportReason" required style="width:100%;min-height:60px;"></textarea>
            <div style="margin-top:1rem; text-align:right;">
                <button type="button" class="btn btn-secondary" id="cancelReportBtn">Hủy</button>
                <button type="submit" class="btn btn-danger">Gửi báo cáo</button>
            </div>
        </form>
    </div>
</div>
<script>
// Hiển thị modal khi bấm nút Báo cáo
const reportBtns = document.querySelectorAll('.report-btn');
const reportModal = document.getElementById('reportModal');
const reportForm = document.getElementById('reportForm');
const reportCommentId = document.getElementById('reportCommentId');
const cancelReportBtn = document.getElementById('cancelReportBtn');
reportBtns.forEach(btn => {
    btn.addEventListener('click', function() {
        reportCommentId.value = this.getAttribute('data-comment-id');
        reportModal.style.display = 'flex';
    });
});
cancelReportBtn.addEventListener('click', function() {
    reportModal.style.display = 'none';
    reportForm.reset();
});
// Đóng modal khi submit thành công (có thể reload lại trang hoặc show thông báo)
reportForm.addEventListener('submit', function() {
    reportModal.style.display = 'none';
});
</script>
 