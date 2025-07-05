<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Read Book - EbookWebsite</title>
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
        .read-main {
            max-width: 900px;
            margin: 2.5rem auto 0 auto;
            padding: 0 1rem;
        }
        .read-title {
            font-size: 2rem;
            font-weight: 700;
            color: #4f8cff;
            margin-bottom: 1.2rem;
            text-align: center;
        }
        .chapter-nav {
            display: flex;
            justify-content: center;
            gap: 1.2rem;
            margin-bottom: 1.5rem;
        }
        .chapter-btn {
            background: #f8faff;
            color: #4f8cff;
            font-weight: 600;
            border: none;
            border-radius: 10px;
            padding: 0.6rem 1.3rem;
            box-shadow: 0 1px 6px #4f8cff11;
            transition: background 0.15s, color 0.15s;
            cursor: pointer;
            text-decoration: none;
        }
        .chapter-btn:hover {
            background: #e0f7fa;
            color: #222;
        }
        .read-card {
            background: #fff;
            border-radius: 18px;
            box-shadow: 0 8px 32px rgba(124,131,253,0.10);
            padding: 2rem 1.5rem;
            font-size: 1.13rem;
            color: #222;
            line-height: 1.7;
            min-height: 320px;
        }
        @media (max-width: 600px) {
            .header { flex-direction: column; gap: 0.7rem; padding: 1rem; }
            .read-title { font-size: 1.3rem; }
            .read-card { padding: 1rem 0.5rem; font-size: 1rem; }
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
<div class="read-main">
    <div class="read-title">${book.title} - ${chapter.title}</div>
    <div class="chapter-nav">
        <c:if test="${not empty prevChapterId}">
            <a class="chapter-btn" href="read.jsp?bookId=${book.id}&chapterId=${prevChapterId}"><i class="fas fa-arrow-left"></i> Previous</a>
        </c:if>
        <c:if test="${not empty nextChapterId}">
            <a class="chapter-btn" href="read.jsp?bookId=${book.id}&chapterId=${nextChapterId}">Next <i class="fas fa-arrow-right"></i></a>
        </c:if>
    </div>
    <div class="read-card">
        ${chapter.content}
    </div>
</div>
</body>
</html>