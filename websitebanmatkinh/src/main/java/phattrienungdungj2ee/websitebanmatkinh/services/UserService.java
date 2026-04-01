package phattrienungdungj2ee.websitebanmatkinh.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import phattrienungdungj2ee.websitebanmatkinh.entity.Address;
import phattrienungdungj2ee.websitebanmatkinh.entity.User;
import phattrienungdungj2ee.websitebanmatkinh.repository.AddressRepository;
import phattrienungdungj2ee.websitebanmatkinh.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public User getById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        newAddress.setUser(user);

        // Kiểm tra nếu là địa chỉ đầu tiên hoặc được tick mặc định
        List<Address> existingAddresses = addressRepository.findByUserId(userId);
        if (newAddress.isDefault() || existingAddresses.isEmpty()) {
            // Reset tất cả địa chỉ cũ về false trước khi lưu cái mới
            addressRepository.resetDefaultAddress(userId);
            newAddress.setDefault(true);
        }

        addressRepository.save(newAddress);
    }

    // --- MỚI: Thiết lập địa chỉ mặc định ---
    @Transactional
    public void setDefaultAddress(Long userId, Long addressId) {
        // 1. Reset toàn bộ địa chỉ của user này về false
        addressRepository.resetDefaultAddress(userId);

        // 2. Tìm địa chỉ cụ thể và set thành true
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Địa chỉ không tồn tại"));

        // Bảo mật: Kiểm tra xem địa chỉ này có đúng là của User đang đăng nhập không
        if (!address.getUser().getId().equals(userId)) {
            throw new RuntimeException("Bạn không có quyền chỉnh sửa địa chỉ này");
        }

        address.setDefault(true);
        addressRepository.save(address);
    }

    // --- MỚI: Xóa địa chỉ ---
    @Transactional
    public void deleteAddress(Long userId, Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Địa chỉ không tồn tại"));

        // Kiểm tra quyền sở hữu
        if (!address.getUser().getId().equals(userId)) {
            throw new RuntimeException("Bạn không có quyền xóa địa chỉ này");
        }

        // Không cho phép xóa địa chỉ mặc định để tránh lỗi logic khi giao hàng
        if (address.isDefault()) {
            throw new RuntimeException("Không thể xóa địa chỉ mặc định");
        }

        addressRepository.delete(address);
    }
}