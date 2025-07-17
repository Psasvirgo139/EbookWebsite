<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/common/header.jsp" %>

<style>
    /* === Modern Tech Design System - Enhanced Contrast === */
    :root {
        --primary: #4361ee;
        --primary-dark: #3a56d4;
        --secondary: #7209b7;
        --accent: #06d6a0;
        --dark: #0f172a;
        --darker: #0a101f;
        --light: #f8f9ff;
        --text-primary: #ffffff;
        --text-secondary: #e2e8f0;
        --gray: #94a3b8;
        --gradient: linear-gradient(135deg, var(--primary), var(--secondary));
        --card-bg: rgba(23, 30, 51, 0.9);
        --card-border: rgba(255, 255, 255, 0.15);
        --shadow: 0 8px 32px rgba(0, 0, 0, 0.5);
        --glow: 0 0 20px rgba(67, 97, 238, 0.5);
    }
    
    .payment-result-container {
        background: linear-gradient(135deg, var(--darker), var(--dark));
        color: var(--text-primary);
        min-height: 100vh;
        display: flex;
        align-items: center;
        justify-content: center;
        padding: 2rem 1rem;
    }
    
    .payment-card {
        background: var(--card-bg);
        backdrop-filter: blur(12px);
        border: 1px solid var(--card-border);
        border-radius: 16px;
        box-shadow: var(--shadow);
        overflow: hidden;
        transition: all 0.4s ease;
        animation: fadeIn 0.7s ease forwards;
        max-width: 800px;
        width: 100%;
        margin: 0 auto;
    }
    
    .payment-card:hover {
        transform: translateY(-5px);
        box-shadow: var(--shadow), var(--glow);
    }
    
    .card-header {
        padding: 1.8rem;
        border-bottom: 1px solid var(--card-border);
        text-align: center;
        background: rgba(67, 97, 238, 0.1);
    }
    
    .card-body {
        padding: 2.5rem;
    }
    
    /* === Typography === */
    h1, h2, h3, h4, h5, h6 {
        font-weight: 700;
        letter-spacing: -0.015em;
        color: var(--text-primary);
        margin-bottom: 1.5rem;
    }
    
    h3 {
        font-size: 2rem;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 12px;
    }
    
    /* === Alert Styles === */
    .alert {
        border-radius: 16px;
        padding: 1.8rem;
        margin-bottom: 2rem;
        border: 1px solid transparent;
        text-align: center;
        position: relative;
        overflow: hidden;
    }
    
    .alert:before {
        content: "";
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        opacity: 0.08;
        z-index: 0;
    }
    
    .alert-success {
        background: rgba(6, 214, 160, 0.15);
        border-color: rgba(6, 214, 160, 0.3);
        color: #d4fdf0;
    }
    
    .alert-success:before {
        background: var(--accent);
    }
    
    .alert-danger {
        background: rgba(220, 53, 69, 0.15);
        border-color: rgba(220, 53, 69, 0.3);
        color: #fecaca;
    }
    
    .alert-danger:before {
        background: #dc3545;
    }
    
    .alert h4 {
        font-size: 1.8rem;
        margin-top: 0;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 12px;
    }
    
    /* === Button Styles === */
    .btn {
        border-radius: 50px;
        font-weight: 600;
        padding: 0.9rem 1.8rem;
        transition: all 0.3s ease;
        border: none;
        display: inline-flex;
        align-items: center;
        gap: 10px;
        font-size: 1.05rem;
        margin: 0.5rem;
        text-decoration: none;
        justify-content: center;
    }
    
    .btn-primary {
        background: var(--primary);
        color: white;
        box-shadow: 0 4px 20px rgba(67, 97, 238, 0.4);
    }
    
    .btn-primary:hover {
        background: var(--primary-dark);
        transform: translateY(-3px);
        box-shadow: 0 7px 25px rgba(67, 97, 238, 0.5);
    }
    
    .btn-success {
        background: var(--accent);
        color: #0a1e18;
        box-shadow: 0 4px 20px rgba(6, 214, 160, 0.4);
    }
    
    .btn-success:hover {
        background: #05b890;
        transform: translateY(-3px);
        box-shadow: 0 7px 25px rgba(6, 214, 160, 0.5);
    }
    
    .btn-outline-secondary {
        background: transparent;
        border: 2px solid var(--gray);
        color: var(--text-secondary);
    }
    
    .btn-outline-secondary:hover {
        background: rgba(148, 163, 184, 0.15);
        transform: translateY(-3px);
        color: var(--text-primary);
    }
    
    .action-group {
        display: flex;
        flex-wrap: wrap;
        justify-content: center;
        margin-top: 2rem;
    }
    
    /* === Icon Styles === */
    .icon-large {
        font-size: 4rem;
        margin-bottom: 1.5rem;
        display: block;
    }
    
    .text-success {
        color: var(--accent);
    }
    
    .text-danger {
        color: #ff6b6b;
    }
    
    .text-warning {
        color: #f59e0b;
    }
    
    /* === Animation === */
    @keyframes fadeIn {
        from { opacity: 0; transform: translateY(20px); }
        to { opacity: 1; transform: translateY(0); }
    }
    
    @keyframes bounce {
        0%, 100% { transform: translateY(0); }
        50% { transform: translateY(-15px); }
    }
    
    .success-animate {
        animation: bounce 1s ease infinite;
    }
    
    /* === Responsive Design === */
    @media (max-width: 768px) {
        .card-body {
            padding: 1.5rem;
        }
        
        h3 {
            font-size: 1.6rem;
        }
        
        .alert h4 {
            font-size: 1.4rem;
        }
        
        .action-group {
            flex-direction: column;
            align-items: center;
        }
        
        .btn {
            width: 100%;
            max-width: 300px;
            margin: 0.5rem 0;
        }
    }
</style>

<div class="payment-result-container">
    <div class="payment-card">
        <div class="card-header">
            <h3>
                <c:choose>
                    <c:when test="${transResult == true}">
                        <i class="fas fa-check-circle text-success"></i>
                        Thanh toán thành công
                    </c:when>
                    <c:otherwise>
                        <i class="fas fa-times-circle text-danger"></i>
                        Thanh toán thất bại
                    </c:otherwise>
                </c:choose>
            </h3>
        </div>
        <div class="card-body text-center">
            <c:choose>
                <c:when test="${transResult == true}">
                    <div class="alert alert-success">
                        <i class="fas fa-check-circle icon-large text-success success-animate"></i>
                        <h4>🎉 Giao dịch thành công!</h4>
                        <p class="lead">Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi.</p>
                        
                        <c:choose>
                            <c:when test="${transactionType == 'premium_upgrade'}">
                                <div class="mt-4">
                                    <i class="fas fa-crown text-warning" style="font-size: 3rem;"></i>
                                    <p class="h5 mt-3"><strong>Tài khoản của bạn đã được nâng cấp lên Premium!</strong></p>
                                    <p>Bạn có thể đọc tất cả sách và truyện mà không cần coin.</p>
                                </div>
                            </c:when>
                            <c:when test="${transactionType == 'coin_topup'}">
                                <div class="mt-4">
                                    <i class="fas fa-coins text-warning" style="font-size: 3rem;"></i>
                                    <p class="h5 mt-3"><strong>${coinAmount} coins</strong> đã được thêm vào tài khoản của bạn.</p>
                                    <p>Bạn có thể sử dụng coins để mở khóa các chương hoặc nâng cấp Premium.</p>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <p>Giao dịch đã được xử lý thành công.</p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="alert alert-danger">
                        <i class="fas fa-times-circle icon-large text-danger"></i>
                        <h4>❌ Giao dịch không thành công</h4>
                        <p class="lead">Đã có lỗi xảy ra trong quá trình thanh toán. Vui lòng thử lại.</p>
                    </div>
                </c:otherwise>
            </c:choose>
            
            <div class="action-group">
                <a href="${pageContext.request.contextPath}/coin/payment" class="btn btn-primary">
                    <i class="fas fa-redo"></i> Thực hiện giao dịch khác
                </a>
                <a href="${pageContext.request.contextPath}/profile" class="btn btn-success">
                    <i class="fas fa-user"></i> Xem trang cá nhân
                </a>
                <a href="${pageContext.request.contextPath}/" class="btn btn-outline-secondary">
                    <i class="fas fa-home"></i> Về trang chủ
                </a>
            </div>
        </div>
    </div>
</div>

<%@ include file="/common/footer.jsp" %>