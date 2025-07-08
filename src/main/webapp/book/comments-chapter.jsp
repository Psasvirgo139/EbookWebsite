<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="comments-section">
    <h3>Bình Luận Về Chương</h3>
    
    <p>DEBUG bookId: ${ebook.id}</p>
    <p>DEBUG chapterId: ${chapter.id}</p>
    
    <!-- Form thêm comment mới -->
    <c:if test="${not empty sessionScope.user}">
        <div class="comment-form">
            <form method="post" action="${pageContext.request.contextPath}/add-comment">
                <input type="hidden" name="bookId" value="${ebook.id}">
                <input type="hidden" name="chapterId" value="${chapter.id}">
                <textarea name="content" placeholder="Viết bình luận về chương này..." required></textarea>
                <button type="submit" class="btn btn-primary">Gửi bình luận</button>
            </form>
        </div>
    </c:if>
    
    <!-- Danh sách comment -->
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
    <div class="comments-list">
        <c:forEach var="comment" items="${chapterComments}">
            <c:if test="${comment.parentCommentID == null}">
                <div class="comment-item" data-comment-id="${comment.id}">
                    <div class="comment-header">
                        <span class="comment-author">
                            <c:set var="username" value="Unknown" />
                            <c:forEach var="entry" items="${userMap}">
                                <c:if test="${entry.key == comment.userID}">
                                    <c:set var="username" value="${entry.value}" />
                                </c:if>
                            </c:forEach>
                            ${username}
                            <fmt:formatDate value="${comment.contentDate}" pattern="dd/MM/yyyy HH:mm"/>
                        </span>
                        <span class="comment-edited" style="color:#888; font-size:0.9em;" id="edited-flag-${comment.id}"></span>
                        <span class="comment-deleted" style="color:#d00; font-size:0.9em;" id="deleted-flag-${comment.id}"></span>
                    </div>
                    <div class="comment-content" id="content-${comment.id}">
                        ${comment.content}
                    </div>
                    <div class="comment-actions">
                        <form method="post" action="${pageContext.request.contextPath}/comment/like" style="display: inline;">
                            <input type="hidden" name="commentId" value="${comment.id}">
                            <button type="submit" class="btn btn-sm btn-outline-primary like-btn" data-type="like">
                                👍 <span class="like-count" id="like-count-${comment.id}">${likeMap[comment.id] != null ? likeMap[comment.id] : 0}</span>
                            </button>
                        </form>
                        <form method="post" action="${pageContext.request.contextPath}/comment/dislike" style="display: inline;">
                            <input type="hidden" name="commentId" value="${comment.id}">
                            <button type="submit" class="btn btn-sm btn-outline-secondary dislike-btn" data-type="dislike">
                                👎 <span class="dislike-count" id="dislike-count-${comment.id}">${dislikeMap[comment.id] != null ? dislikeMap[comment.id] : 0}</span>
                            </button>
                        </form>
                        <c:if test="${not empty sessionScope.user}">
                            <form method="get" action="">
                                <input type="hidden" name="bookId" value="${ebook.id}">
                                <input type="hidden" name="chapterId" value="${chapter.id}">
                                <input type="hidden" name="replyTo" value="${comment.id}">
                                <button type="submit" class="btn btn-sm btn-outline-secondary reply-btn">Trả lời</button>
                            </form>
                        </c:if>
                        <c:if test="${sessionScope.userId == comment.userID || sessionScope.role == 'admin'}">
                            <form method="get" action="">
                                <input type="hidden" name="editId" value="${comment.id}"/>
                                <input type="hidden" name="chapterId" value="${chapter.id}"/>
                                <input type="hidden" name="bookId" value="${ebook.id}"/>
                                <button type="submit" class="btn btn-sm btn-outline-warning edit-btn">Sửa</button>
                            </form>
                            <form method="post" action="${pageContext.request.contextPath}/comment/delete" style="display: inline;">
                                <input type="hidden" name="commentId" value="${comment.id}">
                                <input type="hidden" name="chapterId" value="${chapter.id}"/>
                                <input type="hidden" name="bookId" value="${ebook.id}"/>
                                <button type="submit" class="btn btn-sm btn-outline-danger delete-btn">Xóa</button>
                            </form>
                        </c:if>
                    </div>
                    <c:if test="${replyTo == comment.id}">
                        <div class="reply-form" id="reply-form-${comment.id}">
                            <form method="post" action="${pageContext.request.contextPath}/comment/reply">
                                <input type="hidden" name="bookId" value="${ebook.id}">
                                <input type="hidden" name="chapterId" value="${chapter.id}">
                                <input type="hidden" name="parentCommentId" value="${comment.id}">
                                <textarea name="content" placeholder="Viết trả lời..." required></textarea>
                                <button type="submit" class="btn btn-sm btn-primary">Gửi trả lời</button>
                            </form>
                        </div>
                    </c:if>
                    <div class="replies">
                        <c:forEach var="reply" items="${chapterComments}">
                            <c:if test="${reply.parentCommentID == comment.id}">
                                <div class="reply-item">
                                    <div class="comment-header">
                                        <span class="comment-author">
                                            <c:set var="username" value="Unknown" />
                                            <c:forEach var="entry" items="${userMap}">
                                                <c:if test="${entry.key == reply.userID}">
                                                    <c:set var="username" value="${entry.value}" />
                                                </c:if>
                                            </c:forEach>
                                            ${username}
                                            <fmt:formatDate value="${reply.contentDate}" pattern="dd/MM/yyyy HH:mm"/>
                                        </span>
                                        <span class="comment-edited" style="color:#888; font-size:0.9em;" id="edited-flag-${reply.id}"></span>
                                        <span class="comment-deleted" style="color:#d00; font-size:0.9em;" id="deleted-flag-${reply.id}"></span>
                                    </div>
                                    <div class="comment-content" id="content-${reply.id}">
                                        ${reply.content}
                                    </div>
                                    <div class="comment-actions">
                                        <form method="post" action="${pageContext.request.contextPath}/comment/like" style="display: inline;">
                                            <input type="hidden" name="commentId" value="${reply.id}">
                                            <button type="submit" class="btn btn-sm btn-outline-primary like-btn" data-type="like">
                                                👍 <span class="like-count" id="like-count-${reply.id}">${likeMap[reply.id] != null ? likeMap[reply.id] : 0}</span>
                                            </button>
                                        </form>
                                        <form method="post" action="${pageContext.request.contextPath}/comment/dislike" style="display: inline;">
                                            <input type="hidden" name="commentId" value="${reply.id}">
                                            <button type="submit" class="btn btn-sm btn-outline-secondary dislike-btn" data-type="dislike">
                                                👎 <span class="dislike-count" id="dislike-count-${reply.id}">${dislikeMap[reply.id] != null ? dislikeMap[reply.id] : 0}</span>
                                            </button>
                                        </form>
                                        <c:if test="${sessionScope.userId == reply.userID || sessionScope.role == 'admin'}">
                                            <form method="get" action="">
                                                <input type="hidden" name="editId" value="${reply.id}"/>
                                                <input type="hidden" name="chapterId" value="${chapter.id}"/>
                                                <input type="hidden" name="bookId" value="${ebook.id}"/>
                                                <button type="submit" class="btn btn-sm btn-outline-warning edit-btn">Sửa</button>
                                            </form>
                                            <form method="post" action="${pageContext.request.contextPath}/comment/delete" style="display: inline;">
                                                <input type="hidden" name="commentId" value="${reply.id}">
                                                <input type="hidden" name="chapterId" value="${chapter.id}"/>
                                                <input type="hidden" name="bookId" value="${ebook.id}"/>
                                                <button type="submit" class="btn btn-sm btn-outline-danger delete-btn">Xóa</button>
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

<form method="post" action="${pageContext.request.contextPath}/test-log">
  <button type="submit">Test Log</button>
</form> 

