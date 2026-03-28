package phattrienungdungj2ee.websitebanmatkinh.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import phattrienungdungj2ee.websitebanmatkinh.entity.User;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Tìm người dùng bằng tên đăng nhập để kiểm tra login
    Optional<User> findByUsername(String username);

    // Kiểm tra xem username đã tồn tại chưa khi đăng ký
    boolean existsByUsername(String username);
}