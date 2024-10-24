package com.ecommerce.cozashop.model;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor	
public class IconForm {

	private Icon iconImage;

	private MultipartFile iconFile;
}