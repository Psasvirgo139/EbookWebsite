<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Error - EbookWebsite</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&family=Inter:wght@400;600&family=Roboto:wght@400;500&display=swap" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(120deg, #f3e7ff 0%, #e0f7fa 100%);
            font-family: 'Poppins', 'Inter', 'Roboto', Arial, sans-serif;
            margin: 0;
            min-height: 100vh;
            color: #222;
        }
        .error-main {
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .error-card {
            background: #fff;
            border-radius: 24px;
            box-shadow: 0 8px 32px rgba(124,131,253,0.10);
            max-width: 400px;
            width: 100%;
            padding: 2.5rem 2rem 2rem 2rem;
            animation: fadeIn 0.8s cubic-bezier(.4,0,.2,1);
            text-align: center;
        }
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(30px);}
            to { opacity: 1; transform: translateY(0);}
        }
        .error-logo {
            text-align: center;
            margin-bottom: 1.2rem;
        }
        .error-logo i {
            font-size: 2.1rem;
            color: #6ee7b7;
            margin-right: 0.4rem;
        }
        .error-logo span {
            font-size: 1.3rem;
            font-weight: 700;
            color: #4f8cff;
            letter-spacing: 1px;
        }
        .error-icon {
            font-size: 3.5rem;
            color: #ff6b81;
            margin-bottom: 0.7rem;
        }
        .error-title {
            font-size: 1.6rem;
            font-weight: 700;
            color: #4f8cff;
            margin-bottom: 0.5rem;
        }
        .error-subtitle {
            font-size: 1.05rem;
            color: #6a7ba2;
            margin-bottom: 1.2rem;
            font-weight: 500;
        }
        .error-message {
            background: #ffeaea;
            color: #c0392b;
            border: 1px solid #e74c3c;
            border-radius: 12px;
            padding: 0.8rem 1rem;
            font-size: 1rem;
            margin-bottom: 1.2rem;
            word-break: break-word;
        }
        .error-btn {
            display: inline-block;
            background: linear-gradient(120deg, #6ee7b7 0%, #4f8cff 100%);
            color: #fff;
            font-weight: 700;
            font-size: 1.08rem;
            border: none;
            border-radius: 16px;
            padding: 0.9rem 2.2rem;
            box-shadow: 0 2px 12px #4f8cff22;
            transition: background 0.18s, box-shadow 0.18s, transform 0.12s;
            cursor: pointer;
            text-decoration: none;
        }
        .error-btn:hover {
            background: linear-gradient(120deg, #4f8cff 0%, #6ee7b7 100%);
            box-shadow: 0 4px 24px #4f8cff33;
            transform: translateY(-2px) scale(1.01);
        }
    </style>
</head>
<body>
<div class="error-main">
    <div class="error-card">
        <div class="error-logo">
            <i class="fas fa-book-open"></i><span>EbookWebsite</span>
        </div>
        <div class="error-icon"><i class="fas fa-exclamation-triangle"></i></div>
        <div class="error-title">Oops! Something went wrong</div>
        <div class="error-subtitle">An unexpected error has occurred.</div>
        <c:if test="${not empty requestScope['javax.servlet.error.message']}">
            <div class="error-message">
                <i class="fas fa-info-circle"></i> ${requestScope['javax.servlet.error.message']}
            </div>
        </c:if>
        <a href="index.jsp" class="error-btn"><i class="fas fa-home"></i> Back to Home</a>
    </div>
</div>
</body>
</html> 