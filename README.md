# Hệ Thống Quản Lý Sản Phẩm

**Sinh viên:** Trình Khôi Nguyên  
**MSSV:** 23162068  
**Bài tập:** Bài tập 9 - Phát triển ứng dụng web với Spring Boot và GraphQL

---

## 📋 Mô tả dự án

Đây là một ứng dụng web quản lý sản phẩm được xây dựng bằng **Spring Boot 3** và **GraphQL**, hỗ trợ cả REST API và GraphQL API. Ứng dụng cung cấp giao diện web để quản lý người dùng, sản phẩm và danh mục sản phẩm.

## 🛠️ Công nghệ sử dụng

- **Backend Framework**: Spring Boot 3.2.0
- **Database**: H2 (In-memory database)
- **API**: REST API + GraphQL
- **ORM**: Spring Data JPA / Hibernate
- **Frontend**: JSP + Bootstrap + jQuery + AJAX
- **Build Tool**: Maven
- **Java Version**: 17

## 🏗️ Kiến trúc ứng dụng

```
src/
├── main/
│   ├── java/com/example/product/
│   │   ├── config/          # Cấu hình ứng dụng
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── entity/         # JPA Entities
│   │   ├── graphql/        # GraphQL Controllers
│   │   ├── repository/     # Data Access Layer
│   │   ├── rest/          # REST API Controllers
│   │   ├── service/       # Business Logic Layer
│   │   └── web/           # Web Controllers (JSP)
│   ├── resources/
│   │   ├── static/        # CSS, JS, Images
│   │   ├── graphql/       # GraphQL Schema
│   │   └── application.properties
│   └── webapp/WEB-INF/jsp/ # JSP Views
```

## 🚀 Cách chạy ứng dụng

### Yêu cầu hệ thống
- Java 17 hoặc cao hơn
- Maven 3.6+
- Port 7002 phải trống

### Các bước thực hiện

1. **Clone hoặc tải xuống dự án**
   ```bash
   git clone [repository-url]
   cd BaiTap
   ```

2. **Chạy ứng dụng**
   ```bash
   mvn spring-boot:run
   ```

3. **Truy cập ứng dụng**
   - Mở trình duyệt và truy cập: http://localhost:7002

## 🌐 Các trang web có sẵn

| Trang | URL | Mô tả |
|-------|-----|-------|
| **Quản lý sản phẩm** | http://localhost:7002/products | Trang chính - CRUD sản phẩm |
| **Quản lý người dùng** | http://localhost:7002/users | Quản lý thông tin người dùng |
| **Quản lý danh mục** | http://localhost:7002/categories | Quản lý danh mục sản phẩm |
| **Dashboard** | http://localhost:7002/dashboard | Tổng quan hệ thống |
| **H2 Console** | http://localhost:7002/h2-console | Truy cập database |
| **GraphQL Playground** | http://localhost:7002/graphql | API GraphQL |

### Thông tin kết nối H2 Database
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: (để trống)

## 🔌 API Endpoints

### REST API

#### Sản phẩm (Products)
- `GET /api/products` - Lấy danh sách sản phẩm
- `GET /api/products/{id}` - Lấy sản phẩm theo ID
- `POST /api/products` - Tạo sản phẩm mới
- `PUT /api/products/{id}` - Cập nhật sản phẩm
- `DELETE /api/products/{id}` - Xóa sản phẩm
- `GET /api/products/search` - Tìm kiếm sản phẩm

#### Người dùng (Users)
- `GET /api/users` - Lấy danh sách người dùng
- `GET /api/users/{id}` - Lấy người dùng theo ID
- `POST /api/users` - Tạo người dùng mới
- `PUT /api/users/{id}` - Cập nhật người dùng
- `DELETE /api/users/{id}` - Xóa người dùng

#### Danh mục (Categories)
- `GET /api/categories` - Lấy danh sách danh mục
- `GET /api/categories/{id}` - Lấy danh mục theo ID
- `POST /api/categories` - Tạo danh mục mới
- `PUT /api/categories/{id}` - Cập nhật danh mục
- `DELETE /api/categories/{id}` - Xóa danh mục

### GraphQL API

Truy cập GraphQL Playground tại: http://localhost:7002/graphql

**Ví dụ Query:**
```graphql
query {
  getAllProducts {
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

**Ví dụ Mutation:**
```graphql
mutation {
  createProduct(productInput: {
    title: "Sản phẩm mới"
    description: "Mô tả sản phẩm"
    price: 299000
    quantity: 10
    userId: 1
  }) {
    id
    title
    price
  }
}
```

## 💾 Dữ liệu mẫu

Ứng dụng tự động tạo dữ liệu mẫu khi khởi động:

### Người dùng mẫu
1. **Nguyễn Văn An** - admin@example.com
2. **Trần Thị Bình** - user@example.com
3. **Lê Văn Cường** - manager@example.com
4. **Phạm Thị Dung** - staff@example.com
5. **Hoàng Văn Em** - guest@example.com

### Danh mục mẫu
- Điện tử
- Thời trang
- Sách & Văn phòng phẩm
- Thể thao
- Nhà cửa & Đời sống

### Sản phẩm mẫu
- 10 sản phẩm đa dạng với giá từ 99,000đ - 15,999,000đ

## 🎯 Các tính năng chính

### 🖥️ Giao diện Web
- **Responsive Design**: Tương thích với mobile và desktop
- **AJAX Integration**: Tải dữ liệu không cần reload trang
- **Real-time Validation**: Kiểm tra dữ liệu trực tiếp
- **Bootstrap UI**: Giao diện hiện đại và thân thiện

### 📊 Quản lý dữ liệu
- **CRUD Operations**: Tạo, đọc, cập nhật, xóa cho tất cả entities
- **Search & Filter**: Tìm kiếm và lọc dữ liệu
- **Data Validation**: Kiểm tra tính hợp lệ của dữ liệu
- **Relationship Management**: Quản lý mối quan hệ giữa các bảng

### 🔗 API Support
- **REST API**: Đầy đủ endpoints cho mobile/web integration
- **GraphQL API**: Query linh hoạt, chỉ lấy dữ liệu cần thiết
- **CORS Support**: Hỗ trợ cross-origin requests
- **DTO Pattern**: Tối ưu hóa data transfer

## 🗂️ Cấu trúc Database

### Bảng Users (Người dùng)
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    fullname VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
```

### Bảng Products (Sản phẩm)
```sql
CREATE TABLE products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    quantity INTEGER NOT NULL,
    images VARCHAR(255),
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

### Bảng Categories (Danh mục)
```sql
CREATE TABLE categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    images TEXT
);
```

### Bảng User_Categories (Quan hệ nhiều-nhiều)
```sql
CREATE TABLE user_categories (
    user_id BIGINT,
    category_id BIGINT,
    PRIMARY KEY (user_id, category_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);
```

## ⚙️ Cấu hình

### Application Properties
```properties
# Server Configuration
server.port=7002

# Database Configuration
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true

# JPA Configuration
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# JSP Configuration
spring.mvc.view.prefix=/WEB-INF/jsp/
spring.mvc.view.suffix=.jsp
```

## 🛠️ Troubleshooting

### Các lỗi thường gặp

1. **Port 7002 đang được sử dụng**
   ```bash
   # Kiểm tra process đang dùng port
   netstat -ano | findstr :7002
   # Hoặc thay đổi port trong application.properties
   server.port=8080
   ```

2. **Java version không tương thích**
   ```bash
   java -version  # Kiểm tra version hiện tại
   # Cần Java 17 trở lên
   ```

3. **Maven build failed**
   ```bash
   mvn clean install  # Clean và build lại
   ```

4. **Database connection error**
   - Kiểm tra H2 console tại: http://localhost:7002/h2-console
   - JDBC URL: `jdbc:h2:mem:testdb`

## 📞 Hỗ trợ

**Sinh viên thực hiện:** Trình Khôi Nguyên  
**MSSV:** 23162068  
**Email:** [student-email]  
**Bài tập:** Bài tập 9 - Phát triển ứng dụng web với Spring Boot và GraphQL

---

## 📄 License

Dự án này được phát triển cho mục đích học tập tại Trường Đại học Sư phạm Kỹ thuật TP.HCM.

© 2025 - Trình Khôi Nguyên - MSSV: 23162068