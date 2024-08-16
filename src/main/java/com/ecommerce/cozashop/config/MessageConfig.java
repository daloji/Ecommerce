package com.ecommerce.cozashop.config;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
public class MessageConfig implements WebMvcConfigurer{
	
	
	 @Bean
	    public MessageSource messageSource() {
	    	ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
	        messageSource.setBasenames("lang/messages");
	        messageSource.setDefaultEncoding("UTF-8");
	        messageSource.setDefaultLocale(Locale.ENGLISH);
	        return messageSource;
	    }
	    
	    
	    @Bean
	    public LocaleResolver localeResolver() {
	        SessionLocaleResolver sessionLocaleResolver=new SessionLocaleResolver();
	        sessionLocaleResolver.setDefaultLocale(Locale.ENGLISH);
	        return sessionLocaleResolver;
	    }
	    
	    @Bean
	    public LocaleChangeInterceptor localeChangeInterceptor() {
	        LocaleChangeInterceptor localeChangeInterceptor=new LocaleChangeInterceptor();
	        localeChangeInterceptor.setParamName("lang");
	        return localeChangeInterceptor;
	    }

	    
	 @Override
	  public void addInterceptors(InterceptorRegistry registry) {
	      registry.addInterceptor(localeChangeInterceptor());
	  }
}



