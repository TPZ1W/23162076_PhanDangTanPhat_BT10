package com.example.product.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Tiêu đề sản phẩm không được để trống")
    @Size(min = 3, max = 255, message = "Tiêu đề phải có từ 3 đến 255 ký tự")
    private String title;

    @Column(nullable = false)
    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 0, message = "Số lượng phải lớn hơn hoặc bằng 0")
    @Max(value = 10000, message = "Số lượng không được vượt quá 10.000")
    private Integer quantity = 0;

    @Column(columnDefinition = "TEXT")
    @Size(max = 1000, message = "Mô tả không được vượt quá 1000 ký tự")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    @NotNull(message = "Giá sản phẩm không được để trống")
    @DecimalMin(value = "0.01", message = "Giá sản phẩm phải lớn hơn 0")
    @DecimalMax(value = "99999999.99", message = "Giá sản phẩm không được vượt quá 99,999,999.99")
    private BigDecimal price;

    @Size(max = 500, message = "Đường dẫn hình ảnh không được vượt quá 500 ký tự")
    private String images;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Constructor without relationships
    public Product(String title, Integer quantity, String description, BigDecimal price, String images) {
        this.title = title;
        this.quantity = quantity;
        this.description = description;
        this.price = price;
        this.images = images;
    }

    // Constructor with user
    public Product(String title, Integer quantity, String description, BigDecimal price, String images, User user) {
        this.title = title;
        this.quantity = quantity;
        this.description = description;
        this.price = price;
        this.images = images;
        this.user = user;
    }
}