// Global variables
let categories = [];
let users = [];
let currentEditId = null;
let deleteCategoryId = null;

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
    document.getElementById('searchInput').addEventListener('input', debounce(filterCategories, 300));

    // Form submission
    document.getElementById('categoryForm').addEventListener('submit', handleFormSubmit);

    // Modal close on outside click
    window.addEventListener('click', function (event) {
        const categoryModal = document.getElementById('categoryModal');
        const confirmModal = document.getElementById('confirmModal');
        const usersModal = document.getElementById('usersModal');

        if (event.target === categoryModal) {
            closeModal();
        }
        if (event.target === confirmModal) {
            closeConfirmModal();
        }
        if (event.target === usersModal) {
            closeUsersModal();
        }
    });
}

// Load initial data
async function loadInitialData() {
    showMessage('Đang tải dữ liệu...', 'info');

    try {
        // Load categories and users in parallel
        await Promise.all([
            loadCategories(),
            loadUsers()
        ]);

        // Load user counts for categories
        await loadCategoryUserCounts();

        hideMessage();
        renderCategories();
    } catch (error) {
        console.error('Error loading initial data:', error);
        showMessage('Lỗi khi tải dữ liệu: ' + error.message, 'error');
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

// Load user counts for categories (simulated since we don't have the relationship API yet)
async function loadCategoryUserCounts() {
    // For now, we'll simulate random counts since the many-to-many relationship
    // endpoints are not implemented yet
    categories.forEach(category => {
        category.userCount = Math.floor(Math.random() * 10); // Random count 0-9
    });
}

// Render categories table
function renderCategories() {
    const tbody = document.getElementById('categoriesTableBody');

    if (!categories || categories.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5" class="text-center">Không có danh mục nào</td></tr>';
        return;
    }

    tbody.innerHTML = categories.map(category => `
        <tr class="fade-in">
            <td>${category.id}</td>
            <td>${category.name}</td>
            <td>
                ${category.images ?
            `<img src="${category.images}" alt="${category.name}" style="width: 50px; height: 50px; object-fit: cover; border-radius: 4px;">` :
            '<span class="text-muted">Chưa có hình</span>'
        }
            </td>
            <td>
                <span class="badge">${category.userCount || 0}</span>
                ${category.userCount > 0 ?
            `<button type="button" class="btn btn-sm btn-info" onclick="showCategoryUsers(${category.id}, '${category.name}')" style="margin-left: 0.5rem;">
                        Xem
                    </button>` : ''
        }
            </td>
            <td>
                <button type="button" class="btn btn-sm btn-warning" onclick="editCategory(${category.id})">
                    Sửa
                </button>
                <button type="button" class="btn btn-sm btn-danger" onclick="showDeleteModal(${category.id})">
                    Xóa
                </button>
            </td>
        </tr>
    `).join('');
}

// Filter categories by search term
function filterCategories() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase();

    if (!searchTerm) {
        renderCategories();
        return;
    }

    const filteredCategories = categories.filter(category =>
        category.name.toLowerCase().includes(searchTerm)
    );

    const tbody = document.getElementById('categoriesTableBody');

    if (filteredCategories.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5" class="text-center">Không tìm thấy danh mục nào</td></tr>';
        return;
    }

    tbody.innerHTML = filteredCategories.map(category => `
        <tr class="fade-in">
            <td>${category.id}</td>
            <td>${category.name}</td>
            <td>
                ${category.images ?
            `<img src="${category.images}" alt="${category.name}" style="width: 50px; height: 50px; object-fit: cover; border-radius: 4px;">` :
            '<span class="text-muted">Chưa có hình</span>'
        }
            </td>
            <td>
                <span class="badge">${category.userCount || 0}</span>
                ${category.userCount > 0 ?
            `<button type="button" class="btn btn-sm btn-info" onclick="showCategoryUsers(${category.id}, '${category.name}')" style="margin-left: 0.5rem;">
                        Xem
                    </button>` : ''
        }
            </td>
            <td>
                <button type="button" class="btn btn-sm btn-warning" onclick="editCategory(${category.id})">
                    Sửa
                </button>
                <button type="button" class="btn btn-sm btn-danger" onclick="showDeleteModal(${category.id})">
                    Xóa
                </button>
            </td>
        </tr>
    `).join('');
}

// Show add category modal
function showAddModal() {
    currentEditId = null;
    document.getElementById('modalTitle').textContent = 'Thêm danh mục mới';
    document.getElementById('submitBtn').textContent = 'Thêm danh mục';
    document.getElementById('categoryForm').reset();
    document.getElementById('categoryId').value = '';
    document.getElementById('categoryModal').style.display = 'block';
}

// Edit category
function editCategory(id) {
    const category = categories.find(c => c.id === id);
    if (!category) {
        showMessage('Không tìm thấy danh mục', 'error');
        return;
    }

    currentEditId = id;
    document.getElementById('modalTitle').textContent = 'Cập nhật danh mục';
    document.getElementById('submitBtn').textContent = 'Cập nhật';

    // Fill form with category data
    document.getElementById('categoryId').value = category.id;
    document.getElementById('name').value = category.name;
    document.getElementById('images').value = category.images || '';

    document.getElementById('categoryModal').style.display = 'block';
}

// Handle form submission
async function handleFormSubmit(event) {
    event.preventDefault();

    const formData = new FormData(event.target);
    const categoryData = {
        name: formData.get('name'),
        images: formData.get('images') || null
    };

    try {
        let response;
        if (currentEditId) {
            // Update existing category
            categoryData.id = currentEditId;
            response = await fetch(`${API_BASE_URL}/categories/${currentEditId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(categoryData)
            });
        } else {
            // Create new category
            response = await fetch(`${API_BASE_URL}/categories`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(categoryData)
            });
        }

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || `HTTP error! status: ${response.status}`);
        }

        const result = await response.json();

        closeModal();
        showMessage(
            currentEditId ? 'Cập nhật danh mục thành công!' : 'Thêm danh mục thành công!',
            'success'
        );

        // Reload categories
        await loadInitialData();

    } catch (error) {
        console.error('Error saving category:', error);
        showMessage('Lỗi khi lưu danh mục: ' + error.message, 'error');
    }
}

// Show delete confirmation modal
function showDeleteModal(id) {
    deleteCategoryId = id;
    document.getElementById('confirmModal').style.display = 'block';
}

// Confirm delete category
async function confirmDelete() {
    if (!deleteCategoryId) return;

    try {
        const response = await fetch(`${API_BASE_URL}/categories/${deleteCategoryId}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || `HTTP error! status: ${response.status}`);
        }

        closeConfirmModal();
        showMessage('Xóa danh mục thành công!', 'success');

        // Reload categories
        await loadInitialData();

    } catch (error) {
        console.error('Error deleting category:', error);
        showMessage('Lỗi khi xóa danh mục: ' + error.message, 'error');
    }
}

// Show category users (simulated for now)
async function showCategoryUsers(categoryId, categoryName) {
    document.getElementById('usersModalTitle').textContent = `Người dùng trong danh mục: ${categoryName}`;
    document.getElementById('categoryUsersList').innerHTML = '<div class="loading">Đang tải dữ liệu...</div>';
    document.getElementById('usersModal').style.display = 'block';

    try {
        // Simulate loading category users (since the API endpoint doesn't exist yet)
        await new Promise(resolve => setTimeout(resolve, 500)); // Simulate delay

        // Get random users for demo
        const categoryUsers = users.slice(0, Math.floor(Math.random() * users.length));

        if (categoryUsers.length === 0) {
            document.getElementById('categoryUsersList').innerHTML =
                '<div class="text-center">Danh mục này chưa có người dùng nào</div>';
            return;
        }

        const usersHtml = `
            <table class="table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Họ và tên</th>
                        <th>Email</th>
                        <th>Số điện thoại</th>
                    </tr>
                </thead>
                <tbody>
                    ${categoryUsers.map(user => `
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.fullname}</td>
                            <td>${user.email}</td>
                            <td>${user.phone || 'Chưa cập nhật'}</td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        `;

        document.getElementById('categoryUsersList').innerHTML = usersHtml;

    } catch (error) {
        console.error('Error loading category users:', error);
        document.getElementById('categoryUsersList').innerHTML =
            '<div class="message error">Lỗi khi tải danh sách người dùng: ' + error.message + '</div>';
    }
}

// Close category modal
function closeModal() {
    document.getElementById('categoryModal').style.display = 'none';
    currentEditId = null;
}

// Close confirm modal
function closeConfirmModal() {
    document.getElementById('confirmModal').style.display = 'none';
    deleteCategoryId = null;
}

// Close users modal
function closeUsersModal() {
    document.getElementById('usersModal').style.display = 'none';
}

// Refresh categories
async function refreshCategories() {
    showMessage('Đang làm mới dữ liệu...', 'info');

    try {
        await loadInitialData();
        showMessage('Làm mới dữ liệu thành công!', 'success');
    } catch (error) {
        console.error('Error refreshing categories:', error);
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