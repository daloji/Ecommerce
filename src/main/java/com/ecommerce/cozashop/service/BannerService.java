package com.ecommerce.cozashop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.cozashop.model.Banner;
import com.ecommerce.cozashop.repository.BannerRepo;

@Service
public class BannerService {

    @Autowired
    private BannerRepo bannerRepo;
    
    
    public List<Banner> findAllBanner() {
    	return bannerRepo.findAll();
    }
    
    public void save(Banner banner) {
    	bannerRepo.save(banner);
    }
    
    public void delete(Integer id) {
    	bannerRepo.deleteById(id);
    }
}
