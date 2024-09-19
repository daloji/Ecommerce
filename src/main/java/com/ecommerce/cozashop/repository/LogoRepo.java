package com.ecommerce.cozashop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.cozashop.model.Logo;


@Repository
public interface LogoRepo extends JpaRepository<Logo, Integer> {


}
