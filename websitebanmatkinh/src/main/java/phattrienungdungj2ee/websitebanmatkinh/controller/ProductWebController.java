package phattrienungdungj2ee.websitebanmatkinh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import phattrienungdungj2ee.websitebanmatkinh.entity.Product;
import phattrienungdungj2ee.websitebanmatkinh.repository.CategoryRepository;
import phattrienungdungj2ee.websitebanmatkinh.repository.ProductRepository;
import phattrienungdungj2ee.websitebanmatkinh.services.ProductService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductWebController {

    @Autowired private ProductService productService;
    @Autowired private ProductRepository productRepository;
    @Autowired private CategoryRepository categoryRepository;

    // Hiển thị danh sách sản phẩm ở trang chủ cho người mua
    @GetMapping({"/", "/index", "/products"})
    public String viewProducts(
            Model model,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "sort", defaultValue = "asc") String sort,
            @RequestParam(value = "page", defaultValue = "0") int page) {

        int pageSize = 6;
        Sort sortOrder = sort.equalsIgnoreCase("desc") ? Sort.by("price").descending() : Sort.by("price").ascending();
        Pageable pageable = PageRequest.of(page, pageSize, sortOrder);

        Page<Product> productPage;

        if (keyword != null && !keyword.isEmpty() && categoryId != null) {
            productPage = productRepository.findByNameContainingIgnoreCaseAndCategoryId(keyword, categoryId, pageable);
        } else if (keyword != null && !keyword.isEmpty()) {
            productPage = productRepository.findByNameContainingIgnoreCase(keyword, pageable);
        } else if (categoryId != null) {
            productPage = productRepository.findByCategoryId(categoryId, pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }

        model.addAttribute("listProducts", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("categories", categoryRepository.findAll());
        return "index";
    }

    // Xem chi tiết một sản phẩm
    @GetMapping("/products/{id}")
    public String viewProductDetail(@PathVariable Long id, Model model) {
        Product product = productService.getById(id);
        if (product == null) return "redirect:/index";

        List<Product> relatedProducts = new ArrayList<>();
        if (product.getCategory() != null) {
            relatedProducts = productRepository.findByCategoryId(
                    product.getCategory().getId(), PageRequest.of(0, 4)).getContent();
        }

        model.addAttribute("product", product);
        model.addAttribute("relatedProducts", relatedProducts);
        return "product-detail";
    }
}