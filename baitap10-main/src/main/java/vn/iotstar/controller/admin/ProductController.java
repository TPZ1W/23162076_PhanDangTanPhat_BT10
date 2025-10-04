package vn.iotstar.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import vn.iotstar.entity.Product;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.ProductService;

@Controller
@RequestMapping("admin/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

    @GetMapping("")
    public String list(ModelMap model){
        List<Product> list = productService.findAll();
        model.addAttribute("products", list);
        return "admin/products/list";
    }

    @GetMapping("add")
    public String add(ModelMap model){
        model.addAttribute("product", new Product());
        return "admin/products/add";
    }
    
    @PostMapping("saveOrUpdate")
    public ModelAndView saveOrUpdate(ModelMap model, @ModelAttribute("product") Product product) {
        productService.save(product);
        model.addAttribute("message", "Product saved successfully");
        return new ModelAndView("redirect:/admin/products");
    }

    @GetMapping("edit/{productId}")
    public ModelAndView edit(ModelMap model, @PathVariable("productId") Long productId) {
        Product product = productService.findById(productId);
        model.addAttribute("product", product);
        return new ModelAndView("admin/products/edit", model);
    }

    @GetMapping("delete/{productId}")
    public ModelAndView delete(ModelMap model, @PathVariable("productId") Long productId) {
        productService.deleteById(productId);
        model.addAttribute("message", "Product deleted successfully");
        return new ModelAndView("redirect:/admin/products", model);
    }

    @RequestMapping("search")
    public String search(ModelMap model, @RequestParam(name = "name", required = false) String name) {
        List<Product> products;
        if (StringUtils.hasText(name)){
            products = productService.search(name);
        }
        else {
            products = productService.findAll();
        }
        model.addAttribute("products", products);
        return "admin/products/list";
    }
}
