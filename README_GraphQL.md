# Product GraphQL API with Spring Boot 3

Ứng dụng quản lý sản phẩm với GraphQL và REST API sử dụng Spring Boot 3.

## 🚀 Tính năng

- **GraphQL API**: Complete CRUD operations cho Product, User, Category
- **REST API**: Complete CRUD operations cho tất cả entities
- **Spring Boot 3**: Framework hiện đại
- **Spring Data JPA**: ORM mapping với relationships
- **MySQL Database**: Cơ sở dữ liệu production
- **H2 Database**: Cơ sở dữ liệu test
- **GraphiQL**: Giao diện test GraphQL tích hợp
- **CORS Support**: Cross-origin resource sharing
- **Input Validation**: Data validation và error handling

## 📋 Yêu cầu hệ thống

- Java 17+
- Maven 3.6+
- MySQL 8.0+ (hoặc sử dụng H2 cho test)

## 🛠️ Cài đặt và chạy

### 1. Clone project
```bash
cd "c:\Users\ASUS\OneDrive - hcmute.edu.vn\Desktop\BaiTap"
```

### 2. Cấu hình database
Tạo database MySQL:
```sql
CREATE DATABASE product_management;
```

Hoặc sử dụng H2 (uncomment trong application.properties)

### 3. Chạy ứng dụng
```bash
mvn spring-boot:run
```

Ứng dụng sẽ chạy tại: `http://localhost:7002`

## 🎯 API Endpoints

### GraphQL
- **GraphQL Endpoint**: `http://localhost:7002/graphql`
- **GraphiQL UI**: `http://localhost:7002/graphiql`

### REST API
- **Users**: `http://localhost:7002/api/user`
- **Products**: `http://localhost:7002/api/product`
- **Categories**: `http://localhost:7002/api/category`

## 🔍 GraphQL Queries và Mutations

### Queries cơ bản

#### Lấy tất cả sản phẩm
```graphql
query {
  products {
    id
    title
    quantity
    price
    description
    images
    user {
      id
      fullname
      email
    }
  }
}
```

#### Lấy sản phẩm theo ID
```graphql
query {
  productById(id: "1") {
    id
    title
    description
    price
    quantity
    user {
      fullname
      email
    }
  }
}
```

#### Tìm kiếm sản phẩm
```graphql
query {
  searchProducts(title: "iPhone", minPrice: 1000000, maxPrice: 50000000) {
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

#### Lấy tất cả người dùng với categories
```graphql
query {
  users {
    id
    fullname
    email
    phone
    categories {
      id
      name
    }
    products {
      id
      title
      price
    }
  }
}
```

### Mutations

#### Tạo sản phẩm mới
```graphql
mutation {
  createProduct(input: {
    title: "Laptop Dell XPS 13"
    quantity: 15
    description: "Laptop cao cấp cho doanh nhân"
    price: 25990000.00
    images: "laptop_dell.jpg"
    userId: "1"
  }) {
    id
    title
    price
    user {
      fullname
    }
  }
}
```

#### Cập nhật sản phẩm
```graphql
mutation {
  updateProduct(id: "1", input: {
    title: "iPhone 15 Pro Max Updated"
    quantity: 45
    price: 28990000.00
  }) {
    id
    title
    quantity
    price
  }
}
```

#### Tạo user mới
```graphql
mutation {
  createUser(input: {
    fullname: "Nguyễn Văn Test"
    email: "test@example.com"
    password: "123456"
    phone: "0901234567"
  }) {
    id
    fullname
    email
    phone
  }
}
```

#### Thêm categories cho user
```graphql
mutation {
  addCategoriesToUser(userId: "1", categoryIds: ["1", "2"]) {
    id
    fullname
    categories {
      id
      name
    }
  }
}
```

### Queries nâng cao

#### Lấy sản phẩm sắp hết hàng
```graphql
query {
  lowStockProducts(threshold: 30) {
    id
    title
    quantity
    price
    user {
      fullname
      phone
    }
  }
}
```

#### Lấy sản phẩm sắp xếp theo giá từ thấp đến cao
```graphql
query {
  productsByPriceAsc {
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

#### Lấy sản phẩm sắp xếp theo giá từ cao đến thấp
```graphql
query {
  productsByPriceDesc {
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

#### Lấy users theo category
```graphql
query {
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

#### Lấy sản phẩm theo category
```graphql
query {
  productsByCategory(categoryId: "1") {
    id
    title
    price
    quantity
    user {
      fullname
      categories {
        name
      }
    }
  }
}
```

#### Lấy sản phẩm theo category sắp xếp theo giá
```graphql
query {
  productsByCategoryOrderByPrice(categoryId: "1") {
    id
    title
    price
    user {
      fullname
    }
  }
}
```

## 🌐 REST API Examples

### GET - Lấy tất cả sản phẩm
```bash
curl -X GET http://localhost:7002/api/product
```

### GET - Lấy sản phẩm theo ID
```bash
curl -X GET http://localhost:7002/api/product/1
```

### POST - Tạo sản phẩm mới
```bash
curl -X POST http://localhost:7002/api/product \
-H "Content-Type: application/json" \
-d '{
  "title": "Máy tính bảng iPad Air",
  "quantity": 25,
  "description": "Máy tính bảng cao cấp",
  "price": 15990000.00,
  "images": "ipad_air.jpg",
  "userId": 1
}'
```

### GET - Tìm kiếm sản phẩm
```bash
curl -X GET "http://localhost:7002/api/product/search?title=iPhone&minPrice=1000000&maxPrice=50000000"
```

### GET - Lấy sản phẩm theo giá từ thấp đến cao
```bash
curl -X GET http://localhost:7002/api/product/sort/price-asc
```

### GET - Lấy sản phẩm theo giá từ cao đến thấp
```bash
curl -X GET http://localhost:7002/api/product/sort/price-desc
```

### GET - Lấy sản phẩm theo category
```bash
curl -X GET http://localhost:7002/api/product/category/1
```

### GET - Lấy sản phẩm theo category sắp xếp theo giá
```bash
curl -X GET http://localhost:7002/api/product/category/1/sort/price-asc
```

## 📊 Database Schema

Xem file `database_schema.sql` để hiểu cấu trúc database đầy đủ.

### Entities chính:
- **Category**: Danh mục sản phẩm
- **User**: Người dùng/Người bán  
- **Product**: Sản phẩm
- **User_Category**: Quan hệ nhiều-nhiều giữa User và Category

## 🧪 Testing với GraphiQL

1. Mở trình duyệt và truy cập: `http://localhost:7002/graphiql`
2. Sử dụng interface để test các queries và mutations
3. Xem schema documentation ở sidebar bên phải

## 🔧 Configuration

### Database Configuration
File `application.properties`:
- MySQL: Port 3306, Database: `product_management`
- H2: In-memory database cho testing
- JPA: Auto DDL, Show SQL

### GraphQL Configuration
- GraphiQL enabled
- Path: `/graphql`
- GraphiQL UI: `/graphiql`

## 📝 Lưu ý

1. **Bảo mật**: Password không được hash trong demo này
2. **Validation**: Đã có validation cho input
3. **Error Handling**: Exception handling cơ bản
4. **Logging**: Debug level cho GraphQL và application

## 🔄 So sánh GraphQL vs REST

### Ưu điểm GraphQL:
- Chỉ lấy dữ liệu cần thiết (no over-fetching)
- Single endpoint
- Strong type system
- Real-time với subscriptions
- Powerful query language

### Ưu điểm REST:
- Đơn giản, dễ hiểu
- Caching tốt hơn
- Tooling phong phú
- Stateless

Trong project này, bạn có thể thấy và so sánh cả hai approach!