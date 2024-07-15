package com.ecommerce.cozashop.repository;

import com.ecommerce.cozashop.model.SizeOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SizeOptionRepo extends JpaRepository<SizeOption, Integer> {

    @Query("select n.value from SizeOption n where n.id=?1")
    String findValueById(Integer id);
}
