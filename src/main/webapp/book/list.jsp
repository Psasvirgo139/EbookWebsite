<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String ctx = request.getContextPath();
%>
<c:choose>
  <c:when test="${not empty param.page and param.page >= '1' and param.page <= '9999'}">
    <c:set var="safePage" value="${param.page}" />
  </c:when>
  <c:otherwise>
    <c:set var="safePage" value="1" />
  </c:otherwise>
</c:choose>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>📚 Danh sách sách - EbookWebsite</title>
    <link rel="stylesheet" href="${ctx}/assets/css/style.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <style>
        .book-card {
            transition: transform 0.2s ease;
            height: 100%;
        }
        .book-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        }
        .book-cover {
            height: 200px;
            object-fit: cover;
            width: 100%;
        }
        .favorite-btn {
            position: absolute;
            top: 10px;
            right: 10px;
            background: rgba(255,255,255,0.9);
            border: none;
            border-radius: 50%;
            width: 40px;
            height: 40px;
            display: flex;
            align-items: center;
            justify-content: center;
            transition: all 0.3s ease;
        }
        .favorite-btn:hover {
            background: rgba(255,255,255,1);
            transform: scale(1.1);
        }
        .search-filters {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 30px;
        }
    </style>
</head>
<body>
<%@ include file="/common/header.jsp" %>

<main>
    <div class="container py-4">
        <h1 class="text-center mb-4">📚 Danh sách sách</h1>
        
        <!-- Search and Filter Section -->
        <div class="search-filters">
            <form method="get" action="${ctx}/book-list">
                <div class="row g-3">
                    <div class="col-md-4">
                        <select class="form-select" name="status">
                            <option value="">Tất cả tiến độ</option>
                            <option value="Ongoing" ${status == 'Ongoing' ? 'selected' : ''}>Đang tiến hành</option>
                            <option value="Completed" ${status == 'Completed' ? 'selected' : ''}>Đã hoàn thành</option>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <select class="form-select" name="genre">
                            <option value="">Tất cả thể loại</option>
                            <c:forEach var="tag" items="${tags}">
                                <option value="${tag.name}" ${selectedGenre == tag.name ? 'selected' : ''}>${tag.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <select class="form-select" name="sortBy">
                            <option value="">Sắp xếp theo</option>
                            <option value="newest" ${sortBy == 'newest' ? 'selected' : ''}>Mới nhất</option>
                            <option value="oldest" ${sortBy == 'oldest' ? 'selected' : ''}>Cũ nhất</option>
                            <option value="title" ${sortBy == 'title' ? 'selected' : ''}>Tên A-Z</option>
                            <option value="views" ${sortBy == 'views' ? 'selected' : ''}>Lượt xem</option>
                        </select>
                    </div>
                    <div class="col-md-2">
                        <button type="submit" class="btn btn-primary w-100">🔍 Lọc</button>
                    </div>
                </div>
            </form>
            </div>
            
            <!-- Results Info -->
        <div class="d-flex justify-content-between align-items-center mb-3">
            <p class="text-muted mb-0">
                Hiển thị ${startBook}-${endBook} trong tổng số ${totalBooks} sách
            </p>
            <c:if test="${totalPages > 1}">
                <span class="text-muted">Trang ${currentPage}/${totalPages}</span>
            </c:if>
        </div>

        <!-- Books Grid -->
        <c:if test="${empty bookList}">
            <div class="text-center py-5">
                <h3>📖 Không tìm thấy sách nào</h3>
                <p class="text-muted">Thử thay đổi từ khóa tìm kiếm hoặc bộ lọc</p>
                </div>
            </c:if>
            
        <c:if test="${not empty bookList}">
            <div class="row">
                <c:forEach var="book" items="${bookList}">
                    <div class="col-md-3 col-sm-6 mb-4">
                        <div class="card book-card position-relative">
                            <!-- Favorite Button (only for logged in users) -->
                            <c:if test="${sessionScope.user != null}">
                                <form method="post" action="${ctx}/favorites" style="display:inline;">
                                    <input type="hidden" name="action" value="add"/>
                                    <input type="hidden" name="ebookId" value="${book.id}"/>
                                    <input type="hidden" name="redirectUrl" value="${ctx}/book-list?page=${safePage}${not empty param.searchKeyword ? '&search=' + param.searchKeyword : ''}"/>
                                    <c:choose>
                                        <c:when test="${favoriteMap[book.id]}">
                                            <button type="submit" class="favorite-btn btn btn-danger" disabled>💖</button>
                                        </c:when>
                                        <c:otherwise>
                                            <button type="submit" class="favorite-btn btn btn-outline-danger">❤️</button>
                                        </c:otherwise>
                                    </c:choose>
                                </form>
                            </c:if>
                            
                            <!-- Book Cover -->
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
            
            <!-- Pagination -->
            <c:if test="${totalPages > 1}">
            <nav aria-label="Book pagination">
                <ul class="pagination justify-content-center">
                    <!-- Previous page -->
                    <c:if test="${currentPage > 1}">
                        <li class="page-item">
                            <a class="page-link" href="${ctx}/book-list?page=${currentPage - 1}&search=${searchKeyword}&genre=${selectedGenre}&sortBy=${sortBy}">
                                « Trước
                            </a>
                        </li>
                    </c:if>

                    <!-- Always show first page -->
                    <li class="page-item ${currentPage == 1 ? 'active' : ''}">
                        <a class="page-link" href="${ctx}/book-list?page=1&search=${searchKeyword}&genre=${selectedGenre}&sortBy=${sortBy}">1</a>
                    </li>

                    <!-- Show pages 2, 3, 4, 5 if totalPages > 1 -->
                    <c:forEach var="i" begin="2" end="${totalPages > 5 ? 5 : totalPages}">
                        <li class="page-item ${currentPage == i ? 'active' : ''}">
                            <a class="page-link" href="${ctx}/book-list?page=${i}&search=${searchKeyword}&genre=${selectedGenre}&sortBy=${sortBy}">${i}</a>
                        </li>
                    </c:forEach>

                    <!-- Dấu ... nếu còn nhiều trang -->
                    <c:if test="${totalPages > 6 && currentPage < totalPages - 2}">
                        <li class="page-item disabled"><span class="page-link">...</span></li>
                    </c:if>

                    <!-- Trang cuối -->
                    <c:if test="${totalPages > 5}">
                        <li class="page-item ${currentPage == totalPages ? 'active' : ''}">
                            <a class="page-link" href="${ctx}/book-list?page=${totalPages}&search=${searchKeyword}&genre=${selectedGenre}&sortBy=${sortBy}">${totalPages}</a>
                        </li>
                    </c:if>

                    <!-- Next page -->
                    <c:if test="${currentPage < totalPages}">
                        <li class="page-item">
                            <a class="page-link" href="${ctx}/book-list?page=${currentPage + 1}&search=${searchKeyword}&genre=${selectedGenre}&sortBy=${sortBy}">
                                Sau »
                            </a>
                        </li>
                    </c:if>
                </ul>
            </nav>
            </c:if>
        </div>
    </main>

<%@ include file="/common/footer.jsp" %>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
// JavaScript function để toggle favorite cho từng sách
function toggleBookFavorite(ebookId, button) {
    // Disable button during request
    button.disabled = true;
    const icon = button.querySelector('span');
    const originalIcon = icon.textContent;
    
    const formData = new FormData();
    formData.append('action', 'add');
    formData.append('ebookId', ebookId);
    
    fetch('${ctx}/favorites', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            // Update button to show "added to favorites"
            icon.textContent = '💖';
            button.style.background = 'rgba(220, 53, 69, 0.9)';
            button.style.color = 'white';
            button.onclick = function() { removeBookFavorite(ebookId, button); };
            } else {
            alert('Lỗi: ' + data.error);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Có lỗi xảy ra khi thêm vào favorites');
    })
    .finally(() => {
        button.disabled = false;
    });
}

function removeBookFavorite(ebookId, button) {
    button.disabled = true;
    const icon = button.querySelector('span');
    
    const formData = new FormData();
    formData.append('action', 'delete');
    formData.append('ebookId', ebookId);
    
    fetch('${ctx}/favorites', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            // Update button back to "add favorite"
            icon.textContent = '❤️';
            button.style.background = 'rgba(255,255,255,0.9)';
            button.style.color = 'inherit';
            button.onclick = function() { toggleBookFavorite(ebookId, button); };
            } else {
            alert('Lỗi: ' + data.error);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Có lỗi xảy ra khi xóa khỏi favorites');
    })
    .finally(() => {
        button.disabled = false;
    });
}
    </script>
</body>
</html>