package com.ecommerce.cozashop.controller;

import com.ecommerce.cozashop.model.*;
import com.ecommerce.cozashop.service.*;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.query.JSqlParserUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductItemService productItemService;

    @Autowired
    private ProductSizeService productSizeService;

    @Autowired
    private SizeOptionService sizeOptionService;

    @Autowired
    private ImageProductService imageProductService;



    @GetMapping("/product")
    public String showProduct(Model model) {
        model.addAttribute("product_list", productService.getAllProduct());
        model.addAttribute("product_item_list", productItemService.getProductItems());
        return "product";
    }

    @GetMapping("/product-detail/{id}")
    public String showProductDetail(@PathVariable(name = "id") Long id, Model model) {
        ProductItem productItem = productItemService.getOneProduct(id);
       //Product product = productService.getProduct(id);
        List<ProductSize> productSizeList = productSizeService.getAllSize(productItem.getId());
        List<String> strings = new ArrayList<>();
        List<ImageProduct> imageProducts = imageProductService.getAllImageById(productItem.getId());

        for (ProductSize p: productSizeList) {
            strings.add(sizeOptionService.getSizeProduct(p.getSizeOption().getId()));
        }

        model.addAttribute("prodItem", productItem);
        //model.addAttribute("prod", product);
        model.addAttribute("product_list", productService.getAllProduct());
        model.addAttribute("product_item_list", productItemService.getProductItems());
        model.addAttribute("size_opt", strings);
        model.addAttribute("img_list", imageProducts);

        return "product-detail";
    }
}
