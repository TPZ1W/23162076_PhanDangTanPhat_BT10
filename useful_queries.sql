-- Các truy vấn SQL hữu ích cho database product_management
USE product_management;

-- 1. Lấy tất cả sản phẩm kèm thông tin người bán
SELECT 
    p.id,
    p.title,
    p.quantity,
    p.desc,
    p.price,
    u.fullname as seller_name,
    u.email as seller_email,
    u.phone as seller_phone
FROM Product p
JOIN User u ON p.userid = u.id
ORDER BY p.created_at DESC;

-- 2. Lấy danh sách category mà một user quan tâm
SELECT 
    u.fullname,
    u.email,
    c.name as category_name,
    c.images as category_images
FROM User u
JOIN User_Category uc ON u.id = uc.user_id
JOIN Category c ON uc.category_id = c.id
WHERE u.id = 1; -- Thay đổi ID user tùy ý

-- 3. Lấy tất cả user quan tâm đến một category cụ thể
SELECT 
    c.name as category_name,
    u.fullname,
    u.email,
    u.phone
FROM Category c
JOIN User_Category uc ON c.id = uc.category_id
JOIN User u ON uc.user_id = u.id
WHERE c.id = 1; -- Thay đổi ID category tùy ý

-- 4. Thống kê số lượng sản phẩm theo từng user
SELECT 
    u.fullname,
    u.email,
    COUNT(p.id) as total_products,
    SUM(p.quantity) as total_quantity,
    AVG(p.price) as avg_price
FROM User u
LEFT JOIN Product p ON u.id = p.userid
GROUP BY u.id, u.fullname, u.email
ORDER BY total_products DESC;

-- 5. Tìm sản phẩm trong khoảng giá
SELECT 
    p.title,
    p.price,
    p.quantity,
    u.fullname as seller_name
FROM Product p
JOIN User u ON p.userid = u.id
WHERE p.price BETWEEN 100000 AND 1000000
ORDER BY p.price ASC;

-- 6. Lấy danh sách user và số category họ quan tâm
SELECT 
    u.fullname,
    u.email,
    COUNT(uc.category_id) as interested_categories
FROM User u
LEFT JOIN User_Category uc ON u.id = uc.user_id
GROUP BY u.id, u.fullname, u.email
ORDER BY interested_categories DESC;

-- 7. Tìm sản phẩm có số lượng thấp (cần nhập thêm)
SELECT 
    p.title,
    p.quantity,
    p.price,
    u.fullname as seller_name
FROM Product p
JOIN User u ON p.userid = u.id
WHERE p.quantity < 50
ORDER BY p.quantity ASC;

-- 8. Cập nhật số lượng sản phẩm
-- UPDATE Product SET quantity = quantity - 1 WHERE id = 1; -- Giảm 1 sản phẩm khi bán

-- 9. Xóa quan hệ user-category
-- DELETE FROM User_Category WHERE user_id = 1 AND category_id = 1;

-- 10. Thêm sản phẩm mới
-- INSERT INTO Product (title, quantity, desc, price, userid) 
-- VALUES ('Laptop Dell XPS 13', 20, 'Laptop cao cấp cho doanh nhân', 25990000.00, 1);