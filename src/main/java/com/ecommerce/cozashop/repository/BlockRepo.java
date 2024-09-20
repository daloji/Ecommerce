package com.ecommerce.cozashop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.cozashop.model.Block;

@Repository
public interface BlockRepo extends JpaRepository<Block, Integer> {

}
