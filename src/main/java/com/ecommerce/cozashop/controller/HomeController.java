package com.ecommerce.cozashop.controller;


import com.ecommerce.cozashop.model.*;
import com.ecommerce.cozashop.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {


    @Autowired
    HttpSession session;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductItemService productItemService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private UserService userService;

    @GetMapping("/home")
    public String showHome(Model model) {
        model.addAttribute("product_list", productService.getAllProduct());
        model.addAttribute("product_item_list", productItemService.getProductItems());
        return "index";
    }

    @GetMapping("/about")
    public String showAbout() {
        return "about";
    }

    @GetMapping("/contact")
    public String showContact() {return "contact";}
}
