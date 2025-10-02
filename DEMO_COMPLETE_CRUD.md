# Demo: Complete CRUD Operations

## 🎯 Tổng quan CRUD

Hệ thống đã được tích hợp đầy đủ các tính năng CRUD (Create, Read, Update, Delete) cho cả **GraphQL** và **REST API**.

### 📊 Entities được hỗ trợ:
- **👤 User** - Quản lý người dùng
- **🛍️ Product** - Quản lý sản phẩm  
- **📂 Category** - Quản lý danh mục

---

## 🔍 GraphQL CRUD Examples

### 👤 USER CRUD

#### CREATE User
```graphql
mutation CreateUser {
  createUser(input: {
    fullname: "Nguyễn Thị Test"
    email: "test@example.com"
    password: "password123"
    phone: "0901234567"
  }) {
    id
    fullname
    email
    phone
  }
}
```

#### READ Users
```graphql
# Lấy tất cả users
query GetAllUsers {
  users {
    id
    fullname
    email
    phone
    categories {
      name
    }
    products {
      title
      price
    }
  }
}

# Lấy user theo ID
query GetUserById {
  userById(id: "1") {
    id
    fullname
    email
    categories {
      name
    }
  }
}

# Tìm kiếm users
query SearchUsers {
  searchUsers(name: "Nguyen") {
    id
    fullname
    email
  }
}
```

#### UPDATE User
```graphql
mutation UpdateUser {
  updateUser(id: "1", input: {
    fullname: "Nguyễn Văn An Updated"
    email: "anupdated@example.com"
    password: "newpassword"
    phone: "0987654321"
  }) {
    id
    fullname
    email
    phone
  }
}

# Thêm categories cho user
mutation AddCategoriesToUser {
  addCategoriesToUser(userId: "1", categoryIds: ["1", "2", "3"]) {
    id
    fullname
    categories {
      id
      name
    }
  }
}
```

#### DELETE User
```graphql
mutation DeleteUser {
  deleteUser(id: "1")
}
```

### 📂 CATEGORY CRUD

#### CREATE Category
```graphql
mutation CreateCategory {
  createCategory(input: {
    name: "Đồ chơi"
    images: "toys.jpg,games.jpg"
  }) {
    id
    name
    images
  }
}
```

#### READ Categories
```graphql
# Lấy tất cả categories
query GetAllCategories {
  categories {
    id
    name
    images
    users {
      fullname
    }
  }
}

# Lấy category theo tên
query GetCategoryByName {
  categoryByName(name: "Điện tử") {
    id
    name
    users {
      fullname
    }
  }
}
```

#### UPDATE Category
```graphql
mutation UpdateCategory {
  updateCategory(id: "1", input: {
    name: "Điện tử & Công nghệ"
    images: "electronics.jpg,tech.jpg,gadgets.jpg"
  }) {
    id
    name
    images
  }
}
```

#### DELETE Category
```graphql
mutation DeleteCategory {
  deleteCategory(id: "6")
}
```

### 🛍️ PRODUCT CRUD

#### CREATE Product
```graphql
mutation CreateProduct {
  createProduct(input: {
    title: "MacBook Pro M3"
    quantity: 20
    description: "Laptop cao cấp với chip M3"
    price: 45990000.00
    images: "macbook_m3.jpg"
    userId: "1"
  }) {
    id
    title
    price
    quantity
    user {
      fullname
    }
  }
}
```

#### READ Products
```graphql
# Lấy tất cả products
query GetAllProducts {
  products {
    id
    title
    price
    quantity
    description
    user {
      fullname
    }
  }
}

# Lấy products theo user
query GetProductsByUser {
  productsByUser(userId: "1") {
    id
    title
    price
    quantity
  }
}

# Tìm kiếm products
query SearchProducts {
  searchProducts(
    title: "iPhone"
    minPrice: 1000000
    maxPrice: 50000000
    userId: 1
  ) {
    id
    title
    price
    user {
      fullname
    }
  }
}
```

#### UPDATE Product
```graphql
mutation UpdateProduct {
  updateProduct(id: "1", input: {
    title: "iPhone 15 Pro Max - Sale Off"
    quantity: 40
    description: "Khuyến mãi đặc biệt"
    price: 27990000.00
    images: "iphone15_sale.jpg"
  }) {
    id
    title
    price
    quantity
    description
  }
}

# Cập nhật chỉ số lượng
mutation UpdateProductQuantity {
  updateProductQuantity(id: "1", quantity: 25) {
    id
    title
    quantity
  }
}
```

#### DELETE Product
```graphql
mutation DeleteProduct {
  deleteProduct(id: "1")
}
```

---

## 🌐 REST API CRUD Examples

### 👤 USER REST APIs

```bash
# CREATE User
curl -X POST http://localhost:7002/api/user \
  -H "Content-Type: application/json" \
  -d '{
    "fullname": "REST Test User",
    "email": "rest@example.com",
    "password": "password123",
    "phone": "0901234567"
  }'

# READ Users
curl -X GET http://localhost:7002/api/user
curl -X GET http://localhost:7002/api/user/1
curl -X GET http://localhost:7002/api/user/email/test@example.com
curl -X GET http://localhost:7002/api/user/search?name=Nguyen

# UPDATE User
curl -X PUT http://localhost:7002/api/user/1 \
  -H "Content-Type: application/json" \
  -d '{
    "fullname": "Updated REST User",
    "email": "updated@example.com",
    "password": "newpassword",
    "phone": "0987654321"
  }'

# DELETE User
curl -X DELETE http://localhost:7002/api/user/1
```

### 📂 CATEGORY REST APIs

```bash
# CREATE Category
curl -X POST http://localhost:7002/api/category \
  -H "Content-Type: application/json" \
  -d '{
    "name": "REST Category",
    "images": "rest.jpg"
  }'

# READ Categories
curl -X GET http://localhost:7002/api/category
curl -X GET http://localhost:7002/api/category/1
curl -X GET http://localhost:7002/api/category/name/Điện%20tử

# UPDATE Category
curl -X PUT http://localhost:7002/api/category/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Updated REST Category",
    "images": "updated_rest.jpg"
  }'

# DELETE Category
curl -X DELETE http://localhost:7002/api/category/1
```

### 🛍️ PRODUCT REST APIs

```bash
# CREATE Product
curl -X POST http://localhost:7002/api/product \
  -H "Content-Type: application/json" \
  -d '{
    "title": "REST Product",
    "quantity": 15,
    "description": "Product created via REST",
    "price": 500000.00,
    "images": "rest_product.jpg",
    "userId": 1
  }'

# READ Products
curl -X GET http://localhost:7002/api/product
curl -X GET http://localhost:7002/api/product/1
curl -X GET http://localhost:7002/api/product/user/1
curl -X GET http://localhost:7002/api/product/category/1
curl -X GET "http://localhost:7002/api/product/search?title=iPhone"

# UPDATE Product
curl -X PUT http://localhost:7002/api/product/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Updated REST Product",
    "quantity": 20,
    "description": "Updated description",
    "price": 600000.00,
    "images": "updated_rest.jpg"
  }'

# UPDATE Product Quantity only
curl -X PATCH "http://localhost:7002/api/product/1/quantity?quantity=30"

# DELETE Product
curl -X DELETE http://localhost:7002/api/product/1
```

---

## 🧪 Testing Workflow

### 1. Setup Environment
```bash
# Start application
mvn spring-boot:run

# Verify endpoints
curl -X GET http://localhost:7002/api/user
curl -X GET http://localhost:7002/graphiql
```

### 2. Test User CRUD
```bash
# 1. Create user
USER_ID=$(curl -X POST http://localhost:7002/api/user \
  -H "Content-Type: application/json" \
  -d '{"fullname":"Test User","email":"test@example.com","password":"pass","phone":"123"}' \
  | jq -r '.id')

# 2. Get user
curl -X GET http://localhost:7002/api/user/$USER_ID

# 3. Update user  
curl -X PUT http://localhost:7002/api/user/$USER_ID \
  -H "Content-Type: application/json" \
  -d '{"fullname":"Updated User","email":"updated@example.com","password":"newpass","phone":"456"}'

# 4. Delete user
curl -X DELETE http://localhost:7002/api/user/$USER_ID
```

### 3. Test Category CRUD
```bash
# Similar workflow for categories
CATEGORY_ID=$(curl -X POST http://localhost:7002/api/category \
  -H "Content-Type: application/json" \
  -d '{"name":"Test Category","images":"test.jpg"}' \
  | jq -r '.id')

curl -X GET http://localhost:7002/api/category/$CATEGORY_ID
curl -X PUT http://localhost:7002/api/category/$CATEGORY_ID \
  -H "Content-Type: application/json" \
  -d '{"name":"Updated Category","images":"updated.jpg"}'
curl -X DELETE http://localhost:7002/api/category/$CATEGORY_ID
```

### 4. Test Product CRUD
```bash
# Create product with existing user
PRODUCT_ID=$(curl -X POST http://localhost:7002/api/product \
  -H "Content-Type: application/json" \
  -d '{"title":"Test Product","quantity":10,"description":"Test","price":100000.00,"images":"test.jpg","userId":1}' \
  | jq -r '.id')

curl -X GET http://localhost:7002/api/product/$PRODUCT_ID
curl -X PATCH "http://localhost:7002/api/product/$PRODUCT_ID/quantity?quantity=5"
curl -X DELETE http://localhost:7002/api/product/$PRODUCT_ID
```

---

## 🎯 Advanced Use Cases

### E-commerce Scenario
```bash
# 1. Create customer
curl -X POST http://localhost:7002/api/user \
  -H "Content-Type: application/json" \
  -d '{"fullname":"John Doe","email":"john@example.com","password":"pass123","phone":"0901111111"}'

# 2. Create categories
curl -X POST http://localhost:7002/api/category \
  -H "Content-Type: application/json" \
  -d '{"name":"Smartphones","images":"phones.jpg"}'

# 3. Add products
curl -X POST http://localhost:7002/api/product \
  -H "Content-Type: application/json" \
  -d '{"title":"iPhone 15","quantity":50,"description":"Latest iPhone","price":25000000.00,"images":"iphone15.jpg","userId":1}'

# 4. Search and filter
curl -X GET "http://localhost:7002/api/product/search?title=iPhone&minPrice=20000000&maxPrice=30000000"

# 5. Get products by category
curl -X GET http://localhost:7002/api/product/category/1
```

### Admin Dashboard Scenario
```graphql
# Get complete overview
query AdminDashboard {
  users {
    id
    fullname
    email
    categories { name }
    products { title price quantity }
  }
  
  categories {
    id
    name
    users { fullname }
  }
  
  lowStockProducts(threshold: 20) {
    title
    quantity
    user { fullname }
  }
}
```

---

## ✅ CRUD Features Completed

### 👤 User Entity
- ✅ **Create**: POST /api/user, createUser mutation
- ✅ **Read**: GET endpoints + GraphQL queries
- ✅ **Update**: PUT /api/user/{id}, updateUser mutation
- ✅ **Delete**: DELETE /api/user/{id}, deleteUser mutation
- ✅ **Relations**: Add/Remove categories

### 📂 Category Entity  
- ✅ **Create**: POST /api/category, createCategory mutation
- ✅ **Read**: GET endpoints + GraphQL queries
- ✅ **Update**: PUT /api/category/{id}, updateCategory mutation
- ✅ **Delete**: DELETE /api/category/{id}, deleteCategory mutation
- ✅ **Search**: By name, fuzzy search

### 🛍️ Product Entity
- ✅ **Create**: POST /api/product, createProduct mutation
- ✅ **Read**: GET endpoints + GraphQL queries
- ✅ **Update**: PUT /api/product/{id}, updateProduct mutation
- ✅ **Delete**: DELETE /api/product/{id}, deleteProduct mutation
- ✅ **Advanced**: Quantity update, search, filtering, sorting

### 🔗 Relationships
- ✅ **User ↔ Category**: Many-to-many
- ✅ **User → Product**: One-to-many
- ✅ **Category → Products**: Via users (many-to-many)

## 🎊 Ready for Production!

All CRUD operations are fully implemented and tested. The system supports both GraphQL and REST APIs with comprehensive error handling, validation, and documentation.