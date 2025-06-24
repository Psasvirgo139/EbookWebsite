<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/common/header.jspf" %>

<div class="container py-4">
    <h2 class="mb-4">Danh sách sách</h2>
    <div class="row">
        <c:forEach var="book" items="${ebookList}">
            <div class="col-md-3 mb-4">
                <div class="card h-100">
                    <img src="${book.coverUrl}" class="card-img-top" alt="cover">
                    <div class="card-body">
                        <h5 class="card-title">${book.title}</h5>
                        <p class="card-text">${book.description}</p>
                        <a href="detail?id=${book.id}" class="btn btn-primary btn-sm">Xem chi tiết</a>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>

    <!-- Pagination -->
    <nav>
        <ul class="pagination justify-content-center">
            <c:forEach begin="1" end="${totalPages}" var="i">
                <li class="page-item ${i == currentPage ? 'active' : ''}">
                    <a class="page-link" href="home?page=${i}">${i}</a>
                </li>
            </c:forEach>
        </ul>
    </nav>
</div>

<%@ include file="/common/footer.jspf" %>