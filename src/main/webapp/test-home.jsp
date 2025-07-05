<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Test Home - EbookWebsite</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
</head>
<body>
    <h1>Test Home Page</h1>
    
    <h2>Session Info:</h2>
    <p>User ID: ${sessionScope.userId}</p>
    <p>Username: ${sessionScope.username}</p>
    
    <h2>Request Attributes:</h2>
    <p>Top Books: ${topBooks != null ? topBooks.size() : 'null'}</p>
    <p>New Books: ${newBooks != null ? newBooks.size() : 'null'}</p>
    <p>Free Books: ${freeBooks != null ? freeBooks.size() : 'null'}</p>
    <p>Categories: ${categories != null ? categories.size() : 'null'}</p>
    
    <h2>Test Links:</h2>
    <ul>
        <li><a href="/">Home (/)</a></li>
        <li><a href="/home">Home (/home)</a></li>
        <li><a href="/test">Test Database (/test)</a></li>
        <li><a href="index.jsp">Direct JSP (index.jsp)</a></li>
    </ul>
    
    <h2>If you see this, JSP is working!</h2>
</body>
</html> 