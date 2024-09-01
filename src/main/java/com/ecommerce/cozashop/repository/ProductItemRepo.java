package com.ecommerce.cozashop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecommerce.cozashop.model.Product;
import com.ecommerce.cozashop.model.ProductItem;

@Repository
public interface ProductItemRepo extends JpaRepository<ProductItem, Long> {

    @Query("select pi from ProductItem pi where pi.product_id.id =?1")
    ProductItem findProductItemByProduct_id(Long id);

    @Query("select pi from ProductItem pi where pi.id=?1")
    ProductItem findProductById(Long id);
    

    @Query("select pi from ProductItem pi where pi.qty_in_stock=pi.product_sell")
    List<ProductItem> findProductItemsAvalaible();
    
    
    @Query("SELECT pr FROM  Product pr,ProductItem pi  WHERE pi.product_id.id=pr.id  AND  pi.qty_in_stock>pi.product_sell")
    List<Product> findProductAvalaible();
    
    ProductItem findProductItemById(Long id);
}