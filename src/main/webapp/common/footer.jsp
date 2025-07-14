<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<script src="${ctx}/assets/js/app.js" defer></script>

<!-- FOOTER (dark, 2 columns, banner, info, social) -->
<div class="footer-scroll-dark">
  <div class="footer-container">
    <div class="footer-col footer-col-left">
      <div class="footer-banner">
        <span class="footer-banner-icon">
          <img src="https://recmiennam.com/wp-content/uploads/2018/08/anh-la-co-viet-nam-dep-1.png" alt="Cờ Việt Nam" />
        </span>
        Hoàng Sa & Trường Sa là của Việt Nam!
      </div>
      <div class="footer-section-title">Giới thiệu</div>
      <div class="footer-desc">
        Scroll – Nền tảng đọc truyện online <b>miễn phí</b>, cập nhật nhanh hàng <b>nghìn truyện hot mỗi ngày</b>, mang đến không gian giải trí lý tưởng cho những ai yêu thích đọc truyện trên nền tảng số.<br><br>
        Người chịu trách nhiệm nội dung: <b>Scroll Team</b><br>
        Website đang trong thời gian thử nghiệm.<br>
        Liên hệ: <a href="mailto:scrollteam@gmail.com">scrollteam@gmail.com</a>
      </div>
      <div class="footer-social-row">
        <a href="https://www.facebook.com/KeyT99999/" target="_blank" aria-label="Facebook">
          <img src="https://tse3.mm.bing.net/th/id/OIP.YPyyLwHHKcK0SnRuibzzcwHaHa?rs=1&pid=ImgDetMain&o=7&rm=3" alt="Facebook" />
        </a>
        <a href="https://zalo.me/0868899104" target="_blank" aria-label="Zalo">
          <img src="https://tse4.mm.bing.net/th/id/OIP.Pw2ayYokefN7Q8ifxnd39wHaHa?rs=1&pid=ImgDetMain&o=7&rm=3" alt="Zalo" />
        </a>
        <a href="https://www.instagram.com/taphoakeyt/" target="_blank" aria-label="Instagram">
          <img src="https://tse4.mm.bing.net/th/id/OIP.8DnCrbL1lmWDH4VRKQqtegHaHa?rs=1&pid=ImgDetMain&o=7&rm=3" alt="Instagram" />
        </a>
      </div>
    </div>
    <div class="footer-col footer-col-right">
      <div class="footer-section-title">Thông tin</div>
      <ul class="footer-links">
        <li><a href="${ctx}/chinh-sach-bao-mat">Chính sách bảo mật</a></li>
        <li><a href="${ctx}/quy-che-hoat-dong">Quy chế hoạt động</a></li>
        <li><a href="${ctx}/gioi-thieu">Giới thiệu</a></li>
        <li><a href="${ctx}/lien-he">Liên hệ</a></li>
      </ul>
    </div>
  </div>
</div>

<style>
.footer-scroll-dark {
  width: 100%;
  background: #393939;
  color: #fff;
  border-top: 2px solid #eee;
  font-family: 'Inter', 'Segoe UI', Arial, sans-serif;
  margin-top: 48px;
}
.footer-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 36px 4vw 18px 4vw;
  display: flex;
  flex-direction: row;
  gap: 48px;
  justify-content: space-between;
}
.footer-col {
  flex: 1 1 0;
  min-width: 220px;
}
.footer-col-left {
  max-width: 600px;
}
.footer-banner {
  display: inline-block;
  background: #a7261a;
  color: #fff;
  font-weight: 600;
  border-radius: 22px;
  padding: 6px 22px 6px 14px;
  font-size: 1.08em;
  margin-bottom: 18px;
  margin-top: 0;
  box-shadow: 0 2px 8px 0 rgba(167,38,26,0.10);
}
.footer-banner-icon {
  display: inline-block;
  vertical-align: middle;
  margin-right: 9px;
}
.footer-banner-icon img {
  width: 1.7em;
  height: 1.7em;
  object-fit: cover;
  border-radius: 50%;
  border: 2px solid #fff;
  box-shadow: 0 2px 6px 0 rgba(0,0,0,0.13);
  background: #fff;
  display: inline-block;
  vertical-align: middle;
}
.footer-section-title {
  font-size: 1.25em;
  font-weight: 700;
  margin: 18px 0 10px 0;
  letter-spacing: 0.01em;
}
.footer-desc {
  font-size: 1.07em;
  color: #f3f3f3;
  margin-bottom: 18px;
  line-height: 1.7;
}
.footer-desc a {
  color: #ffd166;
  text-decoration: underline;
}
.footer-social-row {
  margin-top: 10px;
  display: flex;
  gap: 18px;
  align-items: center;
}
.footer-social-row a {
  display: inline-block;
  border-radius: 50%;
  background: #222;
  box-shadow: 0 2px 8px 0 rgba(0,0,0,0.13);
  transition: transform 0.18s, box-shadow 0.18s;
}
.footer-social-row img {
  height: 2.3em;
  width: 2.3em;
  object-fit: cover;
  border-radius: 50%;
  background: #fff;
  transition: transform 0.18s, box-shadow 0.18s;
}
.footer-social-row a:hover img {
  transform: scale(1.15) rotate(-6deg);
  box-shadow: 0 4px 18px 0 #b39ddb;
}
.footer-col-right {
  max-width: 320px;
  padding-left: 18px;
}
.footer-links {
  list-style: none;
  padding: 0;
  margin: 0;
}
.footer-links li {
  margin-bottom: 13px;
}
.footer-links a {
  color: #fff;
  text-decoration: none;
  font-size: 1.07em;
  transition: color 0.18s;
}
.footer-links a:hover {
  color: #ffd166;
  text-decoration: underline;
}
@media (max-width: 900px) {
  .footer-container {
    flex-direction: column;
    gap: 0;
    padding: 28px 3vw 12px 3vw;
  }
  .footer-col-right {
    padding-left: 0;
    margin-top: 32px;
  }
}
@media (max-width: 600px) {
  .footer-container {
    padding: 18px 1vw 8px 1vw;
  }
  .footer-banner {
    font-size: 0.98em;
    padding: 5px 12px 5px 8px;
  }
  .footer-section-title {
    font-size: 1.08em;
  }
  .footer-desc {
    font-size: 0.97em;
  }
  .footer-social-row img {
    height: 1.7em;
    width: 1.7em;
  }
}
</style>
