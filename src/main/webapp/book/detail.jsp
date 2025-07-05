<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ include file="/common/header.jspf" %>

<div class="book-container">
  <div class="main-content">

    <!-- THÔNG TIN CHÍNH -->
    <div class="book-main">
      <div class="book-left">
        <img src="${ebook.coverUrl}" alt="Bìa sách" class="book-cover-img" />
      </div>

      <div class="book-right">
        <h1 class="book-title">${ebook.title}</h1>
        <div class="book-author">
          Tác giả:
          <c:forEach var="author" items="${authors}" varStatus="s">
            <span>${author.name}</span><c:if test="${!s.last}">, </c:if>
          </c:forEach>
        </div>

        <div class="book-meta">
          <div>
            Thể loại:
            <c:forEach var="tag" items="${ebook.tags}">
              <a href="${pageContext.request.contextPath}/book/list?tag=${tag.id}" class="tag-btn">${tag.name}</a>
            </c:forEach>
          </div>
          <div>Ngôn ngữ: <c:out value="${ebook.language}" /></div>
        </div>

        <div class="book-rating">
          <span class="star">⭐</span>
          <span class="avg-rating">${rating}</span>/5
          <span class="rating-count">(${ebook.reviewCount} đánh giá)</span>
        </div>

        <div class="book-actions">
          <a class="btn btn-primary" href="#">Thêm vào giỏ hàng</a>
          <a class="btn btn-outline" href="#">Tải xuống</a>
          <a class="btn btn-outline" href="#">Yêu thích</a>
        </div>

        <div class="book-chapters">
          <h3>Đọc thử / Xem trước</h3>
          <ul>
            <c:forEach var="chapter" items="${chapters}">
              <li>
                <a href="${pageContext.request.contextPath}/book/read?bookId=${ebook.id}&chapterId=${chapter.id}">
                  ${chapter.title}
                </a>
              </li>
            </c:forEach>
          </ul>
        </div>
      </div>
    </div>

    <!-- GIỚI THIỆU -->
    <div class="book-description">
      <h2>Giới thiệu nội dung</h2>
      <p>${ebook.description}</p>

      <c:if test="${not empty chapters}">
        <h3>Mục lục</h3>
        <ul>
          <c:forEach var="chapter" items="${chapters}">
            <li>${chapter.title}</li>
          </c:forEach>
        </ul>
      </c:if>
    </div>

    <!-- ĐÁNH GIÁ -->
    <div class="book-review">
      <h2>Đánh giá & Bình luận</h2>
      <div class="review-summary">
        <span class="rating-count">(${ebook.reviewCount} đánh giá)</span>
      </div>

      <form class="review-form" action="#" method="post">
        <label>Đánh giá của bạn:</label>
        <select name="rating">
          <c:forEach var="i" begin="1" end="5">
            <option value="${i}">${i} sao</option>
          </c:forEach>
        </select>
        <textarea name="comment" placeholder="Nhận xét của bạn..."></textarea>
        <button type="submit" class="btn btn-primary">Gửi đánh giá</button>
      </form>

      <div class="comment-list">
        <c:forEach var="comment" items="${comments}">
          <div class="comment-item">
            <img class="comment-avatar" src="https://cdn-icons-png.flaticon.com/512/149/149071.png" alt="avatar" />
            <div class="comment-content">
              <span class="comment-user">${comment.username}</span>
              <span class="comment-date">${comment.createdAt}</span>
              <div class="comment-text">${comment.content}</div>
            </div>
          </div>
        </c:forEach>
      </div>
    </div>

    <!-- LIÊN QUAN -->
    <div class="related-books">
      <h2>Sách liên quan</h2>
      <div class="related-grid">
        <c:forEach var="related" items="${relatedBooks}">
          <div class="related-card">
            <img src="${related.coverUrl}" alt="${related.title}" class="related-img" />
            <div class="related-info">
              <div class="related-title">${related.title}</div>
              <div class="related-rating">⭐ ${related.rating}/5</div>
            </div>
          </div>
        </c:forEach>
      </div>
    </div>

  </div> <!-- /.main-content -->
</div> <!-- /.book-container -->

<%@ include file="/common/footer.jspf" %>
