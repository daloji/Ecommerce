package com.ecommerce.cozashop.repository;

import com.ecommerce.cozashop.model.ShopOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopOrderRepo extends JpaRepository<ShopOrder, Long> {
}
