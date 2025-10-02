package com.example.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private Long id;
    private String name;
    private String images;

    // Constructor from Entity
    public CategoryDto(com.example.product.entity.Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.images = category.getImages();
    }
}