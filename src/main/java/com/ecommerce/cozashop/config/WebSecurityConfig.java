package com.ecommerce.cozashop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


		http
		.authorizeHttpRequests(authConfig -> {
			authConfig.requestMatchers(HttpMethod.GET, "/","/index" ,
					"/home","/login", "/product","/error", "/login-error", "/logout").permitAll();
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
			login.failureUrl("/login-error");
		}
				)
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



	@Bean
	public UserDetailsService userDetailsService() {
		UserDetails user =
				User.withDefaultPasswordEncoder()
				.username("user")
				.password("password")
				.roles("USER")
				.build();

		return new InMemoryUserDetailsManager(user);
	}

}
