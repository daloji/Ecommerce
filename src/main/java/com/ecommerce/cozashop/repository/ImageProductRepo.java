package com.ecommerce.cozashop.repository;

import com.ecommerce.cozashop.model.ImageProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageProductRepo extends JpaRepository<ImageProduct, Integer> {

    @Query("select im from ImageProduct im where im.productItem.id=?1")
    List<ImageProduct> findImgProductById(Long id);

}
