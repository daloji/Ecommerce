package com.ecommerce.cozashop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.cozashop.model.ProductCategory;
import com.ecommerce.cozashop.repository.ProductCategoryRepo;

@Service
public class ProductCategoryService {
	
	@Autowired
    private ProductCategoryRepo productCategoryRepo;
	
	
	public void addProductCategory(ProductCategory productCategory) {
		productCategoryRepo.save(productCategory);
	}
	
	public List<ProductCategory> findAllProductCategory(){
		return productCategoryRepo.findAll();
	}
	
	public ProductCategory findCategoryById(int id) {
		ProductCategory category = null;
		Optional<ProductCategory> optCategory = productCategoryRepo.findById(id);
		if(optCategory.isPresent()){
			category = optCategory.get();
		}
		return category;
	}
	
	public void deleteCategory(Integer id) {
		productCategoryRepo.deleteById(id);
	}
}
