package com.ecommerce.cozashop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.cozashop.model.LoginImage;


@Repository
public interface LoginImageRepo extends JpaRepository<LoginImage, Integer> {


}
