package com.example.product.rest;

import com.example.product.dto.LoginRequest;
import com.example.product.entity.User;
import com.example.product.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @Valid @RequestBody LoginRequest loginRequest,
            BindingResult bindingResult,
            HttpServletRequest request) {

        // Check validation errors
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(createValidationErrorResponse(bindingResult));
        }

        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        var userOpt = authService.authenticate(email, password);

        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Email hoặc mật khẩu không đúng"));
        }

        User user = userOpt.get();

        // Create session
        HttpSession session = request.getSession(true);
        session.setAttribute("userId", user.getId());
        session.setAttribute("userEmail", user.getEmail());
        session.setAttribute("userRole", user.isAdmin() ? "ADMIN" : "USER");

        log.info("User {} logged in successfully", user.getEmail());

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Đăng nhập thành công");
        response.put("user", createUserResponse(user));
        
        // Redirect URL dựa trên role
        String redirectUrl;
        if (user.isAdmin()) {
            redirectUrl = "/web/admin";  // Admin đi tới trang quản trị
        } else {
            redirectUrl = "/web/dashboard";  // User đi tới trang chủ user
        }
        response.put("redirectUrl", redirectUrl);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            String userEmail = (String) session.getAttribute("userEmail");
            session.invalidate();
            log.info("User {} logged out", userEmail);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Đăng xuất thành công");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Chưa đăng nhập"));
        }

        Long userId = (Long) session.getAttribute("userId");
        User user = authService.getCurrentUser(userId);

        if (user == null) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Người dùng không tồn tại"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("user", createUserResponse(user));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-permission")
    public ResponseEntity<Map<String, Object>> checkPermission(
            @RequestParam String permission,
            HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            return ResponseEntity.ok(createPermissionResponse(false, "Chưa đăng nhập"));
        }

        Long userId = (Long) session.getAttribute("userId");
        User user = authService.getCurrentUser(userId);

        if (user == null) {
            return ResponseEntity.ok(createPermissionResponse(false, "Người dùng không tồn tại"));
        }

        boolean hasPermission = authService.hasPermission(user, permission);
        String message = hasPermission ? "Có quyền" : "Không có quyền";

        return ResponseEntity.ok(createPermissionResponse(hasPermission, message));
    }

    private Map<String, Object> createValidationErrorResponse(BindingResult bindingResult) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        
        StringBuilder errorMessage = new StringBuilder();
        bindingResult.getFieldErrors().forEach(error -> {
            if (errorMessage.length() > 0) {
                errorMessage.append("; ");
            }
            errorMessage.append(error.getDefaultMessage());
        });
        
        response.put("message", errorMessage.toString());
        return response;
    }

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        return response;
    }

    private Map<String, Object> createUserResponse(User user) {
        Map<String, Object> userResponse = new HashMap<>();
        userResponse.put("id", user.getId());
        userResponse.put("fullname", user.getFullname());
        userResponse.put("email", user.getEmail());
        userResponse.put("phone", user.getPhone());
        userResponse.put("isAdmin", user.isAdmin());
        userResponse.put("isUser", user.isUser());
        return userResponse;
    }

    private Map<String, Object> createPermissionResponse(boolean hasPermission, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("hasPermission", hasPermission);
        response.put("message", message);
        return response;
    }
}