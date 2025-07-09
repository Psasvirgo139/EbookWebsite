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
            
            <!-- Hiển thị thông báo lỗi -->
            <c:if test="${param.error == 'insufficient_coins'}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-triangle"></i>
                    <strong>Không đủ coin!</strong> Bạn cần có ít nhất 100 coins để nâng cấp Premium. 
                    Số coin hiện tại: <strong>${currentCoins}</strong> coins.
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            
            <c:if test="${param.error == 'database_error'}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-triangle"></i>
                    <strong>Lỗi hệ thống!</strong> Vui lòng thử lại sau.
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            
            <div class="row">
                <!-- Premium Upgrade Section - Bên trái -->
                <div class="col-md-6">
                    <div class="card h-100">
                        <div class="card-header bg-warning text-dark">
                            <h4 class="mb-0">
                                <i class="fas fa-crown"></i> Nâng cấp Premium User
                            </h4>
                        </div>
                        <div class="card-body">
                            <h5>Quyền lợi Premium User:</h5>
                            <ul class="list-unstyled mb-4">
                                <li><i class="fas fa-check text-success"></i> Xem tất cả chapter premium miễn phí</li>
                                <li><i class="fas fa-check text-success"></i> Không cần mua từng chapter</li>
                                <li><i class="fas fa-check text-success"></i> Ưu tiên hỗ trợ khách hàng</li>
                                <li><i class="fas fa-check text-success"></i> Truy cập sớm nội dung mới</li>
                                <li><i class="fas fa-check text-success"></i> Giao diện không quảng cáo</li>
                            </ul>
                            
                            <!-- Premium với VND -->
                            <div class="text-center mb-3">
                                <h5 class="text-warning">Thanh toán bằng tiền mặt:</h5>
                                <h3 class="text-primary">100,000 VND</h3>
                                <small class="text-muted">/ tháng</small>
                                <br><br>
                                <form method="post" action="${pageContext.request.contextPath}/vn_pay/ajax">
                                    <input type="hidden" name="totalBill" value="100000">
                                    <button type="submit" class="btn btn-primary btn-lg">
                                        <i class="fas fa-credit-card"></i> Thanh toán 100k VND
                                    </button>
                                </form>
                            </div>
                            
                            <hr>
                            
                            <!-- Premium với Coin -->
                            <div class="text-center">
                                <h5 class="text-warning">Thanh toán bằng coin:</h5>
                                <h3 class="text-success">100 Coins</h3>
                                <small class="text-muted">/ tháng</small>
                                <br><br>
                                <form id="premiumCoinForm" method="post" action="${pageContext.request.contextPath}/coin/payment">
                                    <input type="hidden" name="action" value="upgrade_premium_coin">
                                    <input type="hidden" id="currentCoins" value="${currentCoins}">
                                    <button type="button" class="btn btn-success btn-lg" onclick="confirmPremiumUpgrade()">
                                        <i class="fas fa-coins"></i> Thanh toán 100 Coins
                                    </button>
                                </form>
                                
                                <div class="mt-2">
                                    <small class="text-muted">Coins hiện tại: <strong>${currentCoins}</strong></small>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                
                <!-- Coin Purchase Section - Bên phải -->
                <div class="col-md-6">
                    <div class="card h-100">
                        <div class="card-header bg-primary text-white">
                            <h4 class="mb-0">
                                <i class="fas fa-coins"></i> Nạp Coins
                            </h4>
                        </div>
                        <div class="card-body">
                            <p class="text-muted mb-4">
                                Sử dụng coins để mở khóa từng chapter premium riêng lẻ. 
                                Mỗi chapter premium cần <strong>5 coins</strong> để mở khóa.
                            </p>
                            
                            <div class="text-center">
                                <h5 class="mb-4">Nhập số lượng coin muốn nạp:</h5>
                                
                                <form method="post" action="${pageContext.request.contextPath}/coin/calculate">
                                    <div class="mb-4">
                                        <label for="coinAmount" class="form-label h5">Số lượng coins:</label>
                                        <input type="number" class="form-control form-control-lg text-center" 
                                               id="coinAmount" name="coinAmount" 
                                               min="1" max="1000" value="50" 
                                               style="font-size: 1.5rem;" required>
                                        <small class="text-muted">Tối thiểu: 1 coin, Tối đa: 1000 coins</small>
                                    </div>
                                    
                                    <div class="mb-4">
                                        <h4 class="text-primary">
                                            Tỷ lệ: 1 coin = 1,000 VND
                                        </h4>
                                        <small class="text-muted">Ví dụ: 50 coins = 50,000 VND</small>
                                    </div>
                                    
                                    <button type="submit" class="btn btn-primary btn-lg">
                                        <i class="fas fa-shopping-cart"></i> Nạp Coins
                                    </button>
                                </form>
                                
                                <!-- Quick coin buttons -->
                                <div class="mt-4">
                                    <h6 class="text-muted">Gói coin phổ biến:</h6>
                                    <div class="row">
                                        <div class="col-6 col-md-3 mb-2">
                                            <form method="post" action="${pageContext.request.contextPath}/coin/calculate" style="display: inline;">
                                                <input type="hidden" name="coinAmount" value="50">
                                                <button type="submit" class="btn btn-outline-secondary btn-sm w-100">
                                                    50 coins<br><small>50,000 VND</small>
                                                </button>
                                            </form>
                                        </div>
                                        <div class="col-6 col-md-3 mb-2">
                                            <form method="post" action="${pageContext.request.contextPath}/coin/calculate" style="display: inline;">
                                                <input type="hidden" name="coinAmount" value="100">
                                                <button type="submit" class="btn btn-outline-secondary btn-sm w-100">
                                                    100 coins<br><small>100,000 VND</small>
                                                </button>
                                            </form>
                                        </div>
                                        <div class="col-6 col-md-3 mb-2">
                                            <form method="post" action="${pageContext.request.contextPath}/coin/calculate" style="display: inline;">
                                                <input type="hidden" name="coinAmount" value="200">
                                                <button type="submit" class="btn btn-outline-secondary btn-sm w-100">
                                                    200 coins<br><small>200,000 VND</small>
                                                </button>
                                            </form>
                                        </div>
                                        <div class="col-6 col-md-3 mb-2">
                                            <form method="post" action="${pageContext.request.contextPath}/coin/calculate" style="display: inline;">
                                                <input type="hidden" name="coinAmount" value="500">
                                                <button type="submit" class="btn btn-outline-secondary btn-sm w-100">
                                                    500 coins<br><small>500,000 VND</small>
                                                </button>
                                            </form>
                                        </div>
                                    </div>
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

<script src="${pageContext.request.contextPath}/assets/js/payment.js"></script>

<%@ include file="/common/footer.jspf" %> 