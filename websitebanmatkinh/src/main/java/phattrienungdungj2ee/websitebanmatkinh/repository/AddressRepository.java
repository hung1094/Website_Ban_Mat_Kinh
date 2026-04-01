package phattrienungdungj2ee.websitebanmatkinh.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import phattrienungdungj2ee.websitebanmatkinh.entity.Address;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    // Tìm tất cả địa chỉ của một user
    List<Address> findByUserId(Long userId);

    // Tìm địa chỉ mặc định duy nhất của user (hữu ích cho Checkout)
    Optional<Address> findByUserIdAndIsDefaultTrue(Long userId);

    @Modifying
    @Transactional // Đảm bảo việc update được thực thi trong một transaction
    @Query("UPDATE Address a SET a.isDefault = false WHERE a.user.id = :userId")
    void resetDefaultAddress(@Param("userId") Long userId);
}