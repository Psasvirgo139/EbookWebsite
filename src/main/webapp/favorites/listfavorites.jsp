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
    <title>❤️ Sách yêu thích - EbookWebsite</title>
    <link rel="stylesheet" href="${ctx}/assets/css/style.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <style>
        .favorites-container {
            max-width: 1200px;
            margin: 40px auto;
            padding: 20px;
        }
        .favorite-card {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 16px;
            padding: 25px;
            margin-bottom: 20px;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }
        .favorite-card:hover {
            transform: translateY(-3px);
            box-shadow: 0 8px 25px rgba(0,0,0,0.2);
        }
        .book-title {
            font-size: 1.4rem;
            font-weight: bold;
            margin-bottom: 10px;
        }
        .book-info {
            opacity: 0.9;
            line-height: 1.6;
            margin-bottom: 15px;
        }
        .btn-remove {
            background: linear-gradient(45deg, #ff6b6b, #ff8e8e);
            border: none;
            color: white;
            padding: 8px 16px;
            border-radius: 20px;
            font-weight: 600;
            transition: all 0.3s ease;
        }
        .btn-remove:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(255, 107, 107, 0.4);
            color: white;
        }
        .empty-state {
            text-align: center;
            padding: 60px 20px;
            color: #666;
        }
        .empty-icon {
            font-size: 4rem;
            margin-bottom: 20px;
            opacity: 0.5;
        }
    </style>
</head>
<body>
<%@ include file="/common/header.jsp" %>

<main>
    <div class="favorites-container">
        <div class="text-center mb-5">
            <h1 class="display-4">❤️ Sách yêu thích</h1>
            <p class="lead">Danh sách sách yêu thích của <strong>${userName}</strong></p>
        </div>

        <c:if test="${empty favorites}">
            <div class="empty-state">
                <div class="empty-icon">📚</div>
                <h3>Chưa có sách yêu thích nào</h3>
                <p class="text-muted">Hãy khám phá và thêm sách vào danh sách yêu thích của bạn!</p>
                <a href="${ctx}/book-list" class="btn btn-primary">Khám phá sách</a>
            </div>
        </c:if>

        <c:if test="${not empty favorites}">
            <div class="row">
                <c:forEach var="favorite" items="${favorites}">
                    <div class="col-md-6 col-lg-4 mb-4">
                        <div class="favorite-card">
                            <div class="book-title">
                                📖 ${ebookMap[favorite.ebookID].title}
                            </div>
                            <div class="book-info">
                                <p><strong>Thể loại:</strong> ${ebookMap[favorite.ebookID].releaseType}</p>
                                <p><strong>Lượt xem:</strong> ${ebookMap[favorite.ebookID].viewCount}</p>
                                <c:if test="${not empty ebookMap[favorite.ebookID].description}">
                                    <p><strong>Mô tả:</strong> ${ebookMap[favorite.ebookID].description}</p>
                                </c:if>
                                <p><strong>Ngày thêm:</strong> ${favorite.createdAt}</p>
                            </div>
                            <div class="d-flex justify-content-between align-items-center">
                                <a href="${ctx}/book/detail?id=${favorite.ebookID}" class="btn btn-light">
                                    Xem chi tiết
                                </a>
                                <form method="post" action="${ctx}/favorites" style="display:inline;">
                                    <input type="hidden" name="action" value="delete"/>
                                    <input type="hidden" name="ebookId" value="${favorite.ebookID}"/>
                                    <input type="hidden" name="redirectUrl" value="${ctx}/favorites"/>
                                    <button type="submit" class="btn btn-remove" onclick="return confirm('Bạn có chắc muốn xóa sách này khỏi favorites?');">❌ Xóa</button>
                                </form>
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
function removeFavorite(ebookId, chapterId) {
    if (!confirm('Bạn có chắc muốn xóa sách này khỏi favorites?')) {
        return;
    }
    
    const formData = new FormData();
    formData.append('action', 'delete');
    formData.append('ebookId', ebookId);
    if (chapterId && chapterId !== 'null' && chapterId !== '') {
        formData.append('chapterId', chapterId);
    }
    
    fetch('${ctx}/favorites', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert(data.message);
            location.reload();
        } else {
            alert('Lỗi: ' + data.error);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Có lỗi xảy ra khi xóa favorite');
    });
}
</script>
</body>
</html> 