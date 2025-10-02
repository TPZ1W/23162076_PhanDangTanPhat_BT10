package com.example.product.web;

import com.example.product.entity.User;
import com.example.product.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/web")
@RequiredArgsConstructor
public class AuthWebController {

    private final AuthService authService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", error);
        }
        model.addAttribute("pageTitle", "Đăng nhập");
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("pageTitle", "Đăng ký");
        return "register";
    }

    @GetMapping("/admin")
    public String adminDashboard(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            return "redirect:/web/login?error=Please login first";
        }

        Long userId = (Long) session.getAttribute("userId");
        User user = authService.getCurrentUser(userId);

        if (user == null || !user.isAdmin()) {
            return "redirect:/web/error?code=403&message=Access denied";
        }

        model.addAttribute("pageTitle", "Quản trị hệ thống");
        model.addAttribute("currentUser", user);
        return "admin/dashboard";
    }

    @GetMapping("/admin/users")
    public String adminUsers(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            return "redirect:/web/login?error=Please login first";
        }

        Long userId = (Long) session.getAttribute("userId");
        User user = authService.getCurrentUser(userId);

        if (user == null || !user.isAdmin()) {
            return "redirect:/web/error?code=403&message=Access denied to user management";
        }

        model.addAttribute("pageTitle", "Quản lý người dùng");
        model.addAttribute("currentUser", user);
        return "admin/users";
    }

    @GetMapping("/admin/categories")
    public String adminCategories(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            return "redirect:/web/login?error=Please login first";
        }

        Long userId = (Long) session.getAttribute("userId");
        User user = authService.getCurrentUser(userId);

        if (user == null || !user.isAdmin()) {
            return "redirect:/web/error?code=403&message=Access denied to category management";
        }

        model.addAttribute("pageTitle", "Quản lý danh mục");
        model.addAttribute("currentUser", user);
        return "admin/categories";
    }

    @GetMapping("/error")
    public String errorPage(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String message,
            Model model) {

        model.addAttribute("pageTitle", "Lỗi");
        model.addAttribute("errorCode", code != null ? code : "500");
        model.addAttribute("errorMessage", message != null ? message : "Đã xảy ra lỗi không mong muốn");

        return "error";
    }
}