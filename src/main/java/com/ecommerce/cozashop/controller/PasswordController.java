package com.ecommerce.cozashop.controller;

import static java.util.Objects.nonNull;

import java.util.Locale;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.ecommerce.cozashop.model.Logo;
import com.ecommerce.cozashop.model.LogoForm;
import com.ecommerce.cozashop.model.PasswordResetToken;
import com.ecommerce.cozashop.model.User;
import com.ecommerce.cozashop.service.LogoService;
import com.ecommerce.cozashop.service.PasswordService;
import com.ecommerce.cozashop.service.PasswordTokenService;
import com.ecommerce.cozashop.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class PasswordController {
	
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private MessageSource messageSource;

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordTokenService passwordTokenService;
	
	@Autowired
	private PasswordService passwordService;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private LogoService logoService;
	
	@GetMapping("/reset/password/{token}")
	public String resetPassword(@PathVariable("token") String token,Model model) {
		PasswordResetToken passwordReset = passwordService.getUserToken(token);
		if(nonNull(passwordReset)) {
			LocalDateTime currentDateTime =  LocalDateTime.now();
		   if(currentDateTime.toDate().compareTo(passwordReset.getExpiryDate())>0) {
			   //log token expire
			   Locale locale = LocaleContextHolder.getLocale();
			   String info = messageSource.getMessage("label.expire-token",null, locale);
			   model.addAttribute("error",info); 
			   passwordService.deletePasswordToken(passwordReset);
			   return  "account/forgot-password";
		   }else {
				User user = new User();
				user.setId( passwordReset.getUser().getId());
				model.addAttribute("user", user);   
		   }
		}
		return  "account/reset-password";
	}	

	@GetMapping("/forgot-password")
	public String showForgotPassword() {

		return "account/forgot-password";
	}

	@PostMapping("/reset-password")
	public String resetPassword(@ModelAttribute User user, Model model) {
		String userName = user.getEmail();
		if(userService.checkEmailAlreadyExists(userName)){
			User userInfo = userService.getUserByEmail(userName);
			String token = passwordTokenService.updatePasswordToken(userInfo);
			passwordService.resetPassword(userName,token);
		}
		Locale locale = LocaleContextHolder.getLocale();
		String info = messageSource.getMessage("label.reset-password-info",null, locale);
		model.addAttribute("info",info);

		return "account/forgot-password";
	}
	
	
	@PostMapping("/reset/new-password")
	public String resetnewPassword(@ModelAttribute User userUpdate, Model model) {
		User user =userService.getUserById(userUpdate.getId());
		if(nonNull(user)) {
			userService.updatePassword(userUpdate.getPassword(), user.getEmail());
		}
		return "redirect:/";
	}
	

	@PostMapping("/update-password")
	public String updatePasswordUser(@ModelAttribute User userUpdate, Model model) {
		Logo logo = logoService.getLogo();
		LogoForm logoForm = new LogoForm();
		logoForm.setLogo(logo);
		model.addAttribute("logo", logoForm);
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
	
	
	@GetMapping("/update-password")
	public String updatePassword(Model model) {
		Logo logo = logoService.getLogo();
		LogoForm logoForm = new LogoForm();
		logoForm.setLogo(logo);
		model.addAttribute("logo", logoForm);
		Authentication authentification = SecurityContextHolder.getContext().getAuthentication();
		if(nonNull(authentification) && !( authentification instanceof AnonymousAuthenticationToken)) {
			return "account/update-password";
		}else {
			return "index";	
		}
	}
}
