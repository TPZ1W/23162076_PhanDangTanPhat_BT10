# GraphQL Sample Queries & Mutations

## 🔍 QUERIES

### 1. Lấy tất cả sản phẩm với thông tin user
```graphql
query GetAllProducts {
  products {
    id
    title
    quantity
    description
    price
    images
    user {
      id
      fullname
      email
      phone
    }
  }
}
```

### 2. Lấy sản phẩm theo ID
```graphql
query GetProductById {
  productById(id: "1") {
    id
    title
    description
    price
    quantity
    images
    user {
      fullname
      email
      categories {
        name
      }
    }
  }
}
```

### 3. Tìm kiếm sản phẩm theo tiêu chí
```graphql
query SearchProducts {
  searchProducts(
    title: "iPhone"
    minPrice: 1000000
    maxPrice: 50000000
  ) {
    id
    title
    price
    quantity
    description
    user {
      fullname
      phone
    }
  }
}
```

### 4. Lấy sản phẩm sắp hết hàng
```graphql
query LowStockProducts {
  lowStockProducts(threshold: 30) {
    id
    title
    quantity
    price
    user {
      fullname
      phone
      email
    }
  }
}
```

### 5. Lấy tất cả sản phẩm sắp xếp theo giá từ thấp đến cao
```graphql
query ProductsByPriceAsc {
  productsByPriceAsc {
    id
    title
    price
    quantity
    description
    user {
      fullname
      email
    }
  }
}
```

### 6. Lấy tất cả sản phẩm sắp xếp theo giá từ cao đến thấp
```graphql
query ProductsByPriceDesc {
  productsByPriceDesc {
    id
    title
    price
    quantity
    description
    user {
      fullname
      email
    }
  }
}
```

### 7. Lấy tất cả users với categories và products
```graphql
query GetAllUsers {
  users {
    id
    fullname
    email
    phone
    categories {
      id
      name
      images
    }
    products {
      id
      title
      price
      quantity
    }
  }
}
```

### 6. Lấy user theo email
```graphql
query GetUserByEmail {
  userByEmail(email: "nguyenvanan@email.com") {
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
      quantity
    }
  }
}
```

### 7. Lấy tất cả categories
```graphql
query GetAllCategories {
  categories {
    id
    name
    images
    users {
      fullname
      email
    }
  }
}
```

### 8. Lấy users quan tâm đến category cụ thể
```graphql
query GetUsersByCategory {
  usersByCategory(categoryId: "1") {
    id
    fullname
    email
    categories {
      name
    }
  }
}
```

### 9. Lấy tất cả sản phẩm của một category
```graphql
query ProductsByCategory {
  productsByCategory(categoryId: "1") {
    id
    title
    price
    quantity
    description
    user {
      fullname
      email
      categories {
        name
      }
    }
  }
}
```

### 10. Lấy sản phẩm của category sắp xếp theo giá
```graphql
query ProductsByCategoryOrderByPrice {
  productsByCategoryOrderByPrice(categoryId: "1") {
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

## ✏️ MUTATIONS

### 1. Tạo sản phẩm mới
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
      email
    }
  }
}
```

### 2. Cập nhật sản phẩm
```graphql
mutation UpdateProduct {
  updateProduct(id: "1", input: {
    title: "iPhone 15 Pro Max - Updated"
    quantity: 35
    price: 27990000.00
    description: "Điện thoại flagship mới nhất từ Apple - Cập nhật"
  }) {
    id
    title
    quantity
    price
    description
  }
}
```

### 3. Cập nhật số lượng sản phẩm
```graphql
mutation UpdateQuantity {
  updateProductQuantity(id: "1", quantity: 40) {
    id
    title
    quantity
    user {
      fullname
    }
  }
}
```

### 4. Tạo user mới
```graphql
mutation CreateUser {
  createUser(input: {
    fullname: "Lý Minh Khang"
    email: "lyminhkhang@email.com"
    password: "password123"
    phone: "0987654321"
  }) {
    id
    fullname
    email
    phone
  }
}
```

### 5. Cập nhật thông tin user
```graphql
mutation UpdateUser {
  updateUser(id: "1", input: {
    fullname: "Nguyễn Văn An - Updated"
    phone: "0901234999"
  }) {
    id
    fullname
    email
    phone
  }
}
```

### 6. Thêm categories cho user
```graphql
mutation AddCategoriesToUser {
  addCategoriesToUser(userId: "6", categoryIds: ["1", "3", "5"]) {
    id
    fullname
    categories {
      id
      name
    }
  }
}
```

### 7. Xóa category khỏi user
```graphql
mutation RemoveCategoryFromUser {
  removeCategoryFromUser(userId: "1", categoryId: "3") {
    id
    fullname
    categories {
      name
    }
  }
}
```

### 8. Tạo category mới
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

### 9. Cập nhật category
```graphql
mutation UpdateCategory {
  updateCategory(id: "1", input: {
    name: "Điện tử & Công nghệ"
    images: "electronics.jpg,tech.jpg,phones.jpg"
  }) {
    id
    name
    images
  }
}
```

### 10. Xóa sản phẩm
```graphql
mutation DeleteProduct {
  deleteProduct(id: "1")
}
```

## 🔄 COMPLEX QUERIES

### Lấy thống kê tổng quan
```graphql
query GetDashboardStats {
  categories {
    id
    name
    users {
      id
    }
  }
  
  users {
    id
    fullname
    products {
      id
      price
      quantity
    }
  }
}
```

### Nested query với nhiều thông tin
```graphql
query ComplexQuery {
  productsByUser(userId: "1") {
    id
    title
    price
    quantity
    user {
      fullname
      email
      categories {
        name
        users {
          fullname
        }
      }
    }
  }
}
```

## 📝 Lưu ý khi test

1. **GraphiQL Interface**: Truy cập `http://localhost:7002/graphiql`
2. **Auto-complete**: Sử dụng Ctrl+Space để xem suggestions
3. **Schema Explorer**: Click vào "Docs" để xem schema
4. **Variables**: Sử dụng tab Variables cho dynamic queries
5. **Headers**: Thêm authentication headers nếu cần

## 🎯 Testing Tips

- Bắt đầu với queries đơn giản trước
- Test từng field một để hiểu structure
- Sử dụng fragments cho reusable code
- Test error cases với invalid IDs
- Kiểm tra performance với large datasets