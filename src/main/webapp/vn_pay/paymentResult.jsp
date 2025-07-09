<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/common/header.jspf" %>

<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card">
                <div class="card-header text-center">
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
                                <h4>🎉 Giao dịch thành công!</h4>
                                <p>Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi.</p>
                                
                                <c:choose>
                                    <c:when test="${transactionType == 'premium_upgrade'}">
                                        <div class="mt-3">
                                            <i class="fas fa-crown text-warning"></i>
                                            <p><strong>Tài khoản của bạn đã được nâng cấp lên Premium!</strong></p>
                                            <p>Bạn có thể đọc tất cả sách và truyện mà không cần coin.</p>
                                        </div>
                                    </c:when>
                                    <c:when test="${transactionType == 'coin_topup'}">
                                        <div class="mt-3">
                                            <i class="fas fa-coins text-warning"></i>
                                            <p><strong>${coinAmount} coins</strong> đã được thêm vào tài khoản của bạn.</p>
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
                                <h4>❌ Giao dịch không thành công</h4>
                                <p>Đã có lỗi xảy ra trong quá trình thanh toán. Vui lòng thử lại.</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                    
                    <div class="mt-4">
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
    </div>
</div>

<%@ include file="/common/footer.jspf" %> 