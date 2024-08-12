package com.ecommerce.cozashop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecommerce.cozashop.model.User;
import com.ecommerce.cozashop.model.WishListITem;


@Repository
public interface WishListItemRepo extends JpaRepository<WishListITem, Integer> {

    @Query("select n from WishListITem n where n.user.id=?1")
    List<WishListITem> findWishList(Long id);

    @Query("select c.qty from WishListITem c where c.item.id=?1")
    Integer getQtyCart(Long id);

    @Query("select ci from WishListITem ci where ci.item.id=?1")
    WishListITem checkWishListITem(Long id);

    @Query("select n from WishListITem n where n.id=?1")
    WishListITem getWishListITemById(Integer id);

    void deleteAllByUser(User user);
}
