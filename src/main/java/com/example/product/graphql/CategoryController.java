package com.example.product.graphql;

import com.example.product.dto.CategoryInput;
import com.example.product.entity.Category;
import com.example.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @QueryMapping
    public List<Category> categories() {
        return categoryService.findAll();
    }

    @QueryMapping
    public Optional<Category> categoryById(@Argument Long id) {
        return categoryService.findById(id);
    }

    @QueryMapping
    public Optional<Category> categoryByName(@Argument String name) {
        return categoryService.findByName(name);
    }

    @QueryMapping
    public List<Category> searchCategories(@Argument String name) {
        return categoryService.searchByName(name);
    }

    @QueryMapping
    public List<Category> categoriesByUser(@Argument Long userId) {
        return categoryService.findByUserId(userId);
    }

    @MutationMapping
    public Category createCategory(@Argument CategoryInput input) {
        Category category = new Category();
        category.setName(input.getName());
        category.setImages(input.getImages());

        return categoryService.createCategory(category);
    }

    @MutationMapping
    public Category updateCategory(@Argument Long id, @Argument CategoryInput input) {
        Category category = new Category();
        category.setName(input.getName());
        category.setImages(input.getImages());

        return categoryService.updateCategory(id, category);
    }

    @MutationMapping
    public Boolean deleteCategory(@Argument Long id) {
        try {
            categoryService.deleteCategory(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}