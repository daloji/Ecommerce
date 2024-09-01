package com.ecommerce.cozashop.service;

import com.ecommerce.cozashop.model.OrderLine;
import com.ecommerce.cozashop.repository.OrderLineRepo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderLineService {

    @Autowired
    private OrderLineRepo orderLineRepo;

    public void createOrderLine(OrderLine line) {
        orderLineRepo.save(line);
    }
    
    public 	List<OrderLine>  findAllOrderLineByShopId(Long id) {
    	return orderLineRepo.findAllOrderLineByShopId(id);
    }
    
    public OrderLine findOrdeLineBydId(Long id) {
    	return orderLineRepo.findOrderLineById(id);
    }
    
  
    public void save(OrderLine orderLine) {
    	orderLineRepo.save(orderLine);
    }
}
