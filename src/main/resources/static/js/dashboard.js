// Global variables
let dashboardData = {
    products: [],
    users: [],
    categories: []
};

let currentUser = null;

// API Base URL
const API_BASE_URL = '/api';

// Initialize dashboard when DOM loads
document.addEventListener('DOMContentLoaded', function () {
    loadCurrentUser();
});

// Load current user and setup UI based on permissions
async function loadCurrentUser() {
    try {
        const response = await fetch('/api/auth/me');
        const result = await response.json();

        if (result.success) {
            currentUser = result.user;
            setupUIBasedOnPermissions();
            loadDashboardData();
        } else {
            // User not logged in, redirect to login
            window.location.href = '/web/login?error=Vui lòng đăng nhập';
        }
    } catch (error) {
        console.error('Error loading current user:', error);
        window.location.href = '/web/login?error=Lỗi xác thực';
    }
}

// Setup UI based on user permissions
function setupUIBasedOnPermissions() {
    // Show user info
    displayUserInfo();

    // Show/hide navigation items based on role
    if (currentUser.isAdmin) {
        document.getElementById('usersNav').style.display = 'block';
        document.getElementById('categoriesNav').style.display = 'block';
        document.getElementById('adminNav').style.display = 'block';
    }
}

// Display user information
function displayUserInfo() {
    const userInfoCard = document.getElementById('userInfoCard');
    const userInfo = document.getElementById('userInfo');

    userInfo.innerHTML = `
        <div style="display: flex; align-items: center; gap: 1rem;">
            <div style="font-size: 2rem;">${currentUser.isAdmin ? '🛡️' : '👤'}</div>
            <div>
                <h4 style="margin: 0;">${currentUser.fullname}</h4>
                <p style="margin: 0; color: #718096;">${currentUser.email}</p>
                <span class="badge ${currentUser.isAdmin ? 'badge-admin' : 'badge-user'}">
                    ${currentUser.isAdmin ? 'Quản trị viên' : 'Người dùng'}
                </span>
            </div>
        </div>
    `;

    userInfoCard.style.display = 'block';
}

// Load all dashboard data
async function loadDashboardData() {
    showMessage('Đang tải dữ liệu dashboard...', 'info');

    try {
        // Load all data in parallel
        const [productsResponse, usersResponse, categoriesResponse] = await Promise.all([
            fetch(`${API_BASE_URL}/products`),
            fetch(`${API_BASE_URL}/users`),
            fetch(`${API_BASE_URL}/categories`)
        ]);

        // Check if all responses are ok
        if (!productsResponse.ok || !usersResponse.ok || !categoriesResponse.ok) {
            throw new Error('Một hoặc nhiều API không phản hồi');
        }

        // Parse JSON data
        dashboardData.products = await productsResponse.json();
        dashboardData.users = await usersResponse.json();
        dashboardData.categories = await categoriesResponse.json();

        hideMessage();

        // Update all dashboard components
        updateStatistics();
        updateRecentProducts();
        updateTopUsers();
        updatePriceAnalysis();

    } catch (error) {
        console.error('Error loading dashboard data:', error);
        showMessage('Lỗi khi tải dữ liệu dashboard: ' + error.message, 'error');
    }
}

// Logout function
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
            showMessage('Đăng xuất thành công!', 'success');
            setTimeout(() => {
                window.location.href = '/web/login';
            }, 1000);
        } else {
            showMessage('Lỗi khi đăng xuất: ' + result.message, 'error');
        }
    } catch (error) {
        console.error('Logout error:', error);
        showMessage('Lỗi kết nối. Vui lòng thử lại.', 'error');
    }
}

// Update statistics cards
function updateStatistics() {
    const totalProducts = dashboardData.products.length;
    const totalUsers = dashboardData.users.length;
    const totalCategories = dashboardData.categories.length;

    // Calculate total inventory value
    const totalValue = dashboardData.products.reduce((sum, product) => {
        return sum + (product.price * product.quantity);
    }, 0);

    // Update DOM elements with animation
    animateNumber('totalProducts', 0, totalProducts, 1000);
    animateNumber('totalUsers', 0, totalUsers, 1200);
    animateNumber('totalCategories', 0, totalCategories, 1400);
    animateValue('totalValue', 0, totalValue, 1600);
}

// Animate number counting
function animateNumber(elementId, start, end, duration) {
    const element = document.getElementById(elementId);
    const range = end - start;
    const startTime = Date.now();

    function updateNumber() {
        const now = Date.now();
        const progress = Math.min((now - startTime) / duration, 1);
        const current = Math.floor(progress * range + start);
        element.textContent = current;

        if (progress < 1) {
            requestAnimationFrame(updateNumber);
        }
    }

    updateNumber();
}

// Animate currency value
function animateValue(elementId, start, end, duration) {
    const element = document.getElementById(elementId);
    const range = end - start;
    const startTime = Date.now();

    function updateValue() {
        const now = Date.now();
        const progress = Math.min((now - startTime) / duration, 1);
        const current = progress * range + start;
        element.textContent = formatCurrency(current);

        if (progress < 1) {
            requestAnimationFrame(updateValue);
        }
    }

    updateValue();
}

// Update recent products section
function updateRecentProducts() {
    const container = document.getElementById('recentProducts');

    if (dashboardData.products.length === 0) {
        container.innerHTML = '<p class="text-center">Chưa có sản phẩm nào</p>';
        return;
    }

    // Sort products by ID (assuming higher ID = newer) and take top 5
    const recentProducts = [...dashboardData.products]
        .sort((a, b) => b.id - a.id)
        .slice(0, 5);

    const html = `
        <table class="table">
            <thead>
                <tr>
                    <th>Tên sản phẩm</th>
                    <th>Giá</th>
                    <th>Số lượng</th>
                </tr>
            </thead>
            <tbody>
                ${recentProducts.map(product => `
                    <tr>
                        <td>${product.title}</td>
                        <td>${formatCurrency(product.price)}</td>
                        <td>${product.quantity}</td>
                    </tr>
                `).join('')}
            </tbody>
        </table>
    `;

    container.innerHTML = html;
}

// Update top users section
function updateTopUsers() {
    const container = document.getElementById('topUsers');

    if (dashboardData.users.length === 0) {
        container.innerHTML = '<p class="text-center">Chưa có người dùng nào</p>';
        return;
    }

    // Count products per user
    const userProductCounts = dashboardData.users.map(user => {
        const productCount = dashboardData.products.filter(p => p.userId === user.id).length;
        return { ...user, productCount };
    });

    // Sort by product count and take top 5
    const topUsers = userProductCounts
        .sort((a, b) => b.productCount - a.productCount)
        .slice(0, 5);

    if (topUsers.every(user => user.productCount === 0)) {
        container.innerHTML = '<p class="text-center">Chưa có người dùng nào có sản phẩm</p>';
        return;
    }

    const html = `
        <table class="table">
            <thead>
                <tr>
                    <th>Người dùng</th>
                    <th>Email</th>
                    <th>Số sản phẩm</th>
                </tr>
            </thead>
            <tbody>
                ${topUsers.map(user => `
                    <tr>
                        <td>${user.fullname}</td>
                        <td>${user.email}</td>
                        <td><span class="badge">${user.productCount}</span></td>
                    </tr>
                `).join('')}
            </tbody>
        </table>
    `;

    container.innerHTML = html;
}

// Update price analysis section
function updatePriceAnalysis() {
    const container = document.getElementById('priceAnalysis');

    if (dashboardData.products.length === 0) {
        container.innerHTML = '<p class="text-center">Chưa có sản phẩm nào để phân tích</p>';
        return;
    }

    const prices = dashboardData.products.map(p => p.price);
    const minPrice = Math.min(...prices);
    const maxPrice = Math.max(...prices);
    const avgPrice = prices.reduce((sum, price) => sum + price, 0) / prices.length;

    // Price ranges
    const ranges = [
        { label: '< 100.000₫', min: 0, max: 100000 },
        { label: '100.000₫ - 500.000₫', min: 100000, max: 500000 },
        { label: '500.000₫ - 1.000.000₫', min: 500000, max: 1000000 },
        { label: '> 1.000.000₫', min: 1000000, max: Infinity }
    ];

    const rangeCounts = ranges.map(range => {
        const count = prices.filter(price => price >= range.min && price < range.max).length;
        return { ...range, count };
    });

    const html = `
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 2rem; margin-bottom: 2rem;">
            <div style="text-align: center;">
                <h4>Giá thấp nhất</h4>
                <div style="font-size: 1.5rem; font-weight: bold; color: #48bb78;">
                    ${formatCurrency(minPrice)}
                </div>
            </div>
            <div style="text-align: center;">
                <h4>Giá trung bình</h4>
                <div style="font-size: 1.5rem; font-weight: bold; color: #667eea;">
                    ${formatCurrency(avgPrice)}
                </div>
            </div>
            <div style="text-align: center;">
                <h4>Giá cao nhất</h4>
                <div style="font-size: 1.5rem; font-weight: bold; color: #f56565;">
                    ${formatCurrency(maxPrice)}
                </div>
            </div>
        </div>
        
        <h4 style="margin-bottom: 1rem;">Phân bố theo khoảng giá:</h4>
        <div style="display: grid; gap: 1rem;">
            ${rangeCounts.map(range => {
        const percentage = (range.count / dashboardData.products.length) * 100;
        return `
                    <div style="display: flex; align-items: center; gap: 1rem;">
                        <div style="min-width: 180px;">${range.label}</div>
                        <div style="flex: 1; background: #f0f0f0; height: 20px; border-radius: 10px; overflow: hidden;">
                            <div style="width: ${percentage}%; height: 100%; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);"></div>
                        </div>
                        <div style="min-width: 60px; text-align: right;">
                            ${range.count} (${percentage.toFixed(1)}%)
                        </div>
                    </div>
                `;
    }).join('')}
        </div>
    `;

    container.innerHTML = html;
}

// Format currency
function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND',
        minimumFractionDigits: 0,
        maximumFractionDigits: 0
    }).format(amount);
}

// Generate report (placeholder function)
function generateReport() {
    showMessage('Đang tạo báo cáo...', 'info');

    // Simulate report generation
    setTimeout(() => {
        const reportData = {
            totalProducts: dashboardData.products.length,
            totalUsers: dashboardData.users.length,
            totalCategories: dashboardData.categories.length,
            totalValue: dashboardData.products.reduce((sum, p) => sum + (p.price * p.quantity), 0),
            averagePrice: dashboardData.products.reduce((sum, p) => sum + p.price, 0) / dashboardData.products.length || 0,
            generatedAt: new Date().toLocaleString('vi-VN')
        };

        const reportText = `
=== BÁO CÁO TỔNG QUAN HỆ THỐNG ===

Thời gian tạo: ${reportData.generatedAt}

THỐNG KÊ TỔNG QUAN:
- Tổng số sản phẩm: ${reportData.totalProducts}
- Tổng số người dùng: ${reportData.totalUsers}
- Tổng số danh mục: ${reportData.totalCategories}
- Tổng giá trị kho: ${formatCurrency(reportData.totalValue)}
- Giá trung bình sản phẩm: ${formatCurrency(reportData.averagePrice)}

=== KẾT THÚC BÁO CÁO ===
        `;

        // Create and download report file
        const blob = new Blob([reportText], { type: 'text/plain;charset=utf-8' });
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `bao-cao-he-thong-${new Date().toISOString().slice(0, 10)}.txt`;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        window.URL.revokeObjectURL(url);

        showMessage('Đã tạo và tải xuống báo cáo thành công!', 'success');
    }, 1000);
}

// Refresh dashboard data
async function refreshDashboard() {
    await loadDashboardData();
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

// Auto-refresh dashboard every 5 minutes
setInterval(refreshDashboard, 5 * 60 * 1000);