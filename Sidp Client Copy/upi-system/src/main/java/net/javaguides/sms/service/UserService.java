package net.javaguides.sms.service;

import java.util.List;

public interface UserService {
	
	User saveUser(User user);
	User getUserById(Long id);
//	User getUserByPhone(String phone);
	List<User> findAllUsers();
}
