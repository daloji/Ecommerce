package com.ecommerce.cozashop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BlogController {

	@GetMapping("/blog-detail")
	public String show(Model model) {
		return  "blog-detail";
	}
	
}
