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
import com.ecommerce.cozashop.service.PasswordService;
import com.ecommerce.cozashop.service.PasswordTokenService;
import com.ecommerce.cozashop.service.UserService;

public class PasswordControllerTest {

	
	private ResourceBundleMessageSource messageSource;
	
	private IMocksControl control;

	private PasswordController passwordController;

	private UserService userService;
	
	private Model model;
	
	private PasswordTokenService passwordTokenService;
	
	private PasswordService passwordService;

	@BeforeEach
	protected void setUp() {
		control = createControl();
		passwordController = new PasswordController();
		model = control.createMock(Model.class);
		userService = control.createMock(UserService.class);
		passwordTokenService = control.createMock(PasswordTokenService.class);
		passwordService = control.createMock(PasswordService.class);
		ReflectionTestUtils.setField( passwordController, "userService", userService );
		ReflectionTestUtils.setField( passwordController, "passwordTokenService", passwordTokenService );
		ReflectionTestUtils.setField( passwordController, "passwordService", passwordService );
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setFallbackToSystemLocale(true);
		messageSource.setUseCodeAsDefaultMessage(true);
		messageSource.setBasenames("lang/messages");
		ReflectionTestUtils.setField( passwordController, "messageSource", messageSource );

	}

	@Test
	public  final  void resetPassword_Local_french(){
		LocaleContextHolder.setLocale(Locale.FRENCH);
		User user = new User();
		user.setEmail("toto@gmail.com");
		expect(userService.checkEmailAlreadyExists(user.getEmail())).andReturn(true);		
		expect(userService.getUserByEmail(user.getEmail())).andReturn(user);
		expect( passwordTokenService.updatePasswordToken(user)).andReturn("token");
		passwordService.resetPassword(user.getEmail(),"token");
		EasyMock.expectLastCall();
		expect(model.addAttribute("info","Vous allez recevoir un e-mail contenant un lien de réinitialisation de votre mot de passe")).andReturn(null);
		control.replay();
		String form = passwordController.resetPassword(user,model);
		control.verify();
		assertEquals(form, "account/forgot-password");
	}	
	
	@Test
	public  final  void resetPassword_Local_english(){
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		User user = new User();
		user.setEmail("toto@gmail.com");
		expect(userService.checkEmailAlreadyExists(user.getEmail())).andReturn(true);
		expect(userService.getUserByEmail(user.getEmail())).andReturn(user);
		expect( passwordTokenService.updatePasswordToken(user)).andReturn("token");
		passwordService.resetPassword(user.getEmail(),"token");
		EasyMock.expectLastCall();
		expect(model.addAttribute("info","You will receive an email containing a link to reset your password")).andReturn(null);
		control.replay();
		String form = passwordController.resetPassword(user,model);
		control.verify();
		assertEquals(form, "account/forgot-password");
	}

}
