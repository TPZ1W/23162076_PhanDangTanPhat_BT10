package com.example.product.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidCategoryIdValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCategoryId {
    String message() default "Danh mục không tồn tại trong hệ thống";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}