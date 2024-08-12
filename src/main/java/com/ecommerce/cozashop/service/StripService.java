package com.ecommerce.cozashop.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ecommerce.cozashop.model.CartItem;
import com.ecommerce.cozashop.model.ProductItem;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.model.checkout.Session;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.checkout.SessionCreateParams;

@Service
public class StripService {

	@Value("${stripe.api}")
	public String StripeApiKey;

	@Autowired
	ProductItemService productItemService;

	public Session stripePayment( List<CartItem> cartList) {
		try {
			Stripe.apiKey = StripeApiKey;
			List<com.ecommerce.cozashop.model.Product> listProduct = getListProduct(cartList);
			List<SessionCreateParams.LineItem> listItem = new ArrayList<SessionCreateParams.LineItem>();
			for(com.ecommerce.cozashop.model.Product product:listProduct) {
				ProductItem productItem = 	productItemService.getProductById(product.getId());

				//create Price For Product
				PriceCreateParams params =
						PriceCreateParams.builder()
						.setCurrency("usd")
						.setUnitAmount(productItem.getPrice().longValue()*100)
						.setProductData(
								PriceCreateParams.ProductData.builder()//
								.setName(product.getProduct_name())//
								.setActive(product.getStatus())
								.build()
								)
						.build();
				Price price = Price.create(params);
			
				SessionCreateParams.LineItem  itemLine = SessionCreateParams.LineItem.builder()//
						.setPrice(price.getId())
						.setQuantity(1L)
						.build();
				listItem.add(itemLine);
	

			}

			SessionCreateParams params =
					SessionCreateParams.builder()
					.setSuccessUrl("https://example.com/success")
					.setCancelUrl("https://example.com/cancel")
					.addAllLineItem(listItem)
					.setMode(SessionCreateParams.Mode.PAYMENT)
					.build();

			Session session = Session.create(params);
			return session;

		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}


	}


	private List<com.ecommerce.cozashop.model.Product> getListProduct( List<CartItem> cartList){
		List<com.ecommerce.cozashop.model.Product>  listProduct = new ArrayList<>();
		if(cartList != null && !cartList.isEmpty()) {
			for(CartItem cartItem :cartList) {
				ProductItem productItem =  cartItem.getItem();
				com.ecommerce.cozashop.model.Product product = productItem.getProduct_id();
				listProduct.add(product);
			}
		}
		return listProduct;

	}
}
