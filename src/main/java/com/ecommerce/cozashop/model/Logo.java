package com.ecommerce.cozashop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "logo")
@Data
@RequiredArgsConstructor
public class Logo {
	@Id
    @GeneratedValue
    private Long id;
	
	private String title;
	
	private String  imageLogo;
}
