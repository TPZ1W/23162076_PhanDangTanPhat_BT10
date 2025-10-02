package com.example.product.web;

import com.example.product.entity.User;
import com.example.product.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class WebController {

    private final AuthService authService;

    // Root redirect to login
    @GetMapping({ "/" })
    public String root() {
        return "redirect:/web/login";
    }

    // Dashboard - Trang chính cho user đã đăng nhập
    @GetMapping({ "/web/dashboard" })
    public String dashboard(HttpServletRequest request, Model model) {
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return "redirect:/web/login?error=Please login first";
        }

        model.addAttribute("pageTitle", "Tổng quan hệ thống");
        model.addAttribute("currentUser", currentUser);
        return "dashboard";
    }

    // Products - Cho cả user và admin
    @GetMapping({ "/web/products" })
    public String productList(HttpServletRequest request, Model model) {
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return "redirect:/web/login?error=Please login first";
        }

        model.addAttribute("pageTitle", "Quản lý sản phẩm");
        model.addAttribute("currentUser", currentUser);
        return "products";
    }

    // Users - Chỉ admin
    @GetMapping({ "/web/users" })
    public String userList(HttpServletRequest request, Model model) {
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return "redirect:/web/login?error=Please login first";
        }
        if (!currentUser.isAdmin()) {
            return "redirect:/web/error?code=403&message=Admin access required for user management";
        }

        model.addAttribute("pageTitle", "Quản lý người dùng");
        model.addAttribute("currentUser", currentUser);
        return "users";
    }

    // Categories - Chỉ admin
    @GetMapping({ "/web/categories" })
    public String categoryList(HttpServletRequest request, Model model) {
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return "redirect:/web/login?error=Please login first";
        }
        if (!currentUser.isAdmin()) {
            return "redirect:/web/error?code=403&message=Admin access required for category management";
        }

        model.addAttribute("pageTitle", "Quản lý danh mục");
        model.addAttribute("currentUser", currentUser);
        return "categories";
    }

    // Helper method to get current user
    private User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            return null;
        }

        Long userId = (Long) session.getAttribute("userId");
        return authService.getCurrentUser(userId);
    }
}