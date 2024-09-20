package com.ecommerce.cozashop.controller;

import static java.util.Objects.nonNull;
import static java.util.Objects.isNull;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecommerce.cozashop.model.Banner;
import com.ecommerce.cozashop.model.Block;
import com.ecommerce.cozashop.model.CartItem;
import com.ecommerce.cozashop.model.Logo;
import com.ecommerce.cozashop.model.LogoForm;
import com.ecommerce.cozashop.model.Product;
import com.ecommerce.cozashop.model.Slider;
import com.ecommerce.cozashop.model.User;
import com.ecommerce.cozashop.service.BannerService;
import com.ecommerce.cozashop.service.BlockService;
import com.ecommerce.cozashop.service.CartItemService;
import com.ecommerce.cozashop.service.LogoService;
import com.ecommerce.cozashop.service.ProductItemService;
import com.ecommerce.cozashop.service.ProductService;
import com.ecommerce.cozashop.service.SliderService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	@Autowired
	private HttpSession session;

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductItemService productItemService;

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private BannerService bannerService;

	@Autowired
	private LogoService logoService;

	@Autowired
	private SliderService sliderService;

	@Autowired
	private BlockService blockService;

	@GetMapping("/home")
	public String showHome(@RequestParam(required = false) String search,Model model) {
		//TODO shop Cart

		Logo logo = logoService.getLogo();
		LogoForm logoForm = new LogoForm();
		logoForm.setLogo(logo);
		model.addAttribute("logo", logoForm);
		Slider slider = sliderService.findAllSlider();
		model.addAttribute("listSlider", slider);
		List<Block> listBlock = blockService.findAllBlock();
		model.addAttribute("listBlock", listBlock);
		if(isNull(search)) {
			List<Banner> listBanner = bannerService.findAllBanner();
			model.addAttribute("product_list", productItemService.getProductAvailable());
			model.addAttribute("product_item_list", productItemService.getProductItemsAvalaible());
			model.addAttribute("listBanner", listBanner);
			return "index";
		}else {
			List<Product> listProduct = productService.getAllProductByName(search);
			model.addAttribute("product_list", listProduct);
			model.addAttribute("product_item_list", productItemService.getProductItemsAvalaible());
			return "product";
		}

	}



	@GetMapping("/about")
	public String showAbout() {
		return "about";
	}


	@GetMapping("/index")
	public String showIndex(Model model) {
		Logo logo = logoService.getLogo();
		LogoForm logoForm = new LogoForm();
		logoForm.setLogo(logo);
		model.addAttribute("logo", logoForm);
		Slider slider = sliderService.findAllSlider();
		model.addAttribute("listSlider", slider);
		List<Block> listBlock = blockService.findAllBlock();
		model.addAttribute("listBlock", listBlock);
		List<Banner> listBanner = bannerService.findAllBanner();
		model.addAttribute("product_list", productItemService.getProductAvailable());
		model.addAttribute("product_item_list", productItemService.getProductItems());
		model.addAttribute("listBanner", listBanner);
		Authentication authentification = SecurityContextHolder.getContext().getAuthentication();
		if(nonNull(authentification) && !( authentification instanceof AnonymousAuthenticationToken)) {
			User user = (User) authentification.getPrincipal();
			List<CartItem> list = cartItemService.getAllProductCartWithUser(user.getId());
			session.removeAttribute("totalCart");
			session.setAttribute("totalCart", list.size()); 
		}else {
			//get total cart from Redis for anonymous
			session.removeAttribute("totalCart");
			session.setAttribute("totalCart", 0);
		}		
		return "index";


	}
	@GetMapping("/")
	public String afterConnection(@RequestParam(required = false) String search,Model model) {

		Logo logo = logoService.getLogo();
		LogoForm logoForm = new LogoForm();
		logoForm.setLogo(logo);
		model.addAttribute("logo", logoForm);
		Slider slider = sliderService.findAllSlider();
		model.addAttribute("listSlider", slider);
		List<Block> listBlock = blockService.findAllBlock();
		model.addAttribute("listBlock", listBlock);
		
		if(isNull(search)) {
			List<Banner> listBanner = bannerService.findAllBanner();
			model.addAttribute("product_list", productItemService.getProductAvailable());
			model.addAttribute("product_item_list", productItemService.getProductItems());
			model.addAttribute("listBanner", listBanner);
			Authentication authentification = SecurityContextHolder.getContext().getAuthentication();
			if(nonNull(authentification) && !( authentification instanceof AnonymousAuthenticationToken)) {
				User user = (User) authentification.getPrincipal();
				List<CartItem> list = cartItemService.getAllProductCartWithUser(user.getId());
				session.removeAttribute("totalCart");
				session.setAttribute("totalCart", list.size()); 
			}else {
				//get total cart from Redis for anonymous
				session.removeAttribute("totalCart");
				session.setAttribute("totalCart", 0);
			}		
			return "index";
		}else {

			List<Product> listProduct = productService.getAllProductByName(search);
			model.addAttribute("product_list", listProduct);
			model.addAttribute("product_item_list", productItemService.getProductItemsAvalaible());
			return "product";
		}

	}


	@GetMapping("/contact")
	public String showContact() {
		return "contact";
	}
}
