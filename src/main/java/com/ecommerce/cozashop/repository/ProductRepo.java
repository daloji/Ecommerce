package com.ecommerce.cozashop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecommerce.cozashop.model.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    Product findAllById(Long id);
    
    @Query("select pr from Product pr where pr.product_name LIKE %?1%") 
    List<Product> findAllProductbyname(String name);
}
