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
                        <li><a href="/web/products" class="active">Sản phẩm</a></li>
                        <li><a href="/web/users">Người dùng</a></li>
                        <li><a href="/web/categories">Danh mục</a></li>
                    </ul>
                </div>
            </div>

            <div class="container">
                <!-- Message Area -->
                <div id="messageArea"></div>

                <!-- Product Management Card -->
                <div class="card">
                    <div class="card-header">
                        <h2>Quản lý sản phẩm</h2>
                    </div>

                    <!-- Search and Filter Section -->
                    <div class="search-filter-container">
                        <input type="text" id="searchInput" class="search-box" placeholder="Tìm kiếm sản phẩm...">
                        <select id="categoryFilter" class="filter-select">
                            <option value="">Tất cả danh mục</option>
                        </select>
                        <select id="sortOrder" class="filter-select">
                            <option value="asc">Giá: Thấp → Cao</option>
                            <option value="desc">Giá: Cao → Thấp</option>
                        </select>
                    </div>

                    <!-- Action Buttons -->
                    <div style="margin-bottom: 1.5rem;">
                        <button type="button" class="btn btn-primary" onclick="showAddModal()">
                            Thêm sản phẩm mới
                        </button>
                        <button type="button" class="btn btn-success" onclick="refreshProducts()">
                            Làm mới danh sách
                        </button>
                    </div>

                    <!-- Products Table -->
                    <div class="table-container">
                        <table class="table" id="productsTable">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Tiêu đề</th>
                                    <th>Mô tả</th>
                                    <th>Giá</th>
                                    <th>Số lượng</th>
                                    <th>Người tạo</th>
                                    <th>Thao tác</th>
                                </tr>
                            </thead>
                            <tbody id="productsTableBody">
                                <tr>
                                    <td colspan="7" class="text-center">
                                        <div class="loading">Đang tải dữ liệu...</div>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <!-- Add/Edit Product Modal -->
            <div id="productModal" class="modal">
                <div class="modal-content">
                    <span class="close" onclick="closeModal()">&times;</span>
                    <h3 id="modalTitle">Thêm sản phẩm mới</h3>
                    <form id="productForm">
                        <input type="hidden" id="productId" name="id">

                        <div class="form-group">
                            <label class="form-label" for="title">Tiêu đề *</label>
                            <input type="text" class="form-control" id="title" name="title" required>
                        </div>

                        <div class="form-group">
                            <label class="form-label" for="description">Mô tả</label>
                            <textarea class="form-control" id="description" name="description" rows="3"></textarea>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label class="form-label" for="price">Giá *</label>
                                <input type="number" class="form-control" id="price" name="price" step="0.01" required>
                            </div>
                            <div class="form-group">
                                <label class="form-label" for="quantity">Số lượng *</label>
                                <input type="number" class="form-control" id="quantity" name="quantity" required>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="form-label" for="userId">Người tạo *</label>
                            <select class="form-control" id="userId" name="userId" required>
                                <option value="">Chọn người dùng</option>
                            </select>
                        </div>

                        <div style="text-align: right; margin-top: 2rem;">
                            <button type="button" class="btn btn-secondary" onclick="closeModal()">Hủy</button>
                            <button type="submit" class="btn btn-primary" id="submitBtn">Lưu sản phẩm</button>
                        </div>
                    </form>
                </div>
            </div>

            <!-- Confirm Delete Modal -->
            <div id="confirmModal" class="modal">
                <div class="modal-content">
                    <h3>Xác nhận xóa</h3>
                    <p>Bạn có chắc chắn muốn xóa sản phẩm này không?</p>
                    <div style="text-align: right; margin-top: 2rem;">
                        <button type="button" class="btn btn-secondary" onclick="closeConfirmModal()">Hủy</button>
                        <button type="button" class="btn btn-danger" onclick="confirmDelete()">Xóa</button>
                    </div>
                </div>
            </div>

            <script src="/js/products.js"></script>
        </body>

        </html>