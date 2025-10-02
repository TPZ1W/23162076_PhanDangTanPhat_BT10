-- Entity Relationship Diagram (ERD) Description
-- Database: product_management

/*
┌─────────────────┐         ┌─────────────────┐         ┌─────────────────┐
│    Category     │         │  User_Category  │         │      User       │
├─────────────────┤         ├─────────────────┤         ├─────────────────┤
│ id (PK)         │◄────────┤ category_id(FK) │         │ id (PK)         │
│ name            │         │ user_id (FK)    ├────────►│ fullname        │
│ images          │         │ id (PK)         │         │ email           │
└─────────────────┘         │ created_at      │         │ password        │
                            └─────────────────┘         │ phone           │
                                                        └─────────────────┘
                                                                 │
                                                                 │ 1:N
                                                                 ▼
                                                        ┌─────────────────┐
                                                        │     Product     │
                                                        ├─────────────────┤
                                                        │ id (PK)         │
                                                        │ title           │
                                                        │ quantity        │
                                                        │ desc            │
                                                        │ price           │
                                                        │ userid (FK)     │
                                                        │ created_at      │
                                                        │ updated_at      │
                                                        └─────────────────┘

Mối quan hệ:
1. User ↔ Category: Nhiều-nhiều (M:N) thông qua bảng User_Category
   - Một User có thể quan tâm đến nhiều Category
   - Một Category có thể được nhiều User quan tâm

2. User → Product: Một-nhiều (1:N)
   - Một User có thể có nhiều Product
   - Một Product chỉ thuộc về một User (người bán)

Ràng buộc:
- Category.id: Primary Key, Auto Increment
- User.id: Primary Key, Auto Increment
- User.email: Unique
- Product.id: Primary Key, Auto Increment
- Product.userid: Foreign Key references User.id
- User_Category.user_id: Foreign Key references User.id
- User_Category.category_id: Foreign Key references Category.id
- User_Category: Unique constraint on (user_id, category_id)

Indexes được tạo:
- idx_product_userid: Tối ưu truy vấn sản phẩm theo user
- idx_user_email: Tối ưu tìm kiếm user theo email
- idx_user_category_user: Tối ưu truy vấn category của user
- idx_user_category_category: Tối ưu truy vấn user của category
*/