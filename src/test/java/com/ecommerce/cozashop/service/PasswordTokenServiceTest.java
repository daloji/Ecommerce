package com.ecommerce.cozashop.service;


import static org.easymock.EasyMock.createControl;

import org.easymock.IMocksControl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ecommerce.cozashop.model.User;

public class PasswordTokenServiceTest {

	private IMocksControl control;

	private PasswordTokenService passwordTokenService;

	@BeforeEach
	protected void setUp() {
		control = createControl();
		passwordTokenService = new PasswordTokenService();
		/*
		userController = new UserController();
		model = control.createMock(Model.class);
		userService = control.createMock(UserService.class);
		passwordTokenService = control.createMock(PasswordTokenService.class);
		passwordService = control.createMock(PasswordService.class);
		ReflectionTestUtils.setField( userController, "userService", userService );
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setFallbackToSystemLocale(true);
		messageSource.setUseCodeAsDefaultMessage(true);
		messageSource.setBasenames("lang/messages");
		ReflectionTestUtils.setField( userController, "messageSource", messageSource );
		/*

		ReflectionTestUtils.setField( passwordController, "passwordTokenService", passwordTokenService );
		ReflectionTestUtils.setField( passwordController, "passwordService", passwordService );
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setFallbackToSystemLocale(true);
		messageSource.setUseCodeAsDefaultMessage(true);
		messageSource.setBasenames("lang/messages");
		ReflectionTestUtils.setField( passwordController, "messageSource", messageSource );*/

	}

	//@Test
	public  final  void createAccount_Local_english(){
		User user = new User();
		user.setEmail("toto@gmail.com");
		user.setPhone("06354128475");
		control.replay();
		String form = passwordTokenService.updatePasswordToken(user);
		control.verify();
	}	


}
