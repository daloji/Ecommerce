package com.ecommerce.cozashop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.ecommerce.cozashop.service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
	
	private static final String KEY ="ABCDEFGHIJKLMOPWRSTQUWXYZ1234567890";

	private String[] authorizedURL = {"/","/index","/blog" ,"/blog-detail","/home","/login", "/product","/product-detail/*",
			                         "/error", "/register","/forgot-password","/register-new",
			                         "/add-to-cart/*/*", "/reset/password/*", "/update-password","/logout"};
	
    @Autowired
    private UserService userService;
    
	@Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http
		.authorizeHttpRequests(authConfig -> {
			authConfig.requestMatchers(HttpMethod.GET, authorizedURL).permitAll();
			authConfig.requestMatchers(HttpMethod.POST, "/reset/new-password","/register-new","/reset-password","/update-user").permitAll();
			authConfig.anyRequest().authenticated();
			/*authConfig.requestMatchers(HttpMethod.GET, "/user").hasRole("USER");
			authConfig.requestMatchers(HttpMethod.GET, "/admin").hasRole("ADMIN");
			authConfig.requestMatchers(HttpMethod.GET, "/developer").hasRole("DEVELOPER");
			authConfig.requestMatchers(HttpMethod.GET, "/users").hasAnyRole("DEVELOPER");
			authConfig.requestMatchers(HttpMethod.GET, "/authorities").hasAnyRole("DEVELOPER");
			authConfig.anyRequest().authenticated();*/
		})
		.formLogin(login -> {
			login.loginPage("/login");
			login.defaultSuccessUrl("/");
			login.loginProcessingUrl("/login");
			login.successHandler(customAuthenticationSuccessHandler);
			
		})
		.rememberMe(config -> {
			config.key(KEY) .tokenValiditySeconds(3600*24);
			config.userDetailsService(userService);
		})
		.logout(logout -> {
			logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
			logout.logoutSuccessUrl("/");
			logout.deleteCookies("JSESSIONID");
			logout.invalidateHttpSession(true);
		});

		
		return http.build();
	}
	

	@Bean
	WebSecurityCustomizer configureWebSecurity() {
		return (web) -> web.ignoring().requestMatchers("/images/**","/fonts/**", "/js/**", "/css/**", "/vendor/**");
	}

}
