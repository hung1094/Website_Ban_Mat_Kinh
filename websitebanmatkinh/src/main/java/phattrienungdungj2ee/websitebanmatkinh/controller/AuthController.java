package phattrienungdungj2ee.websitebanmatkinh.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import phattrienungdungj2ee.websitebanmatkinh.entity.User;
import phattrienungdungj2ee.websitebanmatkinh.repository.UserRepository;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // HIỂN THỊ TRANG ĐĂNG NHẬP
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        var userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            User user = userOpt.get();

            // 1. Lưu thông tin user vào Session
            session.setAttribute("loggedInUser", user);

            // 2. Kiểm tra Role để điều hướng (Redirect)
            // Lưu ý: So sánh chuỗi nên dùng .equals() hoặc .equalsIgnoreCase() cho an toàn
            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                return "redirect:/admin"; // Chuyển đến trang dashboard của admin
            } else {
                return "redirect:/products"; // Khách hàng bình thường vào trang sản phẩm
            }
        }

        // Nếu sai tài khoản/mật khẩu
        model.addAttribute("error", "Tài khoản hoặc mật khẩu không chính xác!");
        return "login";
    }

    // HIỂN THỊ TRANG ĐĂNG KÝ
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // XỬ LÝ ĐĂNG KÝ
    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) {
        if (userRepository.existsByUsername(user.getUsername())) {
            model.addAttribute("error", "Tên đăng nhập đã tồn tại!");
            return "register";
        }
        user.setRole("CUSTOMER"); // Mặc định là khách hàng
        userRepository.save(user);
        return "redirect:/login?success";
    }

    // ĐĂNG XUẤT
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Hủy toàn bộ session
        return "redirect:/login";
    }
}