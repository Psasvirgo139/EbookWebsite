<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/common/header.jspf" %>

<div class="container py-4">
    <h2>${ebook.title}</h2>
    <h4>Chương ${currentChapter}: ${chapter.title}</h4>
    <hr>
    <p style="white-space: pre-line;">${chapter.content}</p>


    <div class="mt-4">
        <h5>Danh sách chương</h5>
        <ul class="pagination">
            <c:forEach var="ch" items="${chapters}">
                <li class="page-item ${ch.number == currentChapter ? 'active' : ''}">
                    <a class="page-link" href="read?id=${ebook.id}&chapter=${ch.number}">
                        Ch ${ch.number}
                    </a>
                </li>
            </c:forEach>

        </ul>
    </div>
</div>

<%@ include file="/common/footer.jspf" %>