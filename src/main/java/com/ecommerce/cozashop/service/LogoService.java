package com.ecommerce.cozashop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.cozashop.model.Logo;
import com.ecommerce.cozashop.repository.LogoRepo;

@Service
public class LogoService {

	@Autowired
	private LogoRepo logoRepo;
	
	
	public Logo getLogo() {
		List<Logo> logoList =  logoRepo.findAll();
		Logo logo = null;
		if(logoList != null && !logoList.isEmpty()) {
			logo = logoList.get(0);
		}
		return logo;
	}
	
	
	public void addLogo(Logo logo) {
		logoRepo.save(logo);
	}
	
	public void delete(int id) {
		logoRepo.deleteById(id);
	}
	
}
