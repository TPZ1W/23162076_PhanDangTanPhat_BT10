package vn.iotstar.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "products")
@NamedQuery(
        name = "Product.findByTitleContaining",
        query = "SELECT p FROM Product p WHERE p.title LIKE CONCAT('%', :title, '%')"
)
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 200)
    private String title; // tên sản phẩm

    @Column(columnDefinition = "TEXT")
    private String description; // mô tả sản phẩm

    @Column(nullable = false)
    private Double price; // giá sản phẩm

    @Column(nullable = false)
    private Integer quantity; // số lượng trong kho

    @Column(name = "image", nullable = true)
    private String image; // tên file ảnh (lưu trong thư mục upload.path)

    // Nếu bạn có bảng Category thì thêm quan hệ ManyToOne
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
