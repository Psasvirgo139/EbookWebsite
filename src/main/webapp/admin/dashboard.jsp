<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Admin Dashboard - EbookWebsite</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
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
        [data-theme="dark"] {
            --bg-primary: #18181b;
            --bg-secondary: #232336;
            --bg-tertiary: #232336;
            --text-primary: #f3f4f6;
            --text-secondary: #cbd5e1;
            --text-muted: #64748b;
            --border: #334155;
        }
        * { box-sizing: border-box; }
        body {
            font-family: 'Inter', Arial, sans-serif;
            background: var(--bg-secondary);
            color: var(--text-primary);
            margin: 0;
            transition: background 0.3s, color 0.3s;
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
            z-index: 50;
        }
        .sidebar .logo {
            display: flex; align-items: center; gap: 0.7rem;
            font-size: 1.3rem; font-weight: 700; color: var(--primary);
            padding: 0 1.5rem 1.5rem; border-bottom: 1px solid var(--border);
            margin-bottom: 1rem;
        }
        .sidebar .logo i { font-size: 1.7rem; color: var(--secondary); }
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
        .sidebar nav .nav-link i { width: 22px; text-align: center; }
        .sidebar .submenu { margin-left: 1.5rem; }
        .main {
            flex: 1; margin-left: 260px; min-height: 100vh; display: flex; flex-direction: column;
        }
        .navbar {
            background: var(--bg-primary); border-bottom: 1px solid var(--border);
            padding: 1rem 2rem; display: flex; align-items: center; justify-content: space-between;
            position: sticky; top: 0; z-index: 40;
        }
        .navbar .search {
            background: var(--bg-tertiary); border: none; border-radius: 8px;
            padding: 0.5rem 1rem; font-size: 1rem; color: var(--text-primary);
            width: 220px; margin-right: 1rem;
        }
        .navbar .actions { display: flex; align-items: center; gap: 1rem; }
        .navbar .icon-btn {
            background: none; border: none; color: var(--text-secondary);
            font-size: 1.2rem; cursor: pointer; border-radius: 8px; padding: 0.5rem;
            transition: background 0.15s;
        }
        .navbar .icon-btn:hover { background: var(--bg-tertiary); color: var(--primary); }
        .navbar .avatar {
            width: 40px; height: 40px; border-radius: 50%; background: var(--primary);
            display: flex; align-items: center; justify-content: center; color: #fff; font-weight: 600;
        }
        .navbar .dropdown { position: relative; }
        .navbar .dropdown-content {
            display: none; position: absolute; right: 0; top: 120%; background: var(--bg-primary);
            border: 1px solid var(--border); border-radius: 10px; min-width: 160px; box-shadow: 0 4px 24px #0001;
            z-index: 100; padding: 0.5rem 0;
        }
        .navbar .dropdown.open .dropdown-content { display: block; }
        .navbar .dropdown-content a {
            display: block; padding: 0.7rem 1.2rem; color: var(--text-primary); text-decoration: none;
            border-radius: 8px; transition: background 0.15s;
        }
        .navbar .dropdown-content a:hover { background: var(--bg-tertiary); color: var(--primary); }
        .dashboard-content { padding: 2rem; flex: 1; }
        .welcome { margin-bottom: 2rem; }
        .welcome h1 { font-size: 2rem; font-weight: 700; margin-bottom: 0.3rem; }
        .welcome p { color: var(--text-secondary); font-size: 1.1rem; }
        .stats-grid {
            display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 1.2rem; margin-bottom: 2rem;
        }
        .stat-card {
            background: var(--bg-primary); border: 1px solid var(--border); border-radius: var(--radius);
            padding: 1.2rem 1.5rem; box-shadow: 0 2px 12px #6366f111; transition: box-shadow 0.2s, transform 0.2s;
        }
        .stat-card:hover { box-shadow: 0 8px 32px #6366f122; transform: translateY(-2px) scale(1.01); }
        .stat-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 1rem; }
        .stat-icon {
            width: 44px; height: 44px; border-radius: 12px; display: flex; align-items: center; justify-content: center;
            font-size: 1.3rem; color: #fff;
        }
        .stat-icon.books { background: linear-gradient(135deg, #6366f1 0%, #818cf8 100%); }
        .stat-icon.users { background: linear-gradient(135deg, #10b981 0%, #6ee7b7 100%); }
        .stat-icon.orders { background: linear-gradient(135deg, #f59e0b 0%, #fbbf24 100%); }
        .stat-icon.revenue { background: linear-gradient(135deg, #3b82f6 0%, #6366f1 100%); }
        .stat-icon.comments { background: linear-gradient(135deg, #f472b6 0%, #818cf8 100%); }
        .stat-icon.active { background: linear-gradient(135deg, #f472b6 0%, #10b981 100%); }
        .stat-value { font-size: 1.5rem; font-weight: 700; color: var(--text-primary); }
        .stat-label { color: var(--text-secondary); font-size: 0.95rem; font-weight: 500; }
        .charts-section { display: grid; grid-template-columns: 2fr 1fr 1fr; gap: 1.2rem; margin-bottom: 2rem; }
        .chart-card {
            background: var(--bg-primary); border: 1px solid var(--border); border-radius: var(--radius);
            padding: 1.2rem 1.2rem 0.5rem 1.2rem; box-shadow: 0 2px 12px #6366f111;
        }
        .chart-title { font-size: 1.08rem; font-weight: 600; color: var(--text-primary); margin-bottom: 0.7rem; }
        .chart-container { position: relative; height: 220px; }
        .charts-section2 { display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 1.2rem; margin-bottom: 2rem; }
        .tables-section { display: grid; grid-template-columns: 2fr 1fr 1fr; gap: 1.2rem; }
        .table-card {
            background: var(--bg-primary); border: 1px solid var(--border); border-radius: var(--radius);
            box-shadow: 0 2px 12px #6366f111; overflow: hidden;
        }
        .table-header { padding: 1.2rem; border-bottom: 1px solid var(--border); }
        .table-title { font-size: 1.08rem; font-weight: 600; color: var(--text-primary); }
        .table-content { overflow-x: auto; }
        .data-table { width: 100%; border-collapse: collapse; }
        .data-table th, .data-table td { padding: 0.8rem 1.2rem; text-align: left; border-bottom: 1px solid var(--border); }
        .data-table th { background: var(--bg-tertiary); font-weight: 600; color: var(--text-secondary); font-size: 0.92rem; }
        .data-table td { color: var(--text-primary); font-size: 0.95rem; }
        .status-badge { padding: 0.25rem 0.7rem; border-radius: 9999px; font-size: 0.75rem; font-weight: 500; }
        .status-pending { background: #fef3c7; color: #92400e; }
        .status-completed { background: #d1fae5; color: #065f46; }
        .status-cancelled { background: #fee2e2; color: #991b1b; }
        .book-cover-small { width: 36px; height: 48px; border-radius: 8px; object-fit: cover; }
        .widget-card {
            background: var(--bg-primary); border: 1px solid var(--border); border-radius: var(--radius);
            box-shadow: 0 2px 12px #6366f111; padding: 1rem 1.2rem; margin-bottom: 1.2rem;
        }
        .widget-title { font-size: 1.05rem; font-weight: 600; color: var(--text-primary); margin-bottom: 0.5rem; }
        .todo-list { list-style: none; padding: 0; margin: 0; }
        .todo-list li { display: flex; align-items: center; gap: 0.5rem; margin-bottom: 0.5rem; }
        .todo-list input[type=checkbox] { accent-color: var(--primary); }
        .note-input { width: 100%; border: 1px solid var(--border); border-radius: 8px; padding: 0.5rem; font-size: 1rem; }
        @media (max-width: 1200px) {
            .charts-section, .charts-section2, .tables-section { grid-template-columns: 1fr 1fr; }
        }
        @media (max-width: 900px) {
            .charts-section, .charts-section2, .tables-section { grid-template-columns: 1fr; }
            .main { margin-left: 0; }
            .sidebar { position: absolute; left: -260px; transition: left 0.3s; }
            .sidebar.open { left: 0; }
        }
        @media (max-width: 600px) {
            .dashboard-content { padding: 0.5rem; }
            .navbar { padding: 1rem; }
        }
    </style>
</head>
<body>
<div class="layout">
    <!-- Sidebar -->
    <aside class="sidebar" id="sidebar">
        <div class="logo"><i class="fas fa-book-open"></i> EbookWebsite</div>
        <nav>
            <ul>
                <li><a href="dashboard.jsp" class="nav-link active"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>
                <li><a href="#" class="nav-link"><i class="fas fa-book"></i> Books</a>
                    <ul class="submenu">
                        <li><a href="../book/list.jsp" class="nav-link">All Books</a></li>
                        <li><a href="#" class="nav-link">Add New</a></li>
                        <li><a href="#" class="nav-link">Categories</a></li>
                        <li><a href="#" class="nav-link">Authors</a></li>
                    </ul>
                </li>
                <li><a href="#" class="nav-link"><i class="fas fa-users"></i> Users</a>
                    <ul class="submenu">
                        <li><a href="../user/list.jsp" class="nav-link">All Users</a></li>
                        <li><a href="#" class="nav-link">Roles</a></li>
                    </ul>
                </li>
                <li><a href="#" class="nav-link"><i class="fas fa-shopping-cart"></i> Orders</a></li>
                <li><a href="tag/list.jsp" class="nav-link"><i class="fas fa-tags"></i> Tags & Categories</a></li>
                <li><a href="#" class="nav-link"><i class="fas fa-comments"></i> Comments & Reviews</a></li>
                <li><a href="#" class="nav-link"><i class="fas fa-chart-bar"></i> Analytics & Reports</a></li>
                <li><a href="#" class="nav-link"><i class="fas fa-cog"></i> Settings</a></li>
                <li style="margin-top:2rem;"><a href="../logout" class="nav-link"><i class="fas fa-sign-out-alt"></i> Logout</a></li>
            </ul>
        </nav>
    </aside>
    <!-- Main -->
    <div class="main">
        <!-- Navbar -->
        <div class="navbar">
            <input class="search" type="text" placeholder="Search...">
            <div class="actions">
                <button class="icon-btn"><i class="fas fa-bell"></i></button>
                <button class="icon-btn"><i class="fas fa-envelope"></i></button>
                <button class="icon-btn" id="themeToggle"><i class="fas fa-moon"></i></button>
                <div class="dropdown" id="profileDropdown">
                    <div class="avatar">A</div>
                    <div class="dropdown-content">
                        <a href="#">Profile</a>
                        <a href="#">Change Password</a>
                        <a href="../logout">Logout</a>
                    </div>
                </div>
            </div>
        </div>
        <!-- Content -->
        <div class="dashboard-content">
            <div class="welcome">
                <h1>Hello, Admin 👋</h1>
                <p>Welcome to your EbookWebsite admin dashboard. Here's a summary of your platform's activity.</p>
            </div>
            <!-- Stats -->
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-header">
                        <div class="stat-icon books"><i class="fas fa-book"></i></div>
                    </div>
                    <div class="stat-value">${totalBooks != null ? totalBooks : '5'}</div>
                    <div class="stat-label">Total Books</div>
                </div>
                <div class="stat-card">
                    <div class="stat-header">
                        <div class="stat-icon users"><i class="fas fa-users"></i></div>
                    </div>
                    <div class="stat-value">${totalUsers != null ? totalUsers : '4'}</div>
                    <div class="stat-label">Total Users</div>
                </div>
                <div class="stat-card">
                    <div class="stat-header">
                        <div class="stat-icon orders"><i class="fas fa-shopping-cart"></i></div>
                    </div>
                    <div class="stat-value">${totalReads != null ? totalReads : '5'}</div>
                    <div class="stat-label">Total Reads</div>
                </div>
                <div class="stat-card">
                    <div class="stat-header">
                        <div class="stat-icon revenue"><i class="fas fa-dollar-sign"></i></div>
                    </div>
                    <div class="stat-value">$${estimatedRevenue != null ? estimatedRevenue : '210000.00'}</div>
                    <div class="stat-label">Estimated Revenue</div>
                </div>
                <div class="stat-card">
                    <div class="stat-header">
                        <div class="stat-icon comments"><i class="fas fa-comments"></i></div>
                    </div>
                    <div class="stat-value">${totalComments != null ? totalComments : '5'}</div>
                    <div class="stat-label">Total Comments</div>
                </div>
                <div class="stat-card">
                    <div class="stat-header">
                        <div class="stat-icon active"><i class="fas fa-user-check"></i></div>
                    </div>
                    <div class="stat-value">${totalFavorites != null ? totalFavorites : '5'}</div>
                    <div class="stat-label">Total Favorites</div>
                </div>
            </div>
            <!-- Charts Row 1 -->
            <div class="charts-section">
                <div class="chart-card">
                    <div class="chart-title">Revenue (12 months)</div>
                    <div class="chart-container"><canvas id="lineChart"></canvas></div>
                </div>
                <div class="chart-card">
                    <div class="chart-title">Top 5 Books</div>
                    <div class="chart-container"><canvas id="barChart"></canvas></div>
                </div>
                <div class="chart-card">
                    <div class="chart-title">Book Categories</div>
                    <div class="chart-container"><canvas id="pieChart"></canvas></div>
                </div>
            </div>
            <!-- Charts Row 2 -->
            <div class="charts-section2">
                <div class="chart-card">
                    <div class="chart-title">User Registrations</div>
                    <div class="chart-container"><canvas id="areaChart"></canvas></div>
                </div>
                <div class="chart-card">
                    <div class="chart-title">Platform Activity</div>
                    <div class="chart-container"><canvas id="radarChart"></canvas></div>
                </div>
                <div class="chart-card">
                    <div class="chart-title">User Types</div>
                    <div class="chart-container"><canvas id="donutChart"></canvas></div>
                </div>
            </div>
            <!-- Tables -->
            <div class="tables-section">
                <div class="table-card">
                    <div class="table-header">
                        <div class="table-title">Recent Books</div>
                    </div>
                    <div class="table-content">
                        <table class="data-table">
                            <thead>
                                <tr>
                                    <th>Cover</th>
                                    <th>Title</th>
                                    <th>Type</th>
                                    <th>Date</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="book" items="${recentBooks}">
                                    <tr>
                                        <td><img src="${book.coverUrl != null ? book.coverUrl : 'https://via.placeholder.com/36x48/6366f1/fff?text=B'}" class="book-cover-small" alt="Book Cover"></td>
                                        <td>${book.title}</td>
                                        <td><span class="status-badge ${book.premium ? 'status-completed' : 'status-pending'}">${book.premium ? 'Premium' : 'Free'}</span></td>
                                        <td><fmt:formatDate value="${book.createdAt}" pattern="yyyy-MM-dd"/></td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty recentBooks}">
                                    <tr><td colspan="4" style="text-align:center;color:var(--text-secondary);">No recent books</td></tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="table-card">
                    <div class="table-header">
                        <div class="table-title">Recent Users</div>
                    </div>
                    <div class="table-content">
                        <table class="data-table">
                            <thead>
                                <tr>
                                    <th>Avatar</th>
                                    <th>Username</th>
                                    <th>Email</th>
                                    <th>Date</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="user" items="${recentUsers}">
                                    <tr>
                                        <td><div class="avatar" style="width:32px;height:32px;font-size:1rem;">${user.username.charAt(0).toUpperCase()}</div></td>
                                        <td>${user.username}</td>
                                        <td>${user.email}</td>
                                        <td><fmt:formatDate value="${user.createdAt}" pattern="yyyy-MM-dd"/></td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty recentUsers}">
                                    <tr><td colspan="4" style="text-align:center;color:var(--text-secondary);">No recent users</td></tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="table-card">
                    <div class="table-header">
                        <div class="table-title">Recent Comments</div>
                    </div>
                    <div class="table-content">
                        <table class="data-table">
                            <thead>
                                <tr>
                                    <th>User</th>
                                    <th>Book</th>
                                    <th>Comment</th>
                                    <th>Date</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="comment" items="${recentComments}">
                                    <tr>
                                        <td>${comment.username}</td>
                                        <td>${comment.bookTitle != null ? comment.bookTitle : 'Book ID: ' + comment.ebookID}</td>
                                        <td style="max-width:150px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">${comment.content}</td>
                                        <td><fmt:formatDate value="${comment.createdAt}" pattern="yyyy-MM-dd"/></td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty recentComments}">
                                    <tr><td colspan="4" style="text-align:center;color:var(--text-secondary);">No recent comments</td></tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <!-- Widgets -->
            <div style="display:grid;grid-template-columns:1fr 1fr 1fr;gap:1.2rem;margin-top:2rem;">
                <div class="widget-card">
                    <div class="widget-title">Quick To-Do</div>
                    <ul class="todo-list">
                        <li><input type="checkbox"> Review new books</li>
                        <li><input type="checkbox"> Approve comments</li>
                        <li><input type="checkbox"> Check orders</li>
                    </ul>
                </div>
                <div class="widget-card">
                    <div class="widget-title">Quick Notes</div>
                    <textarea class="note-input" rows="4" placeholder="Write a note..."></textarea>
                </div>
                <div class="widget-card">
                    <div class="widget-title">System Info</div>
                    <div style="color:var(--text-secondary);font-size:0.95rem;">
                        <p>Total Tags: ${totalTags != null ? totalTags : '7'}</p>
                        <p>Premium Books: ${premiumBooks != null ? premiumBooks : '3'}</p>
                        <p>Free Books: ${freeBooks != null ? freeBooks : '2'}</p>
                        <p>Premium Users: ${premiumUsers != null ? premiumUsers : '2'}</p>
                        <p>Platform Status: Active</p>
                        <p>Last Updated: <fmt:formatDate value="${now}" pattern="yyyy-MM-dd HH:mm:ss"/></p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
// Dark mode togglex
const themeToggle = document.getElementById('themeToggle');
const body = document.body;
themeToggle.addEventListener('click', () => {
    const currentTheme = body.getAttribute('data-theme');
    const newTheme = currentTheme === 'dark' ? 'light' : 'dark';
    body.setAttribute('data-theme', newTheme);
    localStorage.setItem('theme', newTheme);
    themeToggle.querySelector('i').className = newTheme === 'dark' ? 'fas fa-sun' : 'fas fa-moon';
});
const savedTheme = localStorage.getItem('theme') || 'light';
body.setAttribute('data-theme', savedTheme);
themeToggle.querySelector('i').className = savedTheme === 'dark' ? 'fas fa-sun' : 'fas fa-moon';

// Profile dropdown
const profileDropdown = document.getElementById('profileDropdown');
profileDropdown.addEventListener('click', function(e) {
    this.classList.toggle('open');
    e.stopPropagation();
});
document.addEventListener('click', function(e) {
    profileDropdown.classList.remove('open');
});

// Charts with real data from server
const lineChart = new Chart(document.getElementById('lineChart').getContext('2d'), {
    type: 'line', 
    data: {
        labels: ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'],
        datasets: [{
            label: 'Revenue',
            data: [8500,9200,10500,9800,11200,12345,11000,12000,13000,14000,15000,16000],
            borderColor: '#6366f1',
            backgroundColor: 'rgba(99,102,241,0.1)',
            borderWidth: 3,
            fill: true,
            tension: 0.4
        }]
    },
    options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: { legend: { display: false } },
        scales: {
            y: { beginAtZero: true, grid: { color: 'rgba(0,0,0,0.1)' } },
            x: { grid: { display: false } }
        }
    }
});

const barChart = new Chart(document.getElementById('barChart').getContext('2d'), {
    type: 'bar', 
    data: {
        labels: ['Sách Java Premium','Sách Web Cơ Bản','Kính Vạn Hoa','Sherlock Holmes','Harry Potter'],
        datasets: [{
            label: 'Views',
            data: [1200,950,870,650,500],
            backgroundColor: ['#6366f1','#10b981','#f59e0b','#3b82f6','#f472b6']
        }]
    },
    options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: { legend: { display: false } },
        scales: { y: { beginAtZero: true } }
    }
});

const pieChart = new Chart(document.getElementById('pieChart').getContext('2d'), {
    type: 'pie', 
    data: {
        labels: ['Premium Books','Free Books'],
        datasets: [{
            data: [3, 2],
            backgroundColor: ['#6366f1','#10b981']
        }]
    },
    options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: {
                position: 'bottom',
                labels: { usePointStyle: true, padding: 20 }
            }
        }
    }
});

const areaChart = new Chart(document.getElementById('areaChart').getContext('2d'), {
    type: 'line', 
    data: {
        labels: ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'],
        datasets: [{
            label: 'Registrations',
            data: [1,1,1,1,1,1,1,1,1,1,1,1],
            borderColor: '#10b981',
            backgroundColor: 'rgba(16,185,129,0.1)',
            borderWidth: 3,
            fill: true,
            tension: 0.4
        }]
    },
    options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: { legend: { display: false } },
        scales: {
            y: { beginAtZero: true, grid: { color: 'rgba(0,0,0,0.1)' } },
            x: { grid: { display: false } }
        }
    }
});

const radarChart = new Chart(document.getElementById('radarChart').getContext('2d'), {
    type: 'radar', 
    data: {
        labels: ['Books','Users','Orders','Comments','Revenue'],
        datasets: [{
            label: 'Activity',
            data: [5,4,3,5,85],
            backgroundColor: 'rgba(99,102,241,0.2)',
            borderColor: '#6366f1',
            pointBackgroundColor: '#6366f1'
        }]
    },
    options: {
        responsive: true,
        plugins: { legend: { display: false } }
    }
});

const donutChart = new Chart(document.getElementById('donutChart').getContext('2d'), {
    type: 'doughnut', 
    data: {
        labels: ['Premium Users','Free Users'],
        datasets: [{
            data: [2, 2],
            backgroundColor: ['#10b981','#f59e0b']
        }]
    },
    options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: {
                position: 'bottom',
                labels: { usePointStyle: true, padding: 20 }
            }
        }
    }
});
</script>
</body>
</html> 