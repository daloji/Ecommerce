package com.ecommerce.cozashop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "block")
@Data
@RequiredArgsConstructor
public class Block {

	@Id
    @GeneratedValue
    private Long id;
	
	private String smallTitle;
	
	private String  title;
	
	private String  imageBanner;
}