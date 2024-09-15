package com.ecommerce.cozashop.service;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.cozashop.model.Role;
import com.ecommerce.cozashop.model.User;
import com.ecommerce.cozashop.repository.UserRepo;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private RoleService roleService;


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = getUserByEmail(username);
		if(user == null) {
			throw new UsernameNotFoundException("user not found");
		}
		if(!user.isEnabled()) {
		}
		return user;
	}


	public List<User> findAllUser(){
		return userRepo.findAll();
	}

	public boolean checkEmailAlreadyExists(String email) {
		boolean find = true;
		User user = userRepo.findByEmail(email);
		if(isNull(user)) {
			find = false;
		}
		return find;
	}

	public User getUserByEmail(String email) {
		return userRepo.findByEmail(email);
	}
	
	public void updateUser(User user) {
		 userRepo.save(user);
	}

	public Long getIdUserByEmail(String email) {
		return userRepo.findIdByEmail(email);
	}

	public User getUserById(Long id) {
		User user = null;
		Optional<User> optusr = userRepo.findById(id);
		if(optusr.isPresent()) {
			user = optusr.get();
		}
		return user;
	}

	public void registerAccount(User user) {
		Role role = roleService.getRolebyName("USER");;
		String encodeStr = encoder.encode(user.getPassword());
		user.setPassword(encodeStr);
		user.setRole(role);
		user.setAccountNonExpired(true);
		user.setAccountNonLocked(true);
		user.setCredentialsNonExpired(true);
		user.setEnabled(false);
		userRepo.save(user);
	}


	public void updatePassword(String newPassword ,String email) {
		User user = getUserByEmail(email);
		user.setPassword(encoder.encode(newPassword));
		userRepo.save(user);
	}


	public void updateAccount(User user) {
		userRepo.save(user);
	}
	
	public User findById(Integer id) {
		return userRepo.findById(id);
	}

	public String getPasswordByEmail(String email) {
		return userRepo.getPassword(email);
	}

	public List<User> show() {
		return userRepo.findAll();
	}

	public boolean checkPhoneAlreadyExists(String phone) {
		User user = userRepo.findByPhone(phone);
		boolean present = false;
		if(nonNull(user)) {
			present = true;
		}
		return present;
	}
	
	public void deleteUser(User user) {
		userRepo.delete(user);	
	}

}
