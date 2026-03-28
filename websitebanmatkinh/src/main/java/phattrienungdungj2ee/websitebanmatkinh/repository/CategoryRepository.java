package phattrienungdungj2ee.websitebanmatkinh.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import phattrienungdungj2ee.websitebanmatkinh.entity.Category;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Tìm danh mục theo tên (Hữu ích nếu bạn muốn kiểm tra trùng lặp)
    Optional<Category> findByName(String name);
}