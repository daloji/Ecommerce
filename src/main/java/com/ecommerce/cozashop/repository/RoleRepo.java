package com.ecommerce.cozashop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecommerce.cozashop.model.Role;

@Repository
public interface RoleRepo extends JpaRepository<Role, Integer> {

	@Query("select role from Role role where role.role_name=?1")
	Role findRoleByRoleName(String name);
}
