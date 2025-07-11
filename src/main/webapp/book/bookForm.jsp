<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String ctx = request.getContextPath();
    boolean editMode = request.getAttribute("editMode") != null && (Boolean)request.getAttribute("editMode");
    com.mycompany.ebookwebsite.model.Ebook book = (com.mycompany.ebookwebsite.model.Ebook)request.getAttribute("book");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8" />
    <title><%= editMode ? "Sửa sách" : "🚀 Upload sách với AI" %></title>
    <link rel="stylesheet" href="${ctx}/assets/css/style.css" />
    <style>
        .upload-container {
            max-width: 800px;
            margin: 0 auto;
            padding: 20px;
        }
        .upload-header {
            text-align: center;
            margin-bottom: 30px;
        }
        .upload-header h1 {
            color: #2c3e50;
            margin-bottom: 10px;
        }
        .upload-header p {
            color: #7f8c8d;
            font-size: 16px;
        }
        .file-upload-zone {
            border: 3px dashed #bdc3c7;
            border-radius: 12px;
            padding: 60px 20px;
            text-align: center;
            margin: 30px 0;
            background: #f8f9fa;
            transition: all 0.3s ease;
            cursor: pointer;
            position: relative;
        }
        .file-upload-zone:hover {
            border-color: #3498db;
            background: #e8f4f8;
        }
        .file-upload-zone.dragover {
            border-color: #e74c3c;
            background: #ffeaa7;
            transform: scale(1.02);
        }
        .file-upload-zone.file-selected {
            border-color: #27ae60;
            background: #d5f4e6;
        }
        .upload-icon {
            font-size: 48px;
            margin-bottom: 20px;
        }
        .file-info {
            display: none;
            background: #fff;
            padding: 20px;
            border-radius: 8px;
            margin: 20px 0;
            border: 1px solid #ddd;
        }
        .file-info.show {
            display: block;
        }
        .metadata-preview {
            display: none;
            background: #e8f5e8;
            padding: 20px;
            border-radius: 8px;
            margin: 20px 0;
            border: 1px solid #27ae60;
        }
        .metadata-preview.show {
            display: block;
        }
        .progress-bar {
            width: 100%;
            height: 10px;
            background: #ecf0f1;
            border-radius: 5px;
            overflow: hidden;
            margin: 10px 0;
            display: none;
        }
        .progress-fill {
            height: 100%;
            background: linear-gradient(90deg, #3498db, #2ecc71);
            width: 0%;
            transition: width 0.3s ease;
        }
        .upload-actions {
            text-align: center;
            margin: 30px 0;
        }
        .btn {
            padding: 12px 24px;
            border: none;
            border-radius: 6px;
            font-size: 16px;
            cursor: pointer;
            margin: 0 10px;
            transition: all 0.3s ease;
        }
        .btn-primary {
            background: #3498db;
            color: white;
        }
        .btn-primary:hover {
            background: #2980b9;
        }
        .btn-primary:disabled {
            background: #bdc3c7;
            cursor: not-allowed;
        }
        .btn-success {
            background: #27ae60;
            color: white;
        }
        .btn-success:hover {
            background: #229954;
        }
        .btn-secondary {
            background: #95a5a6;
            color: white;
        }
        .btn-secondary:hover {
            background: #7f8c8d;
        }
        .status-message {
            padding: 15px;
            border-radius: 6px;
            margin: 20px 0;
            display: none;
        }
        .status-success {
            background: #d5f4e6;
            color: #27ae60;
            border: 1px solid #27ae60;
        }
        .status-error {
            background: #ffebee;
            color: #e74c3c;
            border: 1px solid #e74c3c;
        }
        .status-info {
            background: #e8f4f8;
            color: #3498db;
            border: 1px solid #3498db;
        }
        .ai-features {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            margin: 20px 0;
        }
        .ai-features h3 {
            color: #2c3e50;
            margin-bottom: 15px;
        }
        .ai-features ul {
            margin: 0;
            padding-left: 20px;
        }
        .ai-features li {
            margin-bottom: 8px;
            color: #34495e;
        }
        .hidden {
            display: none !important;
        }
        .loading {
            display: inline-block;
            width: 20px;
            height: 20px;
            border: 3px solid #f3f3f3;
            border-top: 3px solid #3498db;
            border-radius: 50%;
            animation: spin 1s linear infinite;
        }
        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }
    </style>
</head>
<body>
<%@ include file="/common/header.jsp" %>
<main>
    <div class="upload-container">
        <div class="upload-header">
            <h1>🚀 Upload sách với AI</h1>
            <p>Nhập tên sách và hệ thống sẽ tự động tìm file phù hợp để tạo sách</p>
        </div>
        
        <!-- Success/Error Messages -->
        <% if (request.getParameter("success") != null) { %>
            <div class="status-message status-success" style="display: block;">
                <% if ("ai_upload_completed".equals(request.getParameter("success"))) { %>
                    ✅ Upload và xử lý AI hoàn tất! Sách đã được tạo thành công.
                    <a href="${ctx}/book-list" class="btn btn-success" style="margin-left: 10px;">Xem danh sách sách</a>
                <% } else if ("book_created".equals(request.getParameter("success"))) { %>
                    ✅ Sách đã được tạo thành công!
                    <a href="${ctx}/book-list" class="btn btn-success" style="margin-left: 10px;">Xem danh sách sách</a>
                <% } %>
            </div>
        <% } %>
        
        <c:if test="${not empty error}">
            <div class="status-message status-error" style="display: block;">
                ❌ ${error}
            </div>
        </c:if>
        
        <form id="uploadForm" method="post" action="${ctx}/book/upload" enctype="multipart/form-data">
            <c:if test="${editMode}">
                <input type="hidden" name="id" value="${book.id}" />
            </c:if>
            
            <!-- Book Title Input -->
            <div style="background: #e8f5e8; padding: 25px; border-radius: 12px; margin-bottom: 30px; border: 2px solid #27ae60;">
                <h3 style="color: #27ae60; margin-bottom: 15px;">📝 Thông tin sách</h3>
                <div style="margin-bottom: 20px;">
                    <label for="bookTitle" style="display: block; font-weight: bold; margin-bottom: 8px; color: #2c3e50;">
                        🏷️ Tên sách của bạn:
                    </label>
                    <input type="text" id="bookTitle" name="bookTitle" 
                           placeholder="Nhập tên sách (VD: Harry Potter và Hòn đá Phù thủy)" 
                           style="width: 100%; padding: 15px; border: 2px solid #27ae60; border-radius: 8px; font-size: 16px; box-sizing: border-box;" 
                           required />
                    <p style="color: #7f8c8d; font-size: 14px; margin-top: 8px; margin-bottom: 0;">
                        ⭐ <strong>Quan trọng:</strong> Tên này sẽ được dùng làm tiêu đề chính của sách, không phụ thuộc vào AI extract
                    </p>
                </div>
            </div>
            
            <!-- File Upload Zone -->
            <div class="file-upload-zone" id="fileUploadZone">
                <div class="upload-icon">📚</div>
                <h3>Chọn file sách (tùy chọn)</h3>
                <p>Hỗ trợ: .txt, .pdf, .docx | Tối đa 50MB</p>
                <p style="color: #e67e22; font-weight: bold; margin: 10px 0;">
                    💡 <strong>Tip:</strong> Nếu không chọn file, hệ thống sẽ tự động tìm file phù hợp với tên sách trong thư viện uploads
                </p>
                <input type="file" name="bookFile" id="bookFile" accept=".txt,.pdf,.docx" 
                       style="display: none;" onchange="handleFileSelect(this)" />
                <button type="button" class="btn btn-primary" onclick="document.getElementById('bookFile').click()">
                    Chọn File
                </button>
                <p style="color: #7f8c8d; font-size: 14px; margin-top: 15px;">
                    Hoặc để trống và hệ thống sẽ tìm file dựa trên tên sách bạn nhập ở trên
                </p>
            </div>
            
            <!-- Progress Bar -->
            <div class="progress-bar" id="progressBar">
                <div class="progress-fill" id="progressFill"></div>
            </div>
            
            <!-- File Info -->
            <div class="file-info" id="fileInfo">
                <h4>📄 File đã chọn:</h4>
                <p><strong>Tên file:</strong> <span id="fileName"></span></p>
                <p><strong>Kích thước:</strong> <span id="fileSize"></span></p>
                <p><strong>Trạng thái:</strong> <span id="fileStatus">Đã chọn, sẵn sàng upload</span></p>
            </div>
            
            <!-- Status Messages -->
            <div class="status-message status-info" id="uploadingMessage">
                <div class="loading"></div> Đang upload và xử lý với AI...
            </div>
            
            <div class="status-message status-info" id="moderationMessage">
                <div class="loading"></div> Đang kiểm duyệt nội dung...
            </div>
            
            <div class="status-message status-info" id="metadataMessage">
                <div class="loading"></div> Đang trích xuất metadata...
            </div>
            
            <!-- Metadata Preview -->
            <div class="metadata-preview" id="metadataPreview">
                <h4>🤖 Thông tin AI đã trích xuất:</h4>
                <div id="metadataContent">
                    <!-- Will be populated by JavaScript -->
                </div>
            </div>
            
            <!-- Upload Actions -->
            <div class="upload-actions">
                <button type="submit" class="btn btn-primary" id="uploadBtn">
                    🚀 Tạo sách với AI
                </button>
                <button type="button" class="btn btn-secondary" onclick="resetForm()">
                    🔄 Reset form
                </button>
                <a href="${ctx}/book-list" class="btn btn-secondary">
                    📚 Xem danh sách sách
                </a>
            </div>
        </form>
        
        <!-- AI Features Info -->
        <div class="ai-features">
            <h3>🤖 AI sẽ tự động xử lý:</h3>
            <ul>
                <li>✅ Sử dụng <strong>tên bạn nhập</strong> làm tiêu đề chính</li>
                <li>✅ Tìm file phù hợp dựa trên tên sách</li>
                <li>✅ Kiểm duyệt nội dung an toàn</li>
                <li>✅ Phân loại thể loại chính xác</li>
                <li>✅ Viết mô tả hấp dẫn</li>
                <li>✅ Tạo tóm tắt thông minh</li>
                <li>✅ Tự động tạo EbookAI record</li>
                <li>✅ Đưa sách lên kệ nếu kiểm duyệt qua</li>
            </ul>
        </div>
    </div>
</main>

<script>
    let selectedFile = null;
    let uploadInProgress = false;
    
    // DOM Elements
    const fileUploadZone = document.getElementById('fileUploadZone');
    const fileInput = document.getElementById('bookFile');
    const fileInfo = document.getElementById('fileInfo');
    const uploadBtn = document.getElementById('uploadBtn');
    const progressBar = document.getElementById('progressBar');
    const progressFill = document.getElementById('progressFill');
    const uploadForm = document.getElementById('uploadForm');
    
    // Status messages
    const uploadingMessage = document.getElementById('uploadingMessage');
    const moderationMessage = document.getElementById('moderationMessage');
    const metadataMessage = document.getElementById('metadataMessage');
    const metadataPreview = document.getElementById('metadataPreview');
    
    // File selection handler
    function handleFileSelect(input) {
        const file = input.files[0];
        if (file) {
            selectedFile = file;
            showFileInfo(file);
            enableUploadButton();
            updateUploadZone(true);
        }
    }
    
    // Show file information
    function showFileInfo(file) {
        document.getElementById('fileName').textContent = file.name;
        document.getElementById('fileSize').textContent = formatFileSize(file.size);
        document.getElementById('fileStatus').textContent = 'Sẵn sàng upload';
        fileInfo.classList.add('show');
    }
    
    // Update upload zone appearance
    function updateUploadZone(hasFile) {
        if (hasFile) {
            fileUploadZone.classList.add('file-selected');
            fileUploadZone.innerHTML = `
                <div class="upload-icon">✅</div>
                <h3>File đã chọn: ${selectedFile.name}</h3>
                <p>Kích thước: ${formatFileSize(selectedFile.size)}</p>
                <button type="button" class="btn btn-secondary" onclick="document.getElementById('bookFile').click()">
                    Chọn file khác
                </button>
            `;
        } else {
            fileUploadZone.classList.remove('file-selected');
            fileUploadZone.innerHTML = `
                <div class="upload-icon">📚</div>
                <h3>Chọn file sách (tùy chọn)</h3>
                <p>Hỗ trợ: .txt, .pdf, .docx | Tối đa 50MB</p>
                <p style="color: #e67e22; font-weight: bold; margin: 10px 0;">
                    💡 <strong>Tip:</strong> Nếu không chọn file, hệ thống sẽ tự động tìm file phù hợp với tên sách trong thư viện uploads
                </p>
                <input type="file" name="bookFile" id="bookFile" accept=".txt,.pdf,.docx" 
                       style="display: none;" onchange="handleFileSelect(this)" />
                <button type="button" class="btn btn-primary" onclick="document.getElementById('bookFile').click()">
                    Chọn File
                </button>
                <p style="color: #7f8c8d; font-size: 14px; margin-top: 15px;">
                    Hoặc để trống và hệ thống sẽ tìm file dựa trên tên sách bạn nhập ở trên
                </p>
            `;
        }
    }
    
    // Enable upload button
    function enableUploadButton() {
        uploadBtn.disabled = false;
        if (selectedFile) {
            uploadBtn.textContent = '🚀 Upload & Xử lý AI';
        } else {
            uploadBtn.textContent = '🚀 Tạo sách với AI';
        }
    }
    
    // Reset form
    function resetForm() {
        selectedFile = null;
        fileInput.value = '';
        document.getElementById('bookTitle').value = '';
        fileInfo.classList.remove('show');
        metadataPreview.classList.remove('show');
        uploadBtn.disabled = false;
        uploadBtn.textContent = '🚀 Tạo sách với AI';
        updateUploadZone(false);
        hideAllStatusMessages();
    }
    
    // Hide all status messages
    function hideAllStatusMessages() {
        uploadingMessage.style.display = 'none';
        moderationMessage.style.display = 'none';
        metadataMessage.style.display = 'none';
        progressBar.style.display = 'none';
    }
    
    // Format file size
    function formatFileSize(bytes) {
        if (bytes === 0) return '0 Bytes';
        const k = 1024;
        const sizes = ['Bytes', 'KB', 'MB', 'GB'];
        const i = Math.floor(Math.log(bytes) / Math.log(k));
        return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
    }
    
    // Form submission handler
    uploadForm.addEventListener('submit', function(e) {
        // Check if book title is provided
        const bookTitle = document.getElementById('bookTitle').value.trim();
        if (!bookTitle) {
            e.preventDefault();
            alert('Vui lòng nhập tên sách!');
            return;
        }
        
        if (uploadInProgress) {
            e.preventDefault();
            return;
        }
        
        uploadInProgress = true;
        uploadBtn.disabled = true;
        
        if (selectedFile) {
            uploadBtn.innerHTML = '<div class="loading"></div> Đang upload & xử lý...';
            uploadingMessage.style.display = 'block';
        } else {
            uploadBtn.innerHTML = '<div class="loading"></div> Đang tìm file & xử lý...';
            uploadingMessage.innerHTML = '<div class="loading"></div> Đang tìm file phù hợp với tên sách...';
            uploadingMessage.style.display = 'block';
        }
        
        // Show progress
        progressBar.style.display = 'block';
        
        // Simulate progress (real progress would come from server)
        simulateProgress();
    });
    
    // Simulate upload progress
    function simulateProgress() {
        let progress = 0;
        const interval = setInterval(() => {
            progress += Math.random() * 15;
            if (progress >= 100) {
                progress = 100;
                clearInterval(interval);
            }
            progressFill.style.width = progress + '%';
        }, 500);
    }
    
    // Drag and drop handlers
    fileUploadZone.addEventListener('dragover', (e) => {
        e.preventDefault();
        fileUploadZone.classList.add('dragover');
    });
    
    fileUploadZone.addEventListener('dragleave', () => {
        fileUploadZone.classList.remove('dragover');
    });
    
    fileUploadZone.addEventListener('drop', (e) => {
        e.preventDefault();
        fileUploadZone.classList.remove('dragover');
        
        const files = e.dataTransfer.files;
        if (files.length > 0) {
            const file = files[0];
            
            // Check file type
            const allowedTypes = ['.txt', '.pdf', '.docx'];
            const fileExt = '.' + file.name.split('.').pop().toLowerCase();
            
            if (!allowedTypes.includes(fileExt)) {
                alert('Chỉ hỗ trợ file: .txt, .pdf, .docx');
                return;
            }
            
            // Check file size (50MB)
            if (file.size > 50 * 1024 * 1024) {
                alert('File quá lớn! Tối đa 50MB.');
                return;
            }
            
            // Set file to input
            const dt = new DataTransfer();
            dt.items.add(file);
            fileInput.files = dt.files;
            
            handleFileSelect(fileInput);
        }
    });
    
    // Initial state
    hideAllStatusMessages();
</script>

<%@ include file="/common/footer.jsp" %>
</body>
</html> 