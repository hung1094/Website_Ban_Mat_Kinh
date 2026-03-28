package phattrienungdungj2ee.websitebanmatkinh.services;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;
import phattrienungdungj2ee.websitebanmatkinh.entity.CartItem;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
@SessionScope // Giỏ hàng sẽ tồn tại riêng biệt cho mỗi người dùng (Session)
public class CartService {
    private Map<Long, CartItem> maps = new HashMap<>();

    // 1. Thêm sản phẩm (Create/Update)
    public void add(CartItem item) {
        CartItem cartItem = maps.get(item.getProductId());
        if (cartItem == null) {
            maps.put(item.getProductId(), item);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        }
    }

    // 2. Xóa sản phẩm (Delete)
    public void remove(Long id) {
        maps.remove(id);
    }

    // 3. Cập nhật số lượng (Update)
    public void update(Long id, int qty) {
        CartItem cartItem = maps.get(id);
        if (cartItem != null) {
            cartItem.setQuantity(qty);
        }
    }

    // 4. Lấy tất cả sản phẩm (Read)
    public Collection<CartItem> getItems() {
        return maps.values();
    }

    // 5. Tính tổng tiền
    public double getTotal() {
        return maps.values().stream().mapToDouble(item -> item.getAmount()).sum();
    }

    // 6. Xóa sạch giỏ hàng
    public void clear() {
        maps.clear();
    }

    // 7. Đếm tổng số lượng item
    public int getCount() {
        return maps.values().size();
    }

    public boolean isEmpty() {
        return maps.isEmpty();
    }
}