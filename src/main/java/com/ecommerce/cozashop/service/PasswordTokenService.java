package com.ecommerce.cozashop.service;

import java.util.UUID;
import static java.util.Objects.nonNull;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ecommerce.cozashop.model.PasswordResetToken;
import com.ecommerce.cozashop.model.User;
import com.ecommerce.cozashop.repository.PasswordTokenRepo;

@Service
public class PasswordTokenService {
	
	@Autowired
	private PasswordTokenRepo passwordTokenRepo;
	
	@Value("${token.expire}")
	private String expire;

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
		LocalDateTime currentDateTime =  LocalDateTime.now();
		int expireTime = Integer.valueOf(expire);
		currentDateTime = currentDateTime.plusMinutes(expireTime);
		passworToken.setExpiryDate(currentDateTime.toDate());
		passwordTokenRepo.save(passworToken);
		return token;

	}
	
	
	public void deleteToken(PasswordResetToken passwordToken) {
		passwordTokenRepo.delete(passwordToken);	
	}

}
