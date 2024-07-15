package com.ecommerce.cozashop.service;

import com.ecommerce.cozashop.model.ShopOrder;
import com.ecommerce.cozashop.repository.ShopOrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShopOrderService {

    @Autowired
    private ShopOrderRepo shopOrderRepo;

    public void createNewOrder(ShopOrder order) {
        shopOrderRepo.save(order);
    }
}
