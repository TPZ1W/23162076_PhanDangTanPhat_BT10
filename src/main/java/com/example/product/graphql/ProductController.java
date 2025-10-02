package com.example.product.graphql;

import com.example.product.dto.ProductInput;
import com.example.product.entity.Product;
import com.example.product.entity.User;
import com.example.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @QueryMapping
    public List<Product> products() {
        return productService.findAll();
    }

    @QueryMapping
    public Optional<Product> productById(@Argument Long id) {
        return productService.findById(id);
    }

    @QueryMapping
    public List<Product> productsByUser(@Argument Long userId) {
        return productService.findByUserId(userId);
    }

    @QueryMapping
    public List<Product> searchProducts(@Argument String title,
            @Argument BigDecimal minPrice,
            @Argument BigDecimal maxPrice,
            @Argument Long userId) {
        return productService.searchProducts(title, minPrice, maxPrice, userId);
    }

    @QueryMapping
    public List<Product> lowStockProducts(@Argument Integer threshold) {
        return productService.findLowStock(threshold != null ? threshold : 10);
    }

    @MutationMapping
    public Product createProduct(@Argument ProductInput input) {
        Product product = new Product();
        product.setTitle(input.getTitle());
        product.setQuantity(input.getQuantity());
        product.setDescription(input.getDescription());
        product.setPrice(input.getPrice());
        product.setImages(input.getImages());

        // Set user
        User user = new User();
        user.setId(input.getUserId());
        product.setUser(user);

        return productService.createProduct(product);
    }

    @MutationMapping
    public Product updateProduct(@Argument Long id, @Argument ProductInput input) {
        Product product = new Product();
        product.setTitle(input.getTitle());
        product.setQuantity(input.getQuantity());
        product.setDescription(input.getDescription());
        product.setPrice(input.getPrice());
        product.setImages(input.getImages());

        return productService.updateProduct(id, product);
    }

    @MutationMapping
    public Boolean deleteProduct(@Argument Long id) {
        try {
            productService.deleteProduct(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @MutationMapping
    public Product updateProductQuantity(@Argument Long id, @Argument Integer quantity) {
        return productService.updateQuantity(id, quantity);
    }

    @QueryMapping
    public List<Product> productsByPriceAsc() {
        return productService.findAllOrderByPriceAsc();
    }

    @QueryMapping
    public List<Product> productsByPriceDesc() {
        return productService.findAllOrderByPriceDesc();
    }

    @QueryMapping
    public List<Product> productsByCategory(@Argument Long categoryId) {
        return productService.findByCategoryId(categoryId);
    }

    @QueryMapping
    public List<Product> productsByCategoryOrderByPrice(@Argument Long categoryId) {
        return productService.findByCategoryIdOrderByPriceAsc(categoryId);
    }
}