<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Trang AI - AIEBookWebsite</title>
    <link rel="icon" href="favicon.svg" type="image/svg+xml" />
    <link rel="alternate icon" href="favicon.svg" />
        <link href="assets/css/style.css" rel="stylesheet" type="text/css"/>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </head>
    <body>
        <%@include file="includes/header.jsp" %>
        
        <div class="container mt-4">
            <h2 class="mb-4">Chức năng AI</h2>
            
            <div class="row">
                <div class="col-md-6 mb-4">
                    <div class="card">
                        <div class="card-body">
                            <h5 class="card-title">Kiểm tra nội dung</h5>
                            <form id="checkForm" action="ai" method="POST">
                                <input type="hidden" name="action" value="check">
                                <div class="mb-3">
                                    <label for="checkContent" class="form-label">Nhập nội dung cần kiểm tra</label>
                                    <textarea class="form-control" id="checkContent" name="content" rows="4" required></textarea>
                                </div>
                                <button type="submit" class="btn btn-primary">Kiểm tra</button>
                            </form>
                            <div id="checkResult" class="mt-3"></div>
                        </div>
                    </div>
                </div>

                <div class="col-md-6 mb-4">
                    <div class="card">
                        <div class="card-body">
                            <h5 class="card-title">Phân tích nội dung</h5>
                            <form id="analyzeForm" action="ai" method="POST">
                                <input type="hidden" name="action" value="analyze">
                                <div class="mb-3">
                                    <label for="analyzeContent" class="form-label">Nhập nội dung cần phân tích</label>
                                    <textarea class="form-control" id="analyzeContent" name="content" rows="4" required></textarea>
                                </div>
                                <button type="submit" class="btn btn-primary">Phân tích</button>
                            </form>
                            <div id="analyzeResult" class="mt-3"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script>
            // Xử lý kết quả kiểm tra
            document.getElementById('checkForm').addEventListener('submit', function(e) {
                e.preventDefault();
                const formData = new FormData(this);
                fetch('ai', {
                    method: 'POST',
                    body: formData
                })
                .then(response => response.json())
                .then(data => {
                    const resultDiv = document.getElementById('checkResult');
                    if (data.error) {
                        resultDiv.innerHTML = `<div class="alert alert-danger">${data.error}</div>`;
                    } else {
                        resultDiv.innerHTML = `
                            <div class="alert ${data.valid ? 'alert-success' : 'alert-danger'}">
                                ${data.message}
                            </div>
                        `;
                    }
                })
                .catch(error => {
                    document.getElementById('checkResult').innerHTML = 
                        `<div class="alert alert-danger">Đã xảy ra lỗi: ${error}</div>`;
                });
            });

            // Xử lý kết quả phân tích
            document.getElementById('analyzeForm').addEventListener('submit', function(e) {
                e.preventDefault();
                const formData = new FormData(this);
                fetch('ai', {
                    method: 'POST',
                    body: formData
                })
                .then(response => response.json())
                .then(data => {
                    const resultDiv = document.getElementById('analyzeResult');
                    if (data.error) {
                        resultDiv.innerHTML = `<div class="alert alert-danger">${data.error}</div>`;
                    } else {
                        resultDiv.innerHTML = `
                            <div class="alert alert-info">
                                Phân tích: ${data.analysis}
                            </div>
                        `;
                    }
                })
                .catch(error => {
                    document.getElementById('analyzeResult').innerHTML = 
                        `<div class="alert alert-danger">Đã xảy ra lỗi: ${error}</div>`;
                });
            });
        </script>

        <%@include file="includes/footer.jsp" %>
    </body>
</html>
