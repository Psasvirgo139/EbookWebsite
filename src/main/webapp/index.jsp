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
            grid-template-columns: repeat(auto-fit, minmax(140px, 1fr));
            gap: 16px;
        }
        .book-card {
            background: #fff;
            border-radius: 16px;
            box-shadow: 0 2px 12px rgba(102,126,234,0.08);
            overflow: hidden;
            transition: transform 0.18s, box-shadow 0.18s;
            display: flex;
            flex-direction: column;
            min-height: 280px;
            cursor: pointer;
            text-decoration: none;
            color: inherit;
            height: 100%;
        }
        .book-card:hover {
            transform: translateY(-6px) scale(1.03);
            box-shadow: 0 8px 32px rgba(102,126,234,0.16);
        }
        .book-thumb {
            width: 100%;
            height: 220px;
            object-fit: cover;
            background: #f3f3f3;
        }
        .book-content {
            padding: 10px 10px 8px 10px;
            flex: 1;
            display: flex;
            flex-direction: column;
        }
        .book-bottom {
            margin-top: auto;
            display: flex;
            flex-direction: column;
        }
        .book-stats {
            display: flex;
            flex-direction: row;
            justify-content: space-between;
            align-items: center;
        }
        .book-fav {
            display: flex;
            align-items: center;
            gap: 2px;
            color: #e25555;
            font-weight: 600;
            line-height: 1;
            padding-right: 6px;
        }
        .book-title {
            font-size: 1.05em;
            font-weight: 700;
            color: #333;
            margin-bottom: 6px;
            text-decoration: none;
        }
        .book-title:hover {
            color: #764ba2;
        }
        .book-desc {
            font-size: 0.9em;
            color: #666;
            margin-bottom: 8px;
            flex: 1;
        }
        .book-info {
            font-size: 0.85em;
            color: #999;
            margin-bottom: 6px;
            display: flex;
            align-items: center;
            line-height: 1;
            padding-left: 6px;
            padding-top: 6px;
        }
        .read-btn {
            background: #667eea;
            color: #fff;
            border: none;
            border-radius: 14px;
            padding: 6px 14px;
            font-weight: 600;
            font-size: 0.9em;
            align-self: center;
            margin-top: 8px;
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
        <!-- LAST UPDATED BOOKS SECTION -->
        <section class="featured-section">
            <div class="featured-header">
                <h2>Mới cập nhật</h2>
            </div>
            <div class="featured-grid">
                <c:forEach var="book" items="${lastUpdateList}" varStatus="loop" begin="0" end="5">
                    <a class="book-card" href="${pageContext.request.contextPath}/book/detail?id=${book.id}">
                        <img class="book-thumb" src="${book.coverUrl}" alt="${book.title}" />
                        <div class="book-content">
                            <div class="book-title">${book.title}</div>
                            <div class="book-bottom">
                                <div class="book-stats">
                                    <div class="book-info">
                                       <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 576 512" width="18" height="18" style="vertical-align:middle;margin-right:3px;height:18px;">
                                         <path d="M288 32c-80.8 0-145.5 36.8-192.6 80.6C48.6 156 17.3 208 2.5 243.7c-3.3 7.9-3.3 16.7 0 24.6C17.3 304 48.6 356 95.4 399.4C142.5 443.2 207.2 480 288 480s145.5-36.8 192.6-80.6c46.8-43.5 78.1-95.4 93-131.1c3.3-7.9 3.3-16.7 0-24.6c-14.9-35.7-46.2-87.7-93-131.1C433.5 68.8 368.8 32 288 32zM144 256a144 144 0 1 1 288 0 144 144 0 1 1 -288 0zm144-64c0 35.3-28.7 64-64 64c-7.1 0-13.9-1.2-20.3-3.3c-5.5-1.8-11.9 1.6-11.7 7.4c.3 6.9 1.3 13.8 3.2 20.7c13.7 51.2 66.4 81.6 117.6 67.9s81.6-66.4 67.9-117.6c-11.1-41.5-47.8-69.4-88.6-71.1c-5.8-.2-9.2 6.1-7.4 11.7c2.1 6.4 3.3 13.2 3.3 20.3z"/>
                                       </svg>
                                       ${book.viewCount}
                                    </div>
                                    <div class="book-fav">
                                       <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="16" height="16" fill="#e25555" style="vertical-align:middle;margin-right:2px;height:16px;"><path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41 0.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/></svg>
                                       <c:out value="${lastUpdateFavoriteMap[book.id] != null ? lastUpdateFavoriteMap[book.id] : 0}"/>
                                    </div>
                                </div>
                                <div class="read-btn">Đọc ngay</div>
                            </div>
                        </div>
                    </a>
                </c:forEach>
            </div>
        </section>
        <!-- FEATURED BOOKS SECTION -->
        <section class="featured-section">
            <div class="featured-header">
                <h2>Truyện nổi bật</h2>
                <a href="${ctx}/search?keyword=&genre=&author=&minChapters=0&sortBy=view_total&status=all">Xem tất cả &rarr;</a>
            </div>
            <div class="featured-grid">
                <c:forEach var="book" items="${featuredBooks}" varStatus="loop" begin="0" end="5">
                    <a class="book-card" href="${pageContext.request.contextPath}/book/detail?id=${book.id}">
                        <img class="book-thumb" src="${book.coverUrl}" alt="${book.title}" />
                        <div class="book-content">
                            <div class="book-title">${book.title}</div>
                            <div class="book-bottom">
                                <div class="book-stats">
                                    <div class="book-info">
                                       <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 576 512" width="18" height="18" style="vertical-align:middle;margin-right:3px;height:18px;">
                                         <path d="M288 32c-80.8 0-145.5 36.8-192.6 80.6C48.6 156 17.3 208 2.5 243.7c-3.3 7.9-3.3 16.7 0 24.6C17.3 304 48.6 356 95.4 399.4C142.5 443.2 207.2 480 288 480s145.5-36.8 192.6-80.6c46.8-43.5 78.1-95.4 93-131.1c3.3-7.9 3.3-16.7 0-24.6c-14.9-35.7-46.2-87.7-93-131.1C433.5 68.8 368.8 32 288 32zM144 256a144 144 0 1 1 288 0 144 144 0 1 1 -288 0zm144-64c0 35.3-28.7 64-64 64c-7.1 0-13.9-1.2-20.3-3.3c-5.5-1.8-11.9 1.6-11.7 7.4c.3 6.9 1.3 13.8 3.2 20.7c13.7 51.2 66.4 81.6 117.6 67.9s81.6-66.4 67.9-117.6c-11.1-41.5-47.8-69.4-88.6-71.1c-5.8-.2-9.2 6.1-7.4 11.7c2.1 6.4 3.3 13.2 3.3 20.3z"/>
                                       </svg>
                                       ${book.viewCount}
                                    </div>
                                    <div class="book-fav">
                                       <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="16" height="16" fill="#e25555" style="vertical-align:middle;margin-right:2px;height:16px;"><path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41 0.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/></svg>
                                       <c:out value="${favoriteMap[book.id] != null ? favoriteMap[book.id] : 0}"/>
                                    </div>
                                </div>
                                <div class="read-btn">Đọc ngay</div>
                            </div>
                        </div>
                    </a>
                </c:forEach>
            </div>
        </section>
        <!-- LATEST BOOKS SECTION -->
        <section class="featured-section">
            <div class="featured-header">
                <h2>Truyện mới</h2>
                <a href="${ctx}/book-list?search=&genre=&sortBy=newest">Xem tất cả &rarr;</a>
            </div>
            <div class="featured-grid">
                <c:forEach var="book" items="${latestBooks}" varStatus="loop" begin="0" end="5">
                    <a class="book-card" href="${pageContext.request.contextPath}/book/detail?id=${book.id}">
                        <img class="book-thumb" src="${book.coverUrl}" alt="${book.title}" />
                        <div class="book-content">
                            <div class="book-title">${book.title}</div>
                            <div class="book-bottom">
                                <div class="book-stats">
                                    <div class="book-info">
                                       <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 576 512" width="18" height="18" style="vertical-align:middle;margin-right:3px;height:18px;">
                                         <path d="M288 32c-80.8 0-145.5 36.8-192.6 80.6C48.6 156 17.3 208 2.5 243.7c-3.3 7.9-3.3 16.7 0 24.6C17.3 304 48.6 356 95.4 399.4C142.5 443.2 207.2 480 288 480s145.5-36.8 192.6-80.6c46.8-43.5 78.1-95.4 93-131.1c3.3-7.9 3.3-16.7 0-24.6c-14.9-35.7-46.2-87.7-93-131.1C433.5 68.8 368.8 32 288 32zM144 256a144 144 0 1 1 288 0 144 144 0 1 1 -288 0zm144-64c0 35.3-28.7 64-64 64c-7.1 0-13.9-1.2-20.3-3.3c-5.5-1.8-11.9 1.6-11.7 7.4c.3 6.9 1.3 13.8 3.2 20.7c13.7 51.2 66.4 81.6 117.6 67.9s81.6-66.4 67.9-117.6c-11.1-41.5-47.8-69.4-88.6-71.1c-5.8-.2-9.2 6.1-7.4 11.7c2.1 6.4 3.3 13.2 3.3 20.3z"/>
                                       </svg>
                                       ${book.viewCount}
                                    </div>
                                    <div class="book-fav">
                                       <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="16" height="16" fill="#e25555" style="vertical-align:middle;margin-right:2px;height:16px;"><path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41 0.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/></svg>
                                       <c:out value="${latestFavoriteMap[book.id] != null ? latestFavoriteMap[book.id] : 0}"/>
                                    </div>
                                </div>
                                <div class="read-btn">Đọc ngay</div>
                            </div>
                        </div>
                    </a>
                </c:forEach>
            </div>
        </section>
    </main>
    <%@ include file="/common/footer.jsp" %>
</body>
</html>
