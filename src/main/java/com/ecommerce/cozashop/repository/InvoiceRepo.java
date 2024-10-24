package com.ecommerce.cozashop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecommerce.cozashop.model.Invoice;


@Repository
public interface InvoiceRepo extends JpaRepository<Invoice, Integer> {

	@Query("select n from Invoice n where n.user.id=?1")
    List<Invoice> findInvoice(Long id);
	
    Invoice findInvoiceById(Long id);
    
	/*
    @Query("select n from CartItem n where n.user.id=?1")
    List<CartItem> findCart(Long id);

    @Query("select c.qty from CartItem c where c.item.id=?1")
    Integer getQtyCart(Long id);

    @Query("select ci from CartItem ci where ci.item.id=?1")
    CartItem checkCartItem(Long id);

    @Query("select n from CartItem n where n.id=?1")
    CartItem getCartItemById(Integer id);

    void deleteAllByUser(User user);
    */
}
