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
        /* --- Hiệu ứng bộ lọc nâng cao --- */
        .advanced-filters {
            overflow: hidden;
            transition: max-height 0.4s cubic-bezier(.4,0,.2,1), opacity 0.4s cubic-bezier(.4,0,.2,1), padding 0.3s, margin 0.3s;
            max-height: 1000px;
            opacity: 1;
            margin-bottom: 24px;
            background: #fff;
            border-radius: 16px;
            box-shadow: 0 2px 16px rgba(0,0,0,0.06);
            padding: 24px 32px 16px 32px;
            position: relative;
        }
        .advanced-filters.hide {
            max-height: 0;
            opacity: 0;
            padding: 0 32px;
            margin-bottom: 0;
        }
        .filter-row {
            display: flex;
            flex-wrap: wrap;
            gap: 18px;
            align-items: flex-end;
            justify-content: flex-start;
        }
        .filter-group {
            display: flex;
            flex-direction: column;
            align-items: flex-start;
            min-width: 150px;
            flex: 1 1 160px;
            margin-bottom: 0;
        }
        .filter-group label, .form-label {
            font-weight: 500;
            color: #333;
            margin-bottom: 4px;
            font-size: 0.98rem;
        }
        .filter-group select,
        .filter-group input,
        .form-select,
        .form-control {
            border-radius: 8px;
            border: 1.5px solid #d0d0d0;
            padding: 7px 12px;
            font-size: 1rem;
            min-width: 120px;
            background: #f9f9f9;
            transition: border 0.2s;
        }
        .filter-group select:focus,
        .filter-group input:focus,
        .form-select:focus,
        .form-control:focus {
            border: 1.5px solid #007bff;
            outline: none;
            background: #fff;
        }
        .filter-search-btn {
            align-self: flex-end;
            margin-left: 18px;
            margin-bottom: 0;
            height: 40px;
            min-width: 120px;
            font-weight: 500;
            border-radius: 8px;
        }
        @media (max-width: 1200px) {
            .filter-row { gap: 10px; }
            .filter-group { min-width: 120px; }
        }
        @media (max-width: 992px) {
            .filter-row { flex-direction: column; gap: 10px; align-items: stretch; }
            .filter-group { width: 100%; min-width: 0; }
            .filter-search-btn { align-self: stretch; margin-left: 0; }
        }
        @media (max-width: 768px) {
            .advanced-filters { padding: 10px 4px 4px 4px; }
        }
    </style>
    <style>
    /* Fix dropdown tác giả bị chìm */
    .dropdown-search-author .dropdown-menu {
        z-index: 1055 !important;
        position: absolute !important;
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
                <div class="filter-controls">
                    <button type="button" class="btn btn-outline-secondary filter-toggle-btn" onclick="toggleAdvancedFilters()">
                        <i class="bi bi-funnel"></i> <span id="toggleText">Hiện bộ lọc</span>
                    </button>
                    <button type="submit" class="btn btn-primary">
                        <i class="bi bi-search"></i> Tìm
                    </button>
                </div>
                <div class="search-bar">
                    <input type="text" class="form-control flex-grow-1" name="keyword" 
                           placeholder="Nhập tên truyện để tìm kiếm..." value="${param.keyword}"
                           oninput="handleKeywordInput()">
                </div>
                <div class="advanced-filters ${empty param.keyword and (not empty param.genre or not empty param.author or not empty param.minChapters or not empty param.sortBy or param.status ne 'all') ? 'show' : 'hide'}" id="advancedFilters">
                    <div class="filter-row">
                        <div class="filter-group">
                            <label class="form-label">Thể loại</label>
                            <select class="form-select" name="genre">
                                <option value="">Tất cả thể loại</option>
                                <c:forEach var="tag" items="${tags}">
                                    <option value="${tag.name}" ${param.genre == tag.name ? 'selected' : ''}>${tag.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="filter-group">
                            <label class="form-label">Tác giả</label>
                            <select class="form-select" name="author">
                                <option value="">Tất cả</option>
                                <c:forEach var="author" items="${authors}">
                                    <option value="${author.name}" ${param.author == author.name ? 'selected' : ''}>${author.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        
                        <div class="filter-group">
                            <label class="form-label">Số chapter tối thiểu</label>
                            <select class="form-select" name="minChapters">
                                <option value="0" ${param.minChapters == '0' || empty param.minChapters ? 'selected' : ''}>0 chapter</option>
                                <option value="20" ${param.minChapters == '20' ? 'selected' : ''}>20 chapter</option>
                                <option value="40" ${param.minChapters == '40' ? 'selected' : ''}>40 chapter</option>
                                <option value="60" ${param.minChapters == '60' ? 'selected' : ''}>60 chapter</option>
                            </select>
                        </div>
                        <div class="filter-group">
                            <label class="form-label">Sắp xếp theo</label>
                            <select class="form-select" name="sortBy">
                                <option value="" ${empty param.sortBy ? 'selected' : ''}>Lượt xem trong ngày</option>
                                <option value="view_week" ${param.sortBy == 'view_week' ? 'selected' : ''}>Lượt xem trong tuần</option>
                                <option value="view_month" ${param.sortBy == 'view_month' ? 'selected' : ''}>Lượt xem trong tháng</option>
                                <option value="view_total" ${param.sortBy == 'view_total' ? 'selected' : ''}>Lượt xem tổng</option>
                                <option value="like" ${param.sortBy == 'like' ? 'selected' : ''}>Lượt like</option>
                            </select>
                        </div>
                        <div class="filter-group">
                            <label class="form-label">Tình trạng</label>
                            <select class="form-select" name="status">
                                <option value="all" ${param.status == 'all' || empty param.status ? 'selected' : ''}>Tất cả</option>
                                <option value="ongoing" ${param.status == 'ongoing' ? 'selected' : ''}>Đang ra</option>
                                <option value="completed" ${param.status == 'completed' ? 'selected' : ''}>Hoàn thành</option>
                            </select>
                        </div>
                        <button type="submit" class="btn btn-primary filter-search-btn">
                            <i class="bi bi-search"></i> Tìm kiếm
                        </button>
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
    const btn = document.querySelector('.filter-toggle-btn');
    const toggleText = document.getElementById('toggleText');
    if (filters.classList.contains('hide')) {
        filters.classList.remove('hide');
        filters.classList.add('show');
        toggleText.textContent = 'Ẩn bộ lọc';
        btn.innerHTML = '<i class="bi bi-funnel-fill"></i> <span id="toggleText">Ẩn bộ lọc</span>';
        setTimeout(() => {
            filters.scrollIntoView({ behavior: 'smooth', block: 'center' });
        }, 400);
    } else {
        filters.classList.remove('show');
        filters.classList.add('hide');
        toggleText.textContent = 'Hiện bộ lọc';
        btn.innerHTML = '<i class="bi bi-funnel"></i> <span id="toggleText">Hiện bộ lọc</span>';
    }
}

// Function to show/hide advanced filters based on keyword input
function handleKeywordInput() {
    const keywordInput = document.querySelector('input[name="keyword"]');
    const filterToggle = document.querySelector('.filter-toggle-btn');
    const filters = document.getElementById('advancedFilters');
    const toggleText = document.getElementById('toggleText');
    
    if (keywordInput.value.trim() !== '') {
        // Hide filters when user types keyword
        if (filters.classList.contains('show')) {
            filters.classList.remove('show');
            filters.classList.add('hide');
            toggleText.textContent = 'Hiện bộ lọc';
            filterToggle.innerHTML = '<i class="bi bi-funnel"></i> <span id="toggleText">Hiện bộ lọc</span>';
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

// Auto show filters if any filter is active
document.addEventListener('DOMContentLoaded', function() {
    handleKeywordInput();
});
</script>
</body>
</html> 