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
    <title>AI Smart Recommendations - EbookWebsite</title>
    <link rel="icon" href="${ctx}/favicon.svg" type="image/svg+xml" />
    <link rel="alternate icon" href="${ctx}/favicon.svg" />
    <link rel="stylesheet" href="${ctx}/assets/css/style.css">
    <style>
        .smart-recommendations-container {
            max-width: 1200px;
            margin: 20px auto;
            padding: 20px;
        }
        
        .smart-header {
            text-align: center;
            margin-bottom: 30px;
            padding: 30px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 15px;
            box-shadow: 0 8px 32px rgba(0,0,0,0.1);
        }
        
        .smart-header h1 {
            margin: 0 0 15px 0;
            font-size: 2.5rem;
            font-weight: 700;
        }
        
        .smart-header p {
            margin: 0;
            font-size: 1.2rem;
            opacity: 0.9;
            line-height: 1.6;
        }
        
        .preferences-section {
            background: white;
            border-radius: 12px;
            padding: 25px;
            margin-bottom: 30px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        }
        
        .preferences-section h3 {
            color: var(--primary);
            margin-bottom: 20px;
            font-size: 1.4rem;
            display: flex;
            align-items: center;
            gap: 10px;
        }
        
        .preference-form {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 20px;
            margin-bottom: 20px;
        }
        
        .form-group {
            display: flex;
            flex-direction: column;
        }
        
        .form-group label {
            font-weight: 500;
            margin-bottom: 8px;
            color: #333;
        }
        
        .form-group select,
        .form-group input {
            padding: 12px;
            border: 2px solid #e0e0e0;
            border-radius: 8px;
            font-size: 14px;
            transition: border-color 0.3s;
        }
        
        .form-group select:focus,
        .form-group input:focus {
            outline: none;
            border-color: var(--accent);
        }
        
        .btn-get-recommendations {
            background: linear-gradient(135deg, var(--accent), #5a4fd8);
            color: white;
            padding: 15px 30px;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: transform 0.3s, box-shadow 0.3s;
        }
        
        .btn-get-recommendations:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(106, 90, 224, 0.3);
        }
        
        .recommendations-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
            gap: 25px;
            margin-top: 30px;
        }
        
        .recommendation-card {
            background: white;
            border-radius: 15px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            overflow: hidden;
            transition: transform 0.3s, box-shadow 0.3s;
            position: relative;
        }
        
        .recommendation-card:hover {
            transform: translateY(-8px);
            box-shadow: 0 12px 30px rgba(0,0,0,0.15);
        }
        
        .ai-badge {
            position: absolute;
            top: 15px;
            right: 15px;
            background: linear-gradient(135deg, #ff6b6b, #ee5a52);
            color: white;
            padding: 5px 12px;
            border-radius: 15px;
            font-size: 12px;
            font-weight: 600;
            z-index: 1;
        }
        
        .book-cover {
            width: 100%;
            height: 200px;
            object-fit: cover;
            background: linear-gradient(135deg, #f5f7fa, #c3cfe2);
        }
        
        .book-info {
            padding: 20px;
        }
        
        .book-title {
            font-size: 1.3rem;
            font-weight: 600;
            color: var(--primary);
            margin: 0 0 8px 0;
            line-height: 1.4;
        }
        
        .book-author {
            color: #666;
            font-size: 1rem;
            margin-bottom: 12px;
            font-weight: 500;
        }
        
        .ai-reasoning {
            background: #f8f9ff;
            border-left: 4px solid var(--accent);
            padding: 12px;
            margin: 15px 0;
            border-radius: 0 8px 8px 0;
        }
        
        .ai-reasoning h5 {
            margin: 0 0 8px 0;
            color: var(--accent);
            font-size: 14px;
            font-weight: 600;
        }
        
        .ai-reasoning p {
            margin: 0;
            font-size: 13px;
            color: #555;
            line-height: 1.4;
        }
        
        .book-stats {
            display: flex;
            justify-content: space-between;
            margin: 15px 0;
            font-size: 12px;
            color: #888;
        }
        
        .book-actions {
            display: flex;
            gap: 10px;
            margin-top: 15px;
        }
        
        .btn {
            padding: 10px 16px;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-size: 14px;
            font-weight: 500;
            text-decoration: none;
            display: inline-block;
            text-align: center;
            transition: all 0.3s;
            flex: 1;
        }
        
        .btn-primary {
            background: var(--accent);
            color: white;
        }
        
        .btn-primary:hover {
            background: #5a4fd8;
        }
        
        .btn-outline {
            background: transparent;
            color: var(--accent);
            border: 2px solid var(--accent);
        }
        
        .btn-outline:hover {
            background: var(--accent);
            color: white;
        }
        
        .loading-recommendations {
            text-align: center;
            padding: 60px 20px;
            display: none;
        }
        
        .loading-recommendations .spinner {
            width: 40px;
            height: 40px;
            border: 4px solid #f0f0f0;
            border-top: 4px solid var(--accent);
            border-radius: 50%;
            animation: spin 1s linear infinite;
            margin: 0 auto 20px;
        }
        
        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }
        
        .no-recommendations {
            text-align: center;
            padding: 60px 20px;
            color: #666;
        }
        
        .no-recommendations h3 {
            color: var(--primary);
            margin-bottom: 15px;
        }
        
        .match-score {
            position: absolute;
            top: 15px;
            left: 15px;
            background: rgba(0,0,0,0.7);
            color: white;
            padding: 5px 10px;
            border-radius: 12px;
            font-size: 12px;
            font-weight: 600;
        }
        
        /* AI Recommendations Result Styling */
        .recommendations-result {
            background: white;
            border-radius: 15px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.1);
            overflow: hidden;
            margin-top: 20px;
        }
        
        .result-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 25px;
            text-align: center;
        }
        
        .result-header h3 {
            margin: 0 0 10px 0;
            font-size: 1.5rem;
        }
        
        .result-header p {
            margin: 0;
            opacity: 0.9;
            font-size: 1.1rem;
        }
        
        .ai-recommendations {
            padding: 30px;
        }
        
        .ai-content {
            font-size: 16px;
            line-height: 1.8;
            color: #333;
        }
        
        .ai-content strong {
            color: var(--primary);
            font-weight: 600;
        }
        
        .emoji-star {
            color: #f39c12;
            font-size: 1.2em;
        }
        
        .emoji-target {
            color: #e74c3c;
            font-size: 1.2em;
        }
        
        .emoji-book {
            color: #3498db;
            font-size: 1.2em;
        }
        
        .error-recommendations {
            background: #fee;
            border: 2px solid #fcc;
            border-radius: 12px;
            padding: 30px;
            text-align: center;
            margin-top: 20px;
        }
        
        .error-recommendations h3 {
            color: #e74c3c;
            margin-bottom: 15px;
        }
        
        .error-recommendations p {
            color: #666;
            margin: 0;
        }
    </style>
</head>
<body>
    <%@ include file="/common/header.jsp" %>
    
    <main class="smart-recommendations-container">
        <div class="smart-header">
            <h1>🎯 AI Smart Recommendations</h1>
            <p>Khám phá những cuốn sách được AI tùy chỉnh riêng cho bạn dựa trên sở thích và phong cách đọc</p>
        </div>
        
        <div class="preferences-section">
            <h3>
                <span>⚙️</span>
                Tùy chỉnh sở thích của bạn
            </h3>
            
            <form id="preferencesForm" class="preference-form">
                <div class="form-group">
                    <label for="genre">Thể loại yêu thích:</label>
                    <select id="genre" name="genre">
                        <option value="">Tất cả thể loại</option>
                        <c:forEach var="genreName" items="${availableGenres}">
                            <option value="${genreName}">${genreName}</option>
                        </c:forEach>
                        <!-- Fallback options if no genres from database -->
                        <option value="Tiểu thuyết">Tiểu thuyết</option>
                        <option value="Self-help">Self-help</option>
                        <option value="Khoa học viễn tưởng">Khoa học viễn tưởng</option>
                        <option value="fantasy">Fantasy</option>
                        <option value="romance">Romance</option>
                        <option value="mystery">Mystery</option>
                        <option value="adventure">Adventure</option>
                        <option value="drama">Drama</option>
                        <option value="comedy">Comedy</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="mood">Tâm trạng hiện tại:</label>
                    <select id="mood" name="mood">
                        <option value="">Không xác định</option>
                        <option value="relaxed">Thư giãn</option>
                        <option value="adventurous">Phiêu lưu</option>
                        <option value="emotional">Cảm xúc</option>
                        <option value="intellectual">Trí tuệ</option>
                        <option value="fun">Vui vẻ</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="length">Độ dài sách:</label>
                    <select id="length" name="length">
                        <option value="">Bất kỳ</option>
                        <option value="short">Ngắn (< 200 trang)</option>
                        <option value="medium">Trung bình (200-500 trang)</option>
                        <option value="long">Dài (> 500 trang)</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="complexity">Độ phức tạp:</label>
                    <select id="complexity" name="complexity">
                        <option value="">Bất kỳ</option>
                        <option value="easy">Dễ đọc</option>
                        <option value="moderate">Trung bình</option>
                        <option value="challenging">Thử thách</option>
                    </select>
                </div>
            </form>
            
            <button type="button" class="btn-get-recommendations" onclick="getSmartRecommendations()">
                🚀 Nhận đề xuất thông minh
            </button>
        </div>
        
        <div class="loading-recommendations" id="loadingRecommendations">
            <div class="spinner"></div>
            <h3>AI đang phân tích sở thích của bạn...</h3>
            <p>Vui lòng đợi trong giây lát</p>
        </div>
        
        <div id="recommendationsContainer">
            <div class="no-recommendations">
                <h3>🎯 Sẵn sàng khám phá?</h3>
                <p>Hãy tùy chỉnh sở thích và nhấn "Nhận đề xuất thông minh" để AI tìm những cuốn sách hoàn hảo cho bạn!</p>
            </div>
        </div>
    </main>
    
    <script>
        function getSmartRecommendations() {
            const form = document.getElementById('preferencesForm');
            const formData = new FormData(form);
            const loading = document.getElementById('loadingRecommendations');
            const container = document.getElementById('recommendationsContainer');
            
            // Show loading
            loading.style.display = 'block';
            container.style.display = 'none';
            
            // Prepare data
            const params = new URLSearchParams();
            for (let [key, value] of formData.entries()) {
                if (value) params.append(key, value);
            }
            
            console.log('🔄 Sending Smart Recommendation Request:', params.toString());
            
            // Send request
            fetch('${ctx}/ai/smart-recommendations', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: params.toString()
            })
            .then(response => response.json())
            .then(data => {
                loading.style.display = 'none';
                container.style.display = 'block';
                
                if (data.success) {
                    // Display AI recommendations
                    container.innerHTML = `
                        <div class="recommendations-result">
                            <div class="result-header">
                                <h3>🎯 Đề xuất AI dành cho bạn</h3>
                                <p>Đã tìm thấy <strong>${data.totalFound}</strong> cuốn sách phù hợp với sở thích của bạn</p>
                            </div>
                            <div class="ai-recommendations">
                                <div class="ai-content">
                                    ${formatAIRecommendations(data.recommendations)}
                                </div>
                            </div>
                        </div>
                    `;
                } else {
                    container.innerHTML = `
                        <div class="error-recommendations">
                            <h3>❌ Có lỗi xảy ra</h3>
                            <p>${data.error || 'Vui lòng thử lại sau'}</p>
                        </div>
                    `;
                }
            })
            .catch(error => {
                console.error('Error:', error);
                loading.style.display = 'none';
                container.style.display = 'block';
                container.innerHTML = `
                    <div class="error-recommendations">
                        <h3>❌ Lỗi kết nối</h3>
                        <p>Không thể kết nối đến server. Vui lòng thử lại sau.</p>
                    </div>
                `;
            });
        }
        
        function formatAIRecommendations(text) {
            // Convert newlines to <br> and add some basic formatting
            return text
                .replace(/\n/g, '<br>')
                .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
                .replace(/🌟/g, '<span class="emoji-star">🌟</span>')
                .replace(/🎯/g, '<span class="emoji-target">🎯</span>')
                .replace(/📖/g, '<span class="emoji-book">📖</span>');
        }
    </script>
    
    <%@ include file="/common/footer.jsp" %>
</body>
</html> 