package com.ecommerce.cozashop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Entity
@Table(name = "icon")
@Data
@RequiredArgsConstructor
public class Icon {
	
	@Id
    @GeneratedValue
    private Long id;
	
	private String title;
	
	private String  imageIcon;
}
