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
    <title>scroll | Đọc truyện online</title>
    <link rel="stylesheet" href="${ctx}/assets/css/style.css" />
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Inter', Arial, sans-serif;
            background: #f7f8fa;
            margin: 0;
            padding: 0;
        }
        .hero {
            background: linear-gradient(120deg, #667eea 0%, #764ba2 100%);
            color: #fff;
            padding: 60px 0 40px 0;
            text-align: center;
            position: relative;
            overflow: hidden;
        }
        .hero h1 {
            font-size: 2.8em;
            font-weight: 700;
            margin-bottom: 18px;
            letter-spacing: -1px;
        }
        .hero .highlight {
            color: #ffd700;
        }
        .hero .hero-search {
            margin: 30px auto 0 auto;
            max-width: 480px;
            display: flex;
            gap: 0;
            background: #fff;
            border-radius: 32px;
            box-shadow: 0 4px 24px rgba(102,126,234,0.08);
            overflow: hidden;
        }
        .hero .hero-search input {
            flex: 1;
            border: none;
            padding: 18px 22px;
            font-size: 1.1em;
            border-radius: 32px 0 0 32px;
            outline: none;
        }
        .hero .hero-search button {
            background: #764ba2;
            color: #fff;
            border: none;
            padding: 0 28px;
            font-size: 1.1em;
            border-radius: 0 32px 32px 0;
            cursor: pointer;
            font-weight: 600;
            transition: background 0.2s;
        }
        .hero .hero-search button:hover {
            background: #667eea;
        }
        .hero .cta-group {
            margin-top: 32px;
            display: flex;
            justify-content: center;
            gap: 18px;
        }
        .hero .cta-btn {
            background: #ffd700;
            color: #333;
            padding: 14px 36px;
            border-radius: 28px;
            font-weight: 700;
            font-size: 1.1em;
            border: none;
            box-shadow: 0 2px 8px rgba(102,126,234,0.10);
            transition: background 0.2s, color 0.2s, transform 0.2s;
            cursor: pointer;
        }
        .hero .cta-btn:hover {
            background: #fff;
            color: #764ba2;
            transform: translateY(-2px) scale(1.04);
        }
        .hero-illustration {
            margin: 40px auto 0 auto;
            max-width: 320px;
        }
        /* Genre section */
        .genres-section {
            margin: 40px auto 0 auto;
            max-width: 900px;
            text-align: center;
        }
        .genres-list {
            display: flex;
            flex-wrap: wrap;
            gap: 14px;
            justify-content: center;
            margin-top: 18px;
        }
        .genre-btn {
            background: #fff;
            color: #764ba2;
            border: 1.5px solid #764ba2;
            border-radius: 20px;
            padding: 8px 22px;
            font-size: 1em;
            font-weight: 600;
            cursor: pointer;
            transition: background 0.2s, color 0.2s, border 0.2s;
        }
        .genre-btn:hover {
            background: #764ba2;
            color: #fff;
            border: 1.5px solid #667eea;
        }
        /* Featured books */
        .featured-section {
            margin: 56px auto 0 auto;
            max-width: 1100px;
        }
        .featured-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 18px;
        }
        .featured-header h2 {
            font-size: 1.6em;
            font-weight: 700;
            color: #333;
        }
        .featured-header a {
            color: #764ba2;
            font-weight: 600;
            text-decoration: none;
            font-size: 1em;
            transition: color 0.2s;
        }
        .featured-header a:hover {
            color: #667eea;
        }
        .featured-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
            gap: 28px;
        }
        .book-card {
            background: #fff;
            border-radius: 18px;
            box-shadow: 0 2px 16px rgba(102,126,234,0.08);
            overflow: hidden;
            transition: transform 0.18s, box-shadow 0.18s;
            display: flex;
            flex-direction: column;
            min-height: 340px;
        }
        .book-card:hover {
            transform: translateY(-6px) scale(1.03);
            box-shadow: 0 8px 32px rgba(102,126,234,0.16);
        }
        .book-thumb {
            width: 100%;
            height: 180px;
            object-fit: cover;
            background: #f3f3f3;
        }
        .book-content {
            padding: 18px 18px 12px 18px;
            flex: 1;
            display: flex;
            flex-direction: column;
        }
        .book-title {
            font-size: 1.15em;
            font-weight: 700;
            color: #333;
            margin-bottom: 8px;
            text-decoration: none;
        }
        .book-title:hover {
            color: #764ba2;
        }
        .book-desc {
            font-size: 0.98em;
            color: #666;
            margin-bottom: 10px;
            flex: 1;
        }
        .book-info {
            font-size: 0.92em;
            color: #999;
            margin-bottom: 8px;
        }
        .read-btn {
            background: #667eea;
            color: #fff;
            border: none;
            border-radius: 16px;
            padding: 8px 18px;
            font-weight: 600;
            font-size: 1em;
            align-self: flex-start;
            margin-top: 6px;
            transition: background 0.2s, color 0.2s;
            text-decoration: none;
        }
        .read-btn:hover {
            background: #ffd700;
            color: #764ba2;
        }
        /* Features section */
        .features-section {
            margin: 60px auto 0 auto;
            max-width: 900px;
            text-align: center;
        }
        .features-list {
            display: flex;
            flex-wrap: wrap;
            gap: 32px;
            justify-content: center;
            margin-top: 24px;
        }
        .feature-item {
            background: #fff;
            border-radius: 16px;
            box-shadow: 0 2px 12px rgba(102,126,234,0.07);
            padding: 28px 32px;
            min-width: 180px;
            max-width: 240px;
            flex: 1 1 180px;
            display: flex;
            flex-direction: column;
            align-items: center;
        }
        .feature-item .feature-icon {
            font-size: 2.2em;
            margin-bottom: 12px;
        }
        .feature-item .feature-title {
            font-weight: 700;
            color: #764ba2;
            margin-bottom: 8px;
        }
        .feature-item .feature-desc {
            color: #666;
            font-size: 0.98em;
        }
        @media (max-width: 900px) {
            .featured-section, .features-section, .genres-section {
                max-width: 98vw;
            }
        }
        @media (max-width: 600px) {
            .hero h1 { font-size: 2em; }
            .hero .hero-search input { font-size: 1em; }
            .hero .cta-btn { font-size: 1em; padding: 12px 18px; }
            .featured-header h2 { font-size: 1.1em; }
            .feature-item { padding: 18px 8px; }
        }
    </style>
</head>
<body>
    <a href="#main" class="skip-link">Bỏ qua và tới nội dung chính</a>
    <%@ include file="/common/header.jsp" %>
    <main id="main">
        <!-- HERO SECTION -->
        <section class="hero" id="home">
            <h1>
                Khám phá kho truyện <span class="highlight">khổng lồ</span> &amp;
                <span class="highlight">chất lượng</span>
            </h1>
            <div class="cta-group">
                <a href="${ctx}/book-list" class="cta-btn">Bắt đầu đọc ngay</a>
                <c:if test="${not empty sessionScope.user}">
                    <a href="${ctx}/favorites" class="cta-btn" style="background:#fff;color:#764ba2;">Truyện yêu thích</a>
                </c:if>
            </div>
            <div class="hero-illustration">
                <!-- SVG minh họa sách -->
                <svg width="100%" height="120" viewBox="0 0 320 120" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <rect x="20" y="30" width="80" height="60" rx="12" fill="#fff" fill-opacity="0.9"/>
                  <rect x="120" y="20" width="80" height="80" rx="16" fill="#ffd700" fill-opacity="0.8"/>
                  <rect x="220" y="40" width="80" height="60" rx="12" fill="#fff" fill-opacity="0.9"/>
                  <rect x="40" y="50" width="40" height="8" rx="4" fill="#764ba2" fill-opacity="0.2"/>
                  <rect x="140" y="60" width="60" height="8" rx="4" fill="#764ba2" fill-opacity="0.2"/>
                  <rect x="240" y="70" width="40" height="8" rx="4" fill="#764ba2" fill-opacity="0.2"/>
                </svg>
            </div>
        </section>
        <!-- FEATURED BOOKS SECTION -->
        <section class="featured-section">
            <div class="featured-header">
                <h2>Truyện nổi bật</h2>
                <a href="${ctx}/book-list">Xem tất cả &rarr;</a>
            </div>
            <div class="featured-grid">
                <c:forEach var="book" items="${featuredBooks}" varStatus="loop" begin="0" end="3">
                    <div class="book-card">
                        <img class="book-thumb" src="${book.coverUrl}" alt="${book.title}" />
                        <div class="book-content">
                            <a class="book-title" href="${pageContext.request.contextPath}/book/detail?id=${book.id}">${book.title}</a>
                            <div class="book-desc">
                                <c:choose>
                                    <c:when test="${not empty book.description}">
                                        <c:out value="${book.description.length() > 80 ? book.description.substring(0, 80).concat('...') : book.description}" />
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-muted">Không có mô tả</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div class="book-info">Lượt xem: ${book.viewCount}</div>
                            <a class="read-btn" href="${pageContext.request.contextPath}/book/detail?id=${book.id}">Đọc ngay</a>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </section>
        <!-- FEATURES SECTION -->
        <section class="features-section">
            <h2 style="font-size:1.2em;font-weight:700;color:#333;">Tại sao chọn Scroll?</h2>
            <div class="features-list">
                <div class="feature-item">
                    <div class="feature-icon">📚</div>
                    <div class="feature-title">Kho truyện khổng lồ – phần lớn hoàn toàn miễn phí</div>
                    <div class="feature-desc">Từ truyện hot đến kinh điển, Scroll mở ra thế giới truyện hấp dẫn cho mọi độc giả. Đọc thoải mái với hàng ngàn đầu truyện miễn phí, có chọn lọc.</div>
                </div>
                <div class="feature-item">
                    <div class="feature-icon">✨</div>
                    <div class="feature-title">Trải nghiệm mượt mà, giao diện hiện đại</div>
                    <div class="feature-desc">Giao diện tối giản, dễ dùng, được tối ưu cho mọi thiết bị – từ máy tính đến smartphone, tablet.</div>
                </div>
                <div class="feature-item">
                    <div class="feature-icon">🎯</div>
                    <div class="feature-title">Tập trung vào trải nghiệm đọc</div>
                    <div class="feature-desc">Không quảng cáo chen ngang, không rối mắt – chỉ có bạn và những câu chuyện cuốn hút. Scroll tạo không gian đọc yên tĩnh, giúp bạn đắm chìm trọn vẹn trong từng trang truyện.</div>
                </div>
                <div class="feature-item">
                    <div class="feature-icon">🌐</div>
                    <div class="feature-title">Truy cập mọi lúc, mọi nơi</div>
                    <div class="feature-desc">Dù ở nhà, trên xe hay quán cà phê, bạn luôn có thể tiếp tục hành trình đọc với Scroll – chỉ cần một thiết bị kết nối mạng.</div>
                </div>
            </div>
        </section>
    </main>
    <%@ include file="/common/footer.jsp" %>
</body>
</html>
