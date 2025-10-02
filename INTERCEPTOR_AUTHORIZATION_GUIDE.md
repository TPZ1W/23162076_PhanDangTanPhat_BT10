# Hướng dẫn Interceptor và Authorization trong Spring Boot

## Tổng quan

Interceptor và Authorization là hai thành phần quan trọng trong việc xây dựng hệ thống bảo mật cho ứng dụng web. Chúng giúp kiểm soát quyền truy cập và bảo vệ các tài nguyên quan trọng.

## 1. Interceptor

### 1.1 Khái niệm
Interceptor là một thành phần middleware trong Spring MVC cho phép bạn chặn và xử lý các HTTP requests trước khi chúng đến controller và sau khi response được tạo ra.

### 1.2 Lifecycle của Interceptor
```java
public interface HandlerInterceptor {
    // Thực thi trước khi request đến controller
    boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler);
    
    // Thực thi sau khi controller xử lý xong nhưng trước khi view được render
    void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView);
    
    // Thực thi sau khi toàn bộ request đã được xử lý xong
    void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex);
}
```

### 1.3 Authentication Interceptor
```java
@Component
@RequiredArgsConstructor
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        
        // Skip authentication for public endpoints
        if (isPublicEndpoint(uri)) {
            return true;
        }

        // Check if user is authenticated
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            handleUnauthorized(request, response, "Chưa đăng nhập");
            return false;
        }

        // Get current user and check permissions
        Long userId = (Long) session.getAttribute("userId");
        User currentUser = authService.getCurrentUser(userId);
        
        if (currentUser == null) {
            handleUnauthorized(request, response, "Người dùng không tồn tại");
            return false;
        }

        // Set user in request attributes for later use
        request.setAttribute("currentUser", currentUser);

        // Check permissions based on URL patterns
        if (!hasRequiredPermission(currentUser, uri, method)) {
            handleForbidden(request, response, "Không có quyền truy cập");
            return false;
        }

        return true;
    }
}
```

### 1.4 Đăng ký Interceptor
```java
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**") // Áp dụng cho tất cả các URL
                .excludePathPatterns(
                    "/css/**",
                    "/js/**", 
                    "/images/**",
                    "/favicon.ico",
                    "/error/**"
                ); // Loại trừ static resources
    }
}
```

## 2. Role-Based Authorization

### 2.1 Entity Design

#### Role Entity
```java
@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<User> users;
}
```

#### User Entity with Roles
```java
@Entity
@Table(name = "users")
@Data
public class User {
    // ... other fields

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", 
               joinColumns = @JoinColumn(name = "user_id"), 
               inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    // Helper methods for roles
    public boolean hasRole(String roleName) {
        if (roles == null) return false;
        return roles.stream().anyMatch(role -> roleName.equals(role.getName()));
    }

    public boolean isAdmin() {
        return hasRole("ADMIN");
    }

    public boolean isUser() {
        return hasRole("USER");
    }
}
```

### 2.2 Permission System
```java
@Service
@RequiredArgsConstructor
public class AuthService {

    public boolean hasPermission(User user, String permission) {
        if (user == null) return false;
        
        return switch (permission) {
            case "ADMIN" -> user.isAdmin();
            case "USER" -> user.isUser() || user.isAdmin();
            case "MANAGE_PRODUCTS" -> user.isAdmin();
            case "MANAGE_USERS" -> user.isAdmin();
            case "MANAGE_CATEGORIES" -> user.isAdmin();
            case "VIEW_PRODUCTS" -> true;
            case "CREATE_PRODUCT" -> user.isUser() || user.isAdmin();
            case "EDIT_OWN_PRODUCT" -> user.isUser() || user.isAdmin();
            case "DELETE_ANY_PRODUCT" -> user.isAdmin();
            default -> false;
        };
    }
}
```

## 3. Annotation-Based Authorization

### 3.1 Custom Annotations
```java
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AdminOnly {
    String message() default "Chỉ admin mới có quyền truy cập";
}

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequirePermission {
    String value();
    String message() default "Không có quyền truy cập";
}
```

### 3.2 Authorization Aspect
```java
@Aspect
@Component
@RequiredArgsConstructor
public class AuthorizationAspect {

    private final AuthService authService;

    @Around("@annotation(adminOnly)")
    public Object checkAdminPermission(ProceedingJoinPoint joinPoint, AdminOnly adminOnly) throws Throwable {
        User currentUser = getCurrentUser();
        
        if (currentUser == null) {
            return createUnauthorizedResponse("Chưa đăng nhập");
        }

        if (!currentUser.isAdmin()) {
            return createForbiddenResponse(adminOnly.message());
        }

        return joinPoint.proceed();
    }

    @Around("@annotation(requirePermission)")
    public Object checkPermission(ProceedingJoinPoint joinPoint, RequirePermission requirePermission) throws Throwable {
        User currentUser = getCurrentUser();
        
        if (currentUser == null) {
            return createUnauthorizedResponse("Chưa đăng nhập");
        }

        if (!authService.hasPermission(currentUser, requirePermission.value())) {
            return createForbiddenResponse(requirePermission.message());
        }

        return joinPoint.proceed();
    }
}
```

### 3.3 Sử dụng Annotations trong Controller
```java
@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @GetMapping
    @AdminOnly(message = "Chỉ admin mới có quyền xem danh sách người dùng")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        // Implementation
    }

    @PostMapping
    @RequirePermission(value = "MANAGE_USERS", message = "Không có quyền tạo người dùng")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserInput userInput) {
        // Implementation
    }
}
```

## 4. URL-Based Authorization

### 4.1 Phân quyền theo URL Pattern
```java
private boolean hasRequiredPermission(User user, String uri, String method) {
    // Admin endpoints - chỉ admin mới truy cập được
    if (uri.startsWith("/api/admin/") || uri.startsWith("/web/admin/")) {
        return authService.hasPermission(user, "ADMIN");
    }

    // User management endpoints - chỉ admin
    if ((uri.startsWith("/api/users") && ("POST".equals(method) || "DELETE".equals(method))) ||
        uri.startsWith("/web/users")) {
        return authService.hasPermission(user, "MANAGE_USERS");
    }

    // Category management endpoints - chỉ admin
    if ((uri.startsWith("/api/categories") && ("POST".equals(method) || "PUT".equals(method) || "DELETE".equals(method))) ||
        uri.startsWith("/web/categories")) {
        return authService.hasPermission(user, "MANAGE_CATEGORIES");
    }

    // Product management
    if (uri.startsWith("/api/products")) {
        return switch (method) {
            case "GET" -> authService.hasPermission(user, "VIEW_PRODUCTS");
            case "POST" -> authService.hasPermission(user, "CREATE_PRODUCT");
            case "PUT", "DELETE" -> {
                if (authService.hasPermission(user, "ADMIN")) {
                    yield true;
                }
                yield checkProductOwnership(user, uri);
            }
            default -> false;
        };
    }

    return authService.hasPermission(user, "USER");
}
```

## 5. Frontend Integration

### 5.1 Authentication API
```javascript
// Login
async function login(email, password) {
    const response = await fetch('/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password })
    });
    
    const result = await response.json();
    
    if (result.success) {
        // Redirect based on user role
        if (result.user.isAdmin) {
            window.location.href = '/web/admin';
        } else {
            window.location.href = '/web/dashboard';
        }
    }
}

// Check current user
async function getCurrentUser() {
    const response = await fetch('/api/auth/me');
    const result = await response.json();
    
    if (result.success) {
        return result.user;
    }
    return null;
}

// Check permission
async function checkPermission(permission) {
    const response = await fetch(`/api/auth/check-permission?permission=${permission}`);
    const result = await response.json();
    return result.hasPermission;
}
```

### 5.2 UI Permission Control
```javascript
// Setup UI based on user permissions
function setupUIBasedOnPermissions() {
    // Show user info
    displayUserInfo();

    // Show/hide navigation items based on role
    if (currentUser.isAdmin) {
        document.getElementById('usersNav').style.display = 'block';
        document.getElementById('categoriesNav').style.display = 'block';
        document.getElementById('adminNav').style.display = 'block';
    }
}

// Handle unauthorized responses
async function makeAuthorizedRequest(url, options = {}) {
    const response = await fetch(url, options);
    
    if (response.status === 401) {
        // Redirect to login
        window.location.href = '/web/login?error=Vui lòng đăng nhập';
        return null;
    }
    
    if (response.status === 403) {
        // Show access denied message
        alert('Bạn không có quyền thực hiện thao tác này');
        return null;
    }
    
    return response;
}
```

## 6. Database Schema

### 6.1 Tables Structure
```sql
-- Roles table
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(200)
);

-- User_roles table (Many-to-Many)
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- Insert default roles
INSERT INTO roles (name, description) VALUES 
('ADMIN', 'Quản trị viên - có quyền quản lý toàn bộ hệ thống'),
('USER', 'Người dùng - có quyền tạo và quản lý sản phẩm của mình');
```

## 7. Session Management

### 7.1 Session Configuration
```java
@PostMapping("/login")
public ResponseEntity<Map<String, Object>> login(
        @RequestBody Map<String, String> loginRequest,
        HttpServletRequest request) {
    
    var userOpt = authService.authenticate(email, password);
    
    if (userOpt.isPresent()) {
        User user = userOpt.get();
        
        // Create session
        HttpSession session = request.getSession(true);
        session.setAttribute("userId", user.getId());
        session.setAttribute("userEmail", user.getEmail());
        session.setAttribute("userRole", user.isAdmin() ? "ADMIN" : "USER");
        session.setMaxInactiveInterval(1800); // 30 minutes
        
        return ResponseEntity.ok(createSuccessResponse(user));
    }
    
    return ResponseEntity.badRequest().body(createErrorResponse("Invalid credentials"));
}
```

### 7.2 Session Timeout Handling
```javascript
// Check session periodically
setInterval(async () => {
    try {
        const response = await fetch('/api/auth/me');
        if (!response.ok) {
            // Session expired
            alert('Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.');
            window.location.href = '/web/login';
        }
    } catch (error) {
        console.error('Session check failed:', error);
    }
}, 5 * 60 * 1000); // Check every 5 minutes
```

## 8. Security Best Practices

### 8.1 Password Security
```java
@Service
public class AuthService {
    
    private final PasswordEncoder passwordEncoder;
    
    public User createUser(User user) {
        // Hash password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    
    public Optional<User> authenticate(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return Optional.of(user);
            }
        }
        
        return Optional.empty();
    }
}
```

### 8.2 CSRF Protection
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .ignoringRequestMatchers("/api/auth/**")
        );
        return http.build();
    }
}
```

### 8.3 Rate Limiting
```java
@Component
public class RateLimitInterceptor implements HandlerInterceptor {
    
    private final Map<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();
    private final int maxRequests = 100; // per hour
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String clientIp = getClientIp(request);
        AtomicInteger count = requestCounts.computeIfAbsent(clientIp, k -> new AtomicInteger(0));
        
        if (count.incrementAndGet() > maxRequests) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            return false;
        }
        
        return true;
    }
}
```

## 9. Testing Authorization

### 9.1 Unit Tests
```java
@SpringBootTest
@Transactional
class AuthorizationTest {

    @Test
    void testAdminOnlyEndpoint() {
        // Test with admin user
        User admin = createAdminUser();
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", admin.getId());
        
        mockMvc.perform(get("/api/users")
                .session(session))
                .andExpect(status().isOk());
        
        // Test with regular user
        User user = createRegularUser();
        session.setAttribute("userId", user.getId());
        
        mockMvc.perform(get("/api/users")
                .session(session))
                .andExpect(status().isForbidden());
    }
}
```

### 9.2 Integration Tests
```java
@Test
void testFullAuthenticationFlow() {
    // Test login
    Map<String, String> loginRequest = Map.of(
        "email", "admin@example.com",
        "password", "admin123"
    );
    
    MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andReturn();
    
    MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession();
    
    // Test authorized endpoint
    mockMvc.perform(get("/api/users")
            .session(session))
            .andExpect(status().isOk());
    
    // Test logout
    mockMvc.perform(post("/api/auth/logout")
            .session(session))
            .andExpect(status().isOk());
    
    // Test access after logout
    mockMvc.perform(get("/api/users")
            .session(session))
            .andExpect(status().isUnauthorized());
}
```

## Kết luận

Hệ thống Interceptor và Authorization cung cấp:

1. **Security**: Bảo vệ các endpoint quan trọng
2. **Flexibility**: Phân quyền linh hoạt theo role và permission
3. **Maintainability**: Code dễ bảo trì với annotations
4. **User Experience**: UI/UX phù hợp với từng role
5. **Scalability**: Dễ mở rộng thêm roles và permissions mới

Việc kết hợp Interceptor cho authentication và Aspect cho authorization tạo ra một hệ thống bảo mật mạnh mẽ và linh hoạt cho ứng dụng Spring Boot.