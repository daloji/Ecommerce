package com.ecommerce.cozashop.model;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor	
public class ProductForm {

	private ProductItem productItem;
	
	private String category;
	
	private List<MultipartFile> file;
	
	private MultipartFile filePrincipal;
}
