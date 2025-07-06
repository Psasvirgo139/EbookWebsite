<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ include file="/common/header.jspf" %>

<div class="container py-4">
    <h2>${ebook.title}</h2>
    <h4>Chương ${currentChapter}: ${chapter.title}</h4>

    <!-- Hiển thị thông báo success/error -->
    <c:if test="${not empty successMessage}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="fas fa-check-circle"></i> ${successMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>
    
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="fas fa-exclamation-triangle"></i> ${errorMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <div class="d-flex justify-content-between mb-3">
        <c:if test="${prevChapter != null}">
            <a class="btn btn-outline-primary" href="read?id=${ebook.id}&chapter=${prevChapter}">← Ch ${prevChapter}</a>
        </c:if>
        <c:if test="${nextChapter != null}">
            <a class="btn btn-outline-primary ms-auto" href="read?id=${ebook.id}&chapter=${nextChapter}">Ch ${nextChapter} →</a>
        </c:if>
    </div>

    <c:choose>
        <c:when test="${hasAccess}">
            <hr>
            <div class="chapter-content">
                <p style="white-space: pre-line;">${chapter.content}</p>
            </div>
        </c:when>
        <c:when test="${needUnlock}">
            <!-- UI cho chapter premium cần unlock -->
            <div class="alert alert-warning mt-3">
                <h5><i class="fas fa-lock"></i> Chapter Premium</h5>
                <p><strong>Bạn chưa mở khóa chapter này!</strong></p>
                <p>Chapter này yêu cầu mở khóa để đọc nội dung.</p>
                
                <div class="row mt-3">
                    <div class="col-md-6">
                        <div class="card">
                            <div class="card-body text-center">
                                <h6 class="card-title">Mở khóa với Coin</h6>
                                <p class="card-text">
                                    <strong>Phí: ${unlockCost} coins</strong><br>
                                    <small>Bạn hiện có: ${userCoins} coins</small>
                                </p>
                                <c:choose>
                                    <c:when test="${userCoins >= unlockCost}">
                                        <form method="post" action="${pageContext.request.contextPath}/book/read" 
                                              onsubmit="this.querySelector('button').innerHTML='<i class=&quot;fas fa-spinner fa-spin&quot;></i> Đang mở khóa...'; this.querySelector('button').disabled=true;">
                                            <input type="hidden" name="action" value="unlock">
                                            <input type="hidden" name="bookId" value="${ebook.id}">
                                            <input type="hidden" name="chapterNum" value="${currentChapter}">
                                            <input type="hidden" name="chapterId" value="${chapter.id}">
                                            
                                            <button type="submit" class="btn btn-primary">
                                                <i class="fas fa-unlock"></i> Mở khóa (${unlockCost} coins)
                                            </button>
                                        </form>
                                        <small class="text-muted mt-2">
                                            Sau khi mở khóa, bạn sẽ có ${userCoins - unlockCost} coins
                                        </small>
                                    </c:when>
                                    <c:otherwise>
                                        <button class="btn btn-secondary" disabled>
                                            <i class="fas fa-coins"></i> Không đủ coin
                                        </button>
                                        <small class="text-muted d-block mt-2">
                                            Bạn cần thêm ${unlockCost - userCoins} coins
                                        </small>
                                        <a href="${pageContext.request.contextPath}/coin/payment" 
                                           class="btn btn-sm btn-outline-primary mt-2">
                                            Nạp coin
                                        </a>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-md-6">
                        <div class="card">
                            <div class="card-body text-center">
                                <h6 class="card-title">Trở thành Premium User</h6>
                                <p class="card-text">
                                    <strong>10.000 VND/tháng</strong><br>
                                    <small>Xem tất cả chapter premium</small>
                                </p>
                                <a href="${pageContext.request.contextPath}/coin/payment" class="btn btn-warning">
                                    <i class="fas fa-crown"></i> Nâng cấp Premium
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="alert alert-danger mt-3">
                <h5><i class="fas fa-exclamation-triangle"></i> Không có quyền truy cập</h5>
                <p>Bạn cần đăng nhập để xem chapter này.</p>
                <a href="${pageContext.request.contextPath}/user/login.jsp" class="btn btn-primary">
                    <i class="fas fa-sign-in-alt"></i> Đăng nhập
                </a>
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
                                <a class="page-link" href="read?id=${ebook.id}&chapter=${ch.number}">
                                    Ch ${ch.number}
                                    <c:choose>
                                        <c:when test="${ch.accessLevel == 'free' || ch.accessLevel == 'public'}">
                                            <span class="badge bg-success ms-1">Free</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-warning text-dark ms-1">Premium</span>
                                            <!-- Hiển thị trạng thái unlock cho chapter premium -->
                                            <c:if test="${user != null && ch.accessLevel == 'premium'}">
                                                <!-- Kiểm tra chapter đã unlock chưa -->
                                                <c:set var="isUnlocked" value="false" />
                                                <c:forEach var="unlockedCh" items="${userUnlockedChapters}">
                                                    <c:if test="${unlockedCh.chapterId == ch.id}">
                                                        <c:set var="isUnlocked" value="true" />
                                                    </c:if>
                                                </c:forEach>
                                                <c:choose>
                                                    <c:when test="${isUnlocked}">
                                                        <i class="fas fa-unlock text-success ms-1" title="Đã mở khóa"></i>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <i class="fas fa-lock text-warning ms-1" title="Chưa mở khóa"></i>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:if>
                                        </c:otherwise>
                                    </c:choose>
                                </a>
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
                        <a class="page-link" href="read?id=${ebook.id}&chapter=${ch.number}">
                            Ch ${ch.number}
                            <c:choose>
                                <c:when test="${ch.accessLevel == 'free' || ch.accessLevel == 'public'}">
                                    <span class="badge bg-success ms-1">Free</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-warning text-dark ms-1">Premium</span>
                                    <!-- Hiển thị trạng thái unlock cho chapter premium -->
                                    <c:if test="${user != null && ch.accessLevel == 'premium'}">
                                        <!-- Kiểm tra chapter đã unlock chưa -->
                                        <c:set var="isUnlocked" value="false" />
                                        <c:forEach var="unlockedCh" items="${userUnlockedChapters}">
                                            <c:if test="${unlockedCh.chapterId == ch.id}">
                                                <c:set var="isUnlocked" value="true" />
                                            </c:if>
                                        </c:forEach>
                                        <c:choose>
                                            <c:when test="${isUnlocked}">
                                                <i class="fas fa-unlock text-success ms-1" title="Đã mở khóa"></i>
                                            </c:when>
                                            <c:otherwise>
                                                <i class="fas fa-lock text-warning ms-1" title="Chưa mở khóa"></i>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:if>
                                </c:otherwise>
                            </c:choose>
                        </a>
                    </li>
                </c:forEach>
            </ul>
        </c:if>
    </div>

    <div class="d-flex justify-content-between mt-4">
        <c:if test="${prevChapter != null}">
            <a class="btn btn-outline-primary" href="read?id=${ebook.id}&chapter=${prevChapter}">← Ch ${prevChapter}</a>
        </c:if>
        <c:if test="${nextChapter != null}">
            <a class="btn btn-outline-primary ms-auto" href="read?id=${ebook.id}&chapter=${nextChapter}">Ch ${nextChapter} →</a>
        </c:if>
    </div>
</div>

<%@ include file="/common/footer.jspf" %>