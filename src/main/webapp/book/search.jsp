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
        .search-container {
            background: #f8f9fa;
            padding: 25px;
            border-radius: 12px;
            margin-bottom: 30px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.08);
        }
        .search-bar {
            display: flex;
            gap: 15px;
            align-items: center;
            margin-bottom: 0;
            justify-content: center;
        }
        .search-bar .form-control {
            max-width: 400px;
            height: 45px;
            border-radius: 8px;
            border: 2px solid #e9ecef;
            font-size: 16px;
        }
        .search-bar .form-control:focus {
            border-color: #007bff;
            box-shadow: 0 0 0 0.2rem rgba(0,123,255,.25);
        }
        .search-bar .btn {
            height: 45px;
            border-radius: 8px;
            font-weight: 500;
            min-width: 120px;
        }
        .filter-toggle-btn {
            white-space: nowrap;
            background: #6c757d;
            border-color: #6c757d;
            color: white;
        }
        .filter-toggle-btn:hover {
            background: #5a6268;
            border-color: #545b62;
        }
        .advanced-filters {
            background: white;
            border: 1px solid #ddd;
            border-radius: 12px;
            padding: 25px;
            margin-top: 20px;
            display: none;
            opacity: 0;
            transform: translateY(-10px);
            transition: all 0.3s ease;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        }
        .advanced-filters.show {
            display: block;
            opacity: 1;
            transform: translateY(0);
        }
        .advanced-filters .form-label {
            font-weight: 600;
            color: #495057;
            margin-bottom: 8px;
        }
        .advanced-filters .form-select,
        .advanced-filters .form-control {
            border-radius: 6px;
            border: 1px solid #ced4da;
        }
        .book-card {
            transition: all 0.3s ease;
            height: 100%;
            border-radius: 12px;
            overflow: hidden;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }
        .book-card:hover {
            transform: translateY(-8px);
            box-shadow: 0 8px 25px rgba(0,0,0,0.15);
        }
        .book-cover {
            height: 200px;
            object-fit: cover;
            width: 100%;
        }
        .dropdown-search-author {
            position: relative;
        }
        .dropdown-search-author .dropdown-menu {
            min-width: 280px;
            max-width: 350px;
            border-radius: 8px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.15);
        }
        .dropdown-search-author .dropdown-item {
            padding: 10px 15px;
            transition: background-color 0.2s ease;
        }
        .dropdown-search-author .dropdown-item:hover {
            background-color: #f8f9fa;
        }
        .results-section {
            min-height: 400px;
        }
        .no-results {
            text-align: center;
            padding: 80px 20px;
            color: #6c757d;
        }
        .no-results h3 {
            color: #495057;
            margin-bottom: 15px;
        }
        .card-footer {
            background: #f8f9fa;
            border-top: 1px solid #e9ecef;
        }
        .btn-primary {
            border-radius: 6px;
        }
        .btn-success {
            border-radius: 6px;
        }
        @media (max-width: 768px) {
            .search-bar {
                flex-direction: column;
                align-items: stretch;
                gap: 10px;
            }
            .search-bar > * {
                width: 100%;
                margin-bottom: 0;
            }
            .search-container {
                padding: 20px;
            }
            .advanced-filters {
                padding: 20px;
            }
        }
    </style>
</head>
<body>
<%@ include file="/common/header.jsp" %>
<main>
    <div class="container py-4">
        <h1 class="text-center mb-4">🔎 Tìm kiếm nâng cao</h1>
        
        <div class="search-container">
            <form method="get" action="${ctx}/search" id="searchForm">
                <!-- Thanh tìm kiếm chính -->
                <div class="search-bar">
                    <input type="text" class="form-control flex-grow-1" name="keyword" 
                           placeholder="Nhập tên truyện để tìm kiếm..." value="${param.keyword}"
                           oninput="handleKeywordInput()">
                    <button type="button" class="btn btn-outline-secondary filter-toggle-btn" 
                            onclick="toggleAdvancedFilters()" 
                            ${not empty param.keyword ? 'style="opacity: 0.5;" title="Bộ lọc không khả dụng khi tìm theo từ khóa"' : ''}>
                        <i class="bi bi-funnel"></i> Hiện bộ lọc
                    </button>
                    <button type="submit" class="btn btn-primary">
                        <i class="bi bi-search"></i> Tìm
                    </button>
                </div>

                <!-- Bộ lọc nâng cao (ẩn/hiện) -->
                <div class="advanced-filters ${empty param.keyword and (not empty param.genre or not empty param.author or not empty param.minChapters or not empty param.sortBy or param.status ne 'all') ? 'show' : ''}" id="advancedFilters">
                    <div class="row g-3">
                        <div class="col-md-3">
                            <label class="form-label">Thể loại</label>
                            <select class="form-select" name="genre">
                                <option value="">Tất cả thể loại</option>
                                <c:forEach var="tag" items="${tags}">
                                    <option value="${tag.name}" ${param.genre == tag.name ? 'selected' : ''}>${tag.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-3">
                            <label class="form-label">Tác giả</label>
                            <div class="dropdown dropdown-search-author w-100">
                                <button class="btn btn-outline-secondary dropdown-toggle w-100 text-start" 
                                        type="button" data-bs-toggle="dropdown" aria-expanded="false">
                                    <span class="me-auto">
                                        <c:choose>
                                            <c:when test="${not empty param.author}">
                                                ${param.author}
                                            </c:when>
                                            <c:otherwise>Tất cả</c:otherwise>
                                        </c:choose>
                                    </span>
                                </button>
                                <ul class="dropdown-menu p-2" style="max-height: 300px; overflow-y: auto; width: 100%">
                                    <li>
                                        <input type="text" class="form-control mb-2" 
                                               placeholder="Tìm tác giả..." onkeyup="filterAuthorOptions(this)">
                                    </li>
                                    <li><a class="dropdown-item author-option" href="#" onclick="selectAuthor('')">Tất cả</a></li>
                                    <c:forEach var="author" items="${topAuthors}">
                                        <li>
                                            <a class="dropdown-item author-option" href="#" 
                                               onclick="selectAuthor('${author.name}')">${author.name}</a>
                                        </li>
                                    </c:forEach>
                                </ul>
                                <input type="hidden" name="author" id="authorInput" value="${param.author}">
                            </div>
                        </div>
                        <div class="col-md-2">
                            <label class="form-label">Số chapter tối thiểu</label>
                            <select class="form-select" name="minChapters">
                                <option value="0" ${param.minChapters == '0' || empty param.minChapters ? 'selected' : ''}>0 chapter</option>
                                <option value="20" ${param.minChapters == '20' ? 'selected' : ''}>20 chapter</option>
                                <option value="40" ${param.minChapters == '40' ? 'selected' : ''}>40 chapter</option>
                                <option value="60" ${param.minChapters == '60' ? 'selected' : ''}>60 chapter</option>
                            </select>
                        </div>
                        <div class="col-md-2">
                            <label class="form-label">Sắp xếp theo</label>
                            <select class="form-select" name="sortBy">
                                <option value="" ${empty param.sortBy ? 'selected' : ''}>Lượt xem trong ngày</option>
                                <option value="view_week" ${param.sortBy == 'view_week' ? 'selected' : ''}>Lượt xem trong tuần</option>
                                <option value="view_month" ${param.sortBy == 'view_month' ? 'selected' : ''}>Lượt xem trong tháng</option>
                                <option value="view_total" ${param.sortBy == 'view_total' ? 'selected' : ''}>Lượt xem tổng</option>
                                <option value="like" ${param.sortBy == 'like' ? 'selected' : ''}>Lượt like</option>
                            </select>
                        </div>
                        <div class="col-md-2">
                            <label class="form-label">Tình trạng</label>
                            <select class="form-select" name="status">
                                <option value="all" ${param.status == 'all' || empty param.status ? 'selected' : ''}>Tất cả</option>
                                <option value="ongoing" ${param.status == 'ongoing' ? 'selected' : ''}>Đang ra</option>
                                <option value="completed" ${param.status == 'completed' ? 'selected' : ''}>Hoàn thành</option>
                            </select>
                        </div>
                    </div>
                </div>
            </form>
        </div>
        <div class="results-section">
            <!-- Error message -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger" role="alert">
                    <i class="bi bi-exclamation-triangle"></i> ${error}
                </div>
            </c:if>
            
            <c:if test="${empty bookList && empty error}">
                <div class="no-results">
                    <h3>📖 Không tìm thấy truyện nào</h3>
                    <c:choose>
                        <c:when test="${not empty param.keyword}">
                            <p>Không tìm thấy truyện có tên chứa "<strong>${param.keyword}</strong>"</p>
                            <p><small>Thử tìm kiếm với từ khóa khác hoặc để trống để sử dụng bộ lọc nâng cao</small></p>
                        </c:when>
                        <c:otherwise>
                            <p>Thử thay đổi bộ lọc tìm kiếm hoặc nhập tên truyện cụ thể</p>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:if>
            
            <c:if test="${not empty bookList}">
                <div class="mb-3">
                    <p class="text-muted">
                        Tìm thấy <strong>${bookList.size()}</strong> truyện
                        <c:if test="${not empty param.keyword}">
                            cho từ khóa "<strong>${param.keyword}</strong>"
                        </c:if>
                    </p>
                </div>
                <div class="row">
                    <c:forEach var="book" items="${bookList}">
                        <div class="col-lg-3 col-md-4 col-sm-6 mb-4">
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
                                    <h6 class="card-title fw-bold">${book.title}</h6>
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
    </div>
</main>
<%@ include file="/common/footer.jsp" %>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
<script>
function toggleAdvancedFilters() {
    const filters = document.getElementById('advancedFilters');
    const button = document.querySelector('.filter-toggle-btn');
    
    if (filters.classList.contains('show')) {
        filters.classList.remove('show');
        button.innerHTML = '<i class="bi bi-funnel"></i> Hiện bộ lọc';
        // Smooth transition để avoid layout jump
        setTimeout(() => {
            filters.style.display = 'none';
        }, 300);
    } else {
        filters.style.display = 'block';
        // Force reflow
        filters.offsetHeight;
        filters.classList.add('show');
        button.innerHTML = '<i class="bi bi-funnel-fill"></i> Ẩn bộ lọc';
    }
}

// Function to show/hide advanced filters based on keyword input
function handleKeywordInput() {
    const keywordInput = document.querySelector('input[name="keyword"]');
    const filterToggle = document.querySelector('.filter-toggle-btn');
    const filters = document.getElementById('advancedFilters');
    
    if (keywordInput.value.trim() !== '') {
        // Hide filters when user types keyword
        if (filters.classList.contains('show')) {
            filters.classList.remove('show');
            filterToggle.innerHTML = '<i class="bi bi-funnel"></i> Hiện bộ lọc';
            setTimeout(() => {
                filters.style.display = 'none';
            }, 300);
        }
        filterToggle.style.opacity = '0.5';
        filterToggle.title = 'Bộ lọc không khả dụng khi tìm theo từ khóa';
        filterToggle.disabled = true;
    } else {
        // Enable filters when no keyword
        filterToggle.style.opacity = '1';
        filterToggle.title = '';
        filterToggle.disabled = false;
    }
}

function selectAuthor(name) {
    document.getElementById('authorInput').value = name;
    const displayText = name || 'Tất cả';
    document.querySelector('.dropdown-search-author .dropdown-toggle span').textContent = displayText;
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

// Auto show filters if any filter is active
document.addEventListener('DOMContentLoaded', function() {
    const filters = document.getElementById('advancedFilters');
    const button = document.querySelector('.filter-toggle-btn');
    const keywordInput = document.querySelector('input[name="keyword"]');
    
    if (filters.classList.contains('show')) {
        button.innerHTML = '<i class="bi bi-funnel-fill"></i> Ẩn bộ lọc';
    }
    
    // Initialize state based on keyword
    handleKeywordInput();
});
</script>
</body>
</html> 