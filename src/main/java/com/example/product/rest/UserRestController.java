package com.example.product.rest;

import com.example.product.annotation.AdminOnly;
import com.example.product.annotation.RequirePermission;
import com.example.product.dto.UserDto;
import com.example.product.dto.UserInput;
import com.example.product.entity.User;
import com.example.product.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserRestController {

    private final UserService userService;

    // READ - Get all users (Admin only)
    @GetMapping
    @AdminOnly(message = "Chỉ admin mới có quyền xem danh sách người dùng")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<User> users = userService.findAll();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<UserDto> dtoList = users.stream()
                .map(UserDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    // READ - Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        return user.map(u -> ResponseEntity.ok(new UserDto(u)))
                .orElse(ResponseEntity.notFound().build());
    }

    // READ - Get user by email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userService.findByEmail(email);
        return user.map(u -> ResponseEntity.ok(new UserDto(u)))
                .orElse(ResponseEntity.notFound().build());
    }

    // READ - Search users by name
    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> searchUsers(@RequestParam String name) {
        List<User> users = userService.searchByName(name);
        List<UserDto> dtoList = users.stream()
                .map(UserDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    // READ - Get users by category
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<UserDto>> getUsersByCategory(@PathVariable Long categoryId) {
        List<User> users = userService.findByCategoryId(categoryId);
        List<UserDto> dtoList = users.stream()
                .map(UserDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    // CREATE - Create new user (Admin only)
    @PostMapping
    @AdminOnly(message = "Chỉ admin mới có quyền tạo người dùng mới")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserInput userInput) {
        try {
            User user = new User();
            user.setFullname(userInput.getFullname());
            user.setEmail(userInput.getEmail());
            user.setPassword(userInput.getPassword()); // Should be hashed in production
            user.setPhone(userInput.getPhone());

            User savedUser = userService.createUser(user);
            return new ResponseEntity<>(new UserDto(savedUser), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // UPDATE - Update user (Admin only)
    @PutMapping("/{id}")
    @AdminOnly(message = "Chỉ admin mới có quyền cập nhật thông tin người dùng")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id,
            @Valid @RequestBody UserInput userInput) {
        try {
            User user = new User();
            user.setFullname(userInput.getFullname());
            user.setEmail(userInput.getEmail());
            user.setPassword(userInput.getPassword());
            user.setPhone(userInput.getPhone());

            User updatedUser = userService.updateUser(id, user);
            return ResponseEntity.ok(new UserDto(updatedUser));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // UPDATE - Add categories to user
    @PostMapping("/{userId}/categories")
    public ResponseEntity<UserDto> addCategoriesToUser(@PathVariable Long userId,
            @RequestBody Set<Long> categoryIds) {
        try {
            User updatedUser = userService.addCategoriesToUser(userId, categoryIds);
            return ResponseEntity.ok(new UserDto(updatedUser));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // UPDATE - Remove category from user
    @DeleteMapping("/{userId}/categories/{categoryId}")
    public ResponseEntity<UserDto> removeCategoryFromUser(@PathVariable Long userId,
            @PathVariable Long categoryId) {
        try {
            User updatedUser = userService.removeCategoryFromUser(userId, categoryId);
            return ResponseEntity.ok(new UserDto(updatedUser));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE - Delete user (Admin only)
    @DeleteMapping("/{id}")
    @AdminOnly(message = "Chỉ admin mới có quyền xóa người dùng")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}