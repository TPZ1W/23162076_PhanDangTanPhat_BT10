<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html lang="vi">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>${pageTitle}</title>
            <link rel="stylesheet" href="/css/style.css">
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
                        <li><a href="/web/dashboard">Tổng quan</a></li>
                        <li><a href="/web/products">Sản phẩm</a></li>
                        <li><a href="/web/users" class="active">Người dùng</a></li>
                        <li><a href="/web/categories">Danh mục</a></li>
                    </ul>
                </div>
            </div>

            <div class="container">
                <!-- Message Area -->
                <div id="messageArea"></div>

                <!-- User Management Card -->
                <div class="card">
                    <div class="card-header">
                        <h2>Quản lý người dùng</h2>
                    </div>

                    <!-- Search Section -->
                    <div class="search-filter-container">
                        <input type="text" id="searchInput" class="search-box" placeholder="Tìm kiếm người dùng...">
                        <div></div>
                        <div></div>
                    </div>

                    <!-- Action Buttons -->
                    <div style="margin-bottom: 1.5rem;">
                        <button type="button" class="btn btn-primary" onclick="showAddModal()">
                            Thêm người dùng mới
                        </button>
                        <button type="button" class="btn btn-success" onclick="refreshUsers()">
                            Làm mới danh sách
                        </button>
                    </div>

                    <!-- Users Table -->
                    <div class="table-container">
                        <table class="table" id="usersTable">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Họ và tên</th>
                                    <th>Email</th>
                                    <th>Số điện thoại</th>
                                    <th>Số sản phẩm</th>
                                    <th>Thao tác</th>
                                </tr>
                            </thead>
                            <tbody id="usersTableBody">
                                <tr>
                                    <td colspan="6" class="text-center">
                                        <div class="loading">Đang tải dữ liệu...</div>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <!-- Add/Edit User Modal -->
            <div id="userModal" class="modal">
                <div class="modal-content">
                    <span class="close" onclick="closeModal()">&times;</span>
                    <h3 id="modalTitle">Thêm người dùng mới</h3>
                    <form id="userForm">
                        <input type="hidden" id="userId" name="id">

                        <div class="form-group">
                            <label class="form-label" for="fullname">Họ và tên *</label>
                            <input type="text" class="form-control" id="fullname" name="fullname" required>
                        </div>

                        <div class="form-group">
                            <label class="form-label" for="email">Email *</label>
                            <input type="email" class="form-control" id="email" name="email" required>
                        </div>

                        <div class="form-group">
                            <label class="form-label" for="password">Mật khẩu *</label>
                            <input type="password" class="form-control" id="password" name="password" required>
                        </div>

                        <div class="form-group">
                            <label class="form-label" for="phone">Số điện thoại</label>
                            <input type="tel" class="form-control" id="phone" name="phone">
                        </div>

                        <div style="text-align: right; margin-top: 2rem;">
                            <button type="button" class="btn btn-secondary" onclick="closeModal()">Hủy</button>
                            <button type="submit" class="btn btn-primary" id="submitBtn">Lưu người dùng</button>
                        </div>
                    </form>
                </div>
            </div>

            <!-- Confirm Delete Modal -->
            <div id="confirmModal" class="modal">
                <div class="modal-content">
                    <h3>Xác nhận xóa</h3>
                    <p>Bạn có chắc chắn muốn xóa người dùng này không?</p>
                    <p><strong>Lưu ý:</strong> Tất cả sản phẩm của người dùng này cũng sẽ bị xóa.</p>
                    <div style="text-align: right; margin-top: 2rem;">
                        <button type="button" class="btn btn-secondary" onclick="closeConfirmModal()">Hủy</button>
                        <button type="button" class="btn btn-danger" onclick="confirmDelete()">Xóa</button>
                    </div>
                </div>
            </div>

            <!-- User Products Modal -->
            <div id="productsModal" class="modal">
                <div class="modal-content" style="max-width: 800px;">
                    <span class="close" onclick="closeProductsModal()">&times;</span>
                    <h3 id="productsModalTitle">Sản phẩm của người dùng</h3>
                    <div id="userProductsList">
                        <div class="loading">Đang tải dữ liệu...</div>
                    </div>
                </div>
            </div>

            <script src="/js/users.js"></script>
        </body>

        </html>