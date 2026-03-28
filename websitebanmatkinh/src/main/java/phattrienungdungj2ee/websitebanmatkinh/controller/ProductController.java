package phattrienungdungj2ee.websitebanmatkinh.controller;

import phattrienungdungj2ee.websitebanmatkinh.entity.Product;
import phattrienungdungj2ee.websitebanmatkinh.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping // Lấy danh sách kính
    public List<Product> list() { return productService.getAll(); }

    @PostMapping // Thêm mới kính
    public Product add(@RequestBody Product product) { return productService.save(product); }

    @PutMapping("/{id}") // Cập nhật thông tin kính
    public Product update(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id);
        return productService.save(product);
    }

    @DeleteMapping("/{id}") // Xóa kính
    public String delete(@PathVariable Long id) {
        productService.delete(id);
        return "Đã xóa sản phẩm thành công!";
    }
}