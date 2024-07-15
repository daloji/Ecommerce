package com.ecommerce.cozashop.service;

import com.ecommerce.cozashop.model.ProductSize;
import com.ecommerce.cozashop.repository.ProductSizeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductSizeService {

    @Autowired
    private ProductSizeRepo productSizeRepo;

    public List<ProductSize> getAllSize(Long id) {
        return productSizeRepo.findAllSize(id);
    }
}
