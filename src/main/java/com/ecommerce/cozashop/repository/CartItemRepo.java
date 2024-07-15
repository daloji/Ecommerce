package com.ecommerce.cozashop.repository;

import com.ecommerce.cozashop.model.CartItem;
import com.ecommerce.cozashop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CartItemRepo extends JpaRepository<CartItem, Integer> {

    @Query("select n from CartItem n where n.user.id=?1")
    List<CartItem> findCart(Long id);

    @Query("select c.qty from CartItem c where c.item.id=?1")
    Integer getQtyCart(Long id);

    @Query("select ci from CartItem ci where ci.item.id=?1")
    CartItem checkCartItem(Long id);

    @Query("select n from CartItem n where n.id=?1")
    CartItem getCartItemById(Integer id);

    void deleteAllByUser(User user);
}
