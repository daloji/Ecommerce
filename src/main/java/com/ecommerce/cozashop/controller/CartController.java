package com.ecommerce.cozashop.controller;

import static java.util.Objects.nonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ecommerce.cozashop.config.LocalDateTypeAdapter;
import com.ecommerce.cozashop.model.CartItem;
import com.ecommerce.cozashop.model.Logo;
import com.ecommerce.cozashop.model.LogoForm;
import com.ecommerce.cozashop.model.ProductItem;
import com.ecommerce.cozashop.model.User;
import com.ecommerce.cozashop.service.CartItemService;
import com.ecommerce.cozashop.service.CookieService;
import com.ecommerce.cozashop.service.LogoService;
import com.ecommerce.cozashop.service.ProductItemService;
import com.ecommerce.cozashop.service.ProductService;
import com.ecommerce.cozashop.service.StripService;
import com.ecommerce.cozashop.service.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductItemService productItemService;

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private HttpSession session;

	@Autowired
	private UserService userService;

	@Autowired
	private StripService stripService;

	@Autowired
	private LogoService logoService;

	@Autowired
	private CookieService cookieService;

	@GetMapping("/shopping-cart")
	public ModelAndView  show(Model model) {
		Logo logo = logoService.getLogo();
		LogoForm logoForm = new LogoForm();
		logoForm.setLogo(logo);
		model.addAttribute("logo", logoForm);
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(nonNull(user)) {
			user.setId(userService.getIdUserByEmail(user.getEmail()));
			List<CartItem> cartList = cartItemService.getAllProductCartWithUser(user.getId());
			double total = cartItemService.getTotal(cartList);
			model.addAttribute("cart_item", cartList);
			model.addAttribute("total", total);
			model.addAttribute("countCart", cartList.size());	
			if (cartList.isEmpty()) {
				return new ModelAndView("shopping-cart-empty");
			}
		}else {
			return new ModelAndView("shopping-cart-not-connected");
		}

		return new ModelAndView("shopping-cart"); 
	}


	@GetMapping("/add-to-cart/{id}/{qty}/{size}")
	@ResponseBody
	public Integer addToCart(@PathVariable(name = "id") Long id,
			@PathVariable(name = "qty") Integer qty,@PathVariable(name = "size",required = false) String size) {

		//check if user is authenticated
		Authentication authetication = SecurityContextHolder.getContext().getAuthentication();
		if(nonNull(authetication) && !( authetication instanceof AnonymousAuthenticationToken)) {
			User user = (User) authetication.getPrincipal();
			List<CartItem> list = cartItemService.getAllProductCartWithUser(user.getId());
			if (cartItemService.checkProductAlreadyExists(id)) {
				CartItem item = cartItemService.getOneCartByProduct(id);
				item.setQty(item.getQty() + qty);
				cartItemService.addToCart(item);
				return list.size();
			}
			ProductItem product = new ProductItem();
			CartItem cartItem = new CartItem();
			product.setId(id);
			cartItem.setQty(qty);
			cartItem.setItem(product);
			cartItem.setUser(user);
			cartItemService.addToCart(cartItem);
			list = cartItemService.getAllProductCartWithUser(user.getId());
			session.removeAttribute("totalCart");
			session.setAttribute("totalCart", list.size());
			return list.size();

		}else {
			ProductItem product = new ProductItem();
			CartItem cartItem = new CartItem();
			product.setId(id);
			cartItem.setQty(qty);
			cartItem.setItem(product);
			Cookie cookie = cookieService.readCookie("cartItem");
			if(nonNull(cookie)) {

			}
			/*
			Cookie cookie = cookieService.readCookie("JSESSIONID");
			String sessionId = cookie.getValue();

			session.removeAttribute("totalCart");
			session.setAttribute("totalCart", 1);
			 */
			return 1;	

		}

	}

	@GetMapping("/add-to-cart/{id}/{qty}")
	@ResponseBody
	public Integer addToCartnoSize(@PathVariable(name = "id") Long id,
			@PathVariable(name = "qty") Integer qty) {
		Authentication authetication = SecurityContextHolder.getContext().getAuthentication();
		if(nonNull(authetication) && !( authetication instanceof AnonymousAuthenticationToken)) {
			User user = (User) authetication.getPrincipal();
			List<CartItem> list = cartItemService.getAllProductCartWithUser(user.getId());
			if (cartItemService.checkProductAlreadyExists(id)) {
				CartItem item = cartItemService.getOneCartByProduct(id);
				item.setQty(item.getQty() + qty);
				cartItemService.addToCart(item);
				return list.size();
			}
			ProductItem product = new ProductItem();
			CartItem cartItem = new CartItem();
			product.setId(id);
			cartItem.setQty(qty);
			cartItem.setItem(product);
			cartItem.setUser(user);
			cartItemService.addToCart(cartItem);
			list = cartItemService.getAllProductCartWithUser(user.getId());
			session.removeAttribute("totalCart");
			session.setAttribute("totalCart", list.size());
			return list.size();

		}else {
			 GsonBuilder builder = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter());
			//new GsonBuilder().setPrettyPrinting()
            //.excludeFieldsWithoutExposeAnnotation()
            //.create();
			final Gson gson = builder.create();
			List<CartItem> listCartItem = new ArrayList<CartItem>();
			ProductItem product = new ProductItem();
			CartItem cartItem = new CartItem();
			product.setId(id);
			cartItem.setQty(qty);
			cartItem.setItem(product);
			Cookie cookie = cookieService.readCookie("listCart");
			if(nonNull(cookie)) {
				String listCart = cookie.getValue();
				CartItem[] array = gson.fromJson(listCart, CartItem[].class);
				List<CartItem> listOldCartItem = Arrays.asList(array);
				listCartItem.addAll(listOldCartItem);
				listCartItem.add(cartItem);
			}else {
				listCartItem.add(cartItem);
			}
			String data = gson.toJson(listCartItem);
			cookieService.deleteCookie("listCart");
			cookieService.create("listCart", data,7);

			session.removeAttribute("totalCart");
			session.setAttribute("totalCart", listCartItem.size());
			return listCartItem.size();	

		}
	}

	@RequestMapping( value = "/remove-cart-item/{cid}", produces = "application/json")
	@ResponseBody
	public String removeCartItem(@PathVariable(name = "cid") Integer id) {

		CartItem item = cartItemService.getOneCartByIdCart(id);
		cartItemService.removeCartItem(item);

		JSONObject data = new JSONObject();
		Authentication authentification = SecurityContextHolder.getContext().getAuthentication();
		if(nonNull(authentification) && !( authentification instanceof AnonymousAuthenticationToken)) {
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<CartItem> list = cartItemService.getAllProductCartWithUser(user.getId());
			data.put("size", list.size());
			session.removeAttribute("totalCart");
			session.setAttribute("totalCart", list.size());
		}
		return data.toString();
	}


	@RequestMapping(value = "/update-qty/{cid}/{qty}", produces = "application/json")
	@ResponseBody
	public String changeQty(@PathVariable(name = "cid") Integer id,
			@PathVariable(name = "qty") Integer qty) {
		CartItem cartItem = cartItemService.getOneCartByIdCart(id);

		cartItem.setQty(qty);
		cartItemService.addToCart(cartItem);

		User user = (User) session.getAttribute("user");

		user.setId(userService.getIdUserByEmail(user.getEmail()));

		List<CartItem> list = cartItemService.getAllProductCartWithUser(user.getId());
		double subTotal = cartItemService.getTotal(list);

		JSONObject info = new JSONObject();
		info.put("subTotal", subTotal);
		info.put("sumItem", qty * cartItem.getItem().getPrice());

		return info.toString();
	}

}
