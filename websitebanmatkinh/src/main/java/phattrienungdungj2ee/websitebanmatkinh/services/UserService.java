package phattrienungdungj2ee.websitebanmatkinh.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import phattrienungdungj2ee.websitebanmatkinh.entity.Address;
import phattrienungdungj2ee.websitebanmatkinh.entity.User;
import phattrienungdungj2ee.websitebanmatkinh.repository.AddressRepository;
import phattrienungdungj2ee.websitebanmatkinh.repository.UserRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public User getById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // MỚI: Cập nhật thông tin User
    @Transactional
    public User updateProfile(Long userId, String fullName, String email, String phoneNumber, String gender, LocalDate birthday) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setGender(gender);
        user.setBirthday(birthday);

        return userRepository.save(user);
    }

    @Transactional
    public void addAddressToUser(Long userId, Address newAddress) {
        User user = getById(userId);
        if (user == null) return;

        newAddress.setUser(user);

        // Logic: Nếu đây là địa chỉ đầu tiên hoặc được tick mặc định
        // Cần reset các địa chỉ cũ về isDefault = false
        if (newAddress.isDefault() || addressRepository.findByUserId(userId).isEmpty()) {
            addressRepository.resetDefaultAddress(userId); // Bạn cần định nghĩa hàm này trong Repo bằng @Modifying
            newAddress.setDefault(true);
        }

        addressRepository.save(newAddress);
    }
}