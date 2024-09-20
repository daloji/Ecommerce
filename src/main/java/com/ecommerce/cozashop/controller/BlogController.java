package com.ecommerce.cozashop.controller;

import static java.util.Objects.isNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ecommerce.cozashop.model.Logo;
import com.ecommerce.cozashop.service.LogoService;

@Controller
public class BlogController {


	@Autowired
	private LogoService logoService;

	@GetMapping("/blog-detail")
	public String show(Model model) {
		return  "blog-detail";
	}

	@GetMapping("/blog")
	public String showBlog(Model model) {
		Logo logo = logoService.getLogo();
		if(isNull(logo)) {
			logo = new Logo();
		}
		model.addAttribute("logo", logo);

		return  "blog";
	}

}
