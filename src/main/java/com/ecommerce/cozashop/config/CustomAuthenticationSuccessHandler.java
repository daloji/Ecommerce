package com.ecommerce.cozashop.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.ecommerce.cozashop.model.User;
import com.ecommerce.cozashop.service.CartItemService;
import com.ecommerce.cozashop.service.ProductService;
import com.ecommerce.cozashop.service.UserService;

import jakarta.servlet.ServletException;
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
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		HttpSession session = request.getSession();
		User userSpringSecu = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
   	   int totalCart = cartItemService.getAllProductCartWithUser(userService.getUserByEmail(userSpringSecu.getUsername()).getId()).size();

       session.setAttribute("totalCart", totalCart);
       session.setAttribute("product_list", productService.getAllProduct());

       
       
       response.sendRedirect("/" );
	}

}
