package phattrienungdungj2ee.websitebanmatkinh;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
// Nhớ thay đổi đường dẫn package bên dưới cho đúng với project của bạn nếu bị báo đỏ
import phattrienungdungj2ee.websitebanmatkinh.repository.UserRepository;
import phattrienungdungj2ee.websitebanmatkinh.entity.User;

@SpringBootApplication
public class WebsitebanmatkinhApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebsitebanmatkinhApplication.class, args);
	}

	@Bean
	CommandLineRunner upgradeToAdmin(UserRepository userRepository) {
		return args -> {
			// CHÚ Ý: Thay tên tài khoản bạn đã đăng ký vào đây
			String myUsername = "hung";

			userRepository.findByUsername(myUsername).ifPresent(user -> {
				user.setRole("ADMIN");
				userRepository.save(user);
				System.out.println("----------------------------------------------");
				System.out.println("✅ THÀNH CÔNG: " + myUsername + " ĐÃ LÀ ADMIN!");
				System.out.println("----------------------------------------------");
			});
		};
	}
}