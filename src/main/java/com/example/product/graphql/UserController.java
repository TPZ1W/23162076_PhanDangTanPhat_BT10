package com.example.product.graphql;

import com.example.product.dto.UserInput;
import com.example.product.entity.User;
import com.example.product.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @QueryMapping
    public List<User> users() {
        return userService.findAll();
    }

    @QueryMapping
    public Optional<User> userById(@Argument Long id) {
        return userService.findById(id);
    }

    @QueryMapping
    public Optional<User> userByEmail(@Argument String email) {
        return userService.findByEmail(email);
    }

    @QueryMapping
    public List<User> searchUsers(@Argument String name) {
        return userService.searchByName(name);
    }

    @QueryMapping
    public List<User> usersByCategory(@Argument Long categoryId) {
        return userService.findByCategoryId(categoryId);
    }

    @MutationMapping
    public User createUser(@Argument UserInput input) {
        User user = new User();
        user.setFullname(input.getFullname());
        user.setEmail(input.getEmail());
        user.setPassword(input.getPassword()); // Note: Should hash password in production
        user.setPhone(input.getPhone());

        return userService.createUser(user);
    }

    @MutationMapping
    public User updateUser(@Argument Long id, @Argument UserInput input) {
        User user = new User();
        user.setFullname(input.getFullname());
        user.setEmail(input.getEmail());
        user.setPassword(input.getPassword());
        user.setPhone(input.getPhone());

        return userService.updateUser(id, user);
    }

    @MutationMapping
    public Boolean deleteUser(@Argument Long id) {
        try {
            userService.deleteUser(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @MutationMapping
    public User addCategoriesToUser(@Argument Long userId, @Argument Set<Long> categoryIds) {
        return userService.addCategoriesToUser(userId, categoryIds);
    }

    @MutationMapping
    public User removeCategoryFromUser(@Argument Long userId, @Argument Long categoryId) {
        return userService.removeCategoryFromUser(userId, categoryId);
    }
}