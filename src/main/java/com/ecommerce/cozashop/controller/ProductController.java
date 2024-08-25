package com.ecommerce.cozashop.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ecommerce.cozashop.model.ImageProduct;
import com.ecommerce.cozashop.model.ProductItem;
import com.ecommerce.cozashop.model.ProductSize;
import com.ecommerce.cozashop.service.ImageProductService;
import com.ecommerce.cozashop.service.ProductItemService;
import com.ecommerce.cozashop.service.ProductService;
import com.ecommerce.cozashop.service.ProductSizeService;
import com.ecommerce.cozashop.service.SizeOptionService;

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
    
    
    @GetMapping("/product/{search}")
    public String searchProduct(@PathVariable(name = "search") String search, Model model) {
      
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
