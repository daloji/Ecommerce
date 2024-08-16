package com.ecommerce.cozashop.controller;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecommerce.cozashop.model.Address;
import com.ecommerce.cozashop.model.CartItem;
import com.ecommerce.cozashop.model.UpdateUser;
import com.ecommerce.cozashop.model.User;
import com.ecommerce.cozashop.service.CartItemService;
import com.ecommerce.cozashop.service.CookieService;
import com.ecommerce.cozashop.service.EmailService;
import com.ecommerce.cozashop.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {


	@Autowired
	private UserService userService;

	@Autowired
	private HttpSession session;

	@Autowired
	private CookieService cookieService;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	EmailService  emailService;

	@Autowired
	private BCryptPasswordEncoder encoder;



	@GetMapping("/login")
	public String showLogin(Model model) {

		model.addAttribute("user", new User());
		Cookie cookieEmail = cookieService.readCookie("email");
		Cookie cookiePasswdId = cookieService.readCookie("password");

		if (cookieEmail != null) {
			String email = cookieEmail.getValue();
			String passwd = cookiePasswdId.getValue();

			model.addAttribute("email", email);
			model.addAttribute("password", passwd);
		}

		return "account/login";
	}


	@PostMapping("/login")
	public String loginAccount(@ModelAttribute User user,
			Model model,
			@RequestParam(name = "remember-me", required = false) String remember) {
		if (userService.checkEmailAlreadyExists(user.getEmail())) {
			model.addAttribute("error", "Wrong email address, please re-enter your email!");
			return "account/login";
		}
		int totalCart = cartItemService.getAllProductCartWithUser(userService.getUserByEmail(user.getEmail()).getId()).size();
		session.setAttribute("totalCart", totalCart);

		return "account/login";
	}

	@GetMapping("/logout")
	public String logoutAccount (){
		session.removeAttribute("user");
		return "redirect:/index";
	}

	@GetMapping("/register")
	public String showRegister() {
		return "account/register";
	}

	@PostMapping("/register-new")
	public String registerAccount(@ModelAttribute User user,
			Model model) {

		try {
			if (userService.checkEmailAlreadyExists(user.getEmail())) {
				model.addAttribute("error", "Email address already exists");
				return "account/register";
			} else if (!userService.checkPhoneAlreadyExists(user.getPhone())) {
				model.addAttribute("error", "Phone number address already exists");
				return "account/register";
			} else {
				userService.registerAccount(user);
				return "redirect:account/login";
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("error", "Error 500");
		}
		return "acount/register";
	}


	@GetMapping("/forgot-password")
	public String showForgotPassword() {

		return "account/forgot-password";
	}


	@PostMapping("/reset-password")
	public String resetPassword(@ModelAttribute User user) {
		String userName = user.getEmail();
		if(userService.checkEmailAlreadyExists(userName)){
			emailService.sendSimpleMessage(userName, "reset PAssword"," initi");
		}
		return "account/forgot-password";
	}

	
	@GetMapping("/my-account")
	public String showAccount(Model model) {
		Authentication authentification = SecurityContextHolder.getContext().getAuthentication();
		if(nonNull(authentification) && !( authentification instanceof AnonymousAuthenticationToken)) {
			User user = (User) authentification.getPrincipal();
			List<CartItem> list = cartItemService.getAllProductCartWithUser(user.getId()); 
			model.addAttribute("mail", user.getEmail());
			model.addAttribute("firstName", user.getFirst_name());
			model.addAttribute("lastname", user.getLast_name());
		}

		return "account/my-account";
	}


	@GetMapping("/update-password")
	public String updatePassword(Model model) {
		Authentication authentification = SecurityContextHolder.getContext().getAuthentication();
		if(nonNull(authentification) && !( authentification instanceof AnonymousAuthenticationToken)) {
			return "account/update-password";
		}else {
			return "index";	
		}
	}


	@PostMapping("/update-password")
	public String updatePasswordUser(@ModelAttribute User userUpdate, Model model) {
		Authentication authentification = SecurityContextHolder.getContext().getAuthentication();
		if(nonNull(authentification) && !( authentification instanceof AnonymousAuthenticationToken)) {
			User user = (User) authentification.getPrincipal();
			if(encoder.matches(userUpdate.getOldpassword(), user.getPassword())) {
				userService.updatePassword(userUpdate.getPassword(), user.getEmail());
				HttpSession session = request.getSession();
				session.invalidate();
				SecurityContextHolder.clearContext();
				return "redirect:/";
			}else {
				model.addAttribute("error","old password not valid");
				return "account/update-password";
			}
		}
		return "redirect:/";
	}

	@PostMapping("/update-user")
	public String postUser(@ModelAttribute UpdateUser updateuser) {
		Authentication authentification = SecurityContextHolder.getContext().getAuthentication();
		if(nonNull(authentification) && !( authentification instanceof AnonymousAuthenticationToken)) {
			User user = (User) authentification.getPrincipal();
			if(nonNull(updateuser.getPhone())) {
				user.setPhone(updateuser.getPhone());
			}
			if(nonNull(updateuser.getAdresse())) {
				Address adresse = user.getAddress();
				if(isNull(adresse)) {
					adresse = new Address();
				}
				if(nonNull(updateuser.getAdresse().getRoad())) {
					adresse.setRoad(updateuser.getAdresse().getRoad());
				}
				if(nonNull(updateuser.getAdresse().getCity())) {
					adresse.setCity(updateuser.getAdresse().getCity());
				}
				if(nonNull(updateuser.getAdresse().getCountry())) {
					adresse.setCountry(updateuser.getAdresse().getCountry());
				}
				if(nonNull(updateuser.getAdresse().getDistrict())) {
					adresse.setDistrict(updateuser.getAdresse().getDistrict());
				}
				user.setAddress(adresse);
			}

			userService.updateAccount(user);
		}
		return "redirect:/";
	}

}
