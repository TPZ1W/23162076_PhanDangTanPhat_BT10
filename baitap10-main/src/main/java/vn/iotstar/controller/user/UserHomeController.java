package vn.iotstar.controller.user;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.iotstar.entity.User;

@Controller
public class UserHomeController {

    @GetMapping("/user/home")
    public String home(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "user/home";
    }

    @GetMapping("/user/profile")
    public String profile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "user/profile";
    }
    
    @GetMapping("/user/categories")
    public String categories() {
        return "user/categories";
    }
    
    @GetMapping("/user/products")
    public String products() {
        return "user/products";
    }
}
