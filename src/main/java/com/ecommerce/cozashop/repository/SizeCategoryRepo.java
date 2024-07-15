package com.ecommerce.cozashop.repository;

import com.ecommerce.cozashop.model.SizeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SizeCategoryRepo extends JpaRepository<SizeCategory, Integer> {
}
