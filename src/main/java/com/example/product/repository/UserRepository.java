package com.example.product.repository;

import com.example.product.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByFullnameContainingIgnoreCase(String fullname);

    @Query("SELECT u FROM User u JOIN u.categories c WHERE c.id = :categoryId")
    List<User> findByCategoryId(Long categoryId);

    @Query("SELECT u FROM User u LEFT JOIN u.products p GROUP BY u ORDER BY COUNT(p) DESC")
    List<User> findUsersOrderByProductCount();
}