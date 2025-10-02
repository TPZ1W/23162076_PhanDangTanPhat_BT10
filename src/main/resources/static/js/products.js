// Global variables
let products = [];
let users = [];
let categories = [];
let currentEditId = null;
let deleteProductId = null;

// API Base URL
const API_BASE_URL = '/api';

// Initialize page when DOM loads
document.addEventListener('DOMContentLoaded', function () {
    loadInitialData();
    setupEventListeners();
});

// Setup event listeners
function setupEventListeners() {
    // Search input
    document.getElementById('searchInput').addEventListener('input', debounce(filterProducts, 300));

    // Category filter
    document.getElementById('categoryFilter').addEventListener('change', filterProducts);

    // Sort order
    document.getElementById('sortOrder').addEventListener('change', sortProducts);

    // Form submission
    document.getElementById('productForm').addEventListener('submit', handleFormSubmit);

    // Modal close on outside click
    window.addEventListener('click', function (event) {
        const productModal = document.getElementById('productModal');
        const confirmModal = document.getElementById('confirmModal');
        if (event.target === productModal) {
            closeModal();
        }
        if (event.target === confirmModal) {
            closeConfirmModal();
        }
    });
}

// Load initial data
async function loadInitialData() {
    showMessage('Đang tải dữ liệu...', 'info');

    try {
        // Load products, users, and categories in parallel
        await Promise.all([
            loadProducts(),
            loadUsers(),
            loadCategories()
        ]);

        hideMessage();
        renderProducts();
        populateUserSelect();
        populateCategoryFilter();
    } catch (error) {
        console.error('Error loading initial data:', error);
        showMessage('Lỗi khi tải dữ liệu: ' + error.message, 'error');
    }
}

// Load products from API
async function loadProducts() {
    try {
        const response = await fetch(`${API_BASE_URL}/products`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        products = await response.json();
    } catch (error) {
        console.error('Error loading products:', error);
        throw new Error('Không thể tải danh sách sản phẩm');
    }
}

// Load users from API
async function loadUsers() {
    try {
        const response = await fetch(`${API_BASE_URL}/users`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        users = await response.json();
    } catch (error) {
        console.error('Error loading users:', error);
        throw new Error('Không thể tải danh sách người dùng');
    }
}

// Load categories from API
async function loadCategories() {
    try {
        const response = await fetch(`${API_BASE_URL}/categories`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        categories = await response.json();
    } catch (error) {
        console.error('Error loading categories:', error);
        throw new Error('Không thể tải danh sách danh mục');
    }
}

// Render products table
function renderProducts() {
    const tbody = document.getElementById('productsTableBody');

    if (!products || products.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7" class="text-center">Không có sản phẩm nào</td></tr>';
        return;
    }

    tbody.innerHTML = products.map(product => `
        <tr class="fade-in">
            <td>${product.id}</td>
            <td>${product.title}</td>
            <td>${product.description || 'Chưa có mô tả'}</td>
            <td>${formatCurrency(product.price)}</td>
            <td>${product.quantity}</td>
            <td>${getUserName(product.userId)}</td>
            <td>
                <button type="button" class="btn btn-sm btn-warning" onclick="editProduct(${product.id})">
                    Sửa
                </button>
                <button type="button" class="btn btn-sm btn-danger" onclick="showDeleteModal(${product.id})">
                    Xóa
                </button>
            </td>
        </tr>
    `).join('');
}

// Get user name by ID
function getUserName(userId) {
    const user = users.find(u => u.id === userId);
    return user ? user.fullname : 'Không xác định';
}

// Format currency
function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
    }).format(amount);
}

// Populate user select dropdown
function populateUserSelect() {
    const userSelect = document.getElementById('userId');
    userSelect.innerHTML = '<option value="">Chọn người dùng</option>';

    users.forEach(user => {
        userSelect.innerHTML += `<option value="${user.id}">${user.fullname} (${user.email})</option>`;
    });
}

// Populate category filter dropdown
function populateCategoryFilter() {
    const categoryFilter = document.getElementById('categoryFilter');
    categoryFilter.innerHTML = '<option value="">Tất cả danh mục</option>';

    categories.forEach(category => {
        categoryFilter.innerHTML += `<option value="${category.id}">${category.name}</option>`;
    });
}

// Filter products by search and category
function filterProducts() {
    let filteredProducts = [...products];

    // Search filter
    const searchTerm = document.getElementById('searchInput').value.toLowerCase();
    if (searchTerm) {
        filteredProducts = filteredProducts.filter(product =>
            product.title.toLowerCase().includes(searchTerm) ||
            (product.description && product.description.toLowerCase().includes(searchTerm))
        );
    }

    // Category filter
    const categoryId = document.getElementById('categoryFilter').value;
    if (categoryId) {
        // Note: This would need to be implemented based on the relationship
        // For now, we'll skip this filter
    }

    // Update products array for rendering
    const originalProducts = products;
    products = filteredProducts;
    renderProducts();
    products = originalProducts; // Restore original array
}

// Sort products by price
function sortProducts() {
    const sortOrder = document.getElementById('sortOrder').value;

    products.sort((a, b) => {
        if (sortOrder === 'asc') {
            return a.price - b.price;
        } else {
            return b.price - a.price;
        }
    });

    renderProducts();
}

// Show add product modal
function showAddModal() {
    currentEditId = null;
    document.getElementById('modalTitle').textContent = 'Thêm sản phẩm mới';
    document.getElementById('submitBtn').textContent = 'Thêm sản phẩm';
    document.getElementById('productForm').reset();
    document.getElementById('productId').value = '';
    document.getElementById('productModal').style.display = 'block';
}

// Edit product
function editProduct(id) {
    const product = products.find(p => p.id === id);
    if (!product) {
        showMessage('Không tìm thấy sản phẩm', 'error');
        return;
    }

    currentEditId = id;
    document.getElementById('modalTitle').textContent = 'Cập nhật sản phẩm';
    document.getElementById('submitBtn').textContent = 'Cập nhật';

    // Fill form with product data
    document.getElementById('productId').value = product.id;
    document.getElementById('title').value = product.title;
    document.getElementById('description').value = product.description || '';
    document.getElementById('price').value = product.price;
    document.getElementById('quantity').value = product.quantity;
    document.getElementById('userId').value = product.userId;

    document.getElementById('productModal').style.display = 'block';
}

// Handle form submission
async function handleFormSubmit(event) {
    event.preventDefault();

    // Clear previous validation errors
    clearValidationErrors();

    // Client-side validation
    if (!validateForm()) {
        return;
    }

    const formData = new FormData(event.target);
    const productData = {
        title: formData.get('title'),
        description: formData.get('description'),
        price: parseFloat(formData.get('price')),
        quantity: parseInt(formData.get('quantity')),
        userId: parseInt(formData.get('userId'))
    };

    try {
        let response;
        if (currentEditId) {
            // Update existing product
            productData.id = currentEditId;
            response = await fetch(`${API_BASE_URL}/products/${currentEditId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(productData)
            });
        } else {
            // Create new product
            response = await fetch(`${API_BASE_URL}/products`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(productData)
            });
        }

        if (!response.ok) {
            if (response.status === 400) {
                // Handle validation errors
                const errorData = await response.json();
                handleValidationErrors(errorData);
                return;
            } else {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
        }

        const result = await response.json();

        closeModal();
        showMessage(
            currentEditId ? 'Cập nhật sản phẩm thành công!' : 'Thêm sản phẩm thành công!',
            'success'
        );

        // Reload products
        await loadProducts();
        renderProducts();

    } catch (error) {
        console.error('Error saving product:', error);
        showMessage('Lỗi khi lưu sản phẩm: ' + error.message, 'error');
    }
}

// Show delete confirmation modal
function showDeleteModal(id) {
    deleteProductId = id;
    document.getElementById('confirmModal').style.display = 'block';
}

// Confirm delete product
async function confirmDelete() {
    if (!deleteProductId) return;

    try {
        const response = await fetch(`${API_BASE_URL}/products/${deleteProductId}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        closeConfirmModal();
        showMessage('Xóa sản phẩm thành công!', 'success');

        // Reload products
        await loadProducts();
        renderProducts();

    } catch (error) {
        console.error('Error deleting product:', error);
        showMessage('Lỗi khi xóa sản phẩm: ' + error.message, 'error');
    }
}

// Close product modal
function closeModal() {
    document.getElementById('productModal').style.display = 'none';
    currentEditId = null;
}

// Close confirm modal
function closeConfirmModal() {
    document.getElementById('confirmModal').style.display = 'none';
    deleteProductId = null;
}

// Refresh products
async function refreshProducts() {
    showMessage('Đang làm mới dữ liệu...', 'info');

    try {
        await loadProducts();
        renderProducts();
        showMessage('Làm mới dữ liệu thành công!', 'success');
    } catch (error) {
        console.error('Error refreshing products:', error);
        showMessage('Lỗi khi làm mới dữ liệu: ' + error.message, 'error');
    }
}

// Show message
function showMessage(message, type = 'info') {
    const messageArea = document.getElementById('messageArea');
    messageArea.innerHTML = `<div class="message ${type}">${message}</div>`;

    // Auto hide success messages after 3 seconds
    if (type === 'success') {
        setTimeout(hideMessage, 3000);
    }
}

// Hide message
function hideMessage() {
    document.getElementById('messageArea').innerHTML = '';
}

// Debounce function for search
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

// Client-side form validation
function validateForm() {
    let isValid = true;
    const errors = [];

    // Title validation
    const title = document.getElementById('title').value.trim();
    if (!title) {
        errors.push({ field: 'title', message: 'Tiêu đề sản phẩm không được để trống' });
        isValid = false;
    } else if (title.length < 3 || title.length > 255) {
        errors.push({ field: 'title', message: 'Tiêu đề phải có từ 3 đến 255 ký tự' });
        isValid = false;
    }

    // Price validation
    const price = parseFloat(document.getElementById('price').value);
    if (isNaN(price) || price <= 0) {
        errors.push({ field: 'price', message: 'Giá sản phẩm phải lớn hơn 0' });
        isValid = false;
    } else if (price > 99999999.99) {
        errors.push({ field: 'price', message: 'Giá sản phẩm không được vượt quá 99,999,999.99' });
        isValid = false;
    }

    // Quantity validation
    const quantity = parseInt(document.getElementById('quantity').value);
    if (isNaN(quantity) || quantity < 0) {
        errors.push({ field: 'quantity', message: 'Số lượng phải lớn hơn hoặc bằng 0' });
        isValid = false;
    } else if (quantity > 10000) {
        errors.push({ field: 'quantity', message: 'Số lượng không được vượt quá 10.000' });
        isValid = false;
    }

    // User ID validation
    const userId = document.getElementById('userId').value;
    if (!userId) {
        errors.push({ field: 'userId', message: 'Vui lòng chọn người dùng' });
        isValid = false;
    }

    // Description validation
    const description = document.getElementById('description').value;
    if (description && description.length > 1000) {
        errors.push({ field: 'description', message: 'Mô tả không được vượt quá 1000 ký tự' });
        isValid = false;
    }

    // Display client-side validation errors
    if (!isValid) {
        displayValidationErrors(errors);
    }

    return isValid;
}

// Handle validation errors from backend
function handleValidationErrors(errorData) {
    if (errorData.fieldErrors && errorData.fieldErrors.length > 0) {
        displayValidationErrors(errorData.fieldErrors);
    } else {
        showMessage(errorData.message || 'Dữ liệu không hợp lệ', 'error');
    }
}

// Display validation errors in form
function displayValidationErrors(errors) {
    errors.forEach(error => {
        const field = document.getElementById(error.field);
        if (field) {
            // Add error class to field
            field.classList.add('error');

            // Create or update error message
            let errorElement = field.parentNode.querySelector('.error-message');
            if (!errorElement) {
                errorElement = document.createElement('div');
                errorElement.className = 'error-message';
                field.parentNode.appendChild(errorElement);
            }
            errorElement.textContent = error.message;
        }
    });
}

// Clear validation errors
function clearValidationErrors() {
    // Remove error classes and messages
    document.querySelectorAll('.form-control.error').forEach(field => {
        field.classList.remove('error');
    });

    document.querySelectorAll('.error-message').forEach(errorElement => {
        errorElement.remove();
    });
}