<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.List" %>
<%@ page import="com.mycompany.ebookwebsite.model.Tag,com.mycompany.ebookwebsite.service.TagService" %>
<%
    List<Tag> tags = (List<Tag>) request.getAttribute("tags");
    if (tags == null) {
        TagService tagService = new TagService();
        tags = tagService.getAllTags();
        request.setAttribute("tags", tags);
    }
%>

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
.genres-menu .genres-list {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 8px 32px;
  min-width: 480px;
  width: max-content;
  padding: 16px 24px;
  margin: 0 auto;
}
.genres-menu .genres-list a {
  display: block;
  padding: 4px 0;
  color: #5a3ec8;
  font-weight: 500;
  text-decoration: none;
  border-radius: 4px;
  transition: background 0.15s;
}
.genres-menu .genres-list a:hover {
  background: #f3f0ff;
  color: #2d1582;
}
@media (max-width: 700px) {
  .genres-menu .genres-list {
    grid-template-columns: repeat(2, 1fr);
    min-width: 320px;
    max-width: 98vw;
  }
}
@media (max-width: 480px) {
  .genres-menu .genres-list {
    grid-template-columns: 1fr;
    min-width: 180px;
    max-width: 98vw;
  }
}
.header-search-form {
  display: flex;
  align-items: center;
  position: relative;
  background: #fff;
  border-radius: 999px;
  box-shadow: 0 2px 8px rgba(90,62,200,0.07);
  padding: 2px 8px 2px 16px;
  min-width: 280px;
  max-width: 340px;
  margin-left: 16px;
}
.header-search-input {
  border: none;
  outline: none;
  background: transparent;
  font-size: 1rem;
  padding: 8px 0;
  flex: 1;
  min-width: 0;
}
.header-search-input:focus {
  box-shadow: 0 0 0 2px #a389f4;
}
.header-search-btn {
  background: #5a3ec8;
  border: none;
  color: #fff;
  border-radius: 50%;
  width: 36px;
  height: 36px;
  margin-left: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background 0.18s;
  font-size: 1.2rem;
}
.header-search-btn:hover, .header-search-btn:focus {
  background: #7c5dfa;
}
.header-search-clear {
  position: absolute;
  right: 48px;
  background: none;
  border: none;
  color: #bbb;
  font-size: 1.1rem;
  cursor: pointer;
  padding: 0 4px;
  display: flex;
  align-items: center;
  height: 100%;
  transition: color 0.15s;
}
.header-search-clear:hover {
  color: #5a3ec8;
}
.search-suggest {
  position: absolute;
  top: 110%;
  left: 0;
  right: 0;
  z-index: 100;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(90,62,200,0.10);
  margin-top: 4px;
  padding: 0;
  list-style: none;
  max-height: 260px;
  overflow-y: auto;
  border: 1px solid #eee;
}
@media (max-width: 700px) {
  .header-search-form {
    min-width: 0;
    max-width: 98vw;
    margin-left: 0;
  }
}
.main-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #fff;
  box-shadow: 0 2px 12px rgba(90,62,200,0.07);
  padding: 0 32px;
  height: 68px;
  position: sticky;
  top: 0;
  z-index: 1000;
}
.header-left, .header-center, .header-right {
  display: flex;
  align-items: center;
}
.logo {
  display: flex;
  align-items: center;
  font-size: 1.5rem;
  font-weight: 700;
  color: #5a3ec8;
  text-decoration: none;
  margin-right: 32px;
}
.logo-icon { font-size: 2rem; margin-right: 6px; }
.logo-highlight { color: #ff6b6b; }
.main-nav .nav-link {
  margin: 0 10px;
  color: #444;
  font-weight: 500;
  text-decoration: none;
  transition: color 0.18s;
  position: relative;
}
.main-nav .nav-link.active, .main-nav .nav-link:hover {
  color: #5a3ec8;
}
.upload-btn {
  background: linear-gradient(45deg, #ff6b6b, #ff8e8e);
  color: #fff;
  padding: 8px 18px;
  border-radius: 999px;
  font-weight: 600;
  margin-right: 18px;
  text-decoration: none;
  box-shadow: 0 2px 8px rgba(255,107,107,0.08);
  transition: background 0.18s;
}
.upload-btn:hover { background: linear-gradient(45deg, #ff8e8e, #ff6b6b); }
.header-coin-display {
  background: linear-gradient(45deg, #ffd700, #ffed4e);
  color: #333;
  padding: 4px 12px;
  border-radius: 16px;
  font-size: 1rem;
  font-weight: 600;
  border: 1px solid #e6c200;
  margin-right: 12px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.07);
}
.account-dropdown { position: relative; }
.account-btn {
  background: none;
  border: none;
  color: #444;
  font-size: 1rem;
  font-weight: 500;
  display: flex;
  align-items: center;
  cursor: pointer;
  border-radius: 999px;
  padding: 6px 12px;
  transition: background 0.15s;
}
.account-btn:hover, .account-btn:focus {
  background: #f3f0ff;
}
.account-menu {
  display: none;
  position: absolute;
  right: 0;
  top: 110%;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(90,62,200,0.10);
  min-width: 160px;
  padding: 8px 0;
  z-index: 100;
  list-style: none;
  border: 1px solid #eee;
}
.account-menu.show {
  display: block;
}
.account-menu li a {
  display: block;
  padding: 10px 18px;
  color: #444;
  text-decoration: none;
  border-radius: 6px;
  transition: background 0.15s;
}
.account-menu li a:hover {
  background: #f3f0ff;
  color: #5a3ec8;
}
.hamburger {
  display: none;
  background: none;
  border: none;
  font-size: 2rem;
  margin-left: 12px;
  cursor: pointer;
  color: #5a3ec8;
}
@media (max-width: 1200px) {
  .main-header { padding: 0 8px; }
  .logo { margin-right: 12px; }
}
@media (max-width: 900px) {
  .main-header { padding: 0 4px; }
  .main-nav { display: none; }
  .hamburger { display: block; }
  .header-center { flex: 1; }
}
</style>

<a href="#main" class="skip-link">Bỏ qua và tới nội dung chính</a>
<header class="main-header">
  <div class="header-left">
    <a href="${ctx}/" class="logo">
      <span class="logo-icon">📚</span>
      <span class="logo-text"><span class="logo-highlight">Scroll</span></span>
    </a>
    <nav class="main-nav" id="mainNav" aria-label="Điều hướng chính">
      <a class="nav-link" href="${ctx}/">Trang chủ</a>
      <div class="nav-item genres-dropdown">
        <a class="nav-link" href="#">📚 Thể loại ▾</a>
        <div class="mega-menu genres-menu" aria-label="Danh sách thể loại">
          <div class="genres-list">
            <c:forEach var="tag" items="${tags}">
              <a href="${ctx}/search?genre=${tag.name}">${tag.name}</a>
            </c:forEach>
          </div>
        </div>
      </div>
      <a class="nav-link" href="${ctx}/latest">Truyện mới</a>
      <a class="nav-link" href="${ctx}/ai/recommendations">Đề xuất</a>
      <a class="nav-link" href="${ctx}/ai/chat">💬 AI Chat</a>
      <a class="nav-link" href="${ctx}/favorites">Yêu thích</a>
    </nav>
  </div>
  <div class="header-center">
    <form class="header-search-form" action="${ctx}/search" method="get" autocomplete="off">
      <input id="searchInput" name="keyword" type="search" class="header-search-input"
             placeholder="Tìm truyện" aria-label="Tìm truyện" />
      <button type="submit" class="header-search-btn" aria-label="Tìm kiếm">
        <span class="search-icon">🔍</span>
      </button>
      <button type="button" class="header-search-clear" aria-label="Xóa" style="display:none;">✕</button>
      <ul class="search-suggest" id="suggestList" role="listbox"></ul>
    </form>
  </div>
  <div class="header-right">
    <a class="upload-btn" href="${ctx}/book/upload">📚 Upload Sách</a>
    <span class="header-coin-display" title="Số dư coin">💰 <c:out value="${sessionScope.userCoins != null ? sessionScope.userCoins : 0}" /></span>
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
