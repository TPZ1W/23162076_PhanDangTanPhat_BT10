# Demo: Hiển thị sản phẩm theo giá

## 📈 Tính năng mới đã được thêm

### 🔍 GraphQL Queries

#### 1. Lấy tất cả sản phẩm sắp xếp theo giá từ thấp đến cao
```graphql
query ProductsByPriceAsc {
  productsByPriceAsc {
    id
    title
    price
    quantity
    description
    images
    user {
      fullname
      email
      phone
    }
  }
}
```

#### 2. Lấy tất cả sản phẩm sắp xếp theo giá từ cao đến thấp  
```graphql
query ProductsByPriceDesc {
  productsByPriceDesc {
    id
    title
    price
    quantity
    description
    images
    user {
      fullname
      email
      phone
    }
  }
}
```

### 🌐 REST API Endpoints

#### 1. GET - Sản phẩm theo giá tăng dần
```bash
GET http://localhost:7002/api/product/sort/price-asc

# Với curl:
curl -X GET http://localhost:7002/api/product/sort/price-asc
```

#### 2. GET - Sản phẩm theo giá giảm dần
```bash
GET http://localhost:7002/api/product/sort/price-desc

# Với curl:
curl -X GET http://localhost:7002/api/product/sort/price-desc
```

### 🧪 Cách test

#### Test với GraphiQL:
1. Mở trình duyệt: `http://localhost:7002/graphiql`
2. Copy một trong các query ở trên
3. Click "Play" để thực thi

#### Test với REST API:
1. Sử dụng Postman, Insomnia hoặc curl
2. Gọi GET request đến các endpoint trên
3. Kiểm tra response trả về

### 📊 Kết quả mong đợi

Với dữ liệu mẫu trong hệ thống, bạn sẽ thấy sản phẩm được sắp xếp như sau:

**Giá từ thấp đến cao (productsByPriceAsc):**
1. Sách tiếng Anh giao tiếp - 119,000 VNĐ
2. Sách lập trình Python - 159,000 VNĐ  
3. Áo polo nam - 299,000 VNĐ
4. Bóng đá FIFA Quality - 450,000 VNĐ
5. Quần jeans nữ - 599,000 VNĐ
6. Máy xay sinh tố - 890,000 VNĐ
7. Nồi cơm điện Panasonic - 1,290,000 VNĐ
8. Giày chạy bộ Nike - 2,290,000 VNĐ
9. Samsung Galaxy S24 - 24,990,000 VNĐ
10. iPhone 15 Pro Max - 29,990,000 VNĐ

**Giá từ cao đến thấp (productsByPriceDesc):**
Thứ tự ngược lại với danh sách trên.

### 💡 Use Cases

**Tính năng này hữu ích cho:**
- 🛍️ Khách hàng muốn xem sản phẩm giá rẻ trước
- 💰 Khách hàng muốn xem sản phẩm đắt tiền trước  
- 📱 App mobile cần sắp xếp theo budget
- 🔍 Tìm kiếm và lọc sản phẩm theo giá
- 📊 Phân tích giá sản phẩm trong hệ thống

### 🔧 Implementation Details

**Các thay đổi đã thực hiện:**

1. **ProductRepository**: Thêm 2 query methods mới
   - `findAllOrderByPriceAsc()`
   - `findAllOrderByPriceDesc()`

2. **ProductService**: Thêm 2 service methods
   - `findAllOrderByPriceAsc()`
   - `findAllOrderByPriceDesc()`

3. **GraphQL Controller**: Thêm 2 query mappings
   - `productsByPriceAsc()`
   - `productsByPriceDesc()`

4. **REST Controller**: Thêm 2 endpoints
   - `GET /api/product/sort/price-asc`
   - `GET /api/product/sort/price-desc`

5. **GraphQL Schema**: Cập nhật với 2 queries mới
   - `productsByPriceAsc: [Product!]!`
   - `productsByPriceDesc: [Product!]!`

### ⚡ Performance Notes

- Queries sử dụng database-level sorting (ORDER BY)
- Hiệu suất tốt với index trên cột `price`
- Không cần sort trong memory
- Scalable với large datasets

### 🚀 Mở rộng thêm

**Có thể thêm các tính năng:**
- Sắp xếp theo tên (A-Z, Z-A)
- Sắp xếp theo số lượng (tồn kho)
- Sắp xếp theo ngày tạo (mới nhất, cũ nhất)
- Sắp xếp theo người bán
- Combine với pagination
- Combine với search filters