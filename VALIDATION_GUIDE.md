# Hướng dẫn Validation trong Spring Boot

## Tổng quan

Validation (xác thực dữ liệu) là một phần quan trọng trong việc phát triển ứng dụng web, giúp đảm bảo tính toàn vẹn và chính xác của dữ liệu đầu vào.

## 1. Các loại Validation

### 1.1 Bean Validation (JSR-303/JSR-380)
Spring Boot sử dụng Bean Validation API với Hibernate Validator làm implementation mặc định.

### 1.2 Các annotation validation phổ biến:

#### Validation cơ bản:
- `@NotNull`: Không được null
- `@NotEmpty`: Không được null hoặc rỗng
- `@NotBlank`: Không được null, rỗng hoặc chỉ chứa khoảng trắng

#### Validation chuỗi:
- `@Size(min, max)`: Độ dài chuỗi trong khoảng min-max
- `@Pattern(regexp)`: Phải khớp với regex pattern
- `@Email`: Phải là email hợp lệ

#### Validation số:
- `@Min(value)`: Giá trị tối thiểu
- `@Max(value)`: Giá trị tối đa
- `@DecimalMin(value)`: Giá trị decimal tối thiểu
- `@DecimalMax(value)`: Giá trị decimal tối đa
- `@Positive`: Phải là số dương
- `@PositiveOrZero`: Phải là số dương hoặc 0
- `@Negative`: Phải là số âm
- `@NegativeOrZero`: Phải là số âm hoặc 0
- `@Digits(integer, fraction)`: Số chữ số nguyên và thập phân

## 2. Áp dụng trong dự án

### 2.1 Entity Classes
```java
@Entity
public class Product {
    @NotBlank(message = "Tiêu đề sản phẩm không được để trống")
    @Size(min = 3, max = 255, message = "Tiêu đề phải có từ 3 đến 255 ký tự")
    private String title;

    @NotNull(message = "Giá sản phẩm không được để trống")
    @DecimalMin(value = "0.01", message = "Giá sản phẩm phải lớn hơn 0")
    @DecimalMax(value = "99999999.99", message = "Giá sản phẩm không được vượt quá 99,999,999.99")
    private BigDecimal price;

    @Pattern(regexp = "^[\\p{L}\\s]+$", message = "Tên chỉ được chứa chữ cái và khoảng trắng")
    private String name;
}
```

### 2.2 DTO Classes
```java
@Data
public class ProductInput {
    @NotBlank(message = "Tiêu đề sản phẩm không được để trống")
    @Size(min = 3, max = 255, message = "Tiêu đề phải có từ 3 đến 255 ký tự")
    private String title;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 0, message = "Số lượng phải lớn hơn hoặc bằng 0")
    @Max(value = 10000, message = "Số lượng không được vượt quá 10.000")
    private Integer quantity;
}
```

### 2.3 REST Controller
```java
@PostMapping
public ResponseEntity<ProductDto> save(@Valid @RequestBody ProductInput input) {
    // Logic xử lý
}
```

## 3. Custom Validator

### 3.1 Tạo annotation custom
```java
@Documented
@Constraint(validatedBy = UniqueEmailValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmail {
    String message() default "Email đã tồn tại trong hệ thống";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```

### 3.2 Implement validator
```java
@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.trim().isEmpty()) {
            return true; // Let @NotBlank handle empty validation
        }
        return !userRepository.existsByEmail(email);
    }
}
```

## 4. Global Exception Handler

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        ValidationErrorResponse errorResponse = new ValidationErrorResponse();
        errorResponse.setMessage("Dữ liệu đầu vào không hợp lệ");
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());

        List<ValidationErrorResponse.FieldError> fieldErrors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            fieldErrors.add(new ValidationErrorResponse.FieldError(
                error.getField(),
                error.getRejectedValue(),
                error.getDefaultMessage()
            ));
        });

        errorResponse.setFieldErrors(fieldErrors);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
```

## 5. Frontend Validation

### 5.1 Client-side validation với JavaScript
```javascript
function validateForm() {
    let isValid = true;
    const errors = [];

    // Title validation
    const title = document.getElementById('title').value.trim();
    if (!title) {
        errors.push({ field: 'title', message: 'Tiêu đề không được để trống' });
        isValid = false;
    } else if (title.length < 3 || title.length > 255) {
        errors.push({ field: 'title', message: 'Tiêu đề phải có từ 3 đến 255 ký tự' });
        isValid = false;
    }

    return isValid;
}
```

### 5.2 Xử lý validation errors từ backend
```javascript
async function handleFormSubmit(event) {
    try {
        const response = await fetch('/api/products', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(productData)
        });

        if (!response.ok) {
            if (response.status === 400) {
                const errorData = await response.json();
                handleValidationErrors(errorData);
                return;
            }
        }
    } catch (error) {
        console.error('Error:', error);
    }
}

function handleValidationErrors(errorData) {
    if (errorData.fieldErrors && errorData.fieldErrors.length > 0) {
        errorData.fieldErrors.forEach(error => {
            const field = document.getElementById(error.field);
            if (field) {
                field.classList.add('error');
                // Hiển thị message lỗi
            }
        });
    }
}
```

## 6. CSS cho Validation

```css
.form-control.error {
    border-color: #e53e3e;
    box-shadow: 0 0 0 3px rgba(229, 62, 62, 0.1);
}

.error-message {
    color: #e53e3e;
    font-size: 0.875rem;
    margin-top: 0.25rem;
    display: block;
}

.form-control.valid {
    border-color: #38a169;
    box-shadow: 0 0 0 3px rgba(56, 161, 105, 0.1);
}
```

## 7. Best Practices

### 7.1 Validation Messages
- Sử dụng message tiếng Việt rõ ràng, dễ hiểu
- Cung cấp thông tin cụ thể về lỗi
- Hướng dẫn người dùng cách khắc phục

### 7.2 Validation Layers
- **Frontend**: Validation cơ bản để cải thiện UX
- **Backend**: Validation toàn diện để đảm bảo data integrity
- **Database**: Constraints ở tầng database

### 7.3 Performance
- Sử dụng lazy validation khi có thể
- Cache validation results nếu cần
- Optimize custom validators

### 7.4 Security
- Luôn validate ở backend ngay cả khi có frontend validation
- Sanitize input data
- Validate business logic rules

## 8. Regex Patterns hữu ích

```java
// Email
@Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$")

// Số điện thoại Việt Nam
@Pattern(regexp = "^(\\+84|0)[0-9]{9,10}$")

// Chỉ chữ cái và khoảng trắng
@Pattern(regexp = "^[\\p{L}\\s]+$")

// Mật khẩu mạnh (ít nhất 1 chữ thường, 1 chữ hoa, 1 số)
@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$")

// URL
@Pattern(regexp = "^https?://.+")
```

## 9. Testing Validation

```java
@Test
public void testProductValidation() {
    ProductInput input = new ProductInput();
    input.setTitle(""); // Invalid
    
    Set<ConstraintViolation<ProductInput>> violations = validator.validate(input);
    assertFalse(violations.isEmpty());
    
    // Check specific violation
    assertTrue(violations.stream()
        .anyMatch(v -> v.getPropertyPath().toString().equals("title")));
}
```

## Kết luận

Validation là một phần quan trọng không thể thiếu trong việc phát triển ứng dụng web. Việc áp dụng validation đúng cách giúp:

1. **Bảo vệ dữ liệu**: Đảm bảo chỉ dữ liệu hợp lệ được lưu trữ
2. **Cải thiện UX**: Cung cấp feedback rõ ràng cho người dùng
3. **Tăng security**: Ngăn chặn các cuộc tấn công injection
4. **Giảm lỗi**: Phát hiện lỗi sớm trong quá trình phát triển

Hãy luôn nhớ áp dụng validation ở nhiều tầng (frontend, backend, database) để đảm bảo tính toàn vẹn của ứng dụng.