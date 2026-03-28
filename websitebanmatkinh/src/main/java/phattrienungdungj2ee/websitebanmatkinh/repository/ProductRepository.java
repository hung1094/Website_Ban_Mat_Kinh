package phattrienungdungj2ee.websitebanmatkinh.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import phattrienungdungj2ee.websitebanmatkinh.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // 1. Tìm kiếm theo tên sản phẩm (không phân biệt hoa thường) + Phân trang
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // 2. Lọc sản phẩm theo ID của Category + Phân trang
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    // 3. Tìm kiếm kết hợp: Theo tên VÀ Theo Category + Phân trang
    // Dùng khi người dùng vừa nhập ô tìm kiếm vừa chọn dropdown danh mục
    Page<Product> findByNameContainingIgnoreCaseAndCategoryId(String name, Long categoryId, Pageable pageable);
}