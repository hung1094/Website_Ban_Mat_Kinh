package phattrienungdungj2ee.websitebanmatkinh.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import phattrienungdungj2ee.websitebanmatkinh.entity.Order;
import phattrienungdungj2ee.websitebanmatkinh.entity.User;
import phattrienungdungj2ee.websitebanmatkinh.repository.OrderRepository;

import java.util.Optional;

@Controller
@RequestMapping("/payment/momo")
public class MomoController {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/{orderId}")
    public String showMomoPayment(@PathVariable Long orderId, HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/login";

        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            if (!order.getUser().getId().equals(loggedInUser.getId())) return "redirect:/products";
            if ("PAID".equals(order.getStatus())) return "redirect:/checkout/success";

            model.addAttribute("order", order);
            return "payment_momo";
        }
        return "redirect:/products";
    }

    // --- API MỚI: Kiểm tra trạng thái đơn hàng cho JavaScript ---
    @GetMapping("/check-status/{orderId}")
    @ResponseBody
    public String checkStatus(@PathVariable Long orderId) {
        return orderRepository.findById(orderId)
                .map(Order::getStatus)
                .orElse("NOT_FOUND");
    }

    // --- API MỚI: Giả lập tín hiệu từ App MoMo (Dùng để TEST) ---
    // Bạn hãy mở URL này ở 1 tab khác để thấy tab thanh toán tự nhảy
    @GetMapping("/simulate-app-confirm/{orderId}")
    @ResponseBody
    public String simulateAppConfirm(@PathVariable Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatus("PAID");
            orderRepository.save(order);
            return "SUCCESS: Order " + orderId + " has been marked as PAID.";
        }
        return "FAILED: Order not found.";
    }

    @PostMapping("/confirm/{orderId}")
    public String confirmPayment(@PathVariable Long orderId, HttpSession session) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatus("PAID");
            orderRepository.save(order);
            session.setAttribute("lastOrder", order);
            return "redirect:/checkout/success";
        }
        return "redirect:/products";
    }
}