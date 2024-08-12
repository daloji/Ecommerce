package com.ecommerce.cozashop.controller;

import com.ecommerce.cozashop.model.*;
import com.ecommerce.cozashop.service.*;
import com.stripe.model.checkout.Session;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import static java.util.Objects.nonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductItemService productItemService;

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private ShopOrderService shopOrderService;

	@Autowired
	private OrderLineService orderLineService;

	@Autowired
	private HttpSession session;

	@Autowired
	private UserService userService;

	@Autowired
	private StripService stripService;

	@GetMapping("/check-out")
	public String show(Model model) {

		Authentication authentification = SecurityContextHolder.getContext().getAuthentication();
		if(nonNull(authentification) && !( authentification instanceof AnonymousAuthenticationToken)) {
			User user = (User) authentification.getPrincipal();

			List<CartItem> cartList = cartItemService.getAllProductCartWithUser(user.getId());

			double total = cartItemService.getTotal(cartList) + 15.00;
			double subTotal = cartItemService.getTotal(cartList);

			model.addAttribute("cart_item", cartList);
			model.addAttribute("total", total);
			model.addAttribute("subTotal", subTotal);
			model.addAttribute("countCart", cartList.size());

		}


		return "check-out";
	}

	@PostMapping("/check-out")
	public ModelAndView checkOut(@ModelAttribute CartItem item, @ModelAttribute Address fullAddress) {


		Authentication authentification = SecurityContextHolder.getContext().getAuthentication();
		if(nonNull(authentification) && !( authentification instanceof AnonymousAuthenticationToken)) {
			User user = (User) authentification.getPrincipal();
			List<CartItem> cartList = cartItemService.getAllProductCartWithUser(user.getId());
			ShopOrder shopOrder = new ShopOrder(user, LocalDate.now(), fullAddress.toString(), cartItemService.getTotal(cartList), true);
			shopOrderService.createNewOrder(shopOrder);

			Session sessionService = stripService.stripePayment(cartList);
			if(sessionService != null) {
				return new ModelAndView("redirect:"+sessionService.getUrl());
			}
			for (CartItem c : cartList) {
				OrderLine orderLine = new OrderLine();

				orderLine.setShopOrder(shopOrder);
				orderLine.setProductItem(c.getItem());
				orderLine.setQty(c.getQty());
				orderLine.setPrice(c.getQty() * c.getItem().getPrice());

				// Add cart item -> order line
				orderLineService.createOrderLine(orderLine);

				// Remove cart item
				cartItemService.removeCartItem(c);
				System.out.println("Success");
			}

			return new ModelAndView("redirect:/shopping-cart");
		}else {
			return null;
		}
	}
}
