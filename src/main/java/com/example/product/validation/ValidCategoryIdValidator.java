package com.example.product.validation;

import com.example.product.repository.CategoryRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidCategoryIdValidator implements ConstraintValidator<ValidCategoryId, Long> {

    private final CategoryRepository categoryRepository;

    @Override
    public void initialize(ValidCategoryId constraintAnnotation) {
        // Initialization logic if needed
    }

    @Override
    public boolean isValid(Long categoryId, ConstraintValidatorContext context) {
        if (categoryId == null) {
            return true; // Let @NotNull handle null validation
        }
        return categoryRepository.existsById(categoryId);
    }
}