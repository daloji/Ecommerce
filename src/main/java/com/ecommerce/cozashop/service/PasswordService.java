package com.ecommerce.cozashop.service;

import java.io.File;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.ecommerce.cozashop.model.PasswordResetToken;
import com.ecommerce.cozashop.repository.PasswordTokenRepo;

@Service
public class PasswordService {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private EmailService  emailService;

	@Autowired
	private PasswordTokenRepo passwordTokenRepo;
	
	
	public PasswordResetToken getUserToken(String token) {
		return passwordTokenRepo.findByToken(token);
	}
	
	
	public void resetPassword(String username, String token) {
		try {
			Locale locale = LocaleContextHolder.getLocale();
			File file =  ResourceUtils.getFile("classpath:file/reset-password-email.html");
			String preheader = messageSource.getMessage("label.init-preheader",null, locale);
			String content = new String(Files.readAllBytes(file.toPath()));
			String subject = messageSource.getMessage("label.init-password",null, locale);
			File fileCss = ResourceUtils.getFile("classpath:file/info.css");
			String contentCss = new String(Files.readAllBytes(fileCss.toPath()));
			String info1 = messageSource.getMessage("label.init-password-mail-1",null, locale);
			info1 = MessageFormat.format(info1,username);
			String info2 = messageSource.getMessage("label.init-password-mail-2",null, locale);
			String button = messageSource.getMessage("label.button-reset-password",null, locale);
			String grettings = messageSource.getMessage("label.regards",null, locale);

			content = MessageFormat.format(content,contentCss,preheader,messageSource.getMessage("label.hello",null, locale),info1,token,button,info2,grettings);
			emailService.sendSimpleMessage(username,subject,content); 
		}catch (Exception e) {
			//TODO logging
		}
	}
	
	
	public void deletePasswordToken(PasswordResetToken passwordToken) {
		passwordTokenRepo.delete(passwordToken);
	}
}
