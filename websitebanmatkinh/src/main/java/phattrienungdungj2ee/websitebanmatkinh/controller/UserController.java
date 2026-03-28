package phattrienungdungj2ee.websitebanmatkinh.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import phattrienungdungj2ee.websitebanmatkinh.entity.Address;
import phattrienungdungj2ee.websitebanmatkinh.entity.User;
import phattrienungdungj2ee.websitebanmatkinh.repository.OrderRepository;
import phattrienungdungj2ee.websitebanmatkinh.services.UserService;

import java.time.LocalDate;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final OrderRepository orderRepository;

    private User getLoggedInUser(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        return (user != null) ? userService.getById(user.getId()) : null;
    }

    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        User user = getLoggedInUser(session);
        if (user == null) return "redirect:/login";

        model.addAttribute("user", user);
        model.addAttribute("activePage", "profile");
        return "user/profile";
    }

    // MỚI: Xử lý cập nhật thông tin cá nhân
    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam String fullName,
                                @RequestParam String email,
                                @RequestParam String phoneNumber,
                                @RequestParam String gender,
                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthday,
                                HttpSession session) {
        User user = getLoggedInUser(session);
        if (user == null) return "redirect:/login";

        User updatedUser = userService.updateProfile(user.getId(), fullName, email, phoneNumber, gender, birthday);

        // Cập nhật lại user trong session để hiển thị đúng thông tin mới ở các trang khác (Header...)
        session.setAttribute("loggedInUser", updatedUser);

        return "redirect:/user/profile?success";
    }

    @GetMapping("/addresses")
    public String showAddresses(HttpSession session, Model model) {
        User user = getLoggedInUser(session);
        if (user == null) return "redirect:/login";

        model.addAttribute("user", user);
        model.addAttribute("addresses", user.getAddresses());
        model.addAttribute("activePage", "addresses");
        return "user/addresses";
    }

    @PostMapping("/addresses/add")
    public String addAddress(@ModelAttribute Address newAddress, HttpSession session) {
        User user = getLoggedInUser(session);
        if (user == null) return "redirect:/login";

        userService.addAddressToUser(user.getId(), newAddress);
        return "redirect:/user/addresses?success";
    }

    @GetMapping("/orders")
    public String showOrders(HttpSession session, Model model) {
        User user = getLoggedInUser(session);
        if (user == null) return "redirect:/login";

        model.addAttribute("user", user); // Thêm user để sidebar hiển thị ảnh/tên
        model.addAttribute("orders", orderRepository.findByUserId(user.getId()));
        model.addAttribute("activePage", "orders");
        return "user/orders";
    }
}