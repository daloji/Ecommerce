package com.ecommerce.cozashop.repository;

import com.ecommerce.cozashop.model.Product;
import com.ecommerce.cozashop.model.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductItemRepo extends JpaRepository<ProductItem, Long> {

    @Query("select pi from ProductItem pi where pi.product_id.id =?1")
    ProductItem findProductItemByProduct_id(Long id);

    @Query("select pi from ProductItem pi where pi.id=?1")
    ProductItem findProductById(Long id);
}