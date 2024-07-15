package com.ecommerce.cozashop.repository;

import com.ecommerce.cozashop.model.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderLineRepo extends JpaRepository<OrderLine, Long> {


}
