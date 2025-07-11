<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<link rel="stylesheet" href="${ctx}/assets/css/style.css" />
<link rel="stylesheet" href="${ctx}/assets/css/coin-system.css" />

<style>
/* Header coin display */
.header-coin-display {
    background: linear-gradient(45deg, #ffd700, #ffed4e);
    color: #333;
    padding: 2px 8px;
    border-radius: 12px;
    font-size: 0.85rem;
    font-weight: 600;
    border: 1px solid #e6c200;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    display: inline-block;
    white-space: nowrap;
}

.header-coin-display:hover {
    background: linear-gradient(45deg, #ffed4e, #ffd700);
    transform: scale(1.05);
    transition: all 0.2s ease;
}

/* Responsive coin display */
@media (max-width: 768px) {
    .header-coin-display {
        font-size: 0.75rem;
        padding: 1px 6px;
    }
}
</style>

<a href="#main" class="skip-link">Bỏ qua và tới nội dung chính</a>
<header class="header" id="top">
  <div class="logo" aria-label="Trang chủ Scroll">
    📚 <span>scroll</span>
  </div>
  <nav class="main-nav" id="mainNav" aria-label="Điều hướng chính">
    <a class="nav-link" href="${ctx}/index.jsp">Trang chủ</a>
    <div class="nav-item genres-dropdown">
      <a class="nav-link" href="#" aria-haspopup="true" aria-expanded="false">
        Thể loại ▾
      </a>
      <div class="mega-menu" aria-label="Danh sách thể loại">
        <div class="mega-column">
          <a href="#">Ngôn Tình</a>
          <a href="#">Đô Thị</a>
          <a href="#">Khoa Huyễn</a>
        </div>
        <div class="mega-column">
          <a href="#">Tiên Hiệp</a>
          <a href="#">Quan Trường</a>
        </div>
        <div class="mega-column">
          <a href="#">Kiếm Hiệp</a>
          <a href="#">Võng Du</a>
        </div>
      </div>
    </div>
    <a class="nav-link" href="${ctx}/latest">Truyện mới</a>
    <a class="nav-link" href="${ctx}/recommend">Đề xuất</a>
    <a class="nav-link" href="${ctx}/ai.jsp">🤖 AI Hub</a>
    <a class="nav-link" href="${ctx}/favorites">Yêu thích</a>
    <c:if test="${not empty sessionScope.user}">
      <a class="nav-link" href="${ctx}/book/upload" style="background: linear-gradient(45deg, #ff6b6b, #ff8e8e); color: white; padding: 6px 12px; border-radius: 15px; font-weight: 600;">📚 Upload Sách</a>
    </c:if>
    <a class="premium-badge" href="${ctx}/premium">Premium</a>
  </nav>
  <div class="header-actions">
    <form class="search-container" role="search" aria-label="Tìm kiếm truyện">
      <input id="searchInput" type="search" placeholder="Tìm truyện, tác giả…" autocomplete="off"
        aria-haspopup="listbox" aria-controls="suggestList" />
      <ul class="search-suggest" id="suggestList" role="listbox"></ul>
    </form>
    <button id="toggleTheme" aria-label="Chuyển dark/light mode">
      <span id="themeIcon">🌙</span>
    </button>
    <div class="account-dropdown" id="accountDropdown">
      <button class="account-btn" id="accountBtn" aria-haspopup="true" aria-expanded="false">
        <span id="accountIcon">👤</span>
        <span id="accountName">
          <c:choose>
            <c:when test="${not empty sessionScope.user}">
              <c:out value="${sessionScope.user.username}" />
              <span class="header-coin-display" style="margin-left: 8px;">
                💰 <c:out value="${sessionScope.userCoins != null ? sessionScope.userCoins : 0}" />
              </span>
            </c:when>
            <c:otherwise>
              Tài khoản
            </c:otherwise>
          </c:choose>
        </span>
        ▼
      </button>
      <ul class="account-menu" id="accountMenu" role="menu">
        <c:choose>
          <c:when test="${not empty sessionScope.user}">
            <c:choose>
              <c:when test="${sessionScope.user.role == 'admin'}">
                <li><a href="${ctx}/admin/dashboard" role="menuitem">Trang quản trị</a></li>
              </c:when>
              <c:otherwise>
                <li><a href="${ctx}/profile" role="menuitem">Trang cá nhân</a></li>
              </c:otherwise>
            </c:choose>
            <li><a href="${ctx}/logout" role="menuitem">Đăng xuất</a></li>
          </c:when>
          <c:otherwise>
            <li><a href="${ctx}/login" role="menuitem">Đăng nhập</a></li>
            <li><a href="${ctx}/register" role="menuitem">Đăng ký</a></li>
          </c:otherwise>
        </c:choose>
      </ul>
    </div>
    <button class="hamburger" id="hamburgerBtn" aria-label="Mở menu di động">&#9776;</button>
  </div>
</header>
