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
                        <li><a href="/web/users">Người dùng</a></li>
                        <li><a href="/web/categories" class="active">Danh mục</a></li>
                    </ul>
                </div>
            </div>

            <div class="container">
                <!-- Message Area -->
                <div id="messageArea"></div>

                <!-- Category Management Card -->
                <div class="card">
                    <div class="card-header">
                        <h2>Quản lý danh mục</h2>
                    </div>

                    <!-- Search Section -->
                    <div class="search-filter-container">
                        <input type="text" id="searchInput" class="search-box" placeholder="Tìm kiếm danh mục...">
                        <div></div>
                        <div></div>
                    </div>

                    <!-- Action Buttons -->
                    <div style="margin-bottom: 1.5rem;">
                        <button type="button" class="btn btn-primary" onclick="showAddModal()">
                            Thêm danh mục mới
                        </button>
                        <button type="button" class="btn btn-success" onclick="refreshCategories()">
                            Làm mới danh sách
                        </button>
                    </div>

                    <!-- Categories Table -->
                    <div class="table-container">
                        <table class="table" id="categoriesTable">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Tên danh mục</th>
                                    <th>Hình ảnh</th>
                                    <th>Số người dùng</th>
                                    <th>Thao tác</th>
                                </tr>
                            </thead>
                            <tbody id="categoriesTableBody">
                                <tr>
                                    <td colspan="5" class="text-center">
                                        <div class="loading">Đang tải dữ liệu...</div>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <!-- Add/Edit Category Modal -->
            <div id="categoryModal" class="modal">
                <div class="modal-content">
                    <span class="close" onclick="closeModal()">&times;</span>
                    <h3 id="modalTitle">Thêm danh mục mới</h3>
                    <form id="categoryForm">
                        <input type="hidden" id="categoryId" name="id">

                        <div class="form-group">
                            <label class="form-label" for="name">Tên danh mục *</label>
                            <input type="text" class="form-control" id="name" name="name" required>
                        </div>

                        <div class="form-group">
                            <label class="form-label" for="images">URL Hình ảnh</label>
                            <input type="url" class="form-control" id="images" name="images"
                                placeholder="https://example.com/image.jpg">
                        </div>

                        <div style="text-align: right; margin-top: 2rem;">
                            <button type="button" class="btn btn-secondary" onclick="closeModal()">Hủy</button>
                            <button type="submit" class="btn btn-primary" id="submitBtn">Lưu danh mục</button>
                        </div>
                    </form>
                </div>
            </div>

            <!-- Confirm Delete Modal -->
            <div id="confirmModal" class="modal">
                <div class="modal-content">
                    <h3>Xác nhận xóa</h3>
                    <p>Bạn có chắc chắn muốn xóa danh mục này không?</p>
                    <p><strong>Lưu ý:</strong> Tất cả mối quan hệ với người dùng sẽ bị xóa.</p>
                    <div style="text-align: right; margin-top: 2rem;">
                        <button type="button" class="btn btn-secondary" onclick="closeConfirmModal()">Hủy</button>
                        <button type="button" class="btn btn-danger" onclick="confirmDelete()">Xóa</button>
                    </div>
                </div>
            </div>

            <!-- Category Users Modal -->
            <div id="usersModal" class="modal">
                <div class="modal-content" style="max-width: 800px;">
                    <span class="close" onclick="closeUsersModal()">&times;</span>
                    <h3 id="usersModalTitle">Người dùng trong danh mục</h3>
                    <div id="categoryUsersList">
                        <div class="loading">Đang tải dữ liệu...</div>
                    </div>
                </div>
            </div>

            <script src="/js/categories.js"></script>
        </body>

        </html>