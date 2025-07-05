<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Admin - Book Management</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        :root {
            --primary: #6366f1;
            --secondary: #10b981;
            --accent: #f59e0b;
            --danger: #ef4444;
            --success: #22c55e;
            --info: #3b82f6;
            --bg-primary: #fff;
            --bg-secondary: #f9fafb;
            --bg-tertiary: #f3f4f6;
            --text-primary: #1f2937;
            --text-secondary: #6b7280;
            --text-muted: #9ca3af;
            --border: #e5e7eb;
            --radius: 14px;
        }
        * { box-sizing: border-box; }
        body {
            font-family: 'Inter', Arial, sans-serif;
            background: var(--bg-secondary);
            color: var(--text-primary);
            margin: 0;
        }
        .layout { display: flex; min-height: 100vh; }
        .sidebar {
            width: 260px;
            background: var(--bg-primary);
            border-right: 1px solid var(--border);
            padding: 1.5rem 0;
            position: fixed;
            height: 100vh;
            overflow-y: auto;
        }
        .sidebar .logo {
            display: flex; align-items: center; gap: 0.7rem;
            font-size: 1.3rem; font-weight: 700; color: var(--primary);
            padding: 0 1.5rem 1.5rem; border-bottom: 1px solid var(--border);
            margin-bottom: 1rem;
        }
        .sidebar nav ul { list-style: none; padding: 0 1rem; }
        .sidebar nav li { margin-bottom: 0.2rem; }
        .sidebar nav .nav-link {
            display: flex; align-items: center; gap: 0.7rem;
            padding: 0.7rem 1rem; color: var(--text-secondary);
            text-decoration: none; border-radius: 10px; font-weight: 500;
            transition: all 0.2s;
        }
        .sidebar nav .nav-link:hover, .sidebar nav .nav-link.active {
            background: var(--bg-tertiary); color: var(--primary);
        }
        .main {
            flex: 1; margin-left: 260px; min-height: 100vh; display: flex; flex-direction: column;
        }
        .navbar {
            background: var(--bg-primary); border-bottom: 1px solid var(--border);
            padding: 1rem 2rem; display: flex; align-items: center; justify-content: space-between;
            position: sticky; top: 0; z-index: 40;
        }
        .content { padding: 2rem; flex: 1; }
        .page-header {
            display: flex; align-items: center; justify-content: space-between;
            margin-bottom: 2rem;
        }
        .page-title { font-size: 1.8rem; font-weight: 700; color: var(--text-primary); }
        .btn {
            display: inline-flex; align-items: center; gap: 0.5rem;
            padding: 0.7rem 1.2rem; border: none; border-radius: 8px;
            font-weight: 600; text-decoration: none; cursor: pointer;
            transition: all 0.2s;
        }
        .btn-primary {
            background: var(--primary); color: #fff;
        }
        .btn-primary:hover { background: #5a6fd8; }
        .btn-danger {
            background: var(--danger); color: #fff;
        }
        .btn-danger:hover { background: #dc2626; }
        .btn-success {
            background: var(--success); color: #fff;
        }
        .btn-success:hover { background: #16a34a; }
        .card {
            background: var(--bg-primary); border: 1px solid var(--border);
            border-radius: var(--radius); box-shadow: 0 2px 12px #6366f111;
        }
        .card-header {
            padding: 1.2rem; border-bottom: 1px solid var(--border);
            display: flex; align-items: center; justify-content: space-between;
        }
        .card-title { font-size: 1.1rem; font-weight: 600; color: var(--text-primary); }
        .table {
            width: 100%; border-collapse: collapse;
        }
        .table th, .table td {
            padding: 1rem; text-align: left; border-bottom: 1px solid var(--border);
        }
        .table th {
            background: var(--bg-tertiary); font-weight: 600; color: var(--text-secondary);
            font-size: 0.9rem;
        }
        .table td { color: var(--text-primary); }
        .book-cover {
            width: 50px; height: 70px; border-radius: 6px; object-fit: cover;
        }
        .status-badge {
            padding: 0.25rem 0.7rem; border-radius: 9999px; font-size: 0.75rem;
            font-weight: 500;
        }
        .status-active { background: #d1fae5; color: #065f46; }
        .status-inactive { background: #fee2e2; color: #991b1b; }
        .status-draft { background: #fef3c7; color: #92400e; }
        .premium-badge {
            background: #fbbf24; color: #92400e; padding: 0.25rem 0.5rem;
            border-radius: 4px; font-size: 0.7rem; font-weight: 600;
        }
        .pagination {
            display: flex; align-items: center; justify-content: center; gap: 0.5rem;
            margin-top: 2rem;
        }
        .pagination a, .pagination span {
            padding: 0.5rem 0.8rem; border: 1px solid var(--border);
            border-radius: 6px; text-decoration: none; color: var(--text-secondary);
            transition: all 0.2s;
        }
        .pagination a:hover, .pagination .current {
            background: var(--primary); color: #fff; border-color: var(--primary);
        }
        .alert {
            padding: 1rem; border-radius: 8px; margin-bottom: 1rem;
        }
        .alert-success {
            background: #d1fae5; color: #065f46; border: 1px solid #a7f3d0;
        }
        .alert-error {
            background: #fee2e2; color: #991b1b; border: 1px solid #fca5a5;
        }
    </style>
</head>
<body>
<div class="layout">
    <!-- Sidebar -->
    <aside class="sidebar">
        <div class="logo">
            <i class="fas fa-book-open"></i> EbookWebsite
        </div>
        <nav>
            <ul>
                <li><a href="../dashboard.jsp" class="nav-link"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>
                <li><a href="list.jsp" class="nav-link active"><i class="fas fa-book"></i> Books</a></li>
                <li><a href="../tag/list.jsp" class="nav-link"><i class="fas fa-tags"></i> Tags</a></li>
                <li><a href="#" class="nav-link"><i class="fas fa-users"></i> Users</a></li>
                <li><a href="#" class="nav-link"><i class="fas fa-comments"></i> Comments</a></li>
                <li><a href="#" class="nav-link"><i class="fas fa-chart-bar"></i> Analytics</a></li>
            </ul>
        </nav>
    </aside>

    <!-- Main Content -->
    <main class="main">
        <nav class="navbar">
            <div>
                <h2>Book Management</h2>
            </div>
            <div>
                <span>Welcome, Admin</span>
            </div>
        </nav>

        <div class="content">
            <!-- Alerts -->
            <c:if test="${not empty success}">
                <div class="alert alert-success">
                    <i class="fas fa-check-circle"></i> ${success}
                </div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert alert-error">
                    <i class="fas fa-exclamation-triangle"></i> ${error}
                </div>
            </c:if>

            <!-- Page Header -->
            <div class="page-header">
                <h1 class="page-title">Books (${totalBooks})</h1>
                <a href="new.jsp" class="btn btn-primary">
                    <i class="fas fa-plus"></i> Add New Book
                </a>
            </div>

            <!-- Books Table -->
            <div class="card">
                <div class="card-header">
                    <h3 class="card-title">All Books</h3>
                </div>
                <table class="table">
                    <thead>
                        <tr>
                            <th>Cover</th>
                            <th>Title</th>
                            <th>Status</th>
                            <th>Type</th>
                            <th>Views</th>
                            <th>Created</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="book" items="${books}">
                            <tr>
                                <td>
                                    <img src="${book.coverUrl}" alt="${book.title}" class="book-cover"
                                         onerror="this.src='https://via.placeholder.com/50x70?text=No+Cover'">
                                </td>
                                <td>
                                    <div>
                                        <strong>${book.title}</strong>
                                        <c:if test="${book.isPremium}">
                                            <span class="premium-badge">PREMIUM</span>
                                        </c:if>
                                    </div>
                                    <small style="color: var(--text-muted);">${book.language}</small>
                                </td>
                                <td>
                                    <span class="status-badge status-${book.status}">${book.status}</span>
                                </td>
                                <td>${book.releaseType}</td>
                                <td>${book.viewCount}</td>
                                <td>${book.createdAt}</td>
                                <td>
                                    <div style="display: flex; gap: 0.5rem;">
                                        <a href="view.jsp?id=${book.id}" class="btn btn-success" style="padding: 0.3rem 0.6rem;">
                                            <i class="fas fa-eye"></i>
                                        </a>
                                        <a href="edit.jsp?id=${book.id}" class="btn btn-primary" style="padding: 0.3rem 0.6rem;">
                                            <i class="fas fa-edit"></i>
                                        </a>
                                        <button onclick="deleteBook(${book.id})" class="btn btn-danger" style="padding: 0.3rem 0.6rem;">
                                            <i class="fas fa-trash"></i>
                                        </button>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <!-- Pagination -->
            <c:if test="${totalPages > 1}">
                <div class="pagination">
                    <c:if test="${currentPage > 1}">
                        <a href="?page=${currentPage - 1}"><i class="fas fa-chevron-left"></i></a>
                    </c:if>
                    
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <c:choose>
                            <c:when test="${i == currentPage}">
                                <span class="current">${i}</span>
                            </c:when>
                            <c:otherwise>
                                <a href="?page=${i}">${i}</a>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                    
                    <c:if test="${currentPage < totalPages}">
                        <a href="?page=${currentPage + 1}"><i class="fas fa-chevron-right"></i></a>
                    </c:if>
                </div>
            </c:if>
        </div>
    </main>
</div>

<script>
function deleteBook(bookId) {
    if (confirm('Are you sure you want to delete this book?')) {
        const form = document.createElement('form');
        form.method = 'POST';
        form.action = 'delete';
        
        const input = document.createElement('input');
        input.type = 'hidden';
        input.name = 'id';
        input.value = bookId;
        
        form.appendChild(input);
        document.body.appendChild(form);
        form.submit();
    }
}
</script>
</body>
</html> 