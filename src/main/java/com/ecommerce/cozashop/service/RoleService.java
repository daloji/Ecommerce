package com.ecommerce.cozashop.service;

import com.ecommerce.cozashop.model.Role;
import com.ecommerce.cozashop.repository.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepo roleRepo;

    public List<Role> getRoles() {
        return roleRepo.findAll();
    }
}
