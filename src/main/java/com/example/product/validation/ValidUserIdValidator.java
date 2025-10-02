package com.example.product.validation;

import com.example.product.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidUserIdValidator implements ConstraintValidator<ValidUserId, Long> {

    private final UserRepository userRepository;

    @Override
    public void initialize(ValidUserId constraintAnnotation) {
        // Initialization logic if needed
    }

    @Override
    public boolean isValid(Long userId, ConstraintValidatorContext context) {
        if (userId == null) {
            return true; // Let @NotNull handle null validation
        }
        return userRepository.existsById(userId);
    }
}