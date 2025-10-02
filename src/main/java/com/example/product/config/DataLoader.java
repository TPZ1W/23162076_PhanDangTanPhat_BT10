package com.example.product.config;

import com.example.product.entity.Category;
import com.example.product.entity.Product;
import com.example.product.entity.Role;
import com.example.product.entity.User;
import com.example.product.repository.CategoryRepository;
import com.example.product.repository.ProductRepository;
import com.example.product.repository.RoleRepository;
import com.example.product.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

        private final CategoryRepository categoryRepository;
        private final UserRepository userRepository;
        private final ProductRepository productRepository;
        private final RoleRepository roleRepository;

        @Override
        public void run(String... args) throws Exception {
                // Initialize roles first
                initializeRoles();

                if (categoryRepository.count() == 0 && userRepository.count() == 0) {
                        loadSampleData();
                }
        }

        private void initializeRoles() {
                if (roleRepository.count() == 0) {
                        log.info("Initializing roles...");

                        Role adminRole = new Role("ADMIN", "Quản trị viên - có quyền quản lý toàn bộ hệ thống");
                        Role userRole = new Role("USER", "Người dùng - có quyền tạo và quản lý sản phẩm của mình");

                        roleRepository.save(adminRole);
                        roleRepository.save(userRole);

                        log.info("Roles initialized successfully!");
                }
        }

        private void loadSampleData() {
                log.info("Loading sample data...");

                // Create Categories
                Category electronics = new Category("Điện tử", "electronics.jpg,phones.jpg");
                Category fashion = new Category("Thời trang", "fashion.jpg,clothes.jpg");
                Category home = new Category("Gia dụng", "home.jpg,kitchen.jpg");
                Category books = new Category("Sách", "books.jpg,education.jpg");
                Category sports = new Category("Thể thao", "sports.jpg,fitness.jpg");

                categoryRepository.save(electronics);
                categoryRepository.save(fashion);
                categoryRepository.save(home);
                categoryRepository.save(books);
                categoryRepository.save(sports);

                // Create Users
                // Get roles
                Role adminRole = roleRepository.findByName("ADMIN").orElse(null);
                Role userRole = roleRepository.findByName("USER").orElse(null);

                if (adminRole == null || userRole == null) {
                        log.error("Roles not found! Cannot initialize users.");
                        return;
                }

                // Create admin user
                User admin = new User("Quản trị viên", "admin@example.com", "admin123", "0900000000");
                Set<Role> adminRoles = new HashSet<>();
                adminRoles.add(adminRole);
                adminRoles.add(userRole); // Admin có cả quyền user
                admin.setRoles(adminRoles);
                userRepository.save(admin);

                User user1 = new User("Nguyễn Văn An", "nguyenvanan@email.com", "password123", "0901234567");
                User user2 = new User("Trần Thị Bình", "tranthibinh@email.com", "password456", "0912345678");
                User user3 = new User("Lê Văn Cường", "levancuong@email.com", "password789", "0923456789");
                User user4 = new User("Phạm Thị Dung", "phamthidung@email.com", "password101", "0934567890");
                User user5 = new User("Hoàng Văn Em", "hoangvanem@email.com", "password202", "0945678901");

                // Set roles for regular users
                Set<Role> normalUserRoles = new HashSet<>();
                normalUserRoles.add(userRole);

                user1.setRoles(new HashSet<>(normalUserRoles));
                user2.setRoles(new HashSet<>(normalUserRoles));
                user3.setRoles(new HashSet<>(normalUserRoles));
                user4.setRoles(new HashSet<>(normalUserRoles));
                user5.setRoles(new HashSet<>(normalUserRoles));

                // Set categories for users
                Set<Category> user1Categories = new HashSet<>();
                user1Categories.add(electronics);
                user1Categories.add(sports);
                user1Categories.add(home);
                user1.setCategories(user1Categories);

                Set<Category> user2Categories = new HashSet<>();
                user2Categories.add(fashion);
                user2Categories.add(home);
                user2Categories.add(electronics);
                user2.setCategories(user2Categories);

                Set<Category> user3Categories = new HashSet<>();
                user3Categories.add(home);
                user3Categories.add(electronics);
                user3Categories.add(fashion);
                user3.setCategories(user3Categories);

                Set<Category> user4Categories = new HashSet<>();
                user4Categories.add(books);
                user4Categories.add(fashion);
                user4.setCategories(user4Categories);

                Set<Category> user5Categories = new HashSet<>();
                user5Categories.add(sports);
                user5Categories.add(books);
                user5.setCategories(user5Categories);

                userRepository.save(user1);
                userRepository.save(user2);
                userRepository.save(user3);
                userRepository.save(user4);
                userRepository.save(user5);

                // Create Products
                Product product1 = new Product("iPhone 15 Pro Max", 50, "Điện thoại thông minh cao cấp từ Apple",
                                new BigDecimal("29990000.00"), "iphone15.jpg", user1);

                Product product2 = new Product("Samsung Galaxy S24", 30, "Flagship Android với camera xuất sắc",
                                new BigDecimal("24990000.00"), "galaxys24.jpg", user1);

                Product product3 = new Product("Áo polo nam", 100, "Áo polo chất liệu cotton cao cấp",
                                new BigDecimal("299000.00"), "polo.jpg", user2);

                Product product4 = new Product("Quần jeans nữ", 75, "Quần jeans slim fit thời trang",
                                new BigDecimal("599000.00"), "jeans.jpg", user2);

                Product product5 = new Product("Nồi cơm điện Panasonic", 25, "Nồi cơm điện 1.8L cho gia đình",
                                new BigDecimal("1290000.00"), "rice_cooker.jpg", user3);

                Product product6 = new Product("Máy xay sinh tố", 40, "Máy xay đa năng công suất 500W",
                                new BigDecimal("890000.00"), "blender.jpg", user3);

                Product product7 = new Product("Sách lập trình Python", 200,
                                "Giáo trình lập trình Python từ cơ bản đến nâng cao",
                                new BigDecimal("159000.00"), "python_book.jpg", user4);

                Product product8 = new Product("Sách tiếng Anh giao tiếp", 150, "Học tiếng Anh giao tiếp hiệu quả",
                                new BigDecimal("119000.00"), "english_book.jpg", user4);

                Product product9 = new Product("Giày chạy bộ Nike", 60, "Giày thể thao chuyên dụng cho chạy bộ",
                                new BigDecimal("2290000.00"), "nike_shoes.jpg", user5);

                Product product10 = new Product("Bóng đá FIFA Quality", 80, "Bóng đá chất lượng FIFA chuẩn quốc tế",
                                new BigDecimal("450000.00"), "football.jpg", user5);

                productRepository.save(product1);
                productRepository.save(product2);
                productRepository.save(product3);
                productRepository.save(product4);
                productRepository.save(product5);
                productRepository.save(product6);
                productRepository.save(product7);
                productRepository.save(product8);
                productRepository.save(product9);
                productRepository.save(product10);

                log.info("Sample data loaded successfully!");
        }
}