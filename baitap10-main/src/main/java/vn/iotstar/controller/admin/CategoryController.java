package vn.iotstar.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import vn.iotstar.entity.Category;
import vn.iotstar.service.CategoryService;

import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;




@Controller
@RequestMapping("/admin/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("")
    public String list(ModelMap model) {
        List<Category> list = categoryService.findAll();
        model.addAttribute("categories", list);
        return "admin/categories/list";
    }

    @GetMapping("add")
    public String add(ModelMap model) {
        model.addAttribute("category", new Category());
        return "admin/categories/add";
    }

    @PostMapping("saveOrUpdate")
    public ModelAndView saveOrUpdate(ModelMap model, @ModelAttribute("category") Category category) {
        categoryService.save(category);
        model.addAttribute("message", "Category saved successfully");
        return new ModelAndView("redirect:/index");
        
    }
    
    @GetMapping("edit/{categoryId}")
    public ModelAndView edit(ModelMap model, @PathVariable("categoryId") Long categoryId) {
        Category category = categoryService.findById(categoryId).orElse(null);
        model.addAttribute("category", category);
        return new ModelAndView("admin/categories/edit", model);
    }
    
    @GetMapping("delete/{categoryId}")
    public ModelAndView delete(ModelMap model, @PathVariable("categoryId") Long categoryId) {
        categoryService.deleteById(categoryId);
        model.addAttribute("message", "Category is deleted!");
        return new ModelAndView("redirect:/index", model);
    }

    @RequestMapping("search")
    public String search(ModelMap model, @RequestParam(name = "name", required = false) String name) {
        List<Category> categories;
        if (StringUtils.hasText(name)){
            categories = categoryService.search(name);
        }
        else {
            categories = categoryService.findAll();
        }
        model.addAttribute("categories", categories);
        return "admin/categories/list";
    }
    
    
    
}