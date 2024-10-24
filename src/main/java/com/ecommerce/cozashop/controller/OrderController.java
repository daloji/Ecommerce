package com.ecommerce.cozashop.controller;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ecommerce.cozashop.model.Address;
import com.ecommerce.cozashop.model.CartItem;
import com.ecommerce.cozashop.model.DeliveryStatus;
import com.ecommerce.cozashop.model.Logo;
import com.ecommerce.cozashop.model.LogoForm;
import com.ecommerce.cozashop.model.OrderLine;
import com.ecommerce.cozashop.model.PaymentStatus;
import com.ecommerce.cozashop.model.ProductItem;
import com.ecommerce.cozashop.model.ShopOrder;
import com.ecommerce.cozashop.model.User;
import com.ecommerce.cozashop.service.CartItemService;
import com.ecommerce.cozashop.service.LogoService;
import com.ecommerce.cozashop.service.OrderLineService;
import com.ecommerce.cozashop.service.PdfGeneratorService;
import com.ecommerce.cozashop.service.ProductItemService;
import com.ecommerce.cozashop.service.ProductService;
import com.ecommerce.cozashop.service.ShopOrderService;
import com.ecommerce.cozashop.service.StripService;
import com.ecommerce.cozashop.service.UserService;

import jakarta.servlet.http.HttpSession;

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

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private PdfGeneratorService pdfGenaratorService;

	@Autowired
	private LogoService logoService;


	@GetMapping("/check-out")
	public String show(Model model) {
		Logo logo = logoService.getLogo();
		LogoForm logoform = new LogoForm();
		logoform.setLogo(logo);
		model.addAttribute("logo", logoform);
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
			Address adresse = user.getAddress();
			if(isNull(adresse)){
				adresse = new Address();	
			}
			model.addAttribute("address",adresse);	
		}


		return "check-out";
	}

	@PostMapping("/check-out")
	public ModelAndView checkOut(@ModelAttribute CartItem item, @ModelAttribute Address fullAddress, Model model) {
		Map<ProductItem,Integer> mapProductQty = new HashMap<>();
		Authentication authentification = SecurityContextHolder.getContext().getAuthentication();
		Map<ProductItem,String> mapError = new HashMap<>();
		if(nonNull(authentification) && !( authentification instanceof AnonymousAuthenticationToken)) {
			User user = (User) authentification.getPrincipal();
			List<CartItem> cartList = cartItemService.getAllProductCartWithUser(user.getId());
			Locale locale = LocaleContextHolder.getLocale();
			//check quantity
			for(CartItem cartItem:cartList) {
				ProductItem productItem = cartItem.getItem();
				int productSell = 0;
				if(nonNull(productItem.getProduct_sell())) {
					productSell = productItem.getProduct_sell();
				}
				int productAvalaible = productItem.getQty_in_stock() - productSell;
				if(productAvalaible<cartItem.getQty()) {
					mapError.put(productItem, messageSource.getMessage("label.not_enough",null, locale));
				}
				mapProductQty.put(productItem, cartItem.getQty());
			}

			if(mapError.isEmpty()) {
				ShopOrder shopOrder = new ShopOrder();
				shopOrder.setOrder_date(LocalDate.now());
				shopOrder.setOrder_total(cartItemService.getTotal(cartList));
				shopOrder.setUser(user);
				shopOrder.setStatus(PaymentStatus.PENDING);
				shopOrderService.createNewOrder(shopOrder);

				pdfGenaratorService.generateInvoice(mapProductQty,user,fullAddress);
				//TODO activate Strip
				/*
				Session sessionService = stripService.stripePayment(cartList);
				if(sessionService != null) {
					return new ModelAndView("redirect:"+sessionService.getUrl());
				}*/
				for (CartItem c : cartList) {
					OrderLine orderLine = new OrderLine();

					orderLine.setShopOrder(shopOrder);
					orderLine.setProductItem(c.getItem());
					orderLine.setQty(c.getQty());
					orderLine.setPrice(c.getQty() * c.getItem().getPrice());
					orderLine.setStatus(DeliveryStatus.PROCESSING);
					orderLine.setOrder_date(LocalDate.now());
					// Add cart item -> order line
					orderLineService.createOrderLine(orderLine);

					ProductItem productItem = c.getItem();
					productItem.setProduct_sell(c.getQty());
					productItemService.save(productItem);

					// Remove cart item
					cartItemService.removeCartItem(c);
					System.out.println("Success");
				}
				shopOrder.setStatus(PaymentStatus.ACCEPT);
				shopOrderService.save(shopOrder);
				cartList = cartItemService.getAllProductCartWithUser(user.getId());
				double total = cartItemService.getTotal(cartList);
				session.removeAttribute("totalCart");
				session.setAttribute("totalCart", cartList.size());
				model.addAttribute("cart_item", cartList);
				model.addAttribute("total", total);
				model.addAttribute("countCart", cartList.size());
				return new ModelAndView("redirect:/shopping-cart");
			}else {
				String error = "";
				for (Map.Entry<ProductItem, String> entry : mapError.entrySet()) {
					error = error + "\""+entry.getKey().getProduct_id().getProduct_name()+"\"" + "    "+ entry.getValue() + "\n";
				}
				model.addAttribute("error", error);

				user.setId(userService.getIdUserByEmail(user.getEmail()));
				cartList = cartItemService.getAllProductCartWithUser(user.getId());
				double total = cartItemService.getTotal(cartList);
				model.addAttribute("cart_item", cartList);
				model.addAttribute("total", total);
				model.addAttribute("countCart", cartList.size());
				return new ModelAndView("shopping-cart");
			}




		}else {
			return null;
		}
	}
}
