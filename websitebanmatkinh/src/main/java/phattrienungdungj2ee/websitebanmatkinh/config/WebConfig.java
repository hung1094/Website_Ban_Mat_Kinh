package phattrienungdungj2ee.websitebanmatkinh.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${upload.path}")
    private String uploadFolder;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // Convert sang đường dẫn tuyệt đối
        Path uploadDir = Paths.get(uploadFolder).toAbsolutePath().normalize();

        // Luôn thêm "file:" + "/" cuối để Spring hiểu đúng
        String location = "file:" + uploadDir.toString() + "/";

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(location);

        // Debug cực rõ ràng
        System.out.println("======================================");
        System.out.println("UPLOAD CONFIG DEBUG");
        System.out.println("Upload folder (config): " + uploadFolder);
        System.out.println("Absolute path: " + uploadDir);
        System.out.println("Resource location: " + location);
        System.out.println("======================================");
    }
}