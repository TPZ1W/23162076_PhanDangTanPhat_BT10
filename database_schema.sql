-- Database Schema cho hệ thống quản lý sản phẩm
-- Tạo database
CREATE DATABASE IF NOT EXISTS product_management;
USE product_management;

-- Bảng Category
CREATE TABLE Category (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    images TEXT
);

-- Bảng User
CREATE TABLE User (
    id INT PRIMARY KEY AUTO_INCREMENT,
    fullname VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20)
);

-- Bảng Product (có quan hệ với User thông qua userid)
CREATE TABLE Product (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    quantity INT DEFAULT 0,
    desc TEXT,
    price DECIMAL(10,2) NOT NULL,
    userid INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (userid) REFERENCES User(id) ON DELETE CASCADE
);

-- Bảng trung gian cho quan hệ nhiều-nhiều giữa Category và User
CREATE TABLE User_Category (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    category_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES Category(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_category (user_id, category_id)
);

-- Thêm index để tối ưu hiệu suất
CREATE INDEX idx_product_userid ON Product(userid);
CREATE INDEX idx_user_email ON User(email);
CREATE INDEX idx_user_category_user ON User_Category(user_id);
CREATE INDEX idx_user_category_category ON User_Category(category_id);