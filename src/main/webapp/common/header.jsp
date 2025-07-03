<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<link rel="stylesheet" href="${ctx}/assets/css/style.css" />

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
    <a class="nav-link" href="${ctx}/favorites">Yêu thích</a>
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
