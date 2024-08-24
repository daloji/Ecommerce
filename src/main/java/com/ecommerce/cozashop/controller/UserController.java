package com.ecommerce.cozashop.controller;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.io.File;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private HttpSession session;

	@Autowired
	private CookieService cookieService;

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private EmailService  emailService;

	@Autowired
	private MessageSource messageSource;

	
	@GetMapping("/confirm-account/{email}")
	public String confirm_account(@PathVariable("email") String email)  {
		logger.info("account confirmation  /confirm-account/{}",email);
		try {
			User user = userService.getUserByEmail(email);
			if(!user.isEnabled()) {
				user.setEnabled(true);
				logger.info("user account confirmation {} is enabled",email);
				userService.updateAccount(user);
				Locale locale = user.getLocal();
				File file =  ResourceUtils.getFile("classpath:file/welcome-email.html");
				String preheader = messageSource.getMessage("label.init-preheader-welcome",null, locale);
				String content = new String(Files.readAllBytes(file.toPath()));
				String subject = messageSource.getMessage("label.init-welcome-account",null, locale);
				File fileCss = ResourceUtils.getFile("classpath:file/info.css");
				String contentCss = new String(Files.readAllBytes(fileCss.toPath()));
				String info1 = messageSource.getMessage("label.welcome-account-mail-1",null, locale);
				String hello = messageSource.getMessage("label.hello",null, locale);
				String bouton = messageSource.getMessage("label.sign-in",null, locale);
				String gretting = messageSource.getMessage("label.regards",null, locale);
				content = MessageFormat.format(content,contentCss,preheader,hello,info1,bouton,gretting);
				logger.info("send e-mail welcome for user account {} ",email);
				emailService.sendSimpleMessage(user.getEmail(),subject,content); 	
			}
		} catch (Exception e) {
			logger.error("error confirmation account for id {} => {}",email,e.getMessage() );
		}
		return "redirect:/";
	}
	
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
		Locale locale = LocaleContextHolder.getLocale();
		logger.info("register new account email:{} local:{}",user.getEmail(),locale);
		try {
			if (userService.checkEmailAlreadyExists(user.getEmail())) {
				String info = messageSource.getMessage("label.email-already-exist",null, locale);
				model.addAttribute("error", info);
				logger.error("user account creation error => {} ",info);
				return "account/register";
			} else if (userService.checkPhoneAlreadyExists(user.getPhone())) {
				model.addAttribute("error", "Phone number address already exists");
				return "account/register";
			} else {
				user.setLocal(locale);
				userService.registerAccount(user);
				File file =  ResourceUtils.getFile("classpath:file/create-account-email.html");
				String preheader = messageSource.getMessage("label.init-preheader-account",null, locale);
				String content = new String(Files.readAllBytes(file.toPath()));
				String subject = messageSource.getMessage("label.init-create-account",null, locale);
				File fileCss = ResourceUtils.getFile("classpath:file/info.css");
				String contentCss = new String(Files.readAllBytes(fileCss.toPath()));
				String info1 = messageSource.getMessage("label.create-account-mail-1",null, locale);
				String info2 = messageSource.getMessage("label.create-account-mail-2",null, locale);
				String mailboutton = messageSource.getMessage("label.create-account-mail-confirm",null, locale);
				String greeting = messageSource.getMessage("label.hello",null, locale);
				String regards = messageSource.getMessage("label.regards",null, locale);
				content = MessageFormat.format(content,contentCss,preheader,greeting,info1,user.getEmail(),mailboutton,info2,regards);

				emailService.sendSimpleMessage(user.getEmail(),subject,content); 
				String info = messageSource.getMessage("label.info-create-account",null, locale);
				model.addAttribute("info", info);
				return "account/login";
			}
		} catch (Exception e) {
			String info = messageSource.getMessage("label.error-create-account",null, locale);
			model.addAttribute("error", info);
			logger.error("user account creation error => {} : {}",info,e.getMessage());
			return "account/register";
		}
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
