package com.ecommerce.cozashop.repository;

import com.ecommerce.cozashop.model.ProductSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductSizeRepo extends JpaRepository<ProductSize, Long> {

    @Query("select ps from ProductSize ps where ps.productItem.id = ?1")
    List<ProductSize> findAllSize(Long id);
}
