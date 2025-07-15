<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/common/header.jspf" %>

<div class="container py-4">
    <div class="row">
        <div class="col-md-12">
            <h2><i class="fas fa-file-alt"></i> Cập nhật Content URL cho Chapter</h2>
            <p class="text-muted">Tự động cập nhật đường dẫn file chapter dựa trên cấu trúc thư mục bookreal</p>
            
            <!-- Hiển thị thông báo lỗi -->
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-triangle"></i> ${errorMessage}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            
            <!-- Hiển thị messages -->
            <c:if test="${not empty messages}">
                <div class="alert alert-info">
                    <h6><i class="fas fa-info-circle"></i> Kết quả cập nhật:</h6>
                    <div style="max-height: 300px; overflow-y: auto; font-family: monospace; font-size: 12px;">
                        <c:forEach var="message" items="${messages}">
                            <div>${message}</div>
                        </c:forEach>
                    </div>
                </div>
            </c:if>
            
            <div class="row">
                <!-- Cập nhật sách cụ thể -->
                <div class="col-md-6">
                    <div class="card">
                        <div class="card-header">
                            <h5><i class="fas fa-book"></i> Cập nhật sách cụ thể</h5>
                        </div>
                        <div class="card-body">
                            <form method="get" action="${pageContext.request.contextPath}/admin/chapter-content-update">
                                <input type="hidden" name="action" value="update_specific">
                                
                                <div class="mb-3">
                                    <label for="bookId" class="form-label">ID sách:</label>
                                    <input type="number" class="form-control" id="bookId" name="bookId" 
                                           value="${bookId}" placeholder="Nhập ID sách (ví dụ: 385)" required>
                                    <div class="form-text">Nhập ID của sách cần cập nhật contentUrl</div>
                                </div>
                                
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-sync-alt"></i> Cập nhật sách này
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
                
                <!-- Cập nhật tất cả sách -->
                <div class="col-md-6">
                    <div class="card">
                        <div class="card-header">
                            <h5><i class="fas fa-database"></i> Cập nhật tất cả sách</h5>
                        </div>
                        <div class="card-body">
                            <p class="text-warning">
                                <i class="fas fa-exclamation-triangle"></i> 
                                <strong>Cẩn thận!</strong> Hành động này sẽ cập nhật contentUrl cho tất cả sách trong hệ thống.
                            </p>
                            
                            <form method="get" action="${pageContext.request.contextPath}/admin/chapter-content-update" 
                                  onsubmit="return confirm('Bạn có chắc chắn muốn cập nhật tất cả sách?')">
                                <input type="hidden" name="action" value="update_all">
                                
                                <button type="submit" class="btn btn-warning">
                                    <i class="fas fa-rocket"></i> Cập nhật tất cả sách
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- Kiểm tra file -->
            <div class="row mt-4">
                <div class="col-md-6">
                    <div class="card">
                        <div class="card-header">
                            <h5><i class="fas fa-search"></i> Kiểm tra file tồn tại</h5>
                        </div>
                        <div class="card-body">
                            <form method="get" action="${pageContext.request.contextPath}/admin/chapter-content-update">
                                <input type="hidden" name="action" value="validate">
                                
                                <div class="mb-3">
                                    <label for="validateBookId" class="form-label">ID sách:</label>
                                    <input type="number" class="form-control" id="validateBookId" name="bookId" 
                                           value="${bookId}" placeholder="Nhập ID sách để kiểm tra" required>
                                    <div class="form-text">Kiểm tra xem file chapter có tồn tại không</div>
                                </div>
                                
                                <button type="submit" class="btn btn-info">
                                    <i class="fas fa-check-circle"></i> Kiểm tra file
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
                
                <!-- Thông tin cấu trúc -->
                <div class="col-md-6">
                    <div class="card">
                        <div class="card-header">
                            <h5><i class="fas fa-folder-tree"></i> Cấu trúc thư mục</h5>
                        </div>
                        <div class="card-body">
                            <p><strong>Cấu trúc file chapter:</strong></p>
                            <code style="font-size: 12px;">
                                bookreal/[Category]/[Book Title]/chuong_XXX.txt
                            </code>
                            
                            <hr>
                            
                            <p><strong>Ví dụ:</strong></p>
                            <code style="font-size: 12px;">
                                bookreal/Văn học Việt Nam/Tuyển Tập Truyện Ngắn Vũ Trọng Phụng/chuong_001.txt
                            </code>
                            
                            <hr>
                            
                            <p><strong>Mapping Category:</strong></p>
                            <ul style="font-size: 12px;">
                                <li>Văn học Việt Nam → Văn học Việt Nam</li>
                                <li>Tiểu thuyết phương Tây → Tiểu thuyết phương Tây</li>
                                <li>Truyện ngắn → Truyện ngắn</li>
                                <li>...</li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- Hướng dẫn sử dụng -->
            <div class="row mt-4">
                <div class="col-md-12">
                    <div class="card">
                        <div class="card-header">
                            <h5><i class="fas fa-question-circle"></i> Hướng dẫn sử dụng</h5>
                        </div>
                        <div class="card-body">
                            <ol>
                                <li><strong>Cập nhật sách cụ thể:</strong> Nhập ID sách và click "Cập nhật sách này"</li>
                                <li><strong>Cập nhật tất cả sách:</strong> Click "Cập nhật tất cả sách" (cẩn thận!)</li>
                                <li><strong>Kiểm tra file:</strong> Nhập ID sách và click "Kiểm tra file" để xem file nào tồn tại</li>
                                <li><strong>Sau khi cập nhật:</strong> Truy cập lại trang đọc chapter để kiểm tra</li>
                            </ol>
                            
                            <div class="alert alert-success">
                                <h6><i class="fas fa-lightbulb"></i> Lưu ý:</h6>
                                <ul class="mb-0">
                                    <li>Script sẽ tự động tạo đường dẫn dựa trên tên sách và category</li>
                                    <li>File chapter phải tồn tại trong thư mục bookreal</li>
                                    <li>Tên file phải theo format: chuong_001.txt, chuong_002.txt, ...</li>
                                    <li>Nếu tên sách trong DB khác với tên folder, cần sửa thủ công</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
// Auto-focus vào input khi load trang
document.addEventListener('DOMContentLoaded', function() {
    const bookIdInput = document.getElementById('bookId');
    if (bookIdInput && !bookIdInput.value) {
        bookIdInput.focus();
    }
});

// Confirm trước khi cập nhật tất cả
function confirmUpdateAll() {
    return confirm('⚠️ CẢNH BÁO: Hành động này sẽ cập nhật contentUrl cho TẤT CẢ sách trong hệ thống.\n\nBạn có chắc chắn muốn tiếp tục?');
}
</script>

<%@ include file="/common/footer.jspf" %> 