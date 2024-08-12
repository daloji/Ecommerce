package com.ecommerce.cozashop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.cozashop.model.WishListITem;
import com.ecommerce.cozashop.repository.WishListItemRepo;

@Service
public class WishListService {

	@Autowired
	private WishListItemRepo whisItemRepo;


	public void addWishList(WishListITem wishList) {
		whisItemRepo.save(wishList);
	}
}
