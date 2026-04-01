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

    @GetMapping({"/", "/index", "/products"})
    public String viewProducts(
            Model model,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "categoryId", required = false) List<Long> categoryIds,
            @RequestParam(value = "brand", required = false) List<String> brands,
            @RequestParam(value = "material", required = false) List<String> materials,
            @RequestParam(value = "minPrice", required = false) Double minPrice,
            @RequestParam(value = "maxPrice", required = false) Double maxPrice,
            @RequestParam(value = "page", defaultValue = "0") int page) {

        int pageSize = 9; // Tăng lên 9 để khớp với row-cols-md-3 (3 hàng x 3 cột) // phn trang 9sp/1trang
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("id").descending());

        // Thực thi lọc dữ liệu
        Page<Product> productPage = productRepository.findWithFilters(
                (keyword != null && !keyword.isEmpty()) ? keyword : null,
                (categoryIds != null && !categoryIds.isEmpty()) ? categoryIds : null,
                (brands != null && !brands.isEmpty()) ? brands : null,
                (materials != null && !materials.isEmpty()) ? materials : null,
                minPrice,
                maxPrice,
                pageable
        );

        // Đẩy dữ liệu ra View
        model.addAttribute("listProducts", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("categories", categoryRepository.findAll());

        // QUAN TRỌNG: Gửi lại các giá trị đã lọc để UI giữ trạng thái Checkbox
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedCategoryIds", categoryIds);
        model.addAttribute("selectedBrands", brands);
        model.addAttribute("selectedMaterials", materials);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);

        return "index";
    }

    @GetMapping("/products/{id}")
    public String viewProductDetail(@PathVariable Long id, Model model) {
        Product product = productService.getById(id);
        if (product == null) return "redirect:/index";

        // Lấy 4 sản phẩm liên quan cùng danh mục
        List<Product> relatedProducts = new ArrayList<>();
        if (product.getCategory() != null) {
            relatedProducts = productRepository.findRelatedProducts(
                    product.getCategory().getId(), product.getId(), PageRequest.of(0, 4));
        }

        model.addAttribute("product", product);
        model.addAttribute("relatedProducts", relatedProducts);
        return "product-detail";
    }
}