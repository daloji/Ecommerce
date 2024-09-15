package com.ecommerce.cozashop.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.ecommerce.cozashop.model.Address;
import com.ecommerce.cozashop.model.Invoice;
import com.ecommerce.cozashop.model.PdfTemplate;
import com.ecommerce.cozashop.model.ProductItem;
import com.ecommerce.cozashop.model.User;
import com.ecommerce.cozashop.repository.InvoiceRepo;

@Service 	
public class PdfGeneratorService {
	
	@Autowired
	private InvoiceRepo invoiceRepo;
	
	@Autowired
	ResourceLoader resourceLoader;

	public void generateInvoice(Map<ProductItem,Integer> mapProductQty,User user,Address adresse) {
		PdfTemplate pdfTemplate = new PdfTemplate(resourceLoader);	
		Map<String,String> mapInvoice = pdfTemplate.generateInvoice(mapProductQty, user, adresse);
		if (!mapInvoice.isEmpty()) {
			for (Map.Entry<String, String> mapentry : mapInvoice.entrySet()) {
				Invoice invoice = new Invoice();
				invoice.setFilename(mapentry.getValue());
				invoice.setInvoiceId(mapentry.getKey());
				invoice.setUser(user);
				invoiceRepo.save(invoice);
			}
		}
	}
}
