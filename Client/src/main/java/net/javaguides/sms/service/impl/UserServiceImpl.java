package net.javaguides.sms.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import net.javaguides.sms.entity.User;
import net.javaguides.sms.repository.UserRepository;
import net.javaguides.sms.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	
private UserRepository userRepository;
	
	public UserServiceImpl(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	@Override
	public User saveUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public List<User> findAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public User getUserById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public User getUserByPhone(String phone) {
//		return userRepository.findByPhone(phone);
//		
//	}
		
	
}
