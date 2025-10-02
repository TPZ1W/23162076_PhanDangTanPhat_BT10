package com.example.product.interceptor;

import com.example.product.entity.User;
import com.example.product.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    private final AuthService authService;
    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String uri = request.getRequestURI();
        String method = request.getMethod();

        log.info("Intercepting request: {} {}", method, uri);

        // Skip authentication for public endpoints
        if (isPublicEndpoint(uri)) {
            return true;
        }

        // Check if user is authenticated
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            handleUnauthorized(request, response, "Not authenticated");
            return false;
        }

        // Get current user
        Long userId = (Long) session.getAttribute("userId");
        User currentUser = authService.getCurrentUser(userId);

        if (currentUser == null) {
            handleUnauthorized(request, response, "User not found");
            return false;
        }

        // Set user in request attributes for later use
        request.setAttribute("currentUser", currentUser);

        // Check permissions based on URL patterns
        if (!hasRequiredPermission(currentUser, uri, method)) {
            // Special case: redirect admin to admin dashboard if they try to access user dashboard
            if (uri.startsWith("/web/dashboard") && currentUser.isAdmin()) {
                try {
                    response.sendRedirect("/web/admin");
                    return false;
                } catch (IOException e) {
                    log.error("Error redirecting admin to admin dashboard", e);
                }
            }
            handleForbidden(request, response, "Access denied");
            return false;
        }

        return true;
    }

    private boolean isPublicEndpoint(String uri) {
        return uri.startsWith("/api/auth/") ||
                uri.startsWith("/web/login") ||
                uri.startsWith("/web/register") ||
                uri.equals("/web/") ||
                uri.equals("/web") ||
                uri.equals("/") ||
                uri.startsWith("/css/") ||
                uri.startsWith("/js/") ||
                uri.startsWith("/images/") ||
                uri.startsWith("/favicon.ico") ||
                uri.startsWith("/error");
    }

    private boolean hasRequiredPermission(User user, String uri, String method) {
        // Admin endpoints - chỉ admin mới truy cập được
        if (uri.startsWith("/api/admin") || uri.startsWith("/web/admin")) {
            boolean hasAdminPermission = authService.hasPermission(user, "ADMIN");
            log.info("Checking admin permission for user {} accessing {}: hasAdminPermission={}, userRoles={}",
                    user.getEmail(), uri, hasAdminPermission, user.getRoles());
            return hasAdminPermission;
        }

        // User management endpoints - chỉ admin
        if ((uri.startsWith("/api/users") && ("POST".equals(method) || "DELETE".equals(method))) ||
                uri.startsWith("/web/users")) {
            return authService.hasPermission(user, "MANAGE_USERS");
        }

        // Category management endpoints - chỉ admin
        if ((uri.startsWith("/api/categories")
                && ("POST".equals(method) || "PUT".equals(method) || "DELETE".equals(method))) ||
                uri.startsWith("/web/categories")) {
            return authService.hasPermission(user, "MANAGE_CATEGORIES");
        }

        // Product management
        if (uri.startsWith("/api/products")) {
            return switch (method) {
                case "GET" -> authService.hasPermission(user, "VIEW_PRODUCTS");
                case "POST" -> authService.hasPermission(user, "CREATE_PRODUCT");
                case "PUT", "DELETE" -> {
                    // For PUT/DELETE, check if user owns the product or is admin
                    if (authService.hasPermission(user, "ADMIN")) {
                        yield true;
                    }
                    // Extract product ID from URI and check ownership
                    yield checkProductOwnership(user, uri);
                }
                default -> false;
            };
        }

        // Product web pages - user và admin
        if (uri.startsWith("/web/products")) {
            return authService.hasPermission(user, "USER");
        }

        // Dashboard - tất cả user đã đăng nhập
        if (uri.startsWith("/web/dashboard")) {
            return authService.hasPermission(user, "USER");
        }

        // Default: allow if user is authenticated
        return authService.hasPermission(user, "USER");
    }

    private boolean checkProductOwnership(User user, String uri) {
        // Extract product ID from URI like /api/products/123
        try {
            String[] parts = uri.split("/");
            if (parts.length >= 4 && "products".equals(parts[2])) {
                // Long productId = Long.parseLong(parts[3]);
                // You would need to inject ProductService to check ownership
                // For now, assume user can edit their own products
                return authService.hasPermission(user, "EDIT_OWN_PRODUCT");
            }
        } catch (NumberFormatException e) {
            log.warn("Invalid product ID in URI: {}", uri);
        }
        return false;
    }

    private void handleUnauthorized(HttpServletRequest request, HttpServletResponse response, String message)
            throws IOException {
        if (isApiRequest(request)) {
            sendJsonError(response, HttpServletResponse.SC_UNAUTHORIZED, message);
        } else {
            response.sendRedirect("/web/login?error=" + message);
        }
    }

    private void handleForbidden(HttpServletRequest request, HttpServletResponse response, String message)
            throws IOException {
        if (isApiRequest(request)) {
            sendJsonError(response, HttpServletResponse.SC_FORBIDDEN, message);
        } else {
            response.sendRedirect("/web/error?code=403&message=" + message);
        }
    }

    private boolean isApiRequest(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/api/") ||
                "application/json".equals(request.getHeader("Content-Type")) ||
                "application/json".equals(request.getHeader("Accept"));
    }

    private void sendJsonError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> error = new HashMap<>();
        error.put("error", true);
        error.put("message", message);
        error.put("status", status);
        error.put("timestamp", System.currentTimeMillis());

        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}