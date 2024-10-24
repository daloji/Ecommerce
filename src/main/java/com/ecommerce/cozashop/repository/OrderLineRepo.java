package com.ecommerce.cozashop.repository;

import com.ecommerce.cozashop.model.OrderLine;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderLineRepo extends JpaRepository<OrderLine, Long> {

	@Query("select od from OrderLine od where od.shopOrder.id=?1")
	List<OrderLine> findAllOrderLineByShopId(Long id);
	
	@Query("select od from OrderLine od where od.shopOrder.user.id=?1")
	List<OrderLine> findAllOrderLineByUserId(Long id);
	
	OrderLine findOrderLineById(Long id);
}
