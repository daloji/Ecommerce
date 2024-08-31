package com.ecommerce.cozashop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.cozashop.model.Banner;

@Repository
public interface BannerRepo extends JpaRepository<Banner, Integer> {

}
