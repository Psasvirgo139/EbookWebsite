<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>🔎 Tìm kiếm truyện - EbookWebsite</title>
    <link rel="stylesheet" href="${ctx}/assets/css/style.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <style>
        .search-filters {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 30px;
        }
        .dropdown-search-author {
            min-width: 250px;
        }
    </style>
</head>
<body>
<%@ include file="/common/header.jsp" %>
<main>
    <div class="container py-4">
        <h1 class="text-center mb-4">🔎 Tìm kiếm truyện</h1>
        <div class="search-filters">
            <form method="get" action="${ctx}/search">
                <div class="row g-3 align-items-center">
                    <div class="col-md-4">
                        <input type="text" class="form-control" name="keyword" placeholder="Tìm kiếm tên truyện..." value="${param.keyword}">
                    </div>
                    <div class="col-md-2">
                        <select class="form-select" name="genre">
                            <option value="">Thể loại</option>
                            <c:forEach var="tag" items="${tags}">
                                <option value="${tag.name}" ${param.genre == tag.name ? 'selected' : ''}>${tag.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-2">
                        <div class="dropdown dropdown-search-author w-100">
                            <button class="btn btn-outline-secondary dropdown-toggle w-100" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                                <c:choose>
                                    <c:when test="${not empty param.author}">
                                        ${param.author}
                                    </c:when>
                                    <c:otherwise>Tác giả</c:otherwise>
                                </c:choose>
                            </button>
                            <ul class="dropdown-menu p-2" style="max-height: 300px; overflow-y: auto; width: 100%">
                                <li>
                                    <input type="text" class="form-control mb-2" placeholder="Tìm tác giả..." onkeyup="filterAuthorOptions(this)">
                                </li>
                                <c:forEach var="author" items="${topAuthors}">
                                    <li>
                                        <a class="dropdown-item author-option" href="#" onclick="selectAuthor('${author.name}')">${author.name}</a>
                                    </li>
                                </c:forEach>
                            </ul>
                            <input type="hidden" name="author" id="authorInput" value="${param.author}">
                        </div>
                    </div>
                    <div class="col-md-1">
                        <select class="form-select" name="minChapters">
                            <option value="0" ${param.minChapters == '0' ? 'selected' : ''}>0 chap</option>
                            <option value="20" ${param.minChapters == '20' ? 'selected' : ''}>20 chap</option>
                            <option value="40" ${param.minChapters == '40' ? 'selected' : ''}>40 chap</option>
                            <option value="60" ${param.minChapters == '60' ? 'selected' : ''}>60 chap</option>
                        </select>
                    </div>
                    <div class="col-md-1">
                        <select class="form-select" name="sortBy">
                            <option value="">Sắp xếp</option>
                            <option value="view_week" ${param.sortBy == 'view_week' ? 'selected' : ''}>Xem tuần</option>
                            <option value="view_month" ${param.sortBy == 'view_month' ? 'selected' : ''}>Xem tháng</option>
                            <option value="view_total" ${param.sortBy == 'view_total' ? 'selected' : ''}>Xem tổng</option>
                            <option value="like" ${param.sortBy == 'like' ? 'selected' : ''}>Lượt like</option>
                        </select>
                    </div>
                    <div class="col-md-1">
                        <select class="form-select" name="status">
                            <option value="all" ${param.status == 'all' ? 'selected' : ''}>Tất cả</option>
                            <option value="ongoing" ${param.status == 'ongoing' ? 'selected' : ''}>Ongoing</option>
                            <option value="completed" ${param.status == 'completed' ? 'selected' : ''}>Completed</option>
                        </select>
                    </div>
                    <div class="col-md-1">
                        <button type="submit" class="btn btn-primary w-100">🔍 Tìm</button>
                    </div>
                </div>
            </form>
        </div>
        <c:if test="${empty bookList}">
            <div class="text-center py-5">
                <h3>📖 Không tìm thấy truyện nào</h3>
                <p class="text-muted">Thử thay đổi từ khóa tìm kiếm hoặc bộ lọc</p>
            </div>
        </c:if>
        <c:if test="${not empty bookList}">
            <div class="row">
                <c:forEach var="book" items="${bookList}">
                    <div class="col-md-3 col-sm-6 mb-4">
                        <div class="card book-card position-relative">
                            <c:choose>
                                <c:when test="${not empty book.coverUrl}">
                                    <img src="${book.coverUrl}" class="card-img-top book-cover" alt="${book.title}">
                                </c:when>
                                <c:otherwise>
                                    <div class="card-img-top book-cover bg-light d-flex align-items-center justify-content-center">
                                        <span class="text-muted">📖 Không có ảnh</span>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                            <div class="card-body">
                                <h6 class="card-title">${book.title}</h6>
                                <p class="card-text">
                                    <small class="text-muted">
                                        <strong>Thể loại:</strong> ${book.releaseType}<br>
                                        <strong>Lượt xem:</strong> ${book.viewCount}
                                    </small>
                                </p>
                                <c:if test="${not empty book.description}">
                                    <p class="card-text">
                                        <small>${book.description.length() > 100 ? book.description.substring(0, 100).concat('...') : book.description}</small>
                                    </p>
                                </c:if>
                            </div>
                            <div class="card-footer">
                                <div class="d-flex justify-content-between">
                                    <a href="${ctx}/book/detail?id=${book.id}" class="btn btn-primary btn-sm">
                                        📖 Chi tiết
                                    </a>
                                    <a href="${ctx}/book/read?bookId=${book.id}&chapterId=1" class="btn btn-success btn-sm">
                                        🚀 Đọc ngay
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:if>
    </div>
</main>
<%@ include file="/common/footer.jsp" %>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
function selectAuthor(name) {
    document.getElementById('authorInput').value = name;
    document.querySelector('.dropdown-search-author .dropdown-toggle').textContent = name;
}
function filterAuthorOptions(input) {
    var filter = input.value.toLowerCase();
    var options = document.querySelectorAll('.author-option');
    options.forEach(function(opt) {
        if (opt.textContent.toLowerCase().indexOf(filter) > -1) {
            opt.style.display = '';
        } else {
            opt.style.display = 'none';
        }
    });
}
</script>
</body>
</html> 