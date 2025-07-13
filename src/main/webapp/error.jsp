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
    <title>Lỗi | EbookWebsite</title>
    
    <!-- Core CSS -->
    <link rel="stylesheet" href="${ctx}/assets/css/style.css" />
    
    <style>
        .error-container {
            max-width: 600px;
            margin: 100px auto;
            text-align: center;
            padding: 40px 20px;
        }
        
        .error-icon {
            font-size: 4em;
            margin-bottom: 20px;
            opacity: 0.7;
        }
        
        .error-title {
            font-size: 2em;
            color: #e74c3c;
            margin-bottom: 20px;
        }
        
        .error-message {
            color: #666;
            font-size: 1.1em;
            line-height: 1.6;
            margin-bottom: 30px;
        }
        
        .back-btn {
            display: inline-block;
            padding: 12px 30px;
            background: linear-gradient(45deg, #667eea, #764ba2);
            color: white;
            text-decoration: none;
            border-radius: 25px;
            font-weight: 600;
            transition: all 0.3s ease;
        }
        
        .back-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }
    </style>
</head>
<body>
    <div class="error-container">
        <div class="error-icon">⚠️</div>
        <h1 class="error-title">Đã xảy ra lỗi</h1>
        <p class="error-message">
            ${not empty error ? error : 'Có lỗi xảy ra trong quá trình xử lý yêu cầu của bạn.'}
        </p>
        <a href="${ctx}/" class="back-btn">← Quay về trang chủ</a>
    </div>
</body>
</html> 