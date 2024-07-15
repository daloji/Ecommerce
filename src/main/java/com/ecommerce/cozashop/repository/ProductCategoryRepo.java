package com.ecommerce.cozashop.repository;

import com.ecommerce.cozashop.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepo extends JpaRepository<ProductCategory, Integer> {
}
