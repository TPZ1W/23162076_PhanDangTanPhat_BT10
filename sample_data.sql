-- Dữ liệu mẫu cho database product_management
USE product_management;

-- Thêm dữ liệu vào bảng Category
INSERT INTO Category (name, images) VALUES 
('Điện tử', 'electronics.jpg,phones.jpg'),
('Thời trang', 'fashion.jpg,clothes.jpg'),
('Gia dụng', 'home.jpg,kitchen.jpg'),
('Sách', 'books.jpg,education.jpg'),
('Thể thao', 'sports.jpg,fitness.jpg');

-- Thêm dữ liệu vào bảng User
INSERT INTO User (fullname, email, password, phone) VALUES 
('Nguyễn Văn An', 'nguyenvanan@email.com', 'password123', '0901234567'),
('Trần Thị Bình', 'tranthibinh@email.com', 'password456', '0912345678'),
('Lê Văn Cường', 'levancuong@email.com', 'password789', '0923456789'),
('Phạm Thị Dung', 'phamthidung@email.com', 'password101', '0934567890'),
('Hoàng Văn Em', 'hoangvanem@email.com', 'password202', '0945678901');

-- Thêm dữ liệu vào bảng Product
INSERT INTO Product (title, quantity, desc, price, userid) VALUES 
('iPhone 15 Pro Max', 50, 'Điện thoại thông minh cao cấp từ Apple', 29990000.00, 1),
('Samsung Galaxy S24', 30, 'Flagship Android với camera xuất sắc', 24990000.00, 1),
('Áo polo nam', 100, 'Áo polo chất liệu cotton cao cấp', 299000.00, 2),
('Quần jeans nữ', 75, 'Quần jeans slim fit thời trang', 599000.00, 2),
('Nồi cơm điện Panasonic', 25, 'Nồi cơm điện 1.8L cho gia đình', 1290000.00, 3),
('Máy xay sinh tố', 40, 'Máy xay đa năng công suất 500W', 890000.00, 3),
('Sách lập trình Python', 200, 'Giáo trình lập trình Python từ cơ bản đến nâng cao', 159000.00, 4),
('Sách tiếng Anh giao tiếp', 150, 'Học tiếng Anh giao tiếp hiệu quả', 119000.00, 4),
('Giày chạy bộ Nike', 60, 'Giày thể thao chuyên dụng cho chạy bộ', 2290000.00, 5),
('Bóng đá FIFA Quality', 80, 'Bóng đá chất lượng FIFA chuẩn quốc tế', 450000.00, 5);

-- Thiết lập quan hệ nhiều-nhiều giữa User và Category
INSERT INTO User_Category (user_id, category_id) VALUES 
-- User 1 (Nguyễn Văn An) quan tâm đến Điện tử và Thể thao
(1, 1), (1, 5),
-- User 2 (Trần Thị Bình) quan tâm đến Thời trang và Gia dụng
(2, 2), (2, 3),
-- User 3 (Lê Văn Cường) quan tâm đến Gia dụng và Điện tử
(3, 3), (3, 1),
-- User 4 (Phạm Thị Dung) quan tâm đến Sách và Thời trang
(4, 4), (4, 2),
-- User 5 (Hoàng Văn Em) quan tâm đến Thể thao và Sách
(5, 5), (5, 4),
-- Một số user có thể quan tâm đến nhiều category hơn
(1, 3), -- User 1 cũng quan tâm đến Gia dụng
(2, 1), -- User 2 cũng quan tâm đến Điện tử
(3, 2); -- User 3 cũng quan tâm đến Thời trang