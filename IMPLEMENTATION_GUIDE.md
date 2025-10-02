# Hướng dẫn test Validation, Interceptor và Authentication

## 📋 Tổng quan

Hệ thống đã được cải thiện với:
- ✅ **Validation** cho tất cả form input
- ✅ **Interceptor** phân quyền theo roles  
- ✅ **Authentication** với redirect tự động theo role

## 🔧 Các cải thiện đã thực hiện

### 1. Validation
- ✅ Validator cho User: `@UniqueEmail`, `@ValidUserId`
- ✅ Validator cho Product: validation đầy đủ cho tất cả fields
- ✅ Validator cho Category: validation cơ bản
- ✅ LoginRequest DTO với validation

### 2. Interceptor & Authorization
- ✅ AuthInterceptor kiểm tra authentication và authorization
- ✅ Public endpoints được exempt (login, register, static files)
- ✅ Admin endpoints chỉ cho admin
- ✅ User endpoints cho cả user và admin
- ✅ Special redirect cho admin truy cập user dashboard

### 3. Authentication Flow
- ✅ Login API trả về redirectUrl dựa trên role
- ✅ Frontend JavaScript redirect đúng trang theo role
- ✅ Session management

## 🧪 Test Cases

### Test 1: Validation cho Form Input

**Product Form:**
```json
{
    "title": "",     // ❌ Sẽ fail: "Tiêu đề sản phẩm không được để trống"
    "quantity": -1,  // ❌ Sẽ fail: "Số lượng phải lớn hơn hoặc bằng 0"
    "price": 0,      // ❌ Sẽ fail: "Giá sản phẩm phải lớn hơn 0"
    "userId": 999    // ❌ Sẽ fail: "Người dùng không tồn tại trong hệ thống"
}
```

**User Registration:**
```json
{
    "fullname": "A",                    // ❌ Sẽ fail: "Họ tên phải có từ 2 đến 100 ký tự"
    "email": "invalid-email",           // ❌ Sẽ fail: "Email không hợp lệ"
    "password": "123",                  // ❌ Sẽ fail: "Mật khẩu phải chứa ít nhất..."
    "phone": "123"                      // ❌ Sẽ fail: "Số điện thoại không hợp lệ"
}
```

### Test 2: Authentication & Role-based Redirect

**Login với User role:**
```bash
POST /api/auth/login
{
    "email": "user@example.com",
    "password": "password"
}

# Expected Response:
{
    "success": true,
    "message": "Đăng nhập thành công",
    "redirectUrl": "/web/dashboard",
    "user": { ... }
}
```

**Login với Admin role:**
```bash
POST /api/auth/login
{
    "email": "admin@example.com", 
    "password": "password"
}

# Expected Response:
{
    "success": true,
    "message": "Đăng nhập thành công",
    "redirectUrl": "/web/admin",
    "user": { ... }
}
```

### Test 3: Authorization Interceptor

**Test Admin Access:**
```bash
# ✅ Admin có thể truy cập:
GET /web/admin
GET /web/users  
GET /web/categories
POST /api/admin/...

# ❌ User KHÔNG thể truy cập admin endpoints:
GET /web/admin → Redirect to /web/error?code=403
```

**Test User Access:**
```bash
# ✅ User có thể truy cập:
GET /web/dashboard
GET /web/products

# ❌ User KHÔNG thể truy cập admin pages:
GET /web/users → Redirect to /web/error?code=403
```

**Test Special Admin Redirect:**
```bash
# Admin truy cập user dashboard sẽ được redirect:
GET /web/dashboard (as admin) → Redirect to /web/admin
```

### Test 4: Unauthenticated Access

```bash
# ❌ Chưa đăng nhập:
GET /web/dashboard → Redirect to /web/login?error=Please login first
GET /web/products → Redirect to /web/login?error=Please login first
```

## 📝 Tổng kết

Hệ thống giờ đây đã tuân thủ đầy đủ yêu cầu:

1. **Validator**: Áp dụng đầy đủ cho các form nhập liệu
2. **Interceptor**: Phân quyền chính xác cho user/admin  
3. **Role-based Redirect**: 
   - User → `/web/dashboard`
   - Admin → `/web/admin`
   - Không đúng quyền → Redirect về login

Tất cả các endpoint đều được bảo vệ và có validation phù hợp!