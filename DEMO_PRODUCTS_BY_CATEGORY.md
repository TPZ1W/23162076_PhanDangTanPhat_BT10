# Demo: Lấy tất cả sản phẩm theo Category

## 🎯 Tính năng mới: Products by Category

### 📝 Mô tả
Tính năng này cho phép lấy tất cả sản phẩm thuộc về một category cụ thể thông qua mối quan hệ:
**Product → User → Category**

### 🔍 GraphQL Queries

#### 1. Lấy tất cả sản phẩm của một category
```graphql
query ProductsByCategory {
  productsByCategory(categoryId: "1") {
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
      categories {
        name
      }
    }
  }
}
```

#### 2. Lấy sản phẩm của category sắp xếp theo giá (thấp → cao)
```graphql
query ProductsByCategoryOrderByPrice {
  productsByCategoryOrderByPrice(categoryId: "1") {
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

#### 3. Query kết hợp với thông tin category
```graphql
query ProductsWithCategoryInfo {
  # Lấy thông tin category trước
  categoryById(id: "1") {
    id
    name
    images
  }
  
  # Lấy sản phẩm của category này
  productsByCategory(categoryId: "1") {
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

### 🌐 REST API Endpoints

#### 1. GET - Sản phẩm theo category
```bash
GET http://localhost:7002/api/product/category/{categoryId}

# Ví dụ:
curl -X GET http://localhost:7002/api/product/category/1
```

#### 2. GET - Sản phẩm theo category sắp xếp theo giá
```bash
GET http://localhost:7002/api/product/category/{categoryId}/sort/price-asc

# Ví dụ:
curl -X GET http://localhost:7002/api/product/category/1/sort/price-asc
```

### 📊 Test với dữ liệu mẫu

Với dữ liệu mẫu trong hệ thống:

#### **Category ID = 1 (Điện tử)**
Users quan tâm: Nguyễn Văn An, Lê Văn Cường, Trần Thị Bình
```
Sản phẩm mong đợi:
- iPhone 15 Pro Max (29,990,000 VNĐ) - Nguyễn Văn An
- Samsung Galaxy S24 (24,990,000 VNĐ) - Nguyễn Văn An
- Nồi cơm điện Panasonic (1,290,000 VNĐ) - Lê Văn Cường
- Máy xay sinh tố (890,000 VNĐ) - Lê Văn Cường
- Áo polo nam (299,000 VNĐ) - Trần Thị Bình
- Quần jeans nữ (599,000 VNĐ) - Trần Thị Bình
```

#### **Category ID = 2 (Thời trang)**
Users quan tâm: Trần Thị Bình, Phạm Thị Dung, Lê Văn Cường
```
Sản phẩm mong đợi:
- Áo polo nam (299,000 VNĐ) - Trần Thị Bình
- Quần jeans nữ (599,000 VNĐ) - Trần Thị Bình
- Sách lập trình Python (159,000 VNĐ) - Phạm Thị Dung
- Sách tiếng Anh giao tiếp (119,000 VNĐ) - Phạm Thị Dung
- Nồi cơm điện Panasonic (1,290,000 VNĐ) - Lê Văn Cường
- Máy xay sinh tố (890,000 VNĐ) - Lê Văn Cường
```

### 🧪 Cách test

#### **Test với GraphiQL:**
1. Mở: `http://localhost:7002/graphiql`
2. Thử query với categoryId khác nhau (1, 2, 3, 4, 5)
3. Quan sát kết quả trả về

#### **Test với REST API:**
```bash
# Test category Điện tử (ID=1)
curl -X GET http://localhost:7002/api/product/category/1

# Test category Thời trang (ID=2)  
curl -X GET http://localhost:7002/api/product/category/2

# Test với sắp xếp theo giá
curl -X GET http://localhost:7002/api/product/category/1/sort/price-asc
```

#### **Test với Postman:**
```
GET http://localhost:7002/api/product/category/1
GET http://localhost:7002/api/product/category/2
GET http://localhost:7002/api/product/category/3
```

### 💡 Use Cases

#### **Ứng dụng thực tế:**
1. **🛍️ E-commerce**: Hiển thị sản phẩm theo danh mục
2. **📱 Mobile App**: Filter sản phẩm theo category 
3. **🔍 Search & Filter**: Tìm kiếm trong category cụ thể
4. **📊 Analytics**: Thống kê sản phẩm theo category
5. **🎯 Marketing**: Targeting customers theo sở thích

#### **Business Logic:**
- Khách hàng quan tâm category "Điện tử" sẽ thấy sản phẩm của sellers cũng quan tâm điện tử
- Tạo ecosystem người bán và người mua có cùng sở thích
- Gợi ý sản phẩm dựa trên category preferences

### 🔧 Technical Implementation

#### **Database Query Logic:**
```sql
SELECT p.* 
FROM Product p 
JOIN User u ON p.userid = u.id 
JOIN User_Category uc ON u.id = uc.user_id 
WHERE uc.category_id = :categoryId
ORDER BY p.price ASC
```

#### **GraphQL Resolver Chain:**
1. `ProductController.productsByCategory(categoryId)`
2. `ProductService.findByCategoryId(categoryId)`
3. `ProductRepository.findByCategoryId(categoryId)`
4. **JPA Query**: JOIN Product → User → User_Category → Category

### ⚡ Performance Considerations

#### **Optimizations:**
- ✅ Database-level JOIN (efficient)
- ✅ Index on User_Category table
- ✅ Lazy loading for User entity
- ✅ Query-level sorting (ORDER BY)

#### **Monitoring:**
- Monitor query execution time
- Check for N+1 query problems
- Consider caching for popular categories

### 🚀 Mở rộng tiếp theo

#### **Có thể thêm:**
1. **Pagination**: Phân trang cho category có nhiều sản phẩm
2. **Filter trong category**: Lọc theo giá, rating, etc.
3. **Related products**: Sản phẩm liên quan trong category
4. **Category hierarchy**: Sub-categories support
5. **User preference scoring**: Rank products by user interest level

#### **Advanced Queries:**
```graphql
# Sản phẩm trending trong category
productsByCategoryTrending(categoryId: "1", days: 7)

# Sản phẩm mới nhất trong category  
productsByCategoryNewest(categoryId: "1", limit: 10)

# Sản phẩm best seller trong category
productsByCategoryBestSeller(categoryId: "1")
```

### 🎯 Expected Results

Khi test với **categoryId = 1 (Điện tử)**, bạn sẽ thấy:
- Sản phẩm từ users quan tâm đến điện tử
- Sắp xếp theo giá khi dùng `productsByCategoryOrderByPrice`
- Thông tin đầy đủ về user và categories của họ