package com.ecommerce.cozashop.service;

import com.ecommerce.cozashop.model.SizeOption;
import com.ecommerce.cozashop.repository.SizeOptionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SizeOptionService {

    @Autowired
    private SizeOptionRepo sizeOptionRepo;

    public String getSizeProduct(Integer id) {
        return sizeOptionRepo.findValueById(id);
    }

    public List<SizeOption> getALl() {
        return sizeOptionRepo.findAll();
    }
}
