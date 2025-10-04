package vn.iotstar.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import vn.iotstar.entity.User;
import vn.iotstar.service.UserService;

@Controller
@RequestMapping("/admin/users")
public class UserController {

    @Autowired
    private UserService userService;
    
    @GetMapping({"", "/"})
    public String list(ModelMap model){
        List<User> list = userService.findAll();
        model.addAttribute("users", list);
        return "admin/users/list"; 
    }
    
    @GetMapping("/add")
    public String add(ModelMap model){
        model.addAttribute("user", new User());
        return "admin/users/add";
    }

    @PostMapping("/saveOrUpdate")
    public ModelAndView saveOrUpdate(ModelMap model, @ModelAttribute("user") User user) {
        userService.save(user);
        model.addAttribute("message", "User saved successfully");
        return new ModelAndView("redirect:/admin/users");
    }

    @GetMapping("/edit/{userId}")
    public ModelAndView edit(ModelMap model, @PathVariable("userId") Long userId) {
        User user = userService.findById(userId);
        if(user == null){
            model.addAttribute("error", "User not found");
            return new ModelAndView("redirect:/admin/users");
        }
        model.addAttribute("user", user);
        return new ModelAndView("admin/users/edit", model);
    }

    @GetMapping("/delete/{userId}")
    public ModelAndView delete(ModelMap model, @PathVariable("userId") Long userId) {
        User user = userService.findById(userId);
        if(user != null){
            userService.deleteById(userId);
            model.addAttribute("message", "User deleted successfully");
        } else {
            model.addAttribute("error", "User not found");
        }
        return new ModelAndView("redirect:/admin/users", model);
    }

    @RequestMapping("/search")
    public String search(ModelMap model, @RequestParam(name = "name", required = false) String name) {
        List<User> users;
        if (StringUtils.hasText(name)){
            users = userService.search(name);
        } else {
            users = userService.findAll();
        }
        model.addAttribute("users", users);
        return "admin/users/list";
    }
}
