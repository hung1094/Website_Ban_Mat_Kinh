package phattrienungdungj2ee.websitebanmatkinh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
        List<Order> allOrders = orderRepo.findAll();
        double totalRevenue = allOrders.stream()
                .filter(o -> !"CANCELLED".equals(o.getStatus()))
                .mapToDouble(Order::getTotalPrice).sum();

        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("totalOrders", allOrders.size());
        model.addAttribute("totalProducts", productRepo.count());
        model.addAttribute("totalUsers", userRepo.count());

        // Chỉ lấy 5 đơn hàng gần nhất để hiển thị
        List<Order> recentOrders = allOrders.size() > 5 ? allOrders.subList(allOrders.size() - 5, allOrders.size()) : allOrders;
        model.addAttribute("recentOrders", recentOrders);
        model.addAttribute("activePage", "dashboard");
        return "admin/dashboard";
    }

    // 2. QUẢN LÝ SẢN PHẨM
    @GetMapping("/products")
    public String listProducts(Model model) {
        model.addAttribute("listProducts", productRepo.findAll());
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
            if (product.getId() != null) {
                Product oldProduct = productService.getById(product.getId());
                if (files == null || files.length == 0 || files[0].isEmpty()) {
                    product.setImageUrl(oldProduct.getImageUrl());
                    product.setImages(oldProduct.getImages());
                }
            }

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
            }
        } catch (IOException e) { e.printStackTrace(); }
        productService.save(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/products/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.getById(id);
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
        model.addAttribute("listOrders", orderRepo.findAll());
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