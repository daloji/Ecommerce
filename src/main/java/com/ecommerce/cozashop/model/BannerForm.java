package com.ecommerce.cozashop.model;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor	
public class BannerForm {

	private Banner banner;

	private MultipartFile bannerFile;
}
