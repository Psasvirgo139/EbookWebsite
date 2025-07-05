<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Book Comments - EbookWebsite</title>
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
        .comments-main {
            max-width: 900px;
            margin: 2.5rem auto 0 auto;
            padding: 0 1rem;
        }
        .book-info {
            background: #fff;
            border-radius: 18px;
            box-shadow: 0 8px 32px rgba(124,131,253,0.10);
            padding: 1.5rem 1.2rem;
            margin-bottom: 2rem;
            display: flex;
            flex-direction: column;
            align-items: flex-start;
            gap: 0.5rem;
        }
        .book-title {
            font-size: 1.3rem;
            font-weight: 700;
            color: #4f8cff;
        }
        .book-author {
            font-size: 1rem;
            color: #6a7ba2;
        }
        <%-- <div class="book-author">By ${book.authorName}</div> --%>
        .book-tags {
            display: flex;
            flex-wrap: wrap;
            gap: 0.5rem;
        }
        .book-tag {
            background: #e0f7fa;
            color: #4f8cff;
            border-radius: 8px;
            padding: 0.2rem 0.7rem;
            font-size: 0.93rem;
            font-weight: 600;
        }
        .comments-list {
            display: flex;
            flex-direction: column;
            gap: 1.2rem;
            margin-bottom: 2rem;
        }
        .comment-card {
            background: #fff;
            border-radius: 14px;
            box-shadow: 0 2px 12px #4f8cff11;
            padding: 1.1rem 1.2rem;
            display: flex;
            flex-direction: column;
            gap: 0.4rem;
        }
        .comment-header {
            display: flex;
            align-items: center;
            gap: 0.7rem;
        }
        .comment-user {
            font-weight: 600;
            color: #4f8cff;
        }
        .comment-date {
            font-size: 0.95rem;
            color: #6a7ba2;
        }
        .comment-content {
            font-size: 1.05rem;
            color: #222;
            margin-top: 0.2rem;
        }
        .comment-actions {
            margin-top: 0.5rem;
        }
        .comment-action-btn {
            background: none;
            border: none;
            color: #ff6b81;
            font-size: 1rem;
            cursor: pointer;
            margin-right: 0.7rem;
            transition: color 0.15s;
        }
        .comment-action-btn:hover {
            color: #c0392b;
        }
        .add-comment-form {
            background: #fff;
            border-radius: 14px;
            box-shadow: 0 2px 12px #4f8cff11;
            padding: 1.1rem 1.2rem;
            display: flex;
            flex-direction: column;
            gap: 0.7rem;
        }
        .add-comment-title {
            font-size: 1.1rem;
            font-weight: 700;
            color: #4f8cff;
        }
        .add-comment-input {
            width: 100%;
            border: 1.5px solid #e0e7ff;
            border-radius: 10px;
            font-size: 1rem;
            background: #f8faff;
            outline: none;
            padding: 0.8rem 1rem;
            transition: border 0.18s, box-shadow 0.18s;
            box-shadow: 0 1px 4px #e0e7ff33;
        }
        .add-comment-input:focus {
            border-color: #6ee7b7;
            box-shadow: 0 2px 8px #6ee7b733;
        }
        .add-comment-btn {
            background: linear-gradient(120deg, #6ee7b7 0%, #4f8cff 100%);
            color: #fff;
            font-weight: 700;
            font-size: 1.01rem;
            border: none;
            border-radius: 10px;
            padding: 0.7rem 1.5rem;
            box-shadow: 0 2px 12px #4f8cff22;
            transition: background 0.18s, box-shadow 0.18s, transform 0.12s;
            cursor: pointer;
            text-decoration: none;
            align-self: flex-end;
        }
        .add-comment-btn:hover {
            background: linear-gradient(120deg, #4f8cff 0%, #6ee7b7 100%);
            box-shadow: 0 4px 24px #4f8cff33;
            transform: translateY(-2px) scale(1.01);
        }
        @media (max-width: 600px) {
            .header { flex-direction: column; gap: 0.7rem; padding: 1rem; }
            .book-info { padding: 1rem 0.5rem; }
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
<div class="comments-main">
    <div class="book-info">
        <div class="book-title">${book.title}</div>
        <%-- <div class="book-author">By ${book.authorName}</div> --%>
        <div class="book-tags">
            <c:forEach var="tag" items="${book.tags}">
                <span class="book-tag">${tag.name}</span>
            </c:forEach>
        </div>
    </div>
    <div class="comments-list">
        <c:forEach var="comment" items="${comments}">
            <div class="comment-card">
                <div class="comment-header">
                    <span class="comment-user"><i class="fas fa-user"></i> ${comment.username}</span>
                    <span class="comment-date">${comment.createdAt}</span>
                </div>
                <div class="comment-content">${comment.content}</div>
                <c:if test="${comment.canDelete}">
                    <div class="comment-actions">
                        <form method="post" action="../DeleteCommentServlet" style="display:inline;">
                            <input type="hidden" name="commentId" value="${comment.id}">
                            <input type="hidden" name="bookId" value="${book.id}">
                            <button type="submit" class="comment-action-btn"><i class="fas fa-trash"></i> Delete</button>
                        </form>
                    </div>
                </c:if>
            </div>
        </c:forEach>
    </div>
    <c:if test="${not empty sessionScope.userId}">
        <form class="add-comment-form" method="post" action="../CommentAddServlet">
            <div class="add-comment-title">Add a Comment</div>
            <input type="hidden" name="bookId" value="${book.id}">
            <textarea class="add-comment-input" name="content" rows="3" maxlength="500" placeholder="Write your comment..." required></textarea>
            <button type="submit" class="add-comment-btn"><i class="fas fa-paper-plane"></i> Post</button>
        </form>
    </c:if>
    <c:if test="${empty sessionScope.userId}">
        <div style="text-align:center; color:#6a7ba2; margin-top:1.5rem;">Please <a href="../login.jsp" style="color:#4f8cff; font-weight:600;">login</a> to comment.</div>
    </c:if>
</div>
</body>
</html> 