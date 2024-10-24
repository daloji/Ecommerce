package com.ecommerce.cozashop.service;

import com.ecommerce.cozashop.model.OrderLine;
import com.ecommerce.cozashop.repository.OrderLineRepo;

import static java.util.Objects.isNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    
    
    public 	Map<LocalDate,List<OrderLine>> findAllOrderLineByUserId(Long id) {
    	List<OrderLine> listOrder = orderLineRepo.findAllOrderLineByUserId(id);
    	Map<LocalDate,List<OrderLine>> map = new HashMap();
    	for(OrderLine orderline: listOrder) {
    		if(map.containsKey(orderline.getOrder_date())) {
    			List<OrderLine> listOd = map.get(orderline.getOrder_date());
    			if(isNull(listOd)) {
    				listOd = new ArrayList<OrderLine>();
    			}
    			listOd.add(orderline);
    			map.put(orderline.getOrder_date(), listOd);
    		}else {
    			List<OrderLine> listOd = new ArrayList<OrderLine>();
    			listOd.add(orderline);
    			map.put(orderline.getOrder_date(), listOd);
    		}
    	}
  
     return map;
    }
  
    public void save(OrderLine orderLine) {
    	orderLineRepo.save(orderLine);
    }
}
