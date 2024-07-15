package com.ecommerce.cozashop.repository;

import com.ecommerce.cozashop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    Product findAllById(Long id);
}
