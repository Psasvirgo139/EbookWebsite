<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Admin - View Book</title>
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
        .btn-secondary {
            background: var(--text-secondary); color: #fff;
        }
        .btn-secondary:hover { background: #4b5563; }
        .btn-danger {
            background: var(--danger); color: #fff;
        }
        .btn-danger:hover { background: #dc2626; }
        .card {
            background: var(--bg-primary); border: 1px solid var(--border);
            border-radius: var(--radius); box-shadow: 0 2px 12px #6366f111;
            margin-bottom: 1.5rem;
        }
        .card-header {
            padding: 1.2rem; border-bottom: 1px solid var(--border);
            display: flex; align-items: center; justify-content: space-between;
        }
        .card-title { font-size: 1.1rem; font-weight: 600; color: var(--text-primary); }
        .card-body { padding: 1.5rem; }
        .book-header {
            display: flex; gap: 2rem; margin-bottom: 2rem;
        }
        .book-cover {
            width: 200px; height: 280px; border-radius: 12px; object-fit: cover;
            box-shadow: 0 4px 20px rgba(0,0,0,0.1);
        }
        .book-info h1 { margin: 0 0 1rem 0; font-size: 2rem; }
        .book-meta {
            display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; margin-bottom: 1rem;
        }
        .meta-item {
            display: flex; align-items: center; gap: 0.5rem;
        }
        .meta-label {
            font-weight: 600; color: var(--text-secondary); min-width: 100px;
        }
        .meta-value { color: var(--text-primary); }
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
        .description {
            line-height: 1.6; color: var(--text-secondary);
        }
        .stats-grid {
            display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 1rem; margin-top: 1.5rem;
        }
        .stat-card {
            background: var(--bg-tertiary); padding: 1rem; border-radius: 8px;
            text-align: center;
        }
        .stat-number {
            font-size: 1.5rem; font-weight: 700; color: var(--primary);
        }
        .stat-label {
            font-size: 0.9rem; color: var(--text-secondary); margin-top: 0.25rem;
        }
        @media (max-width: 768px) {
            .book-header { flex-direction: column; }
            .book-meta { grid-template-columns: 1fr; }
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
                <li><a href="list.jsp" class="nav-link"><i class="fas fa-book"></i> Books</a></li>
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
                <h2>Book Details</h2>
            </div>
            <div>
                <span>Welcome, Admin</span>
            </div>
        </nav>

        <div class="content">
            <!-- Page Header -->
            <div class="page-header">
                <h1 class="page-title">Book Details</h1>
                <div>
                    <a href="edit.jsp?id=${book.id}" class="btn btn-primary">
                        <i class="fas fa-edit"></i> Edit
                    </a>
                    <a href="list.jsp" class="btn btn-secondary" style="margin-left: 0.5rem;">
                        <i class="fas fa-arrow-left"></i> Back
                    </a>
                </div>
            </div>

            <!-- Book Information -->
            <div class="card">
                <div class="card-header">
                    <h3 class="card-title">Book Information</h3>
                    <span class="status-badge status-${book.status}">${book.status}</span>
                </div>
                <div class="card-body">
                    <div class="book-header">
                        <img src="${book.coverUrl}" alt="${book.title}" class="book-cover"
                             onerror="this.src='https://via.placeholder.com/200x280?text=No+Cover'">
                        <div class="book-info">
                            <h1>${book.title}</h1>
                            <c:if test="${book.isPremium}">
                                <span class="premium-badge">PREMIUM</span>
                            </c:if>
                            
                            <div class="book-meta">
                                <div class="meta-item">
                                    <span class="meta-label">ID:</span>
                                    <span class="meta-value">${book.id}</span>
                                </div>
                                <div class="meta-item">
                                    <span class="meta-label">Language:</span>
                                    <span class="meta-value">${book.language}</span>
                                </div>
                                <div class="meta-item">
                                    <span class="meta-label">Release Type:</span>
                                    <span class="meta-value">${book.releaseType}</span>
                                </div>
                                <div class="meta-item">
                                    <span class="meta-label">Visibility:</span>
                                    <span class="meta-value">${book.visibility}</span>
                                </div>
                                <div class="meta-item">
                                    <span class="meta-label">Uploader ID:</span>
                                    <span class="meta-value">${book.uploaderId}</span>
                                </div>
                                <div class="meta-item">
                                    <span class="meta-label">Created:</span>
                                    <span class="meta-value">${book.createdAt}</span>
                                </div>
                                <c:if test="${book.isPremium}">
                                    <div class="meta-item">
                                        <span class="meta-label">Price:</span>
                                        <span class="meta-value">${book.price} VND</span>
                                    </div>
                                </c:if>
                            </div>
                            
                            <div class="description">
                                <strong>Description:</strong><br>
                                ${book.description != null ? book.description : 'No description available.'}
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Statistics -->
            <div class="card">
                <div class="card-header">
                    <h3 class="card-title">Statistics</h3>
                </div>
                <div class="card-body">
                    <div class="stats-grid">
                        <div class="stat-card">
                            <div class="stat-number">${book.viewCount}</div>
                            <div class="stat-label">Total Views</div>
                        </div>
                        <div class="stat-card">
                            <div class="stat-number">0</div>
                            <div class="stat-label">Total Chapters</div>
                        </div>
                        <div class="stat-card">
                            <div class="stat-number">0</div>
                            <div class="stat-label">Total Comments</div>
                        </div>
                        <div class="stat-card">
                            <div class="stat-number">0</div>
                            <div class="stat-label">Total Favorites</div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Actions -->
            <div class="card">
                <div class="card-header">
                    <h3 class="card-title">Actions</h3>
                </div>
                <div class="card-body">
                    <div style="display: flex; gap: 1rem; flex-wrap: wrap;">
                        <a href="../book/detail.jsp?id=${book.id}" class="btn btn-primary" target="_blank">
                            <i class="fas fa-eye"></i> View Public Page
                        </a>
                        <a href="../book/read.jsp?bookId=${book.id}" class="btn btn-secondary" target="_blank">
                            <i class="fas fa-book-open"></i> Read Book
                        </a>
                        <button onclick="deleteBook(${book.id})" class="btn btn-danger">
                            <i class="fas fa-trash"></i> Delete Book
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </main>
</div>

<script>
function deleteBook(bookId) {
    if (confirm('Are you sure you want to delete this book? This action cannot be undone.')) {
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