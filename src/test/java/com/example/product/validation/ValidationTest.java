package com.example.product.validation;

import com.example.product.dto.ProductInput;
import com.example.product.dto.UserInput;
import com.example.product.dto.CategoryInput;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ValidationTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidProductInput() {
        ProductInput product = new ProductInput();
        product.setTitle("Sản phẩm test");
        product.setPrice(new BigDecimal("100.00"));
        product.setQuantity(10);
        product.setUserId(1L);
        product.setDescription("Mô tả sản phẩm");

        Set<ConstraintViolation<ProductInput>> violations = validator.validate(product);
        assertTrue(violations.isEmpty(), "Valid product should have no violations");
    }

    @Test
    public void testProductInputWithEmptyTitle() {
        ProductInput product = new ProductInput();
        product.setTitle(""); // Invalid - empty title
        product.setPrice(new BigDecimal("100.00"));
        product.setQuantity(10);
        product.setUserId(1L);

        Set<ConstraintViolation<ProductInput>> violations = validator.validate(product);
        assertFalse(violations.isEmpty(), "Empty title should cause violation");

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("title")),
                "Should have title violation");
    }

    @Test
    public void testProductInputWithShortTitle() {
        ProductInput product = new ProductInput();
        product.setTitle("AB"); // Invalid - too short
        product.setPrice(new BigDecimal("100.00"));
        product.setQuantity(10);
        product.setUserId(1L);

        Set<ConstraintViolation<ProductInput>> violations = validator.validate(product);
        assertFalse(violations.isEmpty(), "Short title should cause violation");
    }

    @Test
    public void testProductInputWithNegativePrice() {
        ProductInput product = new ProductInput();
        product.setTitle("Sản phẩm test");
        product.setPrice(new BigDecimal("-10.00")); // Invalid - negative price
        product.setQuantity(10);
        product.setUserId(1L);

        Set<ConstraintViolation<ProductInput>> violations = validator.validate(product);
        assertFalse(violations.isEmpty(), "Negative price should cause violation");

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("price")),
                "Should have price violation");
    }

    @Test
    public void testProductInputWithNegativeQuantity() {
        ProductInput product = new ProductInput();
        product.setTitle("Sản phẩm test");
        product.setPrice(new BigDecimal("100.00"));
        product.setQuantity(-5); // Invalid - negative quantity
        product.setUserId(1L);

        Set<ConstraintViolation<ProductInput>> violations = validator.validate(product);
        assertFalse(violations.isEmpty(), "Negative quantity should cause violation");
    }

    @Test
    public void testProductInputWithExcessiveQuantity() {
        ProductInput product = new ProductInput();
        product.setTitle("Sản phẩm test");
        product.setPrice(new BigDecimal("100.00"));
        product.setQuantity(15000); // Invalid - too high
        product.setUserId(1L);

        Set<ConstraintViolation<ProductInput>> violations = validator.validate(product);
        assertFalse(violations.isEmpty(), "Excessive quantity should cause violation");
    }

    @Test
    public void testProductInputWithLongDescription() {
        ProductInput product = new ProductInput();
        product.setTitle("Sản phẩm test");
        product.setPrice(new BigDecimal("100.00"));
        product.setQuantity(10);
        product.setUserId(1L);
        product.setDescription("A".repeat(1001)); // Invalid - too long

        Set<ConstraintViolation<ProductInput>> violations = validator.validate(product);
        assertFalse(violations.isEmpty(), "Long description should cause violation");
    }

    @Test
    public void testValidUserInput() {
        UserInput user = new UserInput();
        user.setFullname("Nguyễn Văn A");
        user.setEmail("test@example.com");
        user.setPassword("Password123");
        user.setPhone("0901234567");

        Set<ConstraintViolation<UserInput>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "Valid user should have no violations");
    }

    @Test
    public void testUserInputWithInvalidEmail() {
        UserInput user = new UserInput();
        user.setFullname("Nguyễn Văn A");
        user.setEmail("invalid-email"); // Invalid email format
        user.setPassword("Password123");

        Set<ConstraintViolation<UserInput>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Invalid email should cause violation");

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email")),
                "Should have email violation");
    }

    @Test
    public void testUserInputWithInvalidName() {
        UserInput user = new UserInput();
        user.setFullname("A"); // Too short
        user.setEmail("test@example.com");
        user.setPassword("Password123");

        Set<ConstraintViolation<UserInput>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Short name should cause violation");
    }

    @Test
    public void testUserInputWithInvalidPhone() {
        UserInput user = new UserInput();
        user.setFullname("Nguyễn Văn A");
        user.setEmail("test@example.com");
        user.setPassword("Password123");
        user.setPhone("123"); // Invalid phone format

        Set<ConstraintViolation<UserInput>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Invalid phone should cause violation");
    }

    @Test
    public void testUserInputWithWeakPassword() {
        UserInput user = new UserInput();
        user.setFullname("Nguyễn Văn A");
        user.setEmail("test@example.com");
        user.setPassword("password"); // Weak password - no uppercase or digits
        user.setPhone("0901234567");

        Set<ConstraintViolation<UserInput>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Weak password should cause violation");
    }

    @Test
    public void testValidCategoryInput() {
        CategoryInput category = new CategoryInput();
        category.setName("Danh mục test");
        category.setImages("https://example.com/image.jpg");

        Set<ConstraintViolation<CategoryInput>> violations = validator.validate(category);
        assertTrue(violations.isEmpty(), "Valid category should have no violations");
    }

    @Test
    public void testCategoryInputWithEmptyName() {
        CategoryInput category = new CategoryInput();
        category.setName(""); // Invalid - empty name

        Set<ConstraintViolation<CategoryInput>> violations = validator.validate(category);
        assertFalse(violations.isEmpty(), "Empty name should cause violation");
    }

    @Test
    public void testCategoryInputWithShortName() {
        CategoryInput category = new CategoryInput();
        category.setName("A"); // Too short

        Set<ConstraintViolation<CategoryInput>> violations = validator.validate(category);
        assertFalse(violations.isEmpty(), "Short name should cause violation");
    }

    @Test
    public void testCategoryInputWithLongImagePath() {
        CategoryInput category = new CategoryInput();
        category.setName("Danh mục test");
        category.setImages("A".repeat(501)); // Too long

        Set<ConstraintViolation<CategoryInput>> violations = validator.validate(category);
        assertFalse(violations.isEmpty(), "Long image path should cause violation");
    }

    @Test
    public void testProductInputWithNullValues() {
        ProductInput product = new ProductInput();
        // All required fields are null

        Set<ConstraintViolation<ProductInput>> violations = validator.validate(product);
        assertFalse(violations.isEmpty(), "Null values should cause violations");

        // Should have violations for title, price, quantity, and userId
        long titleViolations = violations.stream()
                .filter(v -> v.getPropertyPath().toString().equals("title"))
                .count();
        long priceViolations = violations.stream()
                .filter(v -> v.getPropertyPath().toString().equals("price"))
                .count();
        long quantityViolations = violations.stream()
                .filter(v -> v.getPropertyPath().toString().equals("quantity"))
                .count();
        long userIdViolations = violations.stream()
                .filter(v -> v.getPropertyPath().toString().equals("userId"))
                .count();

        assertTrue(titleViolations > 0, "Should have title violation");
        assertTrue(priceViolations > 0, "Should have price violation");
        assertTrue(quantityViolations > 0, "Should have quantity violation");
        assertTrue(userIdViolations > 0, "Should have userId violation");
    }
}