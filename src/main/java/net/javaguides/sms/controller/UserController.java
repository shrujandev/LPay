package net.javaguides.sms.controller;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.javaguides.sms.entity.Student;
import net.javaguides.sms.entity.User;
import net.javaguides.sms.service.UserService;


@Controller
public class UserController {
	private UserService userService;

	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}

	
	// handler method to handle list students and return model and view
	@GetMapping("/login")
	public String startPage(Model model) {
		User user = new User();
		model.addAttribute("user",user);
		return "start_page";
	}
	
	@GetMapping("/")
	public String home() {
		return "home_page";
	}
	
//	@PostMapping("/login")
//	public String login(@ModelAttribute("user") User user) {
////		User existingUser = userService.getUserByPhone(user.getPhone());
//		return "redirect:/new_page/{phone}(phone = user.getPhone())"; 
//	}
	
//	@GetMapping("/new_page/{phone}")
//	public String newpage(@PathVariable String phone, Model model) {
//		model.addAttribute("user",userService.getUserByPhone(phone));
//		return "new_page";
//	}
//	
	
	@GetMapping("/signup")
	public String createAccount(Model model) {
		// create user object to hold student form data
		User user = new User();
		model.addAttribute("user",user);
		return "create_account";
	}
	
	
	
//	@PostMapping()
//	public String saveUser(@ModelAttribute("user") User user) {
//		userService.saveUser(user);
//		return "redirect:/start";
//	}
	
	@PostMapping("/signup")
	public String saveUser(@ModelAttribute("user") User user) {
	    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	    String encodedPassword = passwordEncoder.encode(user.getPassword());
	    user.setPassword(encodedPassword);
	    userService.saveUser(user);
	     
	    return "redirect:/start";
	}
	
	@GetMapping("/loggedIn")
	public String listUsers(Model model) {
	    List<User> listUsers = userService.findAllUsers();
	    model.addAttribute("listUsers", listUsers);
	    return "logged_in";
	}
	
	


	

}
