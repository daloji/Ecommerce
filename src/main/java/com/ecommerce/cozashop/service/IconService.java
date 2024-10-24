package com.ecommerce.cozashop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.cozashop.model.Icon;
import com.ecommerce.cozashop.repository.IconRepo;

@Service
public class IconService {
	
	@Autowired
	private IconRepo iconRepo;
	
	
	public List<Icon> getAllIcon() {
		return iconRepo.findAll();
	}
	
	
	public void addIcon(Icon logo) {
		iconRepo.save(logo);
	}
	
	public void delete(int id) {
		iconRepo.deleteById(id);
	}
	
}