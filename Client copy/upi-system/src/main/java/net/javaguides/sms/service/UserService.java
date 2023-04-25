package net.javaguides.sms.service;

import java.util.List;

import net.javaguides.sms.entity.User;

public interface UserService {
	
	User saveUser(User user);
	User getUserById(Long id);
//	User getUserByPhone(String phone);
	List<User> findAllUsers();
}
