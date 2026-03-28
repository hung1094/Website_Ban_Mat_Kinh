package phattrienungdungj2ee.websitebanmatkinh.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import phattrienungdungj2ee.websitebanmatkinh.entity.*;
import phattrienungdungj2ee.websitebanmatkinh.repository.OrderRepository;
import phattrienungdungj2ee.websitebanmatkinh.repository.ProductRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor // Sử dụng Constructor Injection (tốt hơn @Autowired)
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Order createOrder(Order order, User user, Collection<CartItem> cartItems) {
        // 1. Gán thông tin cơ bản cho đơn hàng
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now()); // Lưu ngày đặt hàng
        order.setStatus("PENDING"); // Trạng thái: Chờ xử lý

        // Đảm bảo danh sách chi tiết đơn hàng được khởi tạo để tránh lỗi Null
        if (order.getOrderDetails() == null) {
            order.setOrderDetails(new ArrayList<>());
        }

        // 2. Chuyển đổi từ CartItem sang OrderDetail
        for (CartItem item : cartItems) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setQuantity(item.getQuantity());
            detail.setPrice(item.getPrice());

            // Tìm sản phẩm từ DB
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại: " + item.getProductId()));

            detail.setProduct(product);

            // 3. Logic trừ tồn kho (Quan trọng trong TMĐT)
            /* if (product.getQuantity() < item.getQuantity()) {
                throw new RuntimeException("Sản phẩm " + product.getName() + " không đủ số lượng trong kho!");
            }
            product.setQuantity(product.getQuantity() - item.getQuantity());
            productRepository.save(product);
            */

            order.getOrderDetails().add(detail);
        }

        // 4. Lưu đơn hàng (Cơ chế Cascade.ALL trong Entity Order sẽ tự lưu các OrderDetail)
        return orderRepository.save(order);
    }
}