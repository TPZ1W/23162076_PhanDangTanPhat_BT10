package com.example.product.aspect;

import com.example.product.annotation.AdminOnly;
import com.example.product.annotation.RequirePermission;
import com.example.product.entity.User;
import com.example.product.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
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

    private User getCurrentUser() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            HttpSession session = request.getSession(false);

            if (session == null || session.getAttribute("userId") == null) {
                return null;
            }

            Long userId = (Long) session.getAttribute("userId");
            return authService.getCurrentUser(userId);
        } catch (Exception e) {
            log.warn("Error getting current user: {}", e.getMessage());
            return null;
        }
    }

    private ResponseEntity<Map<String, Object>> createUnauthorizedResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        response.put("status", HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity<Map<String, Object>> createForbiddenResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        response.put("status", HttpStatus.FORBIDDEN.value());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
}