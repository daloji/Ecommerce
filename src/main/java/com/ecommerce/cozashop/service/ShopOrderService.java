package com.ecommerce.cozashop.service;
import static java.util.Objects.nonNull;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.cozashop.model.PaymentStatus;
import com.ecommerce.cozashop.model.ShopOrder;
import com.ecommerce.cozashop.repository.ShopOrderRepo;

@Service
public class ShopOrderService {

	@Autowired
	private ShopOrderRepo shopOrderRepo;

	public void createNewOrder(ShopOrder order) {
		shopOrderRepo.save(order);
	}

	public void save(ShopOrder order) {
		shopOrderRepo.save(order);
	}
	
	public List<ShopOrder> findAllShopOrder() {
		return shopOrderRepo.findAll();
	}
	
	public List<ShopOrder> findAllShopOrderBetweenDates(LocalDate startDate,LocalDate endDate) {
		return shopOrderRepo.findAllShopOrderBetweenDates(startDate, endDate);
	}
	
	public int[] getDistributionOrder() {
		int order[]= new int[12];
		List<ShopOrder> listOrder = shopOrderRepo.findAll();
		if(nonNull(listOrder)) {
			for(ShopOrder shopOrder:listOrder) {
				if(shopOrder.getStatus() == PaymentStatus.ACCEPT) {
					LocalDate date = shopOrder.getOrder_date();
					int month = date.getMonthValue();
					int count = order[month-1];
					count = count + 1;
					order[month-1] = count;
				}
			}
		}
		return order;
	}
}
