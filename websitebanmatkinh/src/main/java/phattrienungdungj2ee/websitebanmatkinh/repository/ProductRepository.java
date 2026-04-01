package phattrienungdungj2ee.websitebanmatkinh.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import phattrienungdungj2ee.websitebanmatkinh.entity.Product;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // QUERY TỔNG HỢP: Lọc theo nhiều tiêu chí cùng lúc
    @Query("SELECT p FROM Product p WHERE " +
            "(:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "(:categoryIds IS NULL OR p.category.id IN :categoryIds) AND " +
            "(:brands IS NULL OR p.brand IN :brands) AND " +
            "(:materials IS NULL OR p.material IN :materials) AND " +
            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice)")
    Page<Product> findWithFilters(
            @Param("keyword") String keyword,
            @Param("categoryIds") List<Long> categoryIds,
            @Param("brands") List<String> brands,
            @Param("materials") List<String> materials,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            Pageable pageable);

    // Lấy sản phẩm cùng danh mục (không bao gồm chính nó) cho trang chi tiết
    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId AND p.id != :excludeId")
    List<Product> findRelatedProducts(@Param("categoryId") Long categoryId, @Param("excludeId") Long excludeId, Pageable pageable);
}