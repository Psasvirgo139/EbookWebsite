<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!-- Nếu muốn load JS ở đây, có thể đặt cuối file (đúng best practice) -->
<script src="${ctx}/assets/js/app.js" defer></script>

<!-- FOOTER (hiện đại – glass) -->
<div class="footer-flex-scroll">
  <div class="footer-left">
    <p>
      <strong>SCROLL</strong> – Nền tảng <a href="${ctx}/index.jsp">đọc truyện online</a> miễn phí, cập nhật nhanh hàng nghìn truyện hot: ngôn tình, tiên hiệp, kiếm hiệp, đam mỹ, light novel, truyện dịch chất lượng cao. Tối ưu cho mọi thiết bị: từ điện thoại đến máy tính bảng.
    </p>
    <p class="footer-note">
      <em>
        Toàn bộ truyện được sưu tầm từ cộng đồng, blog, diễn đàn truyện lớn. Nếu bạn là tác giả hoặc chủ sở hữu bản quyền, vui lòng liên hệ với chúng tôi qua email: <strong>scrollteam@gmail.com</strong>.
      </em>
    </p>
  </div>
  <div class="footer-right">
    <div class="tags-wrap">
      <button class="tagv2 tag-hot">Truyện Hot</button>
      <button class="tagv2">Ngôn Tình Ngắn</button>
      <button class="tagv2">Ngôn Tình Hay</button>
      <button class="tagv2">Ngôn Tình 18+</button>
      <button class="tagv2">Ngôn Tình Hoàn</button>
      <button class="tagv2">Ngôn Tình Ngược</button>
      <button class="tagv2">Ngôn Tình Sủng</button>
      <button class="tagv2">Ngôn Tình Hài</button>
    </div>
  </div>
</div>
