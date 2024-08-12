package com.ecommerce.cozashop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import com.ecommerce.cozashop.model.WishListITem;
import com.ecommerce.cozashop.service.WishListService;

public class WishListControler {

	@Autowired
	WishListService wishListService;
	
	@GetMapping("/add-wishlist")
	public void addWishList(WishListITem wishListITem) {
		wishListService.addWishList(wishListITem);
	}
	
}
