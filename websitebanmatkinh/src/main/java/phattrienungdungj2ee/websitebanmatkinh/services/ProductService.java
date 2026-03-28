package phattrienungdungj2ee.websitebanmatkinh.services;

import phattrienungdungj2ee.websitebanmatkinh.entity.Product;
import phattrienungdungj2ee.websitebanmatkinh.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Lấy tất cả sản phẩm
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    // Lấy chi tiết sản phẩm theo ID
    public Product getById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    // Lưu sản phẩm (Dùng cho cả Thêm mới và Cập nhật)
    public Product save(Product product) {
        return productRepository.save(product);
    }

    // Xóa sản phẩm kèm kiểm tra tồn tại
    public void delete(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        }
    }

    // Logic cập nhật sản phẩm cụ thể (Tùy chọn nếu bạn muốn tách biệt với save)
    public Product updateProduct(Long id, Product productDetails) {
        return productRepository.findById(id).map(product -> {
            product.setName(productDetails.getName());
            product.setBrand(productDetails.getBrand());
            product.setPrice(productDetails.getPrice());
            product.setStock(productDetails.getStock());
            product.setImageUrl(productDetails.getImageUrl());
            return productRepository.save(product);
        }).orElse(null);
    }
}