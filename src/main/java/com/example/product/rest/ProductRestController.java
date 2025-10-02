package com.example.product.rest;

import com.example.product.dto.ProductDto;
import com.example.product.dto.ProductInput;
import com.example.product.entity.Product;
import com.example.product.entity.User;
import com.example.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductRestController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDto>> listAll() {
        List<Product> list = productService.findAll();
        if (list.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<ProductDto> dtoList = list.stream()
                .map(ProductDto::new)
                .collect(Collectors.toList());
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> find(@PathVariable Long id) {
        Optional<Product> pro = productService.findById(id);
        if (pro.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ProductDto(pro.get()));
    }

    @PostMapping
    public ResponseEntity<ProductDto> save(@Valid @RequestBody ProductInput input) {
        try {
            Product product = new Product();
            product.setTitle(input.getTitle());
            product.setQuantity(input.getQuantity());
            product.setDescription(input.getDescription());
            product.setPrice(input.getPrice());
            product.setImages(input.getImages());

            User user = new User();
            user.setId(input.getUserId());
            product.setUser(user);

            Product saved = productService.createProduct(product);
            return new ResponseEntity<>(new ProductDto(saved), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable Long id, @Valid @RequestBody ProductInput input) {
        try {
            Product product = new Product();
            product.setTitle(input.getTitle());
            product.setQuantity(input.getQuantity());
            product.setDescription(input.getDescription());
            product.setPrice(input.getPrice());
            product.setImages(input.getImages());

            User user = new User();
            user.setId(input.getUserId());
            product.setUser(user);

            Product updated = productService.updateProduct(id, product);
            return ResponseEntity.ok(new ProductDto(updated));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> search(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Long userId) {

        List<Product> products = productService.searchProducts(title, minPrice, maxPrice, userId);
        List<ProductDto> dtoList = products.stream()
                .map(ProductDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ProductDto>> getByUser(@PathVariable Long userId) {
        List<Product> products = productService.findByUserId(userId);
        List<ProductDto> dtoList = products.stream()
                .map(ProductDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/sort/price-asc")
    public ResponseEntity<List<ProductDto>> getProductsByPriceAsc() {
        List<Product> products = productService.findAllOrderByPriceAsc();
        List<ProductDto> dtoList = products.stream()
                .map(ProductDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/sort/price-desc")
    public ResponseEntity<List<ProductDto>> getProductsByPriceDesc() {
        List<Product> products = productService.findAllOrderByPriceDesc();
        List<ProductDto> dtoList = products.stream()
                .map(ProductDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductDto>> getProductsByCategory(@PathVariable Long categoryId) {
        List<Product> products = productService.findByCategoryId(categoryId);
        List<ProductDto> dtoList = products.stream()
                .map(ProductDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/category/{categoryId}/sort/price-asc")
    public ResponseEntity<List<ProductDto>> getProductsByCategoryOrderByPrice(@PathVariable Long categoryId) {
        List<Product> products = productService.findByCategoryIdOrderByPriceAsc(categoryId);
        List<ProductDto> dtoList = products.stream()
                .map(ProductDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    // Additional CRUD operations
    @GetMapping("/title/{title}")
    public ResponseEntity<List<ProductDto>> getProductsByTitle(@PathVariable String title) {
        List<Product> products = productService.searchByTitle(title);
        List<ProductDto> dtoList = products.stream()
                .map(ProductDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductDto>> getLowStockProducts(@RequestParam(defaultValue = "10") Integer threshold) {
        List<Product> products = productService.findLowStock(threshold);
        List<ProductDto> dtoList = products.stream()
                .map(ProductDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @PatchMapping("/{id}/quantity")
    public ResponseEntity<ProductDto> updateQuantity(@PathVariable Long id, @RequestParam Integer quantity) {
        try {
            Product updated = productService.updateQuantity(id, quantity);
            return ResponseEntity.ok(new ProductDto(updated));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<ProductDto>> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        List<Product> products = productService.findByPriceRange(minPrice, maxPrice);
        List<ProductDto> dtoList = products.stream()
                .map(ProductDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }
}