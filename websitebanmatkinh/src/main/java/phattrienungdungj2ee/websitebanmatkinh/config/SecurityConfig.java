package phattrienungdungj2ee.websitebanmatkinh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Tắt CSRF sử dụng Lambda
                .csrf(csrf -> csrf.disable())

                // 2. Cấu hình quyền truy cập (Cho phép tất cả để test CRUD)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )

                // 3. Cho phép đăng nhập bằng Form mặc định (nếu cần) hoặc HTTP Basic
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}