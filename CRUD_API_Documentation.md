# CRUD API Documentation

## 📋 Tổng quan

Tài liệu này mô tả tất cả các API CRUD cho 3 entity chính:
- **User** - Quản lý người dùng
- **Product** - Quản lý sản phẩm  
- **Category** - Quản lý danh mục

## 🔗 Base URLs

- **Users**: `http://localhost:7002/api/user`
- **Products**: `http://localhost:7002/api/product`
- **Categories**: `http://localhost:7002/api/category`

---

## 👤 USER CRUD APIs

### 📖 READ Operations

#### 1. Lấy tất cả users
```http
GET /api/user
```

#### 2. Lấy user theo ID  
```http
GET /api/user/{id}
```

#### 3. Lấy user theo email
```http
GET /api/user/email/{email}
```

#### 4. Tìm kiếm users theo tên
```http
GET /api/user/search?name={name}
```

#### 5. Lấy users theo category
```http
GET /api/user/category/{categoryId}
```

### ✏️ CREATE Operation

#### 6. Tạo user mới
```http
POST /api/user
Content-Type: application/json

{
  "fullname": "Nguyễn Văn Test",
  "email": "test@example.com", 
  "password": "password123",
  "phone": "0901234567"
}
```

### 🔄 UPDATE Operations

#### 7. Cập nhật user
```http
PUT /api/user/{id}
Content-Type: application/json

{
  "fullname": "Nguyễn Văn Updated",
  "email": "updated@example.com",
  "password": "newpassword",
  "phone": "0987654321"
}
```

#### 8. Thêm categories cho user
```http
POST /api/user/{userId}/categories
Content-Type: application/json

[1, 2, 3]
```

#### 9. Xóa category khỏi user
```http
DELETE /api/user/{userId}/categories/{categoryId}
```

### 🗑️ DELETE Operation

#### 10. Xóa user
```http
DELETE /api/user/{id}
```

---

## 🛍️ PRODUCT CRUD APIs

### 📖 READ Operations

#### 1. Lấy tất cả products
```http
GET /api/product
```

#### 2. Lấy product theo ID
```http
GET /api/product/{id}
```

#### 3. Lấy products theo user
```http
GET /api/product/user/{userId}
```

#### 4. Tìm kiếm products theo title
```http
GET /api/product/title/{title}
```

#### 5. Tìm kiếm products với filters
```http
GET /api/product/search?title={title}&minPrice={min}&maxPrice={max}&userId={userId}
```

#### 6. Lấy products theo khoảng giá
```http
GET /api/product/price-range?minPrice={min}&maxPrice={max}
```

#### 7. Lấy products sắp hết hàng
```http
GET /api/product/low-stock?threshold={number}
```

#### 8. Lấy products sắp xếp theo giá tăng dần
```http
GET /api/product/sort/price-asc
```

#### 9. Lấy products sắp xếp theo giá giảm dần
```http
GET /api/product/sort/price-desc
```

#### 10. Lấy products theo category
```http
GET /api/product/category/{categoryId}
```

#### 11. Lấy products theo category + sắp xếp giá
```http
GET /api/product/category/{categoryId}/sort/price-asc
```

### ✏️ CREATE Operation

#### 12. Tạo product mới
```http
POST /api/product
Content-Type: application/json

{
  "title": "iPhone 16 Pro",
  "quantity": 50,
  "description": "Smartphone mới nhất",
  "price": 35990000.00,
  "images": "iphone16.jpg",
  "userId": 1
}
```

### 🔄 UPDATE Operations

#### 13. Cập nhật product
```http
PUT /api/product/{id}
Content-Type: application/json

{
  "title": "iPhone 16 Pro Updated",
  "quantity": 45,
  "description": "Mô tả cập nhật",
  "price": 34990000.00,
  "images": "iphone16_updated.jpg"
}
```

#### 14. Cập nhật số lượng product
```http
PATCH /api/product/{id}/quantity?quantity={newQuantity}
```

### 🗑️ DELETE Operation

#### 15. Xóa product
```http
DELETE /api/product/{id}
```

---

## 📂 CATEGORY CRUD APIs

### 📖 READ Operations

#### 1. Lấy tất cả categories
```http
GET /api/category
```

#### 2. Lấy category theo ID
```http
GET /api/category/{id}
```

#### 3. Lấy category theo tên
```http
GET /api/category/name/{name}
```

#### 4. Tìm kiếm categories theo tên
```http
GET /api/category/search?name={name}
```

#### 5. Lấy categories theo user
```http
GET /api/category/user/{userId}
```

### ✏️ CREATE Operation

#### 6. Tạo category mới
```http
POST /api/category
Content-Type: application/json

{
  "name": "Đồ chơi",
  "images": "toys.jpg,games.jpg"
}
```

### 🔄 UPDATE Operation

#### 7. Cập nhật category
```http
PUT /api/category/{id}
Content-Type: application/json

{
  "name": "Đồ chơi & Giải trí",
  "images": "toys.jpg,games.jpg,entertainment.jpg"
}
```

### 🗑️ DELETE Operation

#### 8. Xóa category
```http
DELETE /api/category/{id}
```

---

## 🧪 Testing với cURL

### User APIs
```bash
# Get all users
curl -X GET http://localhost:7002/api/user

# Get user by ID
curl -X GET http://localhost:7002/api/user/1

# Create new user
curl -X POST http://localhost:7002/api/user \
  -H "Content-Type: application/json" \
  -d '{
    "fullname": "Test User",
    "email": "test@example.com",
    "password": "password123",
    "phone": "0901234567"
  }'

# Update user
curl -X PUT http://localhost:7002/api/user/1 \
  -H "Content-Type: application/json" \
  -d '{
    "fullname": "Updated User",
    "email": "updated@example.com",
    "password": "newpassword",
    "phone": "0987654321"
  }'

# Delete user
curl -X DELETE http://localhost:7002/api/user/1
```

### Product APIs
```bash
# Get all products
curl -X GET http://localhost:7002/api/product

# Create new product
curl -X POST http://localhost:7002/api/product \
  -H "Content-Type: application/json" \
  -d '{
    "title": "New Product",
    "quantity": 10,
    "description": "Test product",
    "price": 100000.00,
    "images": "test.jpg",
    "userId": 1
  }'

# Update product quantity
curl -X PATCH "http://localhost:7002/api/product/1/quantity?quantity=50"

# Search products
curl -X GET "http://localhost:7002/api/product/search?title=iPhone&minPrice=1000000"
```

### Category APIs
```bash
# Get all categories
curl -X GET http://localhost:7002/api/category

# Create new category
curl -X POST http://localhost:7002/api/category \
  -H "Content-Type: application/json" \
  -d '{
    "name": "New Category",
    "images": "category.jpg"
  }'

# Update category
curl -X PUT http://localhost:7002/api/category/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Updated Category",
    "images": "updated.jpg"
  }'
```

## 📊 Response Formats

### Success Responses
```json
// Single entity
{
  "id": 1,
  "name": "Entity Name",
  "...": "other fields"
}

// List of entities
[
  {
    "id": 1,
    "name": "Entity 1"
  },
  {
    "id": 2, 
    "name": "Entity 2"
  }
]
```

### Error Responses
```json
// 404 Not Found
{
  "timestamp": "2025-09-27T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Entity not found with id: 1"
}

// 400 Bad Request
{
  "timestamp": "2025-09-27T10:30:00",
  "status": 400,
  "error": "Bad Request", 
  "message": "Validation failed"
}
```

## 🔒 HTTP Status Codes

- **200 OK** - Success (GET, PUT, PATCH)
- **201 Created** - Success (POST)
- **204 No Content** - Success (DELETE)
- **400 Bad Request** - Invalid input
- **404 Not Found** - Entity not found
- **500 Internal Server Error** - Server error

## 💡 Best Practices

1. **Input Validation**: Tất cả input được validate
2. **Error Handling**: Proper error responses
3. **RESTful URLs**: Follow REST conventions
4. **CORS Enabled**: Allow cross-origin requests
5. **Consistent Responses**: Standard response formats
6. **Status Codes**: Appropriate HTTP status codes

## 🎯 Use Cases

### E-commerce Workflow
1. **Quản lý Users**: Register, login, profile management
2. **Quản lý Products**: CRUD products, inventory management  
3. **Quản lý Categories**: Organize products by categories
4. **Relationships**: Link users to categories, products to users

### Admin Dashboard
- Quản lý tất cả entities
- Monitor inventory (low stock products)  
- User management và category assignment
- Product analytics và reporting