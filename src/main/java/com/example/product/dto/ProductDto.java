package com.example.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String title;
    private Integer quantity;
    private String description;
    private BigDecimal price;
    private String images;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
    private String userFullname;

    // Constructor from Entity
    public ProductDto(com.example.product.entity.Product product) {
        this.id = product.getId();
        this.title = product.getTitle();
        this.quantity = product.getQuantity();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.images = product.getImages();
        this.createdAt = product.getCreatedAt();
        this.updatedAt = product.getUpdatedAt();
        this.userId = product.getUser() != null ? product.getUser().getId() : null;
        this.userFullname = product.getUser() != null ? product.getUser().getFullname() : null;
    }
}