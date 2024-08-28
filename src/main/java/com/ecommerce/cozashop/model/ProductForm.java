package com.ecommerce.cozashop.model;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor	
public class ProductForm {

	private ProductItem productItem;
	
	private String category;
	
	private MultipartFile file;
}
