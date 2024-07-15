package com.ecommerce.cozashop.service;

import com.ecommerce.cozashop.model.ProductItem;
import com.ecommerce.cozashop.repository.ProductItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductItemService {

    @Autowired
    private ProductItemRepo productItemRepo;

    public List<ProductItem> getProductItems () {
        return productItemRepo.findAll();
    }

    public ProductItem getOneProduct(Long id) {
        return productItemRepo.findProductItemByProduct_id(id);
    }

    public ProductItem getProductById(Long id) {
        return productItemRepo.findProductById(id);
    }
}
