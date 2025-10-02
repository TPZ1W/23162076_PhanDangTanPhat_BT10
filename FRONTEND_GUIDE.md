# Hệ thống quản lý sản phẩm với AJAX và JSP

## 📋 Tổng quan

Đây là một hệ thống quản lý sản phẩm hoàn chỉnh được xây dựng với:
- **Backend**: Spring Boot 3 + GraphQL + REST API
- **Frontend**: JSP + AJAX + JavaScript
- **Database**: MySQL/H2
- **Build Tool**: Maven

## 🚀 Cách chạy ứng dụng

### 1. Yêu cầu hệ thống
- Java 17 hoặc cao hơn
- Maven 3.6+
- MySQL (tuỳ chọn, có thể dùng H2)

### 2. Khởi động ứng dụng

```bash
# Clone project và di chuyển vào thư mục
cd "c:\Users\ASUS\OneDrive - hcmute.edu.vn\Desktop\BaiTap"

# Chạy ứng dụng
mvn spring-boot:run
```

### 3. Truy cập ứng dụng

- **Giao diện web chính**: http://localhost:8080/web/dashboard
- **GraphQL Playground**: http://localhost:8080/graphiql
- **REST API**: http://localhost:8080/api/

## 🎯 Các tính năng chính

### 🌐 Giao diện Web (JSP + AJAX)

#### 📊 Dashboard (Trang tổng quan)
- URL: `/web/dashboard`
- Hiển thị thống kê tổng quan hệ thống
- Biểu đồ phân tích dữ liệu
- Danh sách sản phẩm mới nhất
- Top người dùng có nhiều sản phẩm
- Phân tích khoảng giá sản phẩm
- Tính năng tạo và tải báo cáo

#### 📦 Quản lý sản phẩm
- URL: `/web/products`
- ✅ Xem danh sách tất cả sản phẩm
- ✅ Thêm sản phẩm mới
- ✅ Sửa thông tin sản phẩm
- ✅ Xóa sản phẩm
- ✅ Tìm kiếm sản phẩm theo tên/mô tả
- ✅ Lọc theo danh mục
- ✅ Sắp xếp theo giá (thấp → cao, cao → thấp)
- ✅ Hiển thị thông tin người tạo

#### 👥 Quản lý người dùng
- URL: `/web/users`
- ✅ Xem danh sách người dùng
- ✅ Thêm người dùng mới
- ✅ Sửa thông tin người dùng
- ✅ Xóa người dùng
- ✅ Tìm kiếm theo tên, email, số điện thoại
- ✅ Xem danh sách sản phẩm của từng người dùng
- ✅ Hiển thị số lượng sản phẩm của mỗi người dùng

#### 🏷️ Quản lý danh mục
- URL: `/web/categories`
- ✅ Xem danh sách danh mục
- ✅ Thêm danh mục mới
- ✅ Sửa thông tin danh mục
- ✅ Xóa danh mục
- ✅ Tìm kiếm danh mục
- ✅ Hiển thị hình ảnh danh mục
- ✅ Xem người dùng trong từng danh mục

### 🔌 REST API Endpoints

#### Products
- `GET /api/products` - Lấy tất cả sản phẩm
- `GET /api/products/{id}` - Lấy sản phẩm theo ID
- `GET /api/products/user/{userId}` - Lấy sản phẩm theo người dùng
- `GET /api/products/sorted-by-price` - Lấy sản phẩm sắp xếp theo giá
- `POST /api/products` - Tạo sản phẩm mới
- `PUT /api/products/{id}` - Cập nhật sản phẩm
- `DELETE /api/products/{id}` - Xóa sản phẩm

#### Users
- `GET /api/users` - Lấy tất cả người dùng
- `GET /api/users/{id}` - Lấy người dùng theo ID
- `POST /api/users` - Tạo người dùng mới
- `PUT /api/users/{id}` - Cập nhật người dùng
- `DELETE /api/users/{id}` - Xóa người dùng

#### Categories
- `GET /api/categories` - Lấy tất cả danh mục
- `GET /api/categories/{id}` - Lấy danh mục theo ID
- `POST /api/categories` - Tạo danh mục mới
- `PUT /api/categories/{id}` - Cập nhật danh mục
- `DELETE /api/categories/{id}` - Xóa danh mục

### 🔍 GraphQL API

Truy cập GraphQL Playground tại: http://localhost:8080/graphiql

#### Queries
```graphql
# Lấy tất cả sản phẩm
query {
  getAllProducts {
    id
    title
    description
    price
    quantity
    userId
  }
}

# Lấy sản phẩm sắp xếp theo giá
query {
  getProductsSortedByPrice {
    id
    title
    price
  }
}

# Lấy tất cả người dùng
query {
  getAllUsers {
    id
    fullname
    email
    phone
  }
}

# Lấy tất cả danh mục
query {
  getAllCategories {
    id
    name
    images
  }
}
```

#### Mutations
```graphql
# Tạo sản phẩm mới
mutation {
  createProduct(input: {
    title: "Sản phẩm mới"
    description: "Mô tả sản phẩm"
    price: 100000
    quantity: 10
    userId: 1
  }) {
    id
    title
    price
  }
}

# Tạo người dùng mới
mutation {
  createUser(input: {
    fullname: "Nguyễn Văn A"
    email: "nguyenvana@example.com"
    password: "123456"
    phone: "0987654321"
  }) {
    id
    fullname
    email
  }
}
```

## 🛠️ Cấu trúc dự án

```
src/main/
├── java/com/example/product/
│   ├── controller/          # REST Controllers
│   ├── graphql/            # GraphQL Controllers
│   ├── web/                # Web Controllers (JSP)
│   ├── entity/             # JPA Entities
│   ├── repository/         # Data Repositories
│   ├── service/            # Business Logic
│   └── ProductApplication.java
├── resources/
│   ├── static/
│   │   ├── css/           # CSS files
│   │   └── js/            # JavaScript files
│   ├── graphql/           # GraphQL schema
│   └── application.properties
└── webapp/WEB-INF/jsp/    # JSP view files
```

## 📱 Giao diện người dùng

### Đặc điểm giao diện:
- ✅ Responsive design (tương thích mobile)
- ✅ Modern UI với gradient và shadow effects
- ✅ Loading states và error handling
- ✅ Form validation
- ✅ Modal dialogs
- ✅ Search và filter tức thời
- ✅ Animation và transitions
- ✅ Consistent color scheme

### Tính năng AJAX:
- ✅ Không reload trang khi thực hiện CRUD
- ✅ Real-time search và filter
- ✅ Asynchronous data loading
- ✅ Error handling với user feedback
- ✅ Loading indicators
- ✅ Success/error notifications

## 📊 Database Schema

### Bảng User
```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fullname VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20)
);
```

### Bảng Category
```sql
CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    images VARCHAR(500)
);
```

### Bảng Product
```sql
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    quantity INT NOT NULL,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

### Bảng quan hệ User-Category (Many-to-Many)
```sql
CREATE TABLE user_categories (
    user_id BIGINT,
    category_id BIGINT,
    PRIMARY KEY (user_id, category_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);
```

## 🔧 Configuration

### Application Properties
```properties
# Server Configuration
server.port=8080

# JSP Configuration
spring.mvc.view.prefix=/WEB-INF/jsp/
spring.mvc.view.suffix=.jsp
spring.web.resources.static-locations=classpath:/static/

# Database Configuration (H2 for demo)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password

# JPA Configuration
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# GraphQL Configuration
spring.graphql.graphiql.enabled=true
```

## 🧪 Testing

### Test với cURL:

```bash
# Lấy tất cả sản phẩm
curl -X GET http://localhost:8080/api/products

# Tạo sản phẩm mới
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"title":"Test Product","price":50000,"quantity":5,"userId":1}'
```

### Test với Postman:
- Import collection từ file `docs/postman/`
- Sử dụng các test cases có sẵn

## 🚀 Deployment

### Local Development:
```bash
mvn spring-boot:run
```

### Production Build:
```bash
mvn clean package
java -jar target/product-management-0.0.1-SNAPSHOT.jar
```

## 📝 Notes

1. **CORS đã được cấu hình** để hỗ trợ cross-origin requests
2. **Data được load tự động** khi khởi động ứng dụng
3. **H2 database** được sử dụng mặc định (có thể chuyển sang MySQL)
4. **JSP support** đã được cấu hình đầy đủ
5. **Static resources** được serve từ `/static/`

## 🔜 Tính năng có thể mở rộng

- [ ] Authentication và Authorization
- [ ] File upload cho hình ảnh sản phẩm
- [ ] Phân trang cho danh sách lớn
- [ ] Export/Import dữ liệu
- [ ] Real-time notifications
- [ ] Advanced search với filters
- [ ] Category-Product relationship management
- [ ] User role management

## 📞 Hỗ trợ

Nếu gặp vấn đề, hãy kiểm tra:
1. Java version (cần Java 17+)
2. Port 8080 có bị occupied không
3. Console logs để xem error details
4. Browser developer tools cho AJAX errors

Happy coding! 🎉