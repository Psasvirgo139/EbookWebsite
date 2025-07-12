<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>🎯 AI Recommendations - EbookWebsite</title>
    <link rel="stylesheet" href="${ctx}/assets/css/style.css" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <style>
        .recommendations-container { 
            max-width: 1200px; 
            margin: 40px auto; 
            padding: 20px; 
        }
        .result-container {
            background: rgba(255,255,255,0.95);
            color: #333;
            padding: 20px;
            border-radius: 12px;
            margin-top: 20px;
            white-space: pre-wrap;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            line-height: 1.6;
        }
        .loading {
            text-align: center;
            padding: 40px;
            color: #333;
        }
        .spinner {
            border: 4px solid rgba(0,0,0,0.1);
            border-top: 4px solid #007bff;
            border-radius: 50%;
            width: 40px;
            height: 40px;
            animation: spin 1s linear infinite;
            margin: 0 auto 20px;
        }
        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }
        .book-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
            gap: 20px;
            margin-top: 20px;
        }
        .book-card {
            background: #f8f9fa;
            padding: 15px;
            border-radius: 8px;
            transition: transform 0.2s ease;
            border: 1px solid #dee2e6;
        }
        .book-card:hover {
            transform: scale(1.02);
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body>
<%@ include file="/common/header.jsp" %>

<main>
    <div class="recommendations-container">
        <!-- Results Container -->
        <div id="resultsContainer" style="display: none;">
            <div class="result-container">
                <h4>📋 Kết quả đề xuất</h4>
                <div id="recommendationResults"></div>
            </div>
        </div>

        <!-- Loading Indicator -->
        <div id="loadingIndicator" class="loading" style="display: none;">
            <div class="spinner"></div>
            <p>🤖 AI đang phân tích và tạo đề xuất cho bạn...</p>
        </div>

        <!-- Latest Books Section -->
        <c:if test="${not empty latestBooks}">
            <div class="mt-5">
                <div class="book-grid">
                    <c:forEach var="book" items="${latestBooks}">
                        <div class="book-card">
                            <h5>📖 ${book.title}</h5>
                            <p class="mb-2"><strong>Thể loại:</strong> ${book.releaseType}</p>
                            <p class="mb-2"><strong>Lượt xem:</strong> ${book.viewCount}</p>
                            <c:if test="${not empty book.description}">
                                <p class="mb-2"><strong>Mô tả:</strong> ${book.description}</p>
                            </c:if>
                            <a href="${ctx}/book/detail?id=${book.id}" class="btn btn-sm btn-primary">
                                Xem chi tiết
                            </a>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </c:if>
    </div>
</main>

<%@ include file="/common/footer.jsp" %>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
function showLoading() {
    document.getElementById('loadingIndicator').style.display = 'block';
    document.getElementById('resultsContainer').style.display = 'none';
}

function hideLoading() {
    document.getElementById('loadingIndicator').style.display = 'none';
}

function showResults(content) {
    document.getElementById('recommendationResults').innerHTML = content;
    document.getElementById('resultsContainer').style.display = 'block';
}
</script>
</body>
</html> 