<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ include file="/common/header.jspf" %>

<div class="container py-4">
    <h2>${ebook.title}</h2>
    <h4>Chương ${currentChapter}: ${chapter.title}</h4>

    <c:choose>
        <c:when test="${hasAccess}">
            <hr>
            <p style="white-space: pre-line;">${chapter.content}</p>
        </c:when>
        <c:otherwise>
            <div class="alert alert-warning mt-3">
                Bạn cần mua chapter này hoặc đăng ký gói Premium để đọc.
            </div>
        </c:otherwise>
    </c:choose>


    <div class="mt-4">
        <h5>Danh sách chương</h5>

        <!-- Nếu có volumes -->
        <c:if test="${not empty volumes and fn:length(volumes) > 1}">
            <c:forEach var="vol" items="${volumes}">
                <h6 class="mt-2">Tập ${vol.number}: ${vol.title}</h6>
                <ul class="pagination">
                    <c:forEach var="ch" items="${chapters}">
                        <c:if test="${ch.volumeID == vol.id}">
                            <li class="page-item ${ch.number == currentChapter ? 'active' : ''}">
                                <a class="page-link" href="read?bookId=${ebook.id}&chapterId=${ch.number}">
                                    Ch ${ch.number}
                                </a>
                                <c:choose>
                                    <c:when test="${ch.accessLevel == 'free' || ch.accessLevel == 'public'}">
                                        <span class="badge bg-success ms-1">Free</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-warning text-dark ms-1">$</span>
                                    </c:otherwise>
                                </c:choose>
                            </li>
                        </c:if>
                    </c:forEach>
                </ul>
            </c:forEach>
        </c:if>

        <!-- Nếu không có volume hoặc chỉ 1 volume -->
        <c:if test="${empty volumes or fn:length(volumes) <= 1}">
            <ul class="pagination">
                <c:forEach var="ch" items="${chapters}">
                    <li class="page-item ${ch.number == currentChapter ? 'active' : ''}">
                        <a class="page-link" href="read?bookId=${ebook.id}&chapterId=${ch.number}">
                            Ch ${ch.number}
                        </a>
                        <c:choose>
                            <c:when test="${ch.accessLevel == 'free' || ch.accessLevel == 'public'}">
                                <span class="badge bg-success ms-1">Free</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-warning text-dark ms-1">$</span>
                            </c:otherwise>
                        </c:choose>
                    </li>
                </c:forEach>
            </ul>
        </c:if>
    </div>

    <div class="d-flex justify-content-between mt-4">
        <c:if test="${prevChapter != null}">
            <a class="btn btn-outline-primary" href="read?bookId=${ebook.id}&chapterId=${prevChapter}">← Ch ${prevChapter}</a>
        </c:if>
        <c:if test="${nextChapter != null}">
            <a class="btn btn-outline-primary ms-auto" href="read?bookId=${ebook.id}&chapterId=${nextChapter}">Ch ${nextChapter} →</a>
        </c:if>
    </div>

    <!-- ======= Bình luận về chương ======= -->
    <div class="mt-5">
        <c:set var="bookId" value="${ebook.id}" />
        <jsp:include page="comments-chapter.jsp" />
    </div>
</div>

<%@ include file="/common/footer.jspf" %>