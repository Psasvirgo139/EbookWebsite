<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Book List - EbookWebsite</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&family=Inter:wght@400;600&family=Roboto:wght@400;500&display=swap" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(120deg, #e0f7fa 0%, #f3e7ff 100%);
            font-family: 'Poppins', 'Inter', 'Roboto', Arial, sans-serif;
            margin: 0;
            min-height: 100vh;
            color: #222;
        }
        .header {
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 1.2rem 2rem 1.2rem 2rem;
            background: #fff;
            box-shadow: 0 2px 12px #4f8cff11;
        }
        .logo {
            display: flex;
            align-items: center;
            font-size: 1.3rem;
            font-weight: 700;
            color: #4f8cff;
        }
        .logo i {
            font-size: 2rem;
            color: #6ee7b7;
            margin-right: 0.5rem;
        }
        .nav {
            display: flex;
            gap: 1.2rem;
        }
        .nav a {
            color: #4f8cff;
            font-weight: 600;
            text-decoration: none;
            font-size: 1.05rem;
            border-radius: 10px;
            padding: 0.5rem 0.8rem;
            transition: background 0.15s;
        }
        .nav a:hover {
            background: #e0f7fa;
        }
        .book-list-main {
            max-width: 1200px;
            margin: 2.5rem auto 0 auto;
            padding: 0 1rem;
        }
        .book-list-title {
            font-size: 2rem;
            font-weight: 700;
            color: #4f8cff;
            margin-bottom: 2rem;
            text-align: center;
        }
        .book-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
            gap: 2rem;
        }
        .book-card {
            background: #fff;
            border-radius: 18px;
            box-shadow: 0 8px 32px rgba(124,131,253,0.10);
            padding: 1.5rem 1.2rem 1.2rem 1.2rem;
            display: flex;
            flex-direction: column;
            align-items: center;
            transition: box-shadow 0.18s, transform 0.12s;
        }
        .book-card:hover {
            box-shadow: 0 12px 36px #4f8cff22;
            transform: translateY(-2px) scale(1.01);
        }
        .book-cover {
            width: 120px;
            height: 180px;
            background: #e0f7fa;
            border-radius: 12px;
            object-fit: cover;
            margin-bottom: 1.1rem;
            box-shadow: 0 2px 12px #4f8cff11;
        }
        .book-title {
            font-size: 1.15rem;
            font-weight: 700;
            color: #4f8cff;
            margin-bottom: 0.3rem;
            text-align: center;
        }
        .book-author {
            font-size: 1rem;
            color: #6a7ba2;
            margin-bottom: 0.5rem;
            text-align: center;
        }
        <%-- <div class="book-author">By ${book.authorName}</div> --%>
        .book-tags {
            display: flex;
            flex-wrap: wrap;
            gap: 0.5rem;
            margin-bottom: 0.7rem;
            justify-content: center;
        }
        .book-tag {
            background: #e0f7fa;
            color: #4f8cff;
            border-radius: 8px;
            padding: 0.2rem 0.7rem;
            font-size: 0.93rem;
            font-weight: 600;
        }
        .book-desc {
            font-size: 0.99rem;
            color: #222;
            margin-bottom: 1.1rem;
            text-align: center;
        }
        .book-actions {
            display: flex;
            gap: 0.7rem;
            margin-top: auto;
        }
        .book-action-btn {
            background: linear-gradient(120deg, #6ee7b7 0%, #4f8cff 100%);
            color: #fff;
            font-weight: 700;
            font-size: 1.01rem;
            border: none;
            border-radius: 12px;
            padding: 0.6rem 1.3rem;
            box-shadow: 0 2px 12px #4f8cff22;
            transition: background 0.18s, box-shadow 0.18s, transform 0.12s;
            cursor: pointer;
            text-decoration: none;
        }
        .book-action-btn:hover {
            background: linear-gradient(120deg, #4f8cff 0%, #6ee7b7 100%);
            box-shadow: 0 4px 24px #4f8cff33;
            transform: translateY(-2px) scale(1.01);
        }
        @media (max-width: 600px) {
            .header { flex-direction: column; gap: 0.7rem; padding: 1rem; }
            .book-list-title { font-size: 1.3rem; }
            .book-grid { gap: 1rem; }
        }
    </style>
</head>
<body>
<div class="header">
    <div class="logo"><i class="fas fa-book-open"></i> EbookWebsite</div>
    <div class="nav">
        <a href="../index.jsp">Home</a>
        <a href="list.jsp">Books</a>
        <a href="../login.jsp">Login</a>
    </div>
</div>
<div class="book-list-main">
    <div class="book-list-title">All Books</div>
    <c:choose>
        <c:when test="${not empty books}">
            <div class="book-grid">
                <c:forEach var="book" items="${books}">
                    <div class="book-card">
                        <img class="book-cover" src="${book.coverUrl != null ? book.coverUrl : 'https://via.placeholder.com/120x180/4f8cff/fff?text=Book'}" alt="Book Cover">
                        <div class="book-title">${book.title}</div>
                        <%-- <div class="book-author">By ${book.authorName}</div> --%>
                        <div class="book-tags">
                            <c:forEach var="tag" items="${book.tags}">
                                <span class="book-tag">${tag.name}</span>
                            </c:forEach>
                        </div>
                        <div class="book-desc">${book.description}</div>
                        <div class="book-actions">
                            <a class="book-action-btn" href="detail.jsp?bookId=${book.id}"><i class="fas fa-info-circle"></i> Detail</a>
                            <a class="book-action-btn" href="read.jsp?bookId=${book.id}"><i class="fas fa-book-reader"></i> Read</a>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <div style="text-align:center;color:#6a7ba2;font-size:1.2rem;margin-top:2rem;">No books found.</div>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>