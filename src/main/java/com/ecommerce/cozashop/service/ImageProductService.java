package com.ecommerce.cozashop.service;

import com.ecommerce.cozashop.model.ImageProduct;
import com.ecommerce.cozashop.repository.ImageProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageProductService {
    @Autowired
    private ImageProductRepo imageProductRepo;

    public List<ImageProduct> getAllImageById(Long id) {
        return imageProductRepo.findImgProductById(id);
    }
}
