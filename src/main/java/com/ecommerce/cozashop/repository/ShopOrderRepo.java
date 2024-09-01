package com.ecommerce.cozashop.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.cozashop.model.ShopOrder;

@Repository
public interface ShopOrderRepo extends JpaRepository<ShopOrder, Long> {
	
	@Query("select si from ShopOrder si where si.order_date BETWEEN :startDate AND :endDate")
	public List<ShopOrder> findAllShopOrderBetweenDates(@Param("startDate")LocalDate startDate,@Param("endDate")LocalDate endDate);
	
}
