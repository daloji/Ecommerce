package com.ecommerce.cozashop.controller;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ecommerce.cozashop.config.LocalDateTypeAdapter;
import com.ecommerce.cozashop.model.CartItem;
import com.ecommerce.cozashop.model.ImageProduct;
import com.ecommerce.cozashop.model.LoginImage;
import com.ecommerce.cozashop.model.LoginImageForm;
import com.ecommerce.cozashop.model.Logo;
import com.ecommerce.cozashop.model.LogoForm;
import com.ecommerce.cozashop.model.ProductItem;
import com.ecommerce.cozashop.model.ProductSize;
import com.ecommerce.cozashop.model.User;
import com.ecommerce.cozashop.service.CartItemService;
import com.ecommerce.cozashop.service.CookieService;
import com.ecommerce.cozashop.service.ImageProductService;
import com.ecommerce.cozashop.service.LogoService;
import com.ecommerce.cozashop.service.ProductItemService;
import com.ecommerce.cozashop.service.ProductService;
import com.ecommerce.cozashop.service.ProductSizeService;
import com.ecommerce.cozashop.service.SizeOptionService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;

@Controller
public class ProductController {

	@Autowired
	private HttpSession session;
	
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

    @Autowired
	private LogoService logoService;
    
	@Autowired
	private CookieService cookieService;
	
	@Autowired
	private CartItemService cartItemService;

    @GetMapping("/product")
    public String showProduct(Model model) {
    	Logo logo = logoService.getLogo();
    	LogoForm logoform = new LogoForm();
    	logoform.setLogo(logo);
		model.addAttribute("logo", logoform);
        model.addAttribute("product_list", productService.getAllProduct());
        model.addAttribute("product_item_list", productItemService.getProductItems());
    	GsonBuilder builder = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter());
		final Gson gson = builder.create();
    	Authentication authentification = SecurityContextHolder.getContext().getAuthentication();
		if(nonNull(authentification) && !( authentification instanceof AnonymousAuthenticationToken)) {
			User user = (User) authentification.getPrincipal();
			List<CartItem> list = cartItemService.getAllProductCartWithUser(user.getId());
			session.removeAttribute("totalCart");
			session.setAttribute("totalCart", list.size()); 
			double price = 0;
			for(CartItem cartItem:list) {
				price = price + (cartItem.getQty() * cartItem.getItem().getPrice());	
			}
			model.addAttribute("totalPrice", price);
			model.addAttribute("listCartItemInfo", list);
		}else {
			List<CartItem> listCartItem = new ArrayList<CartItem>();
			Cookie cookie = cookieService.readCookie("listCart");
			if(nonNull(cookie)) {
				String listCart = cookie.getValue();
				CartItem[] array = gson.fromJson(listCart, CartItem[].class);
				listCartItem = Arrays.asList(array);
				if(nonNull(listCartItem)) {
					double price = 0;
					List<CartItem> listCartItemInfo = new ArrayList<CartItem>();
					for (CartItem cartItem:listCartItem) {
						ProductItem productItem = productItemService.getProductById(cartItem.getItem().getId());
						CartItem cart = new CartItem();
						cart.setItem(productItem);
						cart.setQty(cartItem.getQty());
						price = price + (cartItem.getQty() * productItem.getPrice());
						listCartItemInfo.add(cart);
					}
					model.addAttribute("totalPrice", price);
					model.addAttribute("listCartItemInfo", listCartItemInfo);
				}
				
				
			}
			session.removeAttribute("totalCart");
			session.setAttribute("totalCart", listCartItem.size());
		}
        return "product";
    }
    
    
    @GetMapping("/product/{search}")
    public String searchProduct(@PathVariable(name = "search") String search, Model model) {
      
        return "product";
    }
    

    @GetMapping("/product-detail/{id}")
    public String showProductDetail(@PathVariable(name = "id") Long id, Model model) {

    	Logo logo = logoService.getLogo();
		LogoForm logoForm = new LogoForm();
		logoForm.setLogo(logo);
		model.addAttribute("logo", logoForm);
    	ProductItem productItem = productItemService.getOneProduct(id);
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
