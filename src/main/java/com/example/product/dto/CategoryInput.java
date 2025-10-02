package com.example.product.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CategoryInput {

    @NotBlank(message = "Tên danh mục không được để trống")
    @Size(min = 2, max = 100, message = "Tên danh mục phải có từ 2 đến 100 ký tự")
    private String name;

    @Size(max = 500, message = "Đường dẫn hình ảnh không được vượt quá 500 ký tự")
    private String images;
}