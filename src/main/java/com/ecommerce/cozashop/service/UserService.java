package com.ecommerce.cozashop.service;

import com.ecommerce.cozashop.model.Role;
import com.ecommerce.cozashop.model.User;
import com.ecommerce.cozashop.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public boolean checkEmailAlreadyExists(String email) {
        return userRepo.findByEmail(email) == null ? true : false;
    }


    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public Long getIdUserByEmail(String email) {
        return userRepo.findIdByEmail(email);
    }

    public void registerAccount(User user) {
        Role role = new Role();
        Base64.Encoder encoder = Base64.getEncoder();
        String encodeStr = encoder.encodeToString(user.getPassword().getBytes(StandardCharsets.UTF_8));

        role.setId(1);
        user.setPassword(encodeStr);
        user.setRole(role);
        user.setStatus(true);

        userRepo.save(user);
    }

    public String getPasswordByEmail(String email) {
        return userRepo.getPassword(email);
    }

    public List<User> show() {
        return userRepo.findAll();
    }

    public boolean checkPhoneAlreadyExists(String phone) {
        return userRepo.findByPhone(phone) == null ? true : false;
    }
}
