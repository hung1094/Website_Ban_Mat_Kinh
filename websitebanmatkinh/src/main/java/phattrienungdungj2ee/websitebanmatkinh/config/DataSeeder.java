package phattrienungdungj2ee.websitebanmatkinh.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import phattrienungdungj2ee.websitebanmatkinh.entity.Category;
import phattrienungdungj2ee.websitebanmatkinh.entity.Product;
import phattrienungdungj2ee.websitebanmatkinh.repository.CategoryRepository;
import phattrienungdungj2ee.websitebanmatkinh.repository.ProductRepository;

import java.util.List;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepository, CategoryRepository categoryRepository) {
        return args -> {
            // 1. Kiểm tra và nạp Category trước
            if (categoryRepository.count() == 0) {
                Category cat1 = new Category();
                cat1.setName("Mắt Kính Râm");

                Category cat2 = new Category();
                cat2.setName("Mắt Kính Cận");

                Category cat3 = new Category();
                cat3.setName("Phụ Kiện");

                categoryRepository.saveAll(List.of(cat1, cat2, cat3));
                System.out.println(">>> Đã nạp danh mục mẫu thành công!");

                // 2. Kiểm tra và nạp Sản phẩm gắn với Category
                if (productRepository.count() == 0) {
                    // Sản phẩm 1 - Kính Râm
                    Product p1 = new Product();
                    p1.setName("Kính Phi Công Ray-Ban Aviator Classic");
                    p1.setBrand("Ray-Ban");
                    p1.setPrice(3500000.0);
                    p1.setStock(10);
                    p1.setMaterial("Kim loại mạ vàng");
                    p1.setImageUrl("https://i8.amplience.net/i/luxottica/805289602057_000.png");
                    p1.setDescription("Dòng kính phi công huyền thoại, chống tia UV 100%.");
                    p1.setCategory(cat1); // Gán vào Mắt Kính Râm

                    // Sản phẩm 2 - Kính Râm
                    Product p2 = new Product();
                    p2.setName("Kính Gentle Monster South Side N 01");
                    p2.setBrand("Gentle Monster");
                    p2.setPrice(5200000.0);
                    p2.setStock(5);
                    p2.setMaterial("Nhựa Acetate cao cấp");
                    p2.setImageUrl("https://www.gentlemonster.com/shop/item/southside_n_01/1.jpg");
                    p2.setDescription("Thiết kế gọng vuông đen cổ điển, cá tính.");
                    p2.setCategory(cat1); // Gán vào Mắt Kính Râm

                    // Sản phẩm 3 - Kính Cận
                    Product p3 = new Product();
                    p3.setName("Kính Cận Titan Siêu Nhẹ");
                    p3.setBrand("Seiko");
                    p3.setPrice(1850000.0);
                    p3.setStock(20);
                    p3.setMaterial("Titanium");
                    p3.setImageUrl("https://via.placeholder.com/500x400?text=Kinh+Titan");
                    p3.setDescription("Gọng kính làm từ chất liệu Titan siêu nhẹ.");
                    p3.setCategory(cat2); // Gán vào Mắt Kính Cận

                    productRepository.saveAll(List.of(p1, p2, p3));
                    System.out.println(">>> Đã nạp sản phẩm mẫu thành công!");
                }
            }
        };
    }
}