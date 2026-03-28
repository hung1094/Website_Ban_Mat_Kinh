package phattrienungdungj2ee.websitebanmatkinh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import phattrienungdungj2ee.websitebanmatkinh.entity.CartItem;
import phattrienungdungj2ee.websitebanmatkinh.entity.Product;
import phattrienungdungj2ee.websitebanmatkinh.services.CartService;
import phattrienungdungj2ee.websitebanmatkinh.services.ProductService;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired private CartService cartService;
    @Autowired private ProductService productService;

    // Xem giỏ hàng
    @GetMapping
    public String viewCart(Model model) {
        model.addAttribute("cartItems", cartService.getItems());
        model.addAttribute("totalPrice", cartService.getTotal());
        return "cart"; // Trỏ đến file cart.html
    }

    // Thêm vào giỏ
    @GetMapping("/add/{id}")
    public String add(@PathVariable("id") Long id) {
        Product p = productService.getById(id);
        if (p != null) {
            CartItem item = new CartItem(p.getId(), p.getName(), p.getImageUrl(), p.getPrice(), 1);
            cartService.add(item);
        }
        return "redirect:/cart";
    }

    // Xóa item
    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Long id) {
        cartService.remove(id);
        return "redirect:/cart";
    }

    // Cập nhật số lượng
    @PostMapping("/update")
    public String update(@RequestParam("id") Long id, @RequestParam("qty") int qty) {
        cartService.update(id, qty);
        return "redirect:/cart";
    }
}