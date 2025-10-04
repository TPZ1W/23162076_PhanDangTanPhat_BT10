package vn.iotstar.service;

import java.util.List;
import vn.iotstar.entity.Product;

public interface ProductService {
    List<Product> findAll();
    Product save(Product product);
    void deleteById(Long id);
    List<Product> search(String keyword);
    Product findById(Long id);
}
