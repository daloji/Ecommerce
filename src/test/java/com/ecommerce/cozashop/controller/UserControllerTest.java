package com.ecommerce.cozashop.controller;

import static org.easymock.EasyMock.createControl;
import static org.easymock.EasyMock.expect;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.Model;

import com.ecommerce.cozashop.model.User;
import com.ecommerce.cozashop.service.EmailService;
import com.ecommerce.cozashop.service.PasswordService;
import com.ecommerce.cozashop.service.PasswordTokenService;
import com.ecommerce.cozashop.service.UserService;

public class UserControllerTest {

	
	private ResourceBundleMessageSource messageSource;
	
	private IMocksControl control;

	private UserController userController;

	private UserService userService;
	
	private Model model;
	
	private PasswordTokenService passwordTokenService;
	
	private PasswordService passwordService;
	
	private EmailService  emailService;


	@BeforeEach
	protected void setUp() {
		control = createControl();
		userController = new UserController();
		model = control.createMock(Model.class);
		userService = control.createMock(UserService.class);
		passwordTokenService = control.createMock(PasswordTokenService.class);
		passwordService = control.createMock(PasswordService.class);
		emailService = control.createMock(EmailService.class);
		ReflectionTestUtils.setField( userController, "userService", userService );
		ReflectionTestUtils.setField( userController, "emailService", emailService );

		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setFallbackToSystemLocale(true);
		messageSource.setUseCodeAsDefaultMessage(true);
		messageSource.setBasenames("lang/messages");
		ReflectionTestUtils.setField( userController, "messageSource", messageSource );

	}
	
	@Test
	public  final  void createAccount_Local_email_exist_english(){
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		User user = new User();
		user.setEmail("toto@gmail.com");
		user.setPhone("06354128475");
		expect(userService.checkEmailAlreadyExists(user.getEmail())).andReturn(true);
		expect(model.addAttribute("error","Email address already exists")).andReturn(null);
		control.replay();
		String form = userController.registerAccount(user, model);
		control.verify();
		assertEquals(form, "account/register");
	}	
	
	//@Test
	public  final  void createAccount_Local_phone_exist_english(){
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		User user = new User();
		user.setEmail("toto@gmail.com");
		user.setPhone("06354128475");
		expect(userService.checkEmailAlreadyExists(user.getEmail())).andReturn(false);
		expect(userService.checkPhoneAlreadyExists(user.getPhone())).andReturn(true);
		expect(model.addAttribute("error","Email address already exists")).andReturn(null);
		control.replay();
		String form = userController.registerAccount(user, model);
		control.verify();
		assertEquals(form, "account/register");
	}	
	
	
	@Test
	public  final  void createAccount_Local_email_exist_french(){
		LocaleContextHolder.setLocale(Locale.FRENCH);
		User user = new User();
		user.setEmail("toto@gmail.com");
		user.setPhone("06354128475");
		expect(userService.checkEmailAlreadyExists(user.getEmail())).andReturn(true);
		expect(model.addAttribute("error","L'adresse e-mail existe déjà")).andReturn(null);
		control.replay();
		String form = userController.registerAccount(user, model);
		control.verify();
		assertEquals(form, "account/register");
	}	
	

	@Test
	public  final  void createAccount_Local_english(){
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		User user = new User();
		user.setEmail("toto@gmail.com");
		user.setPhone("06354128475");
		expect(userService.checkEmailAlreadyExists(user.getEmail())).andReturn(false);
		expect(userService.checkPhoneAlreadyExists(user.getPhone())).andReturn(false);
		userService.registerAccount(user);
		EasyMock.expectLastCall();
		emailService.sendSimpleMessage(EasyMock.anyObject(),EasyMock.anyObject(),EasyMock.anyObject()); 
		EasyMock.expectLastCall();
		expect(model.addAttribute("info","Your account has been created")).andReturn(null);
		control.replay();
		String form = userController.registerAccount(user, model);
		control.verify();
		assertEquals(form, "account/login");
	}	
	
	@Test
	public  final  void createAccount_Local_French(){
		LocaleContextHolder.setLocale(Locale.FRENCH);
		User user = new User();
		user.setEmail("toto@gmail.com");
		user.setPhone("06354128475");
		expect(userService.checkEmailAlreadyExists(user.getEmail())).andReturn(false);
		expect(userService.checkPhoneAlreadyExists(user.getPhone())).andReturn(false);
		userService.registerAccount(user);
		EasyMock.expectLastCall();
		emailService.sendSimpleMessage(EasyMock.anyObject(),EasyMock.anyObject(),EasyMock.anyObject()); 
		EasyMock.expectLastCall();
		expect(model.addAttribute("info","Votre compte a été créé")).andReturn(null);
		control.replay();
		String form = userController.registerAccount(user, model);
		control.verify();
		assertEquals(form, "account/login");
	}	
	

}
