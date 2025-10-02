package com.example.product.repository;

import com.example.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByUserId(Long userId);

    List<Product> findByTitleContainingIgnoreCase(String title);

    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    List<Product> findByQuantityLessThan(Integer quantity);

    @Query("SELECT p FROM Product p WHERE p.user.id = :userId ORDER BY p.createdAt DESC")
    List<Product> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);

    @Query("SELECT p FROM Product p JOIN p.user u WHERE u.email = :email")
    List<Product> findByUserEmail(@Param("email") String email);

    @Query("SELECT p FROM Product p WHERE " +
            "(:title IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
            "(:userId IS NULL OR p.user.id = :userId)")
    List<Product> searchProducts(@Param("title") String title,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("userId") Long userId);

    // Lấy tất cả sản phẩm sắp xếp theo giá từ thấp đến cao
    @Query("SELECT p FROM Product p ORDER BY p.price ASC")
    List<Product> findAllOrderByPriceAsc();

    // Lấy tất cả sản phẩm sắp xếp theo giá từ cao đến thấp
    @Query("SELECT p FROM Product p ORDER BY p.price DESC")
    List<Product> findAllOrderByPriceDesc();

    // Lấy tất cả sản phẩm của users quan tâm đến category cụ thể
    @Query("SELECT p FROM Product p JOIN p.user u JOIN u.categories c WHERE c.id = :categoryId")
    List<Product> findByCategoryId(@Param("categoryId") Long categoryId);

    // Lấy tất cả sản phẩm của users quan tâm đến category cụ thể, sắp xếp theo giá
    @Query("SELECT p FROM Product p JOIN p.user u JOIN u.categories c WHERE c.id = :categoryId ORDER BY p.price ASC")
    List<Product> findByCategoryIdOrderByPriceAsc(@Param("categoryId") Long categoryId);
}