package vn.iotstar.service;

import java.util.List;
import java.util.Optional;

import vn.iotstar.entity.Category;


public interface CategoryService {
    List<Category> findAll();
    Category save(Category category);
    void deleteById(Long id);
    List<Category> search(String keyword);
    Optional<Category> findById(Long id);

} 