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
                .admin-header {
                    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                    color: white;
                    padding: 2rem 0;
                    margin-bottom: 2rem;
                }

                .admin-welcome {
                    text-align: center;
                }

                .admin-welcome h1 {
                    margin: 0 0 0.5rem 0;
                    font-size: 2.5rem;
                }

                .admin-welcome p {
                    margin: 0;
                    opacity: 0.9;
                }

                .admin-nav {
                    background: #2d3748;
                    padding: 1rem 0;
                    margin-bottom: 2rem;
                }

                .admin-nav ul {
                    list-style: none;
                    padding: 0;
                    margin: 0;
                    display: flex;
                    justify-content: center;
                    gap: 2rem;
                }

                .admin-nav a {
                    color: white;
                    text-decoration: none;
                    padding: 0.5rem 1rem;
                    border-radius: 4px;
                    transition: background-color 0.3s;
                }

                .admin-nav a:hover,
                .admin-nav a.active {
                    background-color: #4a5568;
                }

                .stats-grid {
                    display: grid;
                    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
                    gap: 1.5rem;
                    margin-bottom: 2rem;
                }

                .stat-card {
                    background: white;
                    padding: 1.5rem;
                    border-radius: 8px;
                    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
                    text-align: center;
                }

                .stat-icon {
                    font-size: 2.5rem;
                    margin-bottom: 1rem;
                }

                .stat-number {
                    font-size: 2rem;
                    font-weight: bold;
                    color: #2d3748;
                    margin-bottom: 0.5rem;
                }

                .stat-label {
                    color: #718096;
                    font-size: 0.9rem;
                }

                .quick-actions {
                    display: grid;
                    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
                    gap: 1rem;
                }

                .action-btn {
                    display: block;
                    padding: 1rem;
                    background: white;
                    border: 1px solid #e2e8f0;
                    border-radius: 8px;
                    text-decoration: none;
                    color: #2d3748;
                    text-align: center;
                    transition: all 0.3s;
                }

                .action-btn:hover {
                    background: #f7fafc;
                    border-color: #cbd5e0;
                    transform: translateY(-2px);
                }

                .logout-section {
                    text-align: center;
                    margin-top: 3rem;
                    padding-top: 2rem;
                    border-top: 1px solid #e2e8f0;
                }
            </style>
        </head>

        <body>
            <!-- Admin Header -->
            <div class="admin-header">
                <div class="container">
                    <div class="admin-welcome">
                        <h1>🛡️ Quản trị hệ thống</h1>
                        <p>Chào mừng, <strong>
                                <c:out value="${currentUser.fullname}" />
                            </strong>!</p>
                    </div>
                </div>
            </div>

            <!-- Admin Navigation -->
            <div class="admin-nav">
                <div class="container">
                    <ul>
                        <li><a href="/web/admin" class="active">Tổng quan</a></li>
                        <li><a href="/web/admin/users">Quản lý người dùng</a></li>
                        <li><a href="/web/admin/categories">Quản lý danh mục</a></li>
                        <li><a href="/web/products">Quản lý sản phẩm</a></li>
                        <li><a href="/web/dashboard">Xem trang chính</a></li>
                    </ul>
                </div>
            </div>

            <div class="container">
                <!-- Statistics Cards -->
                <div class="stats-grid">
                    <div class="stat-card">
                        <div class="stat-icon">👥</div>
                        <div class="stat-number" id="totalUsers">0</div>
                        <div class="stat-label">Tổng người dùng</div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-icon">📦</div>
                        <div class="stat-number" id="totalProducts">0</div>
                        <div class="stat-label">Tổng sản phẩm</div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-icon">📁</div>
                        <div class="stat-number" id="totalCategories">0</div>
                        <div class="stat-label">Tổng danh mục</div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-icon">💰</div>
                        <div class="stat-number" id="totalValue">0</div>
                        <div class="stat-label">Tổng giá trị sản phẩm</div>
                    </div>
                </div>

                <!-- Quick Actions -->
                <div class="card">
                    <div class="card-header">
                        <h3>Thao tác nhanh</h3>
                    </div>
                    <div class="quick-actions">
                        <a href="/web/admin/users" class="action-btn">
                            <strong>👥 Quản lý người dùng</strong><br>
                            <small>Thêm, sửa, xóa người dùng</small>
                        </a>
                        <a href="/web/admin/categories" class="action-btn">
                            <strong>📁 Quản lý danh mục</strong><br>
                            <small>Thêm, sửa, xóa danh mục</small>
                        </a>
                        <a href="/web/products" class="action-btn">
                            <strong>📦 Quản lý sản phẩm</strong><br>
                            <small>Xem và quản lý sản phẩm</small>
                        </a>
                        <a href="#" onclick="viewReports()" class="action-btn">
                            <strong>📊 Báo cáo</strong><br>
                            <small>Xem thống kê và báo cáo</small>
                        </a>
                    </div>
                </div>

                <!-- Logout Section -->
                <div class="logout-section">
                    <button type="button" class="btn btn-danger" onclick="logout()">
                        🚪 Đăng xuất
                    </button>
                </div>
            </div>

            <script>
                // Load dashboard data
                document.addEventListener('DOMContentLoaded', function () {
                    loadDashboardStats();
                });

                async function loadDashboardStats() {
                    try {
                        // Load users count
                        const usersResponse = await fetch('/api/users');
                        if (usersResponse.ok) {
                            const users = await usersResponse.json();
                            document.getElementById('totalUsers').textContent = users.length;
                        }

                        // Load products count and total value
                        const productsResponse = await fetch('/api/products');
                        if (productsResponse.ok) {
                            const products = await productsResponse.json();
                            document.getElementById('totalProducts').textContent = products.length;

                            const totalValue = products.reduce((sum, product) => sum + (product.price || 0), 0);
                            document.getElementById('totalValue').textContent =
                                new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(totalValue);
                        }

                        // Load categories count
                        const categoriesResponse = await fetch('/api/categories');
                        if (categoriesResponse.ok) {
                            const categories = await categoriesResponse.json();
                            document.getElementById('totalCategories').textContent = categories.length;
                        }
                    } catch (error) {
                        console.error('Error loading dashboard stats:', error);
                    }
                }

                async function logout() {
                    if (!confirm('Bạn có chắc chắn muốn đăng xuất?')) {
                        return;
                    }

                    try {
                        const response = await fetch('/api/auth/logout', {
                            method: 'POST'
                        });

                        const result = await response.json();
                        if (result.success) {
                            alert('Đăng xuất thành công!');
                            window.location.href = '/web/login';
                        } else {
                            alert('Lỗi khi đăng xuất: ' + result.message);
                        }
                    } catch (error) {
                        console.error('Logout error:', error);
                        alert('Lỗi kết nối. Vui lòng thử lại.');
                    }
                }

                function viewReports() {
                    alert('Tính năng báo cáo đang được phát triển!');
                }
            </script>
        </body>

        </html>