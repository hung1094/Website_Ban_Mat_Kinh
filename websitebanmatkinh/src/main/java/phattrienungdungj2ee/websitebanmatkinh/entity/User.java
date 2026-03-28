package phattrienungdungj2ee.websitebanmatkinh.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String fullName;

    @Column(unique = true)
    private String email;

    private String phoneNumber; // Để khớp với trang checkout và hồ sơ

    private String avatarUrl; // Lưu đường dẫn ảnh đại diện (image_e57a62.png)

    private String gender; // Nam, Nữ, Khác

    private LocalDate birthday; // Ngày sinh (Dùng LocalDate của Java 8+)

    private String role = "CUSTOMER"; // Mặc định là khách hàng

    // Quan hệ với Address (Sổ địa chỉ)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude // Tránh lỗi vòng lặp vô tận khi in log (Infinite Recursion)
    private List<Address> addresses = new ArrayList<>();

    // Quan hệ với Order (Lịch sử đơn hàng)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Order> orders = new ArrayList<>();

    // Helper method để thêm địa chỉ dễ dàng hơn
    public void addAddress(Address address) {
        addresses.add(address);
        address.setUser(this);
    }
}