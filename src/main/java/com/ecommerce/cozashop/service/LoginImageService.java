package com.ecommerce.cozashop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.cozashop.model.LoginImage;
import com.ecommerce.cozashop.repository.LoginImageRepo;

@Service
public class LoginImageService {

	@Autowired
	private LoginImageRepo loginImageRepo;
	
	
	public LoginImage getLoginImage() {
		List<LoginImage> loginImageList =  loginImageRepo.findAll();
		LoginImage loginImage = null;
		if(loginImageList != null && !loginImageList.isEmpty()) {
			loginImage = loginImageList.get(0);
		}
		return loginImage;
	}
	
	
	public void addLoginImage(LoginImage loginImage) {
		loginImageRepo.save(loginImage);
	}
	
	public void delete(int id) {
		loginImageRepo.deleteById(id);
	}
	
	
}
