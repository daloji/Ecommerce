package com.ecommerce.cozashop.service;

import java.util.List;
import static java.util.Objects.isNull;

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


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = getUserByEmail(username);
		if(user == null) {
			throw new UsernameNotFoundException("user not found");
		}
		return user;
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

	public Long getIdUserByEmail(String email) {
		return userRepo.findIdByEmail(email);
	}

	public void registerAccount(User user) {
		Role role = new Role();

		String encodeStr = encoder.encode(user.getPassword());
		role.setId(1);
		user.setPassword(encodeStr);
		user.setRole(role);
		user.setAccountNonExpired(true);
		user.setAccountNonLocked(true);
		user.setCredentialsNonExpired(true);
		user.setEnabled(true);

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
