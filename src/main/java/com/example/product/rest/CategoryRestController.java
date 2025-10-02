package com.example.product.rest;

import com.example.product.dto.CategoryDto;
import com.example.product.dto.CategoryInput;
import com.example.product.entity.Category;
import com.example.product.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CategoryRestController {

    private final CategoryService categoryService;

    // READ - Get all categories
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<Category> categories = categoryService.findAll();
        if (categories.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<CategoryDto> dtoList = categories.stream()
                .map(CategoryDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    // READ - Get category by ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
        Optional<Category> category = categoryService.findById(id);
        return category.map(c -> ResponseEntity.ok(new CategoryDto(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    // READ - Get category by name
    @GetMapping("/name/{name}")
    public ResponseEntity<CategoryDto> getCategoryByName(@PathVariable String name) {
        Optional<Category> category = categoryService.findByName(name);
        return category.map(c -> ResponseEntity.ok(new CategoryDto(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    // READ - Search categories by name
    @GetMapping("/search")
    public ResponseEntity<List<CategoryDto>> searchCategories(@RequestParam String name) {
        List<Category> categories = categoryService.searchByName(name);
        List<CategoryDto> dtoList = categories.stream()
                .map(CategoryDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    // READ - Get categories by user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CategoryDto>> getCategoriesByUser(@PathVariable Long userId) {
        List<Category> categories = categoryService.findByUserId(userId);
        List<CategoryDto> dtoList = categories.stream()
                .map(CategoryDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    // CREATE - Create new category
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryInput categoryInput) {
        try {
            Category category = new Category();
            category.setName(categoryInput.getName());
            category.setImages(categoryInput.getImages());

            Category savedCategory = categoryService.createCategory(category);
            return new ResponseEntity<>(new CategoryDto(savedCategory), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // UPDATE - Update category
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id,
            @Valid @RequestBody CategoryInput categoryInput) {
        try {
            Category category = new Category();
            category.setName(categoryInput.getName());
            category.setImages(categoryInput.getImages());

            Category updatedCategory = categoryService.updateCategory(id, category);
            return ResponseEntity.ok(new CategoryDto(updatedCategory));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE - Delete category
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}