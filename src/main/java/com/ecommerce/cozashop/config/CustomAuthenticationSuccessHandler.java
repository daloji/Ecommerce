package com.ecommerce.cozashop.config;

import static java.util.Objects.nonNull;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.ecommerce.cozashop.model.CartItem;
import com.ecommerce.cozashop.model.User;
import com.ecommerce.cozashop.service.CartItemService;
import com.ecommerce.cozashop.service.CookieService;
import com.ecommerce.cozashop.service.ProductService;
import com.ecommerce.cozashop.service.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


	@Autowired
	private ProductService productService;

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private CookieService cookieService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		HttpSession session = request.getSession();
		User userSpringSecu = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<CartItem> list = new ArrayList<CartItem>();
		List<CartItem>  listCartItem = cartItemService.getAllProductCartWithUser(userService.getUserByEmail(userSpringSecu.getUsername()).getId());
		Cookie cookie = cookieService.readCookie("listCart");
		if(nonNull(cookie)) {
			GsonBuilder builder = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter());
			final Gson gson = builder.create();
			String listCart = cookie.getValue();
			CartItem[] array = gson.fromJson(listCart, CartItem[].class);
			List<CartItem> listCookie  = Arrays.asList(array);
			for(CartItem cart:listCookie) {
				cart.setUser(userSpringSecu);
				cartItemService.addToCart(cart);
			}
			cookieService.deleteCookie("listCart");
			list.addAll(listCookie);
			list.addAll(listCartItem);
			session.removeAttribute("totalCart");
			session.setAttribute("totalCart", list.size()); 
		}
		
		session.setAttribute("totalCart", list.size());
		session.setAttribute("product_list", productService.getAllProduct());

		response.sendRedirect("/" );
	}

}
