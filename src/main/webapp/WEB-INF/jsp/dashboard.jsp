<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html lang="vi">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>${pageTitle}</title>
            <link rel="stylesheet" href="/css/style.css">
            <style>
                .dashboard-header {
                    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                    color: white;
                    padding: 2rem;
                    border-radius: 12px;
                    margin-bottom: 2rem;
                    text-align: center;
                }

                .welcome-message {
                    font-size: 1.5rem;
                    margin-bottom: 0.5rem;
                }

                .user-role {
                    font-size: 1rem;
                    opacity: 0.9;
                }

                .dashboard-grid {
                    display: grid;
                    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
                    gap: 2rem;
                    margin-bottom: 2rem;
                }

                .stats-card {
                    background: white;
                    border: 1px solid #e2e8f0;
                    padding: 2rem;
                    border-radius: 12px;
                    text-align: center;
                    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
                    transition: transform 0.2s;
                }

                .stats-card:hover {
                    transform: translateY(-5px);
                }

                .stats-number {
                    font-size: 3rem;
                    font-weight: bold;
                    color: #4a5568;
                    margin-bottom: 0.5rem;
                }

                .stats-label {
                    font-size: 1.1rem;
                    color: #718096;
                }

                .quick-actions {
                    display: grid;
                    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
                    gap: 1rem;
                    margin-top: 2rem;
                }

                .action-card {
                    background: white;
                    border: 1px solid #e2e8f0;
                    padding: 1.5rem;
                    border-radius: 8px;
                    text-align: center;
                    text-decoration: none;
                    color: #2d3748;
                    transition: all 0.2s;
                    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
                }

                .action-card:hover {
                    transform: translateY(-3px);
                    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
                    color: #667eea;
                }

                .action-icon {
                    font-size: 2rem;
                    margin-bottom: 1rem;
                }

                .action-title {
                    font-weight: bold;
                    margin-bottom: 0.5rem;
                }

                .action-desc {
                    font-size: 0.9rem;
                    color: #718096;
                }

                .admin-only {
                    border-left: 4px solid #f56565;
                }

                .user-access {
                    border-left: 4px solid #48bb78;
                }
            </style>
        </head>

        <body>
            <!-- Header Navigation -->
            <header class="navbar">
                <div class="nav-brand">
                    <h2>📦 Product Management</h2>
                </div>
                <nav class="nav-links">
                    <a href="/web/dashboard" class="nav-link active">
                        <span class="nav-icon">🏠</span>
                        Dashboard
                    </a>
                    <a href="/web/products" class="nav-link">
                        <span class="nav-icon">📦</span>
                        Sản phẩm
                    </a>
                    <c:if test="${currentUser.admin}">
                        <a href="/web/categories" class="nav-link">
                            <span class="nav-icon">📂</span>
                            Danh mục
                        </a>
                        <a href="/web/users" class="nav-link">
                            <span class="nav-icon">👥</span>
                            Người dùng
                        </a>
                        <a href="/web/admin" class="nav-link">
                            <span class="nav-icon">⚙️</span>
                            Admin
                        </a>
                    </c:if>
                </nav>
                <div class="nav-user">
                    <span class="user-name">👋 ${currentUser.name}</span>
                    <a href="/auth/logout" class="logout-btn">Đăng xuất</a>
                </div>
            </header>

            <!-- Main Content -->
            <main class="main-content">
                <div class="container">
                    <!-- Welcome Header -->
                    <div class="dashboard-header">
                        <div class="welcome-message">
                            Chào mừng, ${currentUser.name}!
                        </div>
                        <div class="user-role">
                            <c:choose>
                                <c:when test="${currentUser.admin}">
                                    🔑 Quản trị viên - Có quyền quản lý toàn hệ thống
                                </c:when>
                                <c:otherwise>
                                    👤 Người dùng - Có thể xem và quản lý sản phẩm
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <!-- Statistics Grid -->
                    <div class="dashboard-grid">
                        <div class="stats-card">
                            <div class="stats-number" id="productCount">-</div>
                            <div class="stats-label">Tổng sản phẩm</div>
                        </div>

                        <c:if test="${currentUser.admin}">
                            <div class="stats-card">
                                <div class="stats-number" id="categoryCount">-</div>
                                <div class="stats-label">Danh mục</div>
                            </div>
                            <div class="stats-card">
                                <div class="stats-number" id="userCount">-</div>
                                <div class="stats-label">Người dùng</div>
                            </div>
                        </c:if>

                        <div class="stats-card">
                            <div class="stats-number" id="lastLoginTime">-</div>
                            <div class="stats-label">Lần đăng nhập cuối</div>
                        </div>
                    </div>

                    <!-- Quick Actions -->
                    <h3>Thao tác nhanh</h3>
                    <div class="quick-actions">
                        <!-- User accessible actions -->
                        <a href="/web/products" class="action-card user-access">
                            <div class="action-icon">📦</div>
                            <div class="action-title">Xem sản phẩm</div>
                            <div class="action-desc">Danh sách tất cả sản phẩm</div>
                        </a>

                        <c:if test="${currentUser.admin}">
                            <!-- Admin only actions -->
                            <a href="/web/categories" class="action-card admin-only">
                                <div class="action-icon">📂</div>
                                <div class="action-title">Quản lý danh mục</div>
                                <div class="action-desc">Thêm, sửa, xóa danh mục</div>
                            </a>

                            <a href="/web/users" class="action-card admin-only">
                                <div class="action-icon">👥</div>
                                <div class="action-title">Quản lý người dùng</div>
                                <div class="action-desc">Thêm, sửa, xóa người dùng</div>
                            </a>

                            <a href="/web/admin" class="action-card admin-only">
                                <div class="action-icon">⚙️</div>
                                <div class="action-title">Bảng điều khiển Admin</div>
                                <div class="action-desc">Quản trị hệ thống toàn diện</div>
                            </a>
                        </c:if>
                    </div>
                </div>
            </main>

            <script src="/js/dashboard.js"></script>
            <script>
                // Hiển thị thời gian đăng nhập
                document.getElementById('lastLoginTime').textContent = new Date().toLocaleString('vi-VN');
            </script>
        </body>

        </html>

        .action-card {
        background: white;
        padding: 1.5rem;
        border-radius: 8px;
        text-align: center;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        transition: transform 0.2s;
        }

        .action-card:hover {
        transform: translateY(-2px);
        }

        .action-icon {
        font-size: 2rem;
        margin-bottom: 1rem;
        }
        </style>
        </head>

        <body>
            <!-- Header -->
            <div class="header">
                <div class="container">
                    <h1>Hệ thống quản lý sản phẩm</h1>
                </div>
            </div>

            <!-- Navigation -->
            <div class="navigation">
                <div class="container">
                    <ul class="nav-menu">
                        <li><a href="/web/dashboard" class="active">Tổng quan</a></li>
                        <li><a href="/web/products">Sản phẩm</a></li>
                        <li id="usersNav" style="display: none;"><a href="/web/users">Người dùng</a></li>
                        <li id="categoriesNav" style="display: none;"><a href="/web/categories">Danh mục</a></li>
                        <li id="adminNav" style="display: none;"><a href="/web/admin">🛡️ Quản trị</a></li>
                        <li><a href="#" onclick="logout()">Đăng xuất</a></li>
                    </ul>
                </div>
            </div>

            <div class="container">
                <!-- Message Area -->
                <div id="messageArea"></div>

                <!-- User Info -->
                <div class="card" id="userInfoCard" style="display: none;">
                    <div class="card-header">
                        <h3>Thông tin người dùng</h3>
                    </div>
                    <div id="userInfo"></div>
                </div>

                <!-- Welcome Card -->
                <div class="card">
                    <div class="card-header">
                        <h2>Chào mừng đến với hệ thống quản lý!</h2>
                        <p>Tổng quan về tình hình hoạt động của hệ thống</p>
                    </div>
                </div>

                <!-- Statistics Cards -->
                <div class="dashboard-grid">
                    <div class="stats-card">
                        <div class="stats-number" id="totalProducts">0</div>
                        <div class="stats-label">Tổng sản phẩm</div>
                    </div>

                    <div class="stats-card" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
                        <div class="stats-number" id="totalUsers">0</div>
                        <div class="stats-label">Tổng người dùng</div>
                    </div>

                    <div class="stats-card" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">
                        <div class="stats-number" id="totalCategories">0</div>
                        <div class="stats-label">Tổng danh mục</div>
                    </div>

                    <div class="stats-card" style="background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);">
                        <div class="stats-number" id="totalValue">0₫</div>
                        <div class="stats-label">Tổng giá trị kho</div>
                    </div>
                </div>

                <!-- Charts and Data -->
                <div class="dashboard-grid">
                    <!-- Recent Products -->
                    <div class="chart-container">
                        <h3>Sản phẩm mới nhất</h3>
                        <div id="recentProducts">
                            <div class="loading">Đang tải dữ liệu...</div>
                        </div>
                    </div>

                    <!-- Top Users -->
                    <div class="chart-container">
                        <h3>Người dùng có nhiều sản phẩm nhất</h3>
                        <div id="topUsers">
                            <div class="loading">Đang tải dữ liệu...</div>
                        </div>
                    </div>
                </div>

                <!-- Price Range Analysis -->
                <div class="card">
                    <div class="card-header">
                        <h3>Phân tích giá sản phẩm</h3>
                    </div>
                    <div id="priceAnalysis">
                        <div class="loading">Đang tải dữ liệu...</div>
                    </div>
                </div>

                <!-- Quick Actions -->
                <div class="card">
                    <div class="card-header">
                        <h3>Thao tác nhanh</h3>
                    </div>

                    <div class="quick-actions">
                        <div class="action-card">
                            <div class="action-icon">📦</div>
                            <h4>Thêm sản phẩm</h4>
                            <p>Thêm sản phẩm mới vào hệ thống</p>
                            <a href="/web/products" class="btn btn-primary">Đi đến</a>
                        </div>

                        <div class="action-card">
                            <div class="action-icon">👥</div>
                            <h4>Quản lý người dùng</h4>
                            <p>Xem và quản lý người dùng</p>
                            <a href="/web/users" class="btn btn-primary">Đi đến</a>
                        </div>

                        <div class="action-card">
                            <div class="action-icon">🏷️</div>
                            <h4>Quản lý danh mục</h4>
                            <p>Tạo và quản lý danh mục</p>
                            <a href="/web/categories" class="btn btn-primary">Đi đến</a>
                        </div>

                        <div class="action-card">
                            <div class="action-icon">📊</div>
                            <h4>Xem báo cáo</h4>
                            <p>Xem báo cáo chi tiết</p>
                            <button class="btn btn-primary" onclick="generateReport()">Tạo báo cáo</button>
                        </div>
                    </div>
                </div>
            </div>

            <script src="/js/dashboard.js"></script>
        </body>

        </html>