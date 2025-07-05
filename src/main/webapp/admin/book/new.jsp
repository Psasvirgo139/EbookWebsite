<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Admin - Add New Book</title>
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
        .card {
            background: var(--bg-primary); border: 1px solid var(--border);
            border-radius: var(--radius); box-shadow: 0 2px 12px #6366f111;
        }
        .card-header {
            padding: 1.2rem; border-bottom: 1px solid var(--border);
        }
        .card-title { font-size: 1.1rem; font-weight: 600; color: var(--text-primary); }
        .card-body { padding: 1.5rem; }
        .form-group {
            margin-bottom: 1.5rem;
        }
        .form-label {
            display: block; margin-bottom: 0.5rem; font-weight: 600;
            color: var(--text-primary);
        }
        .form-control {
            width: 100%; padding: 0.75rem; border: 1px solid var(--border);
            border-radius: 8px; font-size: 1rem; transition: border-color 0.2s;
        }
        .form-control:focus {
            outline: none; border-color: var(--primary); box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.1);
        }
        .form-textarea {
            min-height: 120px; resize: vertical;
        }
        .form-row {
            display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;
        }
        .form-check {
            display: flex; align-items: center; gap: 0.5rem; margin-top: 0.5rem;
        }
        .form-check input[type="checkbox"] {
            width: 18px; height: 18px; accent-color: var(--primary);
        }
        .alert {
            padding: 1rem; border-radius: 8px; margin-bottom: 1rem;
        }
        .alert-error {
            background: #fee2e2; color: #991b1b; border: 1px solid #fca5a5;
        }
        @media (max-width: 768px) {
            .form-row { grid-template-columns: 1fr; }
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
                <h2>Add New Book</h2>
            </div>
            <div>
                <span>Welcome, Admin</span>
            </div>
        </nav>

        <div class="content">
            <!-- Alerts -->
            <c:if test="${not empty error}">
                <div class="alert alert-error">
                    <i class="fas fa-exclamation-triangle"></i> ${error}
                </div>
            </c:if>

            <!-- Page Header -->
            <div class="page-header">
                <h1 class="page-title">Add New Book</h1>
                <a href="list.jsp" class="btn btn-secondary">
                    <i class="fas fa-arrow-left"></i> Back to List
                </a>
            </div>

            <!-- Book Form -->
            <div class="card">
                <div class="card-header">
                    <h3 class="card-title">Book Information</h3>
                </div>
                <div class="card-body">
                    <form action="create" method="post" enctype="multipart/form-data">
                        <div class="form-row">
                            <div class="form-group">
                                <label class="form-label">Title *</label>
                                <input type="text" name="title" class="form-control" required 
                                       placeholder="Enter book title" value="${param.title}">
                            </div>
                            <div class="form-group">
                                <label class="form-label">Language</label>
                                <select name="language" class="form-control">
                                    <option value="Vietnamese" ${param.language == 'Vietnamese' ? 'selected' : ''}>Vietnamese</option>
                                    <option value="English" ${param.language == 'English' ? 'selected' : ''}>English</option>
                                    <option value="Chinese" ${param.language == 'Chinese' ? 'selected' : ''}>Chinese</option>
                                    <option value="Korean" ${param.language == 'Korean' ? 'selected' : ''}>Korean</option>
                                    <option value="Japanese" ${param.language == 'Japanese' ? 'selected' : ''}>Japanese</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="form-label">Description</label>
                            <textarea name="description" class="form-control form-textarea" 
                                      placeholder="Enter book description">${param.description}</textarea>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label class="form-label">Release Type</label>
                                <select name="releaseType" class="form-control">
                                    <option value="completed" ${param.releaseType == 'completed' ? 'selected' : ''}>Completed</option>
                                    <option value="ongoing" ${param.releaseType == 'ongoing' ? 'selected' : ''}>Ongoing</option>
                                    <option value="hiatus" ${param.releaseType == 'hiatus' ? 'selected' : ''}>Hiatus</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label class="form-label">Status</label>
                                <select name="status" class="form-control">
                                    <option value="active" ${param.status == 'active' ? 'selected' : ''}>Active</option>
                                    <option value="draft" ${param.status == 'draft' ? 'selected' : ''}>Draft</option>
                                    <option value="inactive" ${param.status == 'inactive' ? 'selected' : ''}>Inactive</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label class="form-label">Visibility</label>
                                <select name="visibility" class="form-control">
                                    <option value="public" ${param.visibility == 'public' ? 'selected' : ''}>Public</option>
                                    <option value="private" ${param.visibility == 'private' ? 'selected' : ''}>Private</option>
                                    <option value="premium" ${param.visibility == 'premium' ? 'selected' : ''}>Premium Only</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label class="form-label">Cover URL</label>
                                <input type="url" name="coverUrl" class="form-control" 
                                       placeholder="https://example.com/cover.jpg" value="${param.coverUrl}">
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="form-check">
                                <input type="checkbox" name="isPremium" id="isPremium" 
                                       ${param.isPremium == 'on' ? 'checked' : ''}>
                                <label for="isPremium">Premium Book</label>
                            </div>
                        </div>

                        <div class="form-group" id="priceGroup" style="display: none;">
                            <label class="form-label">Price (VND)</label>
                            <input type="number" name="price" class="form-control" 
                                   placeholder="0.00" min="0" step="0.01" value="${param.price}">
                        </div>

                        <div class="form-group">
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-save"></i> Create Book
                            </button>
                            <a href="list.jsp" class="btn btn-secondary" style="margin-left: 1rem;">
                                <i class="fas fa-times"></i> Cancel
                            </a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </main>
</div>

<script>
document.getElementById('isPremium').addEventListener('change', function() {
    const priceGroup = document.getElementById('priceGroup');
    if (this.checked) {
        priceGroup.style.display = 'block';
    } else {
        priceGroup.style.display = 'none';
    }
});

// Show price group if premium is checked on page load
if (document.getElementById('isPremium').checked) {
    document.getElementById('priceGroup').style.display = 'block';
}
</script>
</body>
</html> 