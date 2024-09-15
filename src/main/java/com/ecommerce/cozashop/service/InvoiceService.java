package com.ecommerce.cozashop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.cozashop.model.Invoice;
import com.ecommerce.cozashop.model.User;
import com.ecommerce.cozashop.repository.InvoiceRepo;

@Service
public class InvoiceService {
	
	@Autowired
	private InvoiceRepo invoiceRepo;
	
	
	
	public List<Invoice> getIvoiceByUser(User user){
		return invoiceRepo.findInvoice(user.getId());
	}
}
