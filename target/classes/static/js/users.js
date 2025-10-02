// Global variables
let users = [];
let userProducts = [];
let currentEditId = null;
let deleteUserId = null;

// API Base URL
const API_BASE_URL = '/api';

// Initialize page when DOM loads
document.addEventListener('DOMContentLoaded', function () {
    loadUsers();
    setupEventListeners();
});

// Setup event listeners
function setupEventListeners() {
    // Search input
    document.getElementById('searchInput').addEventListener('input', debounce(filterUsers, 300));

    // Form submission
    document.getElementById('userForm').addEventListener('submit', handleFormSubmit);

    // Modal close on outside click
    window.addEventListener('click', function (event) {
        const userModal = document.getElementById('userModal');
        const confirmModal = document.getElementById('confirmModal');
        const productsModal = document.getElementById('productsModal');

        if (event.target === userModal) {
            closeModal();
        }
        if (event.target === confirmModal) {
            closeConfirmModal();
        }
        if (event.target === productsModal) {
            closeProductsModal();
        }
    });
}

// Load users from API
async function loadUsers() {
    showMessage('Đang tải dữ liệu...', 'info');

    try {
        const response = await fetch(`${API_BASE_URL}/users`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        users = await response.json();

        // Load product count for each user
        await loadUserProductCounts();

        hideMessage();
        renderUsers();
    } catch (error) {
        console.error('Error loading users:', error);
        showMessage('Lỗi khi tải dữ liệu: ' + error.message, 'error');
    }
}

// Load product counts for users
async function loadUserProductCounts() {
    try {
        const response = await fetch(`${API_BASE_URL}/products`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const products = await response.json();

        // Count products for each user
        users.forEach(user => {
            user.productCount = products.filter(p => p.userId === user.id).length;
        });
    } catch (error) {
        console.error('Error loading product counts:', error);
        // Set product count to 0 if failed
        users.forEach(user => {
            user.productCount = 0;
        });
    }
}

// Render users table
function renderUsers() {
    const tbody = document.getElementById('usersTableBody');

    if (!users || users.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" class="text-center">Không có người dùng nào</td></tr>';
        return;
    }

    tbody.innerHTML = users.map(user => `
        <tr class="fade-in">
            <td>${user.id}</td>
            <td>${user.fullname}</td>
            <td>${user.email}</td>
            <td>${user.phone || 'Chưa cập nhật'}</td>
            <td>
                <span class="badge">${user.productCount || 0}</span>
                ${user.productCount > 0 ?
            `<button type="button" class="btn btn-sm btn-info" onclick="showUserProducts(${user.id}, '${user.fullname}')" style="margin-left: 0.5rem;">
                        Xem
                    </button>` : ''
        }
            </td>
            <td>
                <button type="button" class="btn btn-sm btn-warning" onclick="editUser(${user.id})">
                    Sửa
                </button>
                <button type="button" class="btn btn-sm btn-danger" onclick="showDeleteModal(${user.id})">
                    Xóa
                </button>
            </td>
        </tr>
    `).join('');
}

// Filter users by search term
function filterUsers() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase();

    if (!searchTerm) {
        renderUsers();
        return;
    }

    const filteredUsers = users.filter(user =>
        user.fullname.toLowerCase().includes(searchTerm) ||
        user.email.toLowerCase().includes(searchTerm) ||
        (user.phone && user.phone.includes(searchTerm))
    );

    const tbody = document.getElementById('usersTableBody');

    if (filteredUsers.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" class="text-center">Không tìm thấy người dùng nào</td></tr>';
        return;
    }

    tbody.innerHTML = filteredUsers.map(user => `
        <tr class="fade-in">
            <td>${user.id}</td>
            <td>${user.fullname}</td>
            <td>${user.email}</td>
            <td>${user.phone || 'Chưa cập nhật'}</td>
            <td>
                <span class="badge">${user.productCount || 0}</span>
                ${user.productCount > 0 ?
            `<button type="button" class="btn btn-sm btn-info" onclick="showUserProducts(${user.id}, '${user.fullname}')" style="margin-left: 0.5rem;">
                        Xem
                    </button>` : ''
        }
            </td>
            <td>
                <button type="button" class="btn btn-sm btn-warning" onclick="editUser(${user.id})">
                    Sửa
                </button>
                <button type="button" class="btn btn-sm btn-danger" onclick="showDeleteModal(${user.id})">
                    Xóa
                </button>
            </td>
        </tr>
    `).join('');
}

// Show add user modal
function showAddModal() {
    currentEditId = null;
    document.getElementById('modalTitle').textContent = 'Thêm người dùng mới';
    document.getElementById('submitBtn').textContent = 'Thêm người dùng';
    document.getElementById('userForm').reset();
    document.getElementById('userId').value = '';
    document.getElementById('password').required = true;
    document.getElementById('userModal').style.display = 'block';
}

// Edit user
function editUser(id) {
    const user = users.find(u => u.id === id);
    if (!user) {
        showMessage('Không tìm thấy người dùng', 'error');
        return;
    }

    currentEditId = id;
    document.getElementById('modalTitle').textContent = 'Cập nhật người dùng';
    document.getElementById('submitBtn').textContent = 'Cập nhật';

    // Fill form with user data
    document.getElementById('userId').value = user.id;
    document.getElementById('fullname').value = user.fullname;
    document.getElementById('email').value = user.email;
    document.getElementById('phone').value = user.phone || '';
    document.getElementById('password').value = '';
    document.getElementById('password').required = false;

    document.getElementById('userModal').style.display = 'block';
}

// Handle form submission
async function handleFormSubmit(event) {
    event.preventDefault();

    const formData = new FormData(event.target);
    const userData = {
        fullname: formData.get('fullname'),
        email: formData.get('email'),
        phone: formData.get('phone') || null
    };

    // Only include password if provided
    const password = formData.get('password');
    if (password) {
        userData.password = password;
    }

    try {
        let response;
        if (currentEditId) {
            // Update existing user
            userData.id = currentEditId;
            response = await fetch(`${API_BASE_URL}/users/${currentEditId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(userData)
            });
        } else {
            // Create new user
            if (!password) {
                showMessage('Mật khẩu là bắt buộc khi tạo người dùng mới', 'error');
                return;
            }
            response = await fetch(`${API_BASE_URL}/users`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(userData)
            });
        }

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || `HTTP error! status: ${response.status}`);
        }

        const result = await response.json();

        closeModal();
        showMessage(
            currentEditId ? 'Cập nhật người dùng thành công!' : 'Thêm người dùng thành công!',
            'success'
        );

        // Reload users
        await loadUsers();

    } catch (error) {
        console.error('Error saving user:', error);
        showMessage('Lỗi khi lưu người dùng: ' + error.message, 'error');
    }
}

// Show delete confirmation modal
function showDeleteModal(id) {
    deleteUserId = id;
    document.getElementById('confirmModal').style.display = 'block';
}

// Confirm delete user
async function confirmDelete() {
    if (!deleteUserId) return;

    try {
        const response = await fetch(`${API_BASE_URL}/users/${deleteUserId}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || `HTTP error! status: ${response.status}`);
        }

        closeConfirmModal();
        showMessage('Xóa người dùng thành công!', 'success');

        // Reload users
        await loadUsers();

    } catch (error) {
        console.error('Error deleting user:', error);
        showMessage('Lỗi khi xóa người dùng: ' + error.message, 'error');
    }
}

// Show user products
async function showUserProducts(userId, fullname) {
    document.getElementById('productsModalTitle').textContent = `Sản phẩm của ${fullname}`;
    document.getElementById('userProductsList').innerHTML = '<div class="loading">Đang tải dữ liệu...</div>';
    document.getElementById('productsModal').style.display = 'block';

    try {
        const response = await fetch(`${API_BASE_URL}/products/user/${userId}`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const products = await response.json();

        if (products.length === 0) {
            document.getElementById('userProductsList').innerHTML =
                '<div class="text-center">Người dùng này chưa có sản phẩm nào</div>';
            return;
        }

        const productsHtml = `
            <table class="table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Tiêu đề</th>
                        <th>Giá</th>
                        <th>Số lượng</th>
                    </tr>
                </thead>
                <tbody>
                    ${products.map(product => `
                        <tr>
                            <td>${product.id}</td>
                            <td>${product.title}</td>
                            <td>${formatCurrency(product.price)}</td>
                            <td>${product.quantity}</td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        `;

        document.getElementById('userProductsList').innerHTML = productsHtml;

    } catch (error) {
        console.error('Error loading user products:', error);
        document.getElementById('userProductsList').innerHTML =
            '<div class="message error">Lỗi khi tải danh sách sản phẩm: ' + error.message + '</div>';
    }
}

// Format currency
function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
    }).format(amount);
}

// Close user modal
function closeModal() {
    document.getElementById('userModal').style.display = 'none';
    currentEditId = null;
}

// Close confirm modal
function closeConfirmModal() {
    document.getElementById('confirmModal').style.display = 'none';
    deleteUserId = null;
}

// Close products modal
function closeProductsModal() {
    document.getElementById('productsModal').style.display = 'none';
}

// Refresh users
async function refreshUsers() {
    showMessage('Đang làm mới dữ liệu...', 'info');

    try {
        await loadUsers();
        showMessage('Làm mới dữ liệu thành công!', 'success');
    } catch (error) {
        console.error('Error refreshing users:', error);
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