package com.ecommerce.cozashop.service;

import com.ecommerce.cozashop.model.Product;
import com.ecommerce.cozashop.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    public List<Product> getAllProduct() {
        return productRepo.findAll();
    }

    public Product getProduct(Long id) { return productRepo.findAllById(id);}
}
