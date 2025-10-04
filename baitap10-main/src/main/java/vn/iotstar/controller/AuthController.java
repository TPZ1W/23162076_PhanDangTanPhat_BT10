package vn.iotstar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;
import vn.iotstar.entity.User;
import vn.iotstar.service.UserService;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping({"/", "/index", "/login"})
    public String showIndex(Model model) {
        model.addAttribute("user", new User());
        return "index";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") User formUser,
                        HttpSession session,
                        Model model) {
        User user = userService.findByUsernameAndPassword(formUser.getUsername(), formUser.getPassword());
        if (user != null) {
            session.setAttribute("user", user);
            if ("admin".equals(user.getRole())) {
                return "redirect:/admin/home";
            } else {
                return "redirect:/user/home";
            }
        }
        model.addAttribute("errorLogin", "Tên đăng nhập hoặc mật khẩu không đúng");
        model.addAttribute("user", new User());
        return "index";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") User formUser, Model model) {
        if (userService.findByUsername(formUser.getUsername()) != null) {
            model.addAttribute("errorRegister", "Tên đăng nhập đã tồn tại");
            model.addAttribute("user", new User());
            return "index";
        }
        formUser.setRole("user");
        userService.save(formUser);
        model.addAttribute("successRegister", "Đăng ký thành công, vui lòng đăng nhập");
        model.addAttribute("user", new User());
        return "index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/index";
    }
}
