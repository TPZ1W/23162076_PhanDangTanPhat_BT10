package com.example.product.service;

import com.example.product.entity.Product;
import com.example.product.entity.User;
import com.example.product.repository.ProductRepository;
import com.example.product.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> findByUserId(Long userId) {
        return productRepository.findByUserId(userId);
    }

    public List<Product> searchByTitle(String title) {
        return productRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Product> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }

    public List<Product> findLowStock(Integer threshold) {
        return productRepository.findByQuantityLessThan(threshold);
    }

    public List<Product> searchProducts(String title, BigDecimal minPrice, BigDecimal maxPrice, Long userId) {
        return productRepository.searchProducts(title, minPrice, maxPrice, userId);
    }

    public Product createProduct(Product product) {
        if (product.getUser() != null && product.getUser().getId() != null) {
            Optional<User> user = userRepository.findById(product.getUser().getId());
            if (user.isPresent()) {
                product.setUser(user.get());
                return productRepository.save(product);
            } else {
                throw new RuntimeException("User not found with id: " + product.getUser().getId());
            }
        }
        throw new RuntimeException("User is required for product creation");
    }

    public Product updateProduct(Long id, Product productDetails) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            if (productDetails.getTitle() != null) {
                product.setTitle(productDetails.getTitle());
            }
            if (productDetails.getDescription() != null) {
                product.setDescription(productDetails.getDescription());
            }
            if (productDetails.getPrice() != null) {
                product.setPrice(productDetails.getPrice());
            }
            if (productDetails.getQuantity() != null) {
                product.setQuantity(productDetails.getQuantity());
            }
            if (productDetails.getImages() != null) {
                product.setImages(productDetails.getImages());
            }

            return productRepository.save(product);
        }
        throw new RuntimeException("Product not found with id: " + id);
    }

    public void deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new RuntimeException("Product not found with id: " + id);
        }
    }

    public Product updateQuantity(Long id, Integer newQuantity) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setQuantity(newQuantity);
            return productRepository.save(product);
        }
        throw new RuntimeException("Product not found with id: " + id);
    }

    public List<Product> findAllOrderByPriceAsc() {
        return productRepository.findAllOrderByPriceAsc();
    }

    public List<Product> findAllOrderByPriceDesc() {
        return productRepository.findAllOrderByPriceDesc();
    }

    public List<Product> findByCategoryId(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public List<Product> findByCategoryIdOrderByPriceAsc(Long categoryId) {
        return productRepository.findByCategoryIdOrderByPriceAsc(categoryId);
    }
}