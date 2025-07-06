<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ include file="/common/header.jspf" %>

<div class="container py-4">
    <div class="row justify-content-center">
        <div class="col-md-10">
            <h2 class="text-center mb-4">
                <i class="fas fa-credit-card"></i> Nạp tiền & Premium
            </h2>
            
            <!-- Premium Upgrade Section -->
            <div class="card mb-4">
                <div class="card-header bg-warning text-dark">
                    <h4 class="mb-0">
                        <i class="fas fa-crown"></i> Nâng cấp Premium User
                    </h4>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-8">
                            <h5>Quyền lợi Premium User:</h5>
                            <ul class="list-unstyled">
                                <li><i class="fas fa-check text-success"></i> Xem tất cả chapter premium miễn phí</li>
                                <li><i class="fas fa-check text-success"></i> Không cần mua từng chapter</li>
                                <li><i class="fas fa-check text-success"></i> Ưu tiên hỗ trợ khách hàng</li>
                                <li><i class="fas fa-check text-success"></i> Truy cập sớm nội dung mới</li>
                                <li><i class="fas fa-check text-success"></i> Giao diện không quảng cáo</li>
                            </ul>
                        </div>
                        <div class="col-md-4 text-center">
                            <h3 class="text-warning">
                                <fmt:formatNumber value="${premiumPrice}" type="currency" currencyCode="VND"/>
                            </h3>
                            <small class="text-muted">/ tháng</small>
                            <br><br>
                            <form method="post" action="${pageContext.request.contextPath}/coin/payment">
                                <input type="hidden" name="action" value="upgrade_premium">
                                <button type="submit" class="btn btn-warning btn-lg">
                                    <i class="fas fa-crown"></i> Nâng cấp Premium
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- Coin Purchase Section -->
            <div class="card">
                <div class="card-header bg-primary text-white">
                    <h4 class="mb-0">
                        <i class="fas fa-coins"></i> Mua Coins
                    </h4>
                </div>
                <div class="card-body">
                    <p class="text-muted">
                        Sử dụng coins để mở khóa từng chapter premium riêng lẻ. 
                        Mỗi chapter premium cần <strong>5 coins</strong> để mở khóa.
                    </p>
                    
                    <div class="row">
                        <!-- Coin Package 1 -->
                        <div class="col-md-4">
                            <div class="card text-center">
                                <div class="card-body">
                                    <h5 class="card-title">
                                        <i class="fas fa-coins text-warning"></i> Gói Cơ bản
                                    </h5>
                                    <h3 class="text-primary">50 Coins</h3>
                                    <p class="text-muted">
                                        Có thể mở khóa 10 chapter<br>
                                        <strong>50,000 VND</strong>
                                    </p>
                                    <form method="post" action="${pageContext.request.contextPath}/coin/payment">
                                        <input type="hidden" name="action" value="buy_coins">
                                        <input type="hidden" name="coinPackage" value="pack_50">
                                        <button type="submit" class="btn btn-outline-primary">
                                            Mua ngay
                                        </button>
                                    </form>
                                </div>
                            </div>
                        </div>
                        
                        <!-- Coin Package 2 -->
                        <div class="col-md-4">
                            <div class="card text-center border-success">
                                <div class="card-body">
                                    <h5 class="card-title">
                                        <i class="fas fa-coins text-warning"></i> Gói Phổ biến
                                        <span class="badge bg-success">Tiết kiệm</span>
                                    </h5>
                                    <h3 class="text-success">100 Coins</h3>
                                    <p class="text-muted">
                                        Có thể mở khóa 20 chapter<br>
                                        <strong>90,000 VND</strong><br>
                                        <small class="text-success">Tiết kiệm 10,000 VND</small>
                                    </p>
                                    <form method="post" action="${pageContext.request.contextPath}/coin/payment">
                                        <input type="hidden" name="action" value="buy_coins">
                                        <input type="hidden" name="coinPackage" value="pack_100">
                                        <button type="submit" class="btn btn-success">
                                            Mua ngay
                                        </button>
                                    </form>
                                </div>
                            </div>
                        </div>
                        
                        <!-- Coin Package 3 -->
                        <div class="col-md-4">
                            <div class="card text-center border-warning">
                                <div class="card-body">
                                    <h5 class="card-title">
                                        <i class="fas fa-coins text-warning"></i> Gói VIP
                                        <span class="badge bg-warning">Siêu tiết kiệm</span>
                                    </h5>
                                    <h3 class="text-warning">200 Coins</h3>
                                    <p class="text-muted">
                                        Có thể mở khóa 40 chapter<br>
                                        <strong>160,000 VND</strong><br>
                                        <small class="text-warning">Tiết kiệm 40,000 VND</small>
                                    </p>
                                    <form method="post" action="${pageContext.request.contextPath}/coin/payment">
                                        <input type="hidden" name="action" value="buy_coins">
                                        <input type="hidden" name="coinPackage" value="pack_200">
                                        <button type="submit" class="btn btn-warning">
                                            Mua ngay
                                        </button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- Payment Methods Info -->
            <div class="card mt-4">
                <div class="card-header">
                    <h5 class="mb-0">
                        <i class="fas fa-credit-card"></i> Phương thức thanh toán
                    </h5>
                </div>
                <div class="card-body">
                    <div class="row text-center">
                        <div class="col-md-3">
                            <i class="fab fa-cc-visa fa-2x text-primary"></i>
                            <p class="mt-2">Visa</p>
                        </div>
                        <div class="col-md-3">
                            <i class="fab fa-cc-mastercard fa-2x text-warning"></i>
                            <p class="mt-2">MasterCard</p>
                        </div>
                        <div class="col-md-3">
                            <i class="fas fa-mobile-alt fa-2x text-success"></i>
                            <p class="mt-2">Ví điện tử</p>
                        </div>
                        <div class="col-md-3">
                            <i class="fas fa-university fa-2x text-info"></i>
                            <p class="mt-2">Chuyển khoản</p>
                        </div>
                    </div>
                    
                    <div class="alert alert-info mt-3">
                        <i class="fas fa-info-circle"></i>
                        <strong>Lưu ý:</strong> Hiện tại đây là phiên bản demo. Trong thực tế, sẽ tích hợp với các cổng thanh toán như VNPay, MoMo, ZaloPay, v.v.
                    </div>
                </div>
            </div>
            
            <!-- Back to reading -->
            <div class="text-center mt-4">
                <a href="${pageContext.request.contextPath}/book" class="btn btn-outline-secondary">
                    <i class="fas fa-arrow-left"></i> Quay lại đọc sách
                </a>
            </div>
        </div>
    </div>
</div>

<%@ include file="/common/footer.jspf" %> 