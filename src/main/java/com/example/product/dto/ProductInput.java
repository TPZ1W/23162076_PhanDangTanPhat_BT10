package com.example.product.dto;

import com.example.product.validation.ValidUserId;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductInput {

    @NotBlank(message = "Tiêu đề sản phẩm không được để trống")
    @Size(min = 3, max = 255, message = "Tiêu đề phải có từ 3 đến 255 ký tự")
    private String title;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 0, message = "Số lượng phải lớn hơn hoặc bằng 0")
    @Max(value = 10000, message = "Số lượng không được vượt quá 10.000")
    private Integer quantity;

    @Size(max = 1000, message = "Mô tả không được vượt quá 1000 ký tự")
    private String description;

    @NotNull(message = "Giá sản phẩm không được để trống")
    @DecimalMin(value = "0.01", message = "Giá sản phẩm phải lớn hơn 0")
    @DecimalMax(value = "99999999.99", message = "Giá sản phẩm không được vượt quá 99,999,999.99")
    @Digits(integer = 8, fraction = 2, message = "Giá sản phẩm không hợp lệ (tối đa 8 chữ số nguyên và 2 chữ số thập phân)")
    private BigDecimal price;

    @Size(max = 500, message = "Đường dẫn hình ảnh không được vượt quá 500 ký tự")
    private String images;

    @NotNull(message = "ID người dùng không được để trống")
    @Positive(message = "ID người dùng phải là số dương")
    @ValidUserId
    private Long userId;
}