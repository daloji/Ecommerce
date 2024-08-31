package com.ecommerce.cozashop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceCustom implements WebMvcConfigurer  {
	
	@Value("${resources.directory}")
	private String directory;
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry
	      .addResourceHandler("/images/**")
	      .addResourceLocations("file:"+directory);
	}
}
