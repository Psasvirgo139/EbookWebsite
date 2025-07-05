<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>EBookWebsite - Đọc sách online, Nghe ebook và sách nói</title>
  <link href="${pageContext.request.contextPath}/assets/css/style.css" rel="stylesheet" />
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&family=Inter:wght@400;600&family=Roboto:wght@400;500&display=swap" rel="stylesheet" />
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet" />
  <style>
    * {
      box-sizing: border-box;
      margin: 0;
      padding: 0;
    }

    body {
      font-family: 'Poppins', 'Inter', 'Roboto', Arial, sans-serif;
      background: linear-gradient(120deg, #e0f7fa 0%, #f3e7ff 100%);
      color: #222;
      line-height: 1.5;
      min-height: 100vh;
    }

    .container {
      max-width: 1200px;
      margin: 0 auto;
      padding: 0 16px;
    }

    .header {
      background-color: #fff;
      padding: 12px 0;
      border-bottom: 1px solid #e0e7ff;
    }

    .header-container {
      display: flex;
      align-items: center;
      justify-content: space-between;
    }

    .logo img {
      height: 40px;
    }

    .nav-menu ul {
      list-style: none;
      display: flex;
      gap: 24px;
    }

    .nav-menu ul li a {
      color: #222;
      text-decoration: none;
      font-weight: 600;
      font-size: 14px;
      transition: color 0.3s ease;
    }

    .nav-menu ul li a:hover {
      color: #4f8cff;
    }

    .header-actions {
      display: flex;
      gap: 12px;
      align-items: center;
    }

    .btn {
      font-weight: 600;
      font-size: 14px;
      padding: 8px 16px;
      border-radius: 20px;
      cursor: pointer;
      border: none;
      transition: background-color 0.3s ease;
    }

    .btn-gold {
      background-color: #bfa800;
      color: #1a1a1a;
    }

    .btn-gold:hover {
      background-color: #d4c300;
    }

    .btn-outline {
      background-color: transparent;
      color: #4f8cff;
      border: 1px solid #4f8cff;
    }

    .btn-outline:hover {
      background-color: #e0f7fa;
      border-color: #4f8cff;
      color: #222;
    }

    .btn-primary {
      background: linear-gradient(120deg, #6ee7b7 0%, #4f8cff 100%);
      color: #fff;
    }

    .btn-primary:hover {
      background: linear-gradient(120deg, #4f8cff 0%, #6ee7b7 100%);
      color: #fff;
    }

    .main-content {
      padding: 24px 0;
    }

    .carousel-section {
      margin-bottom: 48px;
    }

    .carousel-section h2 {
      font-weight: 700;
      font-size: 20px;
      margin-bottom: 12px;
      color: #4f8cff;
    }

    .carousel {
      position: relative;
      display: flex;
      align-items: center;
    }

    .carousel-track {
      display: flex;
      overflow-x: auto;
      scroll-behavior: smooth;
      gap: 16px;
      padding: 8px 0;
      scrollbar-width: none;
    }

    .carousel-track::-webkit-scrollbar {
      display: none;
    }

    .card {
      min-width: 160px;
      height: 240px;
      background-color: #fff;
      border-radius: 8px;
      box-shadow: 0 2px 12px #4f8cff22;
      flex-shrink: 0;
      border: 1.5px solid #e0e7ff;
    }

    .carousel-btn {
      background-color: rgba(0, 0, 0, 0.6);
      border: none;
      width: 36px;
      height: 36px;
      border-radius: 50%;
      cursor: pointer;
      display: flex;
      align-items: center;
      justify-content: center;
      position: relative;
      z-index: 1;
      transition: background-color 0.3s ease;
    }

    .carousel-btn:hover {
      background-color: rgba(0, 196, 167, 0.8);
    }

    .carousel-btn img {
      width: 16px;
      height: 16px;
    }

    .footer {
      background-color: #fff;
      padding: 32px 0;
      color: #6a7ba2;
      font-size: 14px;
      border-top: 1px solid #e0e7ff;
    }

    .footer-container {
      display: flex;
      flex-wrap: wrap;
      gap: 32px;
      max-width: 1200px;
      margin: 0 auto;
    }

    .footer-logo img {
      height: 40px;
      margin-bottom: 16px;
    }

    .footer-info {
      flex: 2 1 300px;
      line-height: 1.4;
    }

    .footer-contact {
      flex: 1 1 200px;
      display: flex;
      flex-direction: column;
      gap: 12px;
      margin-bottom: 16px;
    }

    .footer-contact div {
      display: flex;
      align-items: center;
      gap: 8px;
    }

    .footer-contact img {
      width: 20px;
      height: 20px;
    }

    .footer-links {
      flex: 2 1 300px;
      display: flex;
      gap: 32px;
      flex-wrap: wrap;
    }

    .footer-links div {
      min-width: 140px;
    }

    .footer-links h4 {
      font-weight: 600;
      margin-bottom: 8px;
      color: #4f8cff;
    }

    .footer-links ul {
      list-style: none;
    }

    .footer-links ul li {
      margin-bottom: 6px;
    }

    .footer-links ul li a {
      color: #4f8cff;
      text-decoration: none;
      transition: color 0.3s ease;
    }

    .footer-links ul li a:hover {
      color: #10b981;
    }

    .footer-apps {
      flex: 1 1 200px;
      display: flex;
      gap: 16px;
      align-items: center;
      justify-content: flex-start;
      margin-top: 16px;
    }

    .footer-apps img {
      height: 40px;
      cursor: pointer;
      transition: transform 0.3s ease;
    }

    .footer-apps img:hover {
      transform: scale(1.05);
    }

    @media (max-width: 768px) {
      .header-container {
        flex-wrap: wrap;
        gap: 12px;
      }

      .nav-menu ul {
        flex-wrap: wrap;
        gap: 12px;
      }

      .footer-container {
        flex-direction: column;
      }

      .footer-links {
        flex-direction: column;
      }
    }

    a, a:visited, a:active, a:hover {
      text-decoration: none !important;
    }
    .card-title, .card-author, .card-tag {
      text-decoration: none !important;
    }
    .card-tag {
      display: inline-block;
      background: #222;
      color: #00c4a7;
      font-size: 12px;
      border-radius: 8px;
      padding: 2px 8px;
      margin-right: 4px;
      margin-top: 4px;
    }
  </style>
</head>
<body>
  <header class="header">
    <div class="container header-container">
      <a href="#" class="logo">
        <span style="display:flex;align-items:center;gap:8px;font-size:1.5rem;font-weight:700;color:#4f8cff;">
          <i class="fas fa-book-open"></i> EBookWebsite
        </span>
      </a>
      <nav class="nav-menu">
        <ul>
          <li><a href="#">Sách điện tử</a></li>
          <li><a href="#">Sách hội viên</a></li>
          <li><a href="#">Sách hiệu sồi</a></li>
          <li><a href="#">Sách nói</a></li>
          <li><a href="#">Podcast</a></li>
          <li><a href="#">Xem thêm</a></li>
        </ul>
      </nav>
      <div class="header-actions">
        <c:choose>
          <c:when test="${not empty sessionScope.user || not empty sessionScope.username}">
            <div style="display:flex; align-items:center; gap:12px;">
              <img src="https://cdn-icons-png.flaticon.com/512/149/149071.png" alt="User Avatar" style="width:36px; height:36px; border-radius:50%; background:#eee; object-fit:cover;" />
              <span style="font-weight:600; color:#222;">
                <c:choose>
                  <c:when test="${not empty sessionScope.user}">${sessionScope.user.username}</c:when>
                  <c:otherwise>${sessionScope.username}</c:otherwise>
                </c:choose>
              </span>
              <form action="${pageContext.request.contextPath}/logout" method="post" style="display:inline; margin:0;">
                <button class="btn btn-primary" type="submit">Đăng xuất</button>
              </form>
            </div>
          </c:when>
          <c:otherwise>
            <a href="${pageContext.request.contextPath}/register" class="btn btn-outline">Đăng ký</a>
            <a href="${pageContext.request.contextPath}/login" class="btn btn-primary">Đăng nhập</a>
          </c:otherwise>
        </c:choose>
      </div>
    </div>
  </header>

  <!-- Banner lớn đầu trang -->
  <section class="main-banner" style="position:relative; margin-bottom:40px;">
    <img src="https://images.unsplash.com/photo-1512820790803-83ca734da794?auto=format&fit=crop&w=1200&q=80" alt="Banner Sách" style="width:100%; max-height:340px; object-fit:cover; border-radius:18px; filter:brightness(0.7);">
    <div style="position:absolute; top:0; left:0; width:100%; height:100%; display:flex; flex-direction:column; align-items:center; justify-content:center; color:#fff;">
      <h1 style="font-size:2.8rem; font-weight:800; text-shadow:0 2px 16px #000a; margin-bottom:12px;">Khám phá kho sách điện tử đặc sắc</h1>
      <p style="font-size:1.3rem; font-weight:500; text-shadow:0 2px 8px #000a;">EBookWebsite - Đọc mọi lúc, học mọi nơi, truyền cảm hứng mỗi ngày!</p>
    </div>
  </section>

  <!-- Banner phụ -->
  <section class="sub-banners" style="display:flex; gap:24px; margin-bottom:32px; flex-wrap:wrap;">
    <div style="flex:1; min-width:260px; position:relative;">
      <img src="https://images.unsplash.com/photo-1464983953574-0892a716854b?auto=format&fit=crop&w=600&q=80" alt="Banner 1" style="width:100%; height:160px; object-fit:cover; border-radius:14px; filter:brightness(0.75);">
      <div style="position:absolute; top:0; left:0; width:100%; height:100%; display:flex; align-items:center; justify-content:center; color:#fff; font-size:1.2rem; font-weight:600; text-shadow:0 2px 8px #000a;">Sách nói truyền cảm hứng</div>
    </div>
    <div style="flex:1; min-width:260px; position:relative;">
      <img src="https://images.unsplash.com/photo-1507842217343-583bb7270b66?auto=format&fit=crop&w=600&q=80" alt="Banner 2" style="width:100%; height:160px; object-fit:cover; border-radius:14px; filter:brightness(0.75);">
      <div style="position:absolute; top:0; left:0; width:100%; height:100%; display:flex; align-items:center; justify-content:center; color:#fff; font-size:1.2rem; font-weight:600; text-shadow:0 2px 8px #000a;">Đọc sách mọi lúc, mọi nơi</div>
    </div>
    <div style="flex:1; min-width:260px; position:relative;">
      <img src="https://images.unsplash.com/photo-1516979187457-637abb4f9353?auto=format&fit=crop&w=600&q=80" alt="Banner 3" style="width:100%; height:160px; object-fit:cover; border-radius:14px; filter:brightness(0.75);">
      <div style="position:absolute; top:0; left:0; width:100%; height:100%; display:flex; align-items:center; justify-content:center; color:#fff; font-size:1.2rem; font-weight:600; text-shadow:0 2px 8px #000a;">Cộng đồng yêu sách & chia sẻ tri thức</div>
    </div>
  </section>

  <main class="main-content container">
    <section class="carousel-section">
      <h2>Đọc nhiều</h2>
      <div class="carousel" id="doc-nhieu">
        <button class="carousel-btn prev" data-target="doc-nhieu">
          <img src="https://waka.vn/svgs/icon-back.svg" alt="Back" />
        </button>
        <div class="carousel-track">
          <c:choose>
            <c:when test="${not empty topBooks}">
              <c:forEach var="book" items="${topBooks}">
                <div class="card">
                  <a href="${pageContext.request.contextPath}/book/detail?id=${book.id}" style="display:block;">
                    <img src="${book.coverUrl}" alt="${book.title}" style="width:100%;height:70%;object-fit:cover;border-radius:8px 8px 0 0;"/>
                  </a>
                  <div style="padding:8px;">
                    <a href="${pageContext.request.contextPath}/book/detail?id=${book.id}" class="card-title" style="font-weight:600; color:#4f8cff; display:block;">${book.title}</a>
                    <div class="card-author" style="font-size:13px;color:#aaa;">Lượt đọc: ${book.viewCount}</div>
                    <c:if test="${not empty book.tags}">
                      <div>
                        <c:forEach var="tag" items="${book.tags}">
                          <span class="card-tag">${tag.name}</span>
                        </c:forEach>
                      </div>
                    </c:if>
                  </div>
                </div>
              </c:forEach>
            </c:when>
            <c:otherwise>
              <div>Không có dữ liệu sách nổi bật.</div>
            </c:otherwise>
          </c:choose>
        </div>
        <button class="carousel-btn next" data-target="doc-nhieu">
          <img src="https://waka.vn/svgs/icon-next-slide.svg" alt="Next" />
        </button>
      </div>
    </section>

    <section class="carousel-section">
      <h2>Nghe nhiều</h2>
      <div class="carousel" id="nghe-nhieu">
        <button class="carousel-btn prev" data-target="nghe-nhieu">
          <img src="https://waka.vn/svgs/icon-back.svg" alt="Back" />
        </button>
        <div class="carousel-track">
          <c:choose>
            <c:when test="${not empty freeBooks}">
              <c:forEach var="book" items="${freeBooks}">
                <div class="card">
                  <a href="${pageContext.request.contextPath}/book/detail?id=${book.id}" style="display:block;">
                    <img src="${book.coverUrl}" alt="${book.title}" style="width:100%;height:70%;object-fit:cover;border-radius:8px 8px 0 0;"/>
                  </a>
                  <div style="padding:8px;">
                    <a href="${pageContext.request.contextPath}/book/detail?id=${book.id}" class="card-title" style="font-weight:600; color:#4f8cff; display:block;">${book.title}</a>
                    <div class="card-author" style="font-size:13px;color:#aaa;">Lượt đọc: ${book.viewCount}</div>
                    <c:if test="${not empty book.tags}">
                      <div>
                        <c:forEach var="tag" items="${book.tags}">
                          <span class="card-tag">${tag.name}</span>
                        </c:forEach>
                      </div>
                    </c:if>
                  </div>
                </div>
              </c:forEach>
            </c:when>
            <c:otherwise>
              <div>Không có dữ liệu sách miễn phí.</div>
            </c:otherwise>
          </c:choose>
        </div>
        <button class="carousel-btn next" data-target="nghe-nhieu">
          <img src="https://waka.vn/svgs/icon-next-slide.svg" alt="Next" />
        </button>
      </div>
    </section>

    <section class="carousel-section">
      <h2>Sách Hiệu Sồi</h2>
      <div class="carousel" id="sach-hieu-soi">
        <button class="carousel-btn prev" data-target="sach-hieu-soi">
          <img src="https://waka.vn/svgs/icon-back.svg" alt="Back" />
        </button>
        <div class="carousel-track">
          <c:choose>
            <c:when test="${not empty newBooks}">
              <c:forEach var="book" items="${newBooks}">
                <div class="card">
                  <a href="${pageContext.request.contextPath}/book/detail?id=${book.id}" style="display:block;">
                    <img src="${book.coverUrl}" alt="${book.title}" style="width:100%;height:70%;object-fit:cover;border-radius:8px 8px 0 0;"/>
                  </a>
                  <div style="padding:8px;">
                    <a href="${pageContext.request.contextPath}/book/detail?id=${book.id}" class="card-title" style="font-weight:600; color:#4f8cff; display:block;">${book.title}</a>
                    <div class="card-author" style="font-size:13px;color:#aaa;">Ngày tạo: <fmt:formatDate value="${book.createdAt}" pattern="dd/MM/yyyy"/></div>
                    <c:if test="${not empty book.tags}">
                      <div>
                        <c:forEach var="tag" items="${book.tags}">
                          <span class="card-tag">${tag.name}</span>
                        </c:forEach>
                      </div>
                    </c:if>
                  </div>
                </div>
              </c:forEach>
            </c:when>
            <c:otherwise>
              <div>Không có dữ liệu sách mới.</div>
            </c:otherwise>
          </c:choose>
        </div>
        <button class="carousel-btn next" data-target="sach-hieu-soi">
          <img src="https://waka.vn/svgs/icon-next-slide.svg" alt="Next" />
        </button>
      </div>
    </section>

    <section class="carousel-section">
      <h2>Podcast</h2>
      <div class="carousel" id="podcast">
        <button class="carousel-btn prev" data-target="podcast">
          <img src="https://waka.vn/svgs/icon-back.svg" alt="Back" />
        </button>
        <div class="carousel-track">
          <c:choose>
            <c:when test="${not empty freeBooks}">
              <c:forEach var="book" items="${freeBooks}">
                <div class="card">
                  <a href="${pageContext.request.contextPath}/book/detail?id=${book.id}" style="display:block;">
                    <img src="${book.coverUrl}" alt="${book.title}" style="width:100%;height:70%;object-fit:cover;border-radius:8px 8px 0 0;"/>
                  </a>
                  <div style="padding:8px;">
                    <a href="${pageContext.request.contextPath}/book/detail?id=${book.id}" class="card-title" style="font-weight:600; color:#4f8cff; display:block;">${book.title}</a>
                    <div class="card-author" style="font-size:13px;color:#aaa;">Lượt đọc: ${book.viewCount}</div>
                    <c:if test="${not empty book.tags}">
                      <div>
                        <c:forEach var="tag" items="${book.tags}">
                          <span class="card-tag">${tag.name}</span>
                        </c:forEach>
                      </div>
                    </c:if>
                  </div>
                </div>
              </c:forEach>
            </c:when>
            <c:otherwise>
              <div>Không có dữ liệu podcast.</div>
            </c:otherwise>
          </c:choose>
        </div>
        <button class="carousel-btn next" data-target="podcast">
          <img src="https://waka.vn/svgs/icon-next-slide.svg" alt="Next" />
        </button>
      </div>
    </section>

    <section class="carousel-section">
      <h2>Cộng đồng viết</h2>
      <div class="carousel" id="cong-dong-viet">
        <button class="carousel-btn prev" data-target="cong-dong-viet">
          <img src="https://waka.vn/svgs/icon-back.svg" alt="Back" />
        </button>
        <div class="carousel-track">
          <c:choose>
            <c:when test="${not empty newBooks}">
              <c:forEach var="book" items="${newBooks}">
                <div class="card">
                  <a href="${pageContext.request.contextPath}/book/detail?id=${book.id}" style="display:block;">
                    <img src="${book.coverUrl}" alt="${book.title}" style="width:100%;height:70%;object-fit:cover;border-radius:8px 8px 0 0;"/>
                  </a>
                  <div style="padding:8px;">
                    <a href="${pageContext.request.contextPath}/book/detail?id=${book.id}" class="card-title" style="font-weight:600; color:#4f8cff; display:block;">${book.title}</a>
                    <div class="card-author" style="font-size:13px;color:#aaa;">Ngày tạo: <fmt:formatDate value="${book.createdAt}" pattern="dd/MM/yyyy"/></div>
                    <c:if test="${not empty book.tags}">
                      <div>
                        <c:forEach var="tag" items="${book.tags}">
                          <span class="card-tag">${tag.name}</span>
                        </c:forEach>
                      </div>
                    </c:if>
                  </div>
                </div>
              </c:forEach>
            </c:when>
            <c:otherwise>
              <div>Không có dữ liệu cộng đồng viết.</div>
            </c:otherwise>
          </c:choose>
        </div>
        <button class="carousel-btn next" data-target="cong-dong-viet">
          <img src="https://waka.vn/svgs/icon-next-slide.svg" alt="Next" />
        </button>
      </div>
    </section>

    <section class="carousel-section">
      <h2>Sách mua lẻ</h2>
      <div class="carousel" id="sach-mua-le">
        <button class="carousel-btn prev" data-target="sach-mua-le">
          <img src="https://waka.vn/svgs/icon-back.svg" alt="Back" />
        </button>
        <div class="carousel-track">
          <c:choose>
            <c:when test="${not empty topBooks}">
              <c:forEach var="book" items="${topBooks}">
                <div class="card">
                  <a href="${pageContext.request.contextPath}/book/detail?id=${book.id}" style="display:block;">
                    <img src="${book.coverUrl}" alt="${book.title}" style="width:100%;height:70%;object-fit:cover;border-radius:8px 8px 0 0;"/>
                  </a>
                  <div style="padding:8px;">
                    <a href="${pageContext.request.contextPath}/book/detail?id=${book.id}" class="card-title" style="font-weight:600; color:#4f8cff; display:block;">${book.title}</a>
                    <div class="card-author" style="font-size:13px;color:#aaa;">Lượt đọc: ${book.viewCount}</div>
                    <c:if test="${not empty book.tags}">
                      <div>
                        <c:forEach var="tag" items="${book.tags}">
                          <span class="card-tag">${tag.name}</span>
                        </c:forEach>
                      </div>
                    </c:if>
                  </div>
                </div>
              </c:forEach>
            </c:when>
            <c:otherwise>
              <div>Không có dữ liệu sách mua lẻ.</div>
            </c:otherwise>
          </c:choose>
        </div>
        <button class="carousel-btn next" data-target="sach-mua-le">
          <img src="https://waka.vn/svgs/icon-next-slide.svg" alt="Next" />
        </button>
      </div>
    </section>
  </main>

  <footer class="footer">
    <div class="container footer-container">
      <div class="footer-logo">
        <span style="display:flex;align-items:center;gap:8px;font-size:1.5rem;font-weight:700;color:#4f8cff;">
          <i class="fas fa-book-open"></i> EBookWebsite
        </span>
      </div>
      <div class="footer-info">
        <p>Công ty EBookWebsite</p>
        <p>Địa chỉ công ty</p>
        <p>Mã số doanh nghiệp: [Mã số mẫu]</p>
        <p>Giấy phép hoạt động: [Thông tin mẫu]</p>
        <p>Hotline: [Hotline mẫu]</p>
      </div>
      <div class="footer-contact">
        <div>
          <img src="https://waka.vn/svgs/icon-phone-footer.svg" alt="Phone" />
          <span>Số điện thoại</span>
        </div>
        <div>
          <img src="https://waka.vn/svgs/icon-email-footer.svg" alt="Email" />
          <span>Email</span>
        </div>
      </div>
      <div class="footer-links">
        <div>
          <h4>Về chúng tôi</h4>
          <ul>
            <li><a href="#">Giới thiệu</a></li>
            <li><a href="#">Cơ cấu tổ chức</a></li>
            <li><a href="#">Lĩnh vực hoạt động</a></li>
            <li><a href="#">Cơ hội hợp tác</a></li>
            <li><a href="#">Tuyển dụng</a></li>
            <li><a href="#">Liên hệ</a></li>
            <li><a href="#">Dịch vụ xuất bản sách</a></li>
          </ul>
        </div>
        <div>
          <h4>Thông tin hữu ích</h4>
          <ul>
            <li><a href="#">Thỏa thuận sử dụng dịch vụ</a></li>
            <li><a href="#">Quyền lợi</a></li>
            <li><a href="#">Quy định riêng tư</a></li>
            <li><a href="#">Câu hỏi thường gặp</a></li>
          </ul>
        </div>
        <div>
          <h4>Tin tức</h4>
          <ul>
            <li><a href="#">Tin dịch vụ</a></li>
            <li><a href="#">Review sách</a></li>
            <li><a href="#">Lịch phát hành</a></li>
          </ul>
        </div>
      </div>
      <div class="footer-apps">
        <a href="https://apps.apple.com/vn/app/waka-4-0-ebook-audiobook/id929027521?l=vi" target="_blank" rel="noopener">
          <img src="https://waka.vn/images/app-store.png" alt="App Store" />
        </a>
        <a href="https://play.google.com/store/apps/details?id=anim.dqh.tvw&hl=en&gl=US" target="_blank" rel="noopener">
          <img src="https://waka.vn/images/google-play.png" alt="Google Play" />
        </a>
      </div>
    </div>
  </footer>

  <script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>
</html>
