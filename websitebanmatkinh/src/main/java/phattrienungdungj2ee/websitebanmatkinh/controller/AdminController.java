package phattrienungdungj2ee.websitebanmatkinh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import phattrienungdungj2ee.websitebanmatkinh.entity.*;
import phattrienungdungj2ee.websitebanmatkinh.repository.*;
import phattrienungdungj2ee.websitebanmatkinh.services.ProductService;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private ProductRepository productRepo;
    @Autowired private OrderRepository orderRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private CategoryRepository categoryRepo;
    @Autowired private ProductService productService;

    @Value("${upload.path}")
    private String uploadPath;

    // 1. TỔNG QUAN (DASHBOARD)
    @GetMapping("")
    public String dashboard(Model model) {
        // Lấy tất cả đơn hàng sắp xếp mới nhất lên đầu
        List<Order> allOrders = orderRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));

        double totalRevenue = allOrders.stream()
                .filter(o -> !"CANCELLED".equalsIgnoreCase(o.getStatus()))
                .mapToDouble(Order::getTotalPrice).sum();

        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("totalOrders", allOrders.size());
        model.addAttribute("totalProducts", productRepo.count());
        model.addAttribute("totalUsers", userRepo.count());

        // Lấy 5 đơn hàng gần nhất (xử lý an toàn tránh lỗi subList)
        List<Order> recentOrders = allOrders.stream().limit(5).toList();
        model.addAttribute("recentOrders", recentOrders);

        model.addAttribute("activePage", "dashboard");
        return "admin/dashboard";
    }

    // 2. QUẢN LÝ SẢN PHẨM
    @GetMapping("/products")
    public String listProducts(Model model) {
        // Sắp xếp ID giảm dần để thấy kính mới thêm ở trên cùng
        model.addAttribute("listProducts", productRepo.findAll(Sort.by(Sort.Direction.DESC, "id")));
        model.addAttribute("activePage", "products");
        return "admin/index";
    }

    @GetMapping("/products/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryRepo.findAll());
        model.addAttribute("activePage", "products");
        return "admin/add-product";
    }

    @PostMapping("/products/save")
    public String saveProduct(@ModelAttribute("product") Product product,
                              @RequestParam(value = "imageFiles", required = false) MultipartFile[] files) {
        try {
            Product existingProduct = (product.getId() != null) ? productService.getById(product.getId()) : null;

            // Xử lý Upload ảnh
            if (files != null && files.length > 0 && !files[0].isEmpty()) {
                Path rootPath = Paths.get(uploadPath).toAbsolutePath().normalize();
                if (!Files.exists(rootPath)) Files.createDirectories(rootPath);

                List<ProductImage> newImages = new ArrayList<>();
                for (int i = 0; i < files.length; i++) {
                    MultipartFile file = files[i];
                    if (file.isEmpty()) continue;

                    String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename().replaceAll("\\s+", "_");
                    Path filePath = rootPath.resolve(fileName);
                    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                    ProductImage img = new ProductImage();
                    img.setUrl("/uploads/" + fileName);
                    img.setProduct(product);
                    newImages.add(img);

                    if (i == 0) product.setImageUrl("/uploads/" + fileName);
                }
                product.setImages(newImages);
            } else {
                // Giữ lại ảnh cũ nếu không upload ảnh mới
                if (existingProduct != null) {
                    product.setImageUrl(existingProduct.getImageUrl());
                    product.setImages(existingProduct.getImages());
                }
            }
        } catch (IOException e) {
            System.err.println("Lỗi upload ảnh: " + e.getMessage());
        }

        productService.save(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/products/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.getById(id);
        if (product == null) return "redirect:/admin/products";

        model.addAttribute("product", product);
        model.addAttribute("categories", categoryRepo.findAll());
        model.addAttribute("activePage", "products");
        return "admin/add-product";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return "redirect:/admin/products";
    }

    // 3. QUẢN LÝ ĐƠN HÀNG
    @GetMapping("/orders")
    public String listOrders(Model model) {
        model.addAttribute("listOrders", orderRepo.findAll(Sort.by(Sort.Direction.DESC, "id")));
        model.addAttribute("activePage", "orders");
        return "admin/orders";
    }

    // 4. QUẢN LÝ KHÁCH HÀNG
    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("listUsers", userRepo.findAll());
        model.addAttribute("activePage", "users");
        return "admin/users";
    }
}