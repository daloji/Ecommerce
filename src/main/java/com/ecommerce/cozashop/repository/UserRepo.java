package com.ecommerce.cozashop.repository;

import com.ecommerce.cozashop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    User findByEmail(String email);
    User findByPhone(String phone);

    @Query("select u.password from User u where u.email=?1")
    String getPassword(String email);

    @Query("select u.id from User u where u.email=?1")
    Long findIdByEmail(String email);

}
