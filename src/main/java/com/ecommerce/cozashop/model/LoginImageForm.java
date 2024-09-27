package com.ecommerce.cozashop.model;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor	
public class LoginImageForm {

	private LoginImage loginImage;

	private MultipartFile bannerFile;
}
