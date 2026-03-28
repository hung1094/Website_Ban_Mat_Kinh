package phattrienungdungj2ee.websitebanmatkinh.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import phattrienungdungj2ee.websitebanmatkinh.entity.Address;
import phattrienungdungj2ee.websitebanmatkinh.entity.Order;
import phattrienungdungj2ee.websitebanmatkinh.entity.User;
import phattrienungdungj2ee.websitebanmatkinh.services.CartService;
import phattrienungdungj2ee.websitebanmatkinh.services.OrderService;
import phattrienungdungj2ee.websitebanmatkinh.services.UserService;

@Controller
@RequestMapping("/checkout")
@RequiredArgsConstructor // Tự động tạo Constructor cho các final field bên dưới
public class CheckoutController {

    private final CartService cartService;
    private final OrderService orderService;
    private final UserService userService;

    @GetMapping
    public String index(Model model, HttpSession session) {
        // 1. Kiểm tra đăng nhập
        User sessionUser = (User) session.getAttribute("loggedInUser");
        if (sessionUser == null) return "redirect:/login";

        // Lấy lại User từ DB để có danh sách địa chỉ mới nhất
        User user = userService.getById(sessionUser.getId());

        // 2. Kiểm tra giỏ hàng
        if (cartService.isEmpty()) return "redirect:/cart";

        // 3. Tìm địa chỉ mặc định của User để hiển thị lên Form
        Address defaultAddr = user.getAddresses().stream()
                .filter(Address::isDefault)
                .findFirst()
                .orElse(null);

        model.addAttribute("user", user);
        model.addAttribute("defaultAddress", defaultAddr); // Truyền sang để th:value trong HTML
        model.addAttribute("order", new Order());
        model.addAttribute("cartItems", cartService.getItems());
        model.addAttribute("totalPrice", cartService.getTotal());

        return "checkout";
    }

    @PostMapping("/process")
    public String process(@ModelAttribute("order") Order order,
                          @RequestParam("paymentMethod") String paymentMethod,
                          @RequestParam("customerName") String customerName, // Lấy từ input tên người nhận
                          @RequestParam("phone") String phone,               // Lấy từ input số điện thoại
                          @RequestParam("city") String city,
                          @RequestParam("district") String district,
                          @RequestParam("ward") String ward,
                          @RequestParam("detailAddress") String detailAddress,
                          HttpSession session,
                          Model model) {

        User sessionUser = (User) session.getAttribute("loggedInUser");
        if (sessionUser == null) return "redirect:/login";

        try {
            // 4. Gộp địa chỉ thành một chuỗi duy nhất để lưu vào bảng Order
            String fullAddress = String.format("%s, %s, %s, %s", detailAddress, ward, district, city);

            // Set các thông tin cần thiết cho Entity Order
            order.setAddress(fullAddress);
            order.setCustomerName(customerName);
            order.setPhone(phone);
            order.setPaymentMethod(paymentMethod);
            order.setTotalPrice(cartService.getTotal());

            // 5. Gọi Service xử lý nghiệp vụ (Lưu Order, lưu OrderDetail, trừ tồn kho, xóa giỏ hàng)
            Order savedOrder = orderService.createOrder(order, sessionUser, cartService.getItems());

            // 6. Xóa giỏ hàng sau khi đặt thành công
            cartService.clear();
            session.setAttribute("cartCount", 0);

            // Lưu thông tin đơn hàng vào session để trang thành công hiển thị
            session.setAttribute("lastOrder", savedOrder);

            if ("MOMO".equals(paymentMethod)) {
                return "redirect:/payment/momo/" + savedOrder.getId();
            }

            return "redirect:/checkout/success";

        } catch (Exception e) {
            // Log lỗi nếu cần: e.printStackTrace();
            model.addAttribute("error", "Lỗi đặt hàng: " + e.getMessage());
            model.addAttribute("user", sessionUser);
            model.addAttribute("cartItems", cartService.getItems());
            model.addAttribute("totalPrice", cartService.getTotal());
            return "checkout";
        }
    }

    @GetMapping("/success")
    public String success(HttpSession session, Model model) {
        Order lastOrder = (Order) session.getAttribute("lastOrder");
        if (lastOrder == null) return "redirect:/";

        model.addAttribute("order", lastOrder);
        // Sau khi hiển thị xong, có thể xóa lastOrder khỏi session để tránh refresh trang lặp lại
        // session.removeAttribute("lastOrder");

        return "checkout_success";
    }
}