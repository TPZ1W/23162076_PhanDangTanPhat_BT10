package com.example.product.service;

import com.example.product.entity.User;
import com.example.product.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;

    public Optional<User> authenticate(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // In production, you should use BCrypt or similar for password hashing
            if (password.equals(user.getPassword())) {
                return Optional.of(user);
            }
        }

        return Optional.empty();
    }

    public User getCurrentUser(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public boolean hasPermission(User user, String permission) {
        if (user == null)
            return false;

        boolean result = switch (permission) {
            case "ADMIN" -> user.isAdmin();
            case "USER" -> user.isUser() || user.isAdmin(); // Admin có tất cả quyền của user
            case "MANAGE_PRODUCTS" -> user.isAdmin();
            case "MANAGE_USERS" -> user.isAdmin();
            case "MANAGE_CATEGORIES" -> user.isAdmin();
            case "VIEW_PRODUCTS" -> true; // Tất cả có thể xem sản phẩm
            case "CREATE_PRODUCT" -> user.isUser() || user.isAdmin();
            case "EDIT_OWN_PRODUCT" -> user.isUser() || user.isAdmin();
            case "DELETE_ANY_PRODUCT" -> user.isAdmin();
            default -> false;
        };

        log.info("Permission check: user={}, permission={}, roles={}, isAdmin={}, result={}",
                user.getEmail(), permission, user.getRoles(), user.isAdmin(), result);

        return result;
    }
}