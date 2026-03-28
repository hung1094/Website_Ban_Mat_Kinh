package phattrienungdungj2ee.websitebanmatkinh.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    private Long productId;
    private String name;
    private String imageUrl;
    private double price;
    private int quantity;

    // Hàm tính thành tiền cho mỗi dòng
    public double getAmount() {
        return this.price * this.quantity;
    }
}