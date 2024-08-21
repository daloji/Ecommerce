package com.ecommerce.cozashop.service;

import java.util.UUID;
import static java.util.Objects.nonNull;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.cozashop.model.PasswordResetToken;
import com.ecommerce.cozashop.model.User;
import com.ecommerce.cozashop.repository.PasswordTokenRepo;

@Service
public class PasswordTokenService {
	
	@Autowired
	private PasswordTokenRepo passwordTokenRepo;

	public String updatePasswordToken(User user) {
		String token = UUID.randomUUID().toString();
		PasswordResetToken passworToken = null;
		if(nonNull(user.getPasswordToken())) {
			passworToken = user.getPasswordToken();
		
		}else {
			passworToken = new PasswordResetToken();
		}
		passworToken.setToken(token);
	    passworToken.setUser(user);	
		LocalDateTime currentDateTimeFromJavaDate =  LocalDateTime.now();
		currentDateTimeFromJavaDate.plusHours(8);
		passworToken.setExpiryDate(currentDateTimeFromJavaDate.toDate());
		passwordTokenRepo.save(passworToken);
		return token;

	}
}
