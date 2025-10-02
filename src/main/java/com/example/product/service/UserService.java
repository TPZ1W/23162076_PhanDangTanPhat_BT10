package com.example.product.service;

import com.example.product.entity.Category;
import com.example.product.entity.User;
import com.example.product.repository.CategoryRepository;
import com.example.product.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> searchByName(String name) {
        return userRepository.findByFullnameContainingIgnoreCase(name);
    }

    public List<User> findByCategoryId(Long categoryId) {
        return userRepository.findByCategoryId(categoryId);
    }

    public User createUser(User user) {
        // Check if email already exists
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists: " + user.getEmail());
        }
        return userRepository.save(user);
    }

    public User updateUser(Long id, User userDetails) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (userDetails.getFullname() != null) {
                user.setFullname(userDetails.getFullname());
            }
            if (userDetails.getEmail() != null && !userDetails.getEmail().equals(user.getEmail())) {
                if (userRepository.findByEmail(userDetails.getEmail()).isPresent()) {
                    throw new RuntimeException("Email already exists: " + userDetails.getEmail());
                }
                user.setEmail(userDetails.getEmail());
            }
            if (userDetails.getPhone() != null) {
                user.setPhone(userDetails.getPhone());
            }
            if (userDetails.getPassword() != null) {
                user.setPassword(userDetails.getPassword());
            }

            return userRepository.save(user);
        }
        throw new RuntimeException("User not found with id: " + id);
    }

    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    public User addCategoriesToUser(Long userId, Set<Long> categoryIds) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            for (Long categoryId : categoryIds) {
                Optional<Category> category = categoryRepository.findById(categoryId);
                if (category.isPresent()) {
                    user.getCategories().add(category.get());
                }
            }

            return userRepository.save(user);
        }
        throw new RuntimeException("User not found with id: " + userId);
    }

    public User removeCategoryFromUser(Long userId, Long categoryId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Optional<Category> category = categoryRepository.findById(categoryId);

            if (category.isPresent()) {
                user.getCategories().remove(category.get());
                return userRepository.save(user);
            } else {
                throw new RuntimeException("Category not found with id: " + categoryId);
            }
        }
        throw new RuntimeException("User not found with id: " + userId);
    }
}