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

/* Hamburger menu */
.hamburger-menu {
    display: flex;
    align-items: center;
    background: none;
    border: none;
    color: #fff;
    font-size: 1.5rem;
    cursor: pointer;
    padding: 8px;
    border-radius: 4px;
    transition: background 0.15s;
    margin-right: 16px;
}

.hamburger-menu:hover {
    background: rgba(255, 255, 255, 0.1);
}

.hamburger-icon {
    display: flex;
    flex-direction: column;
    gap: 3px;
}

.hamburger-line {
    width: 20px;
    height: 2px;
    background: #fff;
    border-radius: 1px;
    transition: all 0.3s ease;
}

@keyframes slideDown {
    from {
        opacity: 0;
        transform: translateY(-10px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
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
  margin-right: 16px;
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
  background: linear-gradient(135deg, #1e3c72, #2a5298);
  box-shadow: 0 2px 12px rgba(0,0,0,0.15);
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
  color: #fff;
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
  color: #fff;
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
  background: rgba(255, 255, 255, 0.1);
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


<header class="main-header">
  <div class="header-left">
    <button class="hamburger-menu" id="hamburgerMenu" aria-label="Mở menu">
      <div class="hamburger-icon">
        <div class="hamburger-line"></div>
        <div class="hamburger-line"></div>
        <div class="hamburger-line"></div>
      </div>
    </button>
    <a href="${ctx}/" class="logo">
      <span class="logo-icon">📚</span>
      <span class="logo-text"><span class="logo-highlight">Scroll</span></span>
    </a>
  </div>
  <div class="header-right">
    <form class="header-search-form" action="${ctx}/search" method="get" autocomplete="off">
      <input id="searchInput" name="keyword" type="search" class="header-search-input"
             placeholder="Tìm kiếm" aria-label="Tìm kiếm" />
      <button type="submit" class="header-search-btn" aria-label="Tìm kiếm">
        <span class="search-icon">🔍</span>
      </button>
      <button type="button" class="header-search-clear" aria-label="Xóa" style="display:none;">✕</button>
      <ul class="search-suggest" id="suggestList" role="listbox"></ul>
    </form>
    
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
  </div>
</header>

<!-- Sidebar menu -->
<div class="sidebar-menu" id="sidebarMenu" style="display: none;">
  <div class="sidebar-header">
    <h3>Menu</h3>
    <button class="sidebar-close" id="sidebarClose">✕</button>
  </div>
  <ul class="sidebar-nav">
    <li><a href="${ctx}/latest">Truyện mới</a></li>
    <li><a href="${ctx}/ai/recommendations">Đề xuất</a></li>
    <li><a href="${ctx}/ai/chat">AI Chat</a></li>
    <li><a href="${ctx}/favorites">Yêu thích</a></li>
    <li><a href="${ctx}/book/upload">Upload Sách</a></li>
    <li><a href="${ctx}/search?keyword=&genre=&author=&minChapters=0&sortBy=&status=all" style="color:#222;font-weight:500;">Search Nâng Cao</a></li>
    <li class="sidebar-divider"></li>
    <li class="sidebar-genres-dropdown">
      <button class="sidebar-genres-btn" id="sidebarGenresBtn">
        <span>Thể loại</span>
        <span class="sidebar-dropdown-arrow">▼</span>
      </button>
      <div class="sidebar-genres-mega" id="sidebarGenresMega">
        <div class="sidebar-genres-grid">
          <c:forEach var="tag" items="${tags}">
            <a href="${ctx}/search?genre=${tag.name}" class="sidebar-genre-item">
              <span class="sidebar-genre-icon">📖</span>
              <span class="sidebar-genre-name">${tag.name}</span>
            </a>
          </c:forEach>
        </div>
      </div>
    </li>
  </ul>
</div>

<style>
/* Sidebar styles */
.sidebar-menu {
  position: fixed;
  top: 0;
  left: 0;
  width: 300px;
  height: 100vh;
  background: #fff;
  box-shadow: 2px 0 16px rgba(34,34,68,0.18);
  z-index: 2000;
  transform: translateX(-100%);
  transition: transform 0.35s cubic-bezier(.77,0,.18,1);
  border-radius: 0 16px 16px 0;
  opacity: 0.98;
}

.sidebar-menu.show {
  transform: translateX(0);
}

.sidebar-backdrop {
  position: fixed;
  top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(30, 60, 114, 0.25);
  z-index: 1999;
  animation: fadeInBackdrop 0.3s;
}
@keyframes fadeInBackdrop {
  from { opacity: 0; }
  to { opacity: 1; }
}

.sidebar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  background: linear-gradient(135deg, #1e3c72, #2a5298);
  color: #fff;
}

.sidebar-header h3 {
  margin: 0;
  font-size: 1.2rem;
}

.sidebar-close {
  background: none;
  border: none;
  color: #fff;
  font-size: 1.5rem;
  cursor: pointer;
  padding: 4px;
}

.sidebar-nav {
  list-style: none;
  padding: 0;
  margin: 0;
}

.sidebar-nav li {
  border-bottom: 1px solid #eee;
}

.sidebar-nav li a {
  display: block;
  padding: 16px 20px;
  color: #444;
  text-decoration: none;
  transition: background 0.15s;
  font-weight: 500;
}

.sidebar-nav li a:hover {
  background: #f3f0ff;
  color: #5a3ec8;
}

.sidebar-divider {
  height: 1px;
  background: #eee;
  margin: 8px 0;
}

.sidebar-genres {
  padding: 16px 20px;
}

.sidebar-genres span {
  display: block;
  font-weight: 600;
  color: #5a3ec8;
  margin-bottom: 12px;
}

.sidebar-genres-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.sidebar-genres-list a {
  padding: 8px 12px;
  color: #666;
  text-decoration: none;
  border-radius: 4px;
  transition: background 0.15s;
}

/* Sidebar Genres Mega Menu */
.sidebar-genres-dropdown {
    position: relative;
}

.sidebar-genres-btn {
    width: 100%;
    background: none;
    border: none;
    color: #444;
    font-size: 1rem;
    font-weight: 500;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 16px 20px;
    cursor: pointer;
    transition: all 0.2s ease;
}

.sidebar-genres-btn:hover {
    background: #f3f0ff;
    color: #5a3ec8;
}

.sidebar-dropdown-arrow {
    font-size: 0.8rem;
    transition: transform 0.2s ease;
}

.sidebar-genres-mega {
    display: none;
    background: #fff;
    border-top: 1px solid #eee;
    overflow: hidden;
    animation: slideDown 0.3s ease;
    max-height: 400px;
    overflow-y: auto;
}

.sidebar-genres-mega.show {
    display: block;
}

/* Add a subtle shadow to indicate scrollable content */
.sidebar-genres-mega::after {
    content: '';
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    height: 20px;
    background: linear-gradient(transparent, rgba(255,255,255,0.8));
    pointer-events: none;
    opacity: 0;
    transition: opacity 0.3s ease;
}

.sidebar-genres-mega:hover::after {
    opacity: 1;
}



.sidebar-genres-grid {
    display: flex;
    flex-direction: column;
    gap: 0;
    padding: 0;
    max-height: 300px;
    overflow-y: auto;
}

.sidebar-genre-item {
    display: flex;
    align-items: center;
    padding: 10px 16px;
    color: #444;
    text-decoration: none;
    transition: all 0.2s ease;
    border-bottom: 1px solid #f5f5f5;
    font-size: 0.85rem;
    min-height: 44px;
}

.sidebar-genre-item:hover {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: #fff;
    transform: translateX(4px);
}

.sidebar-genre-icon {
    font-size: 0.9rem;
    margin-right: 10px;
    opacity: 0.8;
    min-width: 16px;
}

.sidebar-genre-name {
    font-weight: 500;
    font-size: 0.8rem;
    line-height: 1.3;
    flex: 1;
}

/* Scrollbar styling for genres list */
.sidebar-genres-grid::-webkit-scrollbar {
    width: 4px;
}

.sidebar-genres-grid::-webkit-scrollbar-track {
    background: #f1f1f1;
    border-radius: 2px;
}

.sidebar-genres-grid::-webkit-scrollbar-thumb {
    background: #c1c1c1;
    border-radius: 2px;
}

.sidebar-genres-grid::-webkit-scrollbar-thumb:hover {
    background: #a8a8a8;
}

/* Overlay */
.sidebar-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0,0,0,0.5);
  z-index: 1999;
  display: none;
}

.sidebar-overlay.show {
  display: block;
}
</style>

<script>
// Helper function
const $ = (sel, par = document) => par.querySelector(sel);

// Sidebar functionality
document.addEventListener('DOMContentLoaded', function() {
    const hamburgerMenu = document.getElementById('hamburgerMenu');
    const sidebarMenu = document.getElementById('sidebarMenu');
    const sidebarClose = document.getElementById('sidebarClose');

    // Create overlay
    const overlay = document.createElement('div');
    overlay.className = 'sidebar-overlay';
    document.body.appendChild(overlay);

    // Toggle sidebar
    hamburgerMenu.addEventListener('click', function() {
        sidebarMenu.style.display = 'block';
        setTimeout(() => {
            sidebarMenu.classList.add('show');
            overlay.classList.add('show');
        }, 10);
    });

    // Close sidebar
    function closeSidebar() {
        sidebarMenu.classList.remove('show');
        overlay.classList.remove('show');
        setTimeout(() => {
            sidebarMenu.style.display = 'none';
        }, 300);
    }

    sidebarClose.addEventListener('click', closeSidebar);
    overlay.addEventListener('click', closeSidebar);

    // Account dropdown - using old working code
    $('#accountBtn').addEventListener('click', e => {
        e.stopPropagation();
        $('#accountMenu').classList.toggle('show');
        $('#accountBtn').setAttribute('aria-expanded', $('#accountMenu').classList.contains('show'));
    });

    document.body.addEventListener('click', e => {
        if (!$('#accountDropdown').contains(e.target)) {
            $('#accountMenu').classList.remove('show');
        }
    });

    // Close dropdowns when pressing Escape
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape') {
            $('#accountMenu').classList.remove('show');
            $('#accountBtn').setAttribute('aria-expanded', 'false');
            closeSidebar();
        }
    });



    // Sidebar genres dropdown functionality
    const sidebarGenresBtn = document.getElementById('sidebarGenresBtn');
    const sidebarGenresMega = document.getElementById('sidebarGenresMega');
    const sidebarDropdownArrow = document.querySelector('.sidebar-dropdown-arrow');

    if (sidebarGenresBtn && sidebarGenresMega) {
        sidebarGenresBtn.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
            
            sidebarGenresMega.classList.toggle('show');
            sidebarDropdownArrow.style.transform = sidebarGenresMega.classList.contains('show') ? 'rotate(180deg)' : 'rotate(0deg)';
        });

        // Close when clicking outside
        document.addEventListener('click', function(e) {
            if (!sidebarGenresBtn.contains(e.target) && !sidebarGenresMega.contains(e.target)) {
                sidebarGenresMega.classList.remove('show');
                sidebarDropdownArrow.style.transform = 'rotate(0deg)';
            }
        });
    }
});
</script>
