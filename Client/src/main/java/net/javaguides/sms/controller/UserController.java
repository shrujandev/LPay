package net.javaguides.sms.controller;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClient.UriSpec;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.DefaultUriBuilderFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import net.javaguides.sms.entity.NPCIAccount;
import net.javaguides.sms.entity.RegistrationReqBody;
import net.javaguides.sms.entity.Student;
import net.javaguides.sms.entity.User;
import net.javaguides.sms.entity.requestmessage;
import net.javaguides.sms.service.UserService;
//import net.minidev.json.JSONObject;
import org.json.simple.JSONObject;
import reactor.core.publisher.Mono;

class MyRequestObject {
	private String message;

	public MyRequestObject() {

	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}

@RestController
public class UserController {
	private UserService userService;
	private final String upi_server = "http://localhost:8070";

	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}


	// handler method to handle list students and return model and view
	@GetMapping("/login")
	public ModelAndView startPage(Model model) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("start_page.html");

		User user = new User();
		model.addAttribute("user",user);
		return modelAndView;
	}

	@GetMapping("/hello")
	public String getEmployeeById() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		RestTemplate myRest = new RestTemplate();
		HttpServletRequest request1 = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		HttpSession session = request1.getSession(false);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		JSONObject reqBody = new JSONObject();
		reqBody.put("message", "This is the message");
		HttpEntity<String> request = new HttpEntity<String>(reqBody.toString(), headers);
		ResponseEntity<String> respEntity = myRest.postForEntity("http://localhost:8080/greeting", request, String.class);
		if(respEntity.getStatusCode() == HttpStatusCode.valueOf(200)){

			System.out.println("Response received");
			System.out.println(respEntity.getBody());
			return "Hiiiiiiii";

		}else{
			System.out.println(respEntity.getBody());
			return "No";
		}

	}

//	@PostMapping(
//			  value = "/greeting", consumes = "application/json", produces = "application/json")
//			public JSONObject showmessage(@RequestBody requestmessage message , HttpServletResponse response) {
//				response.setHeader("Title", "This is the header");
//				System.out.println("Message received : "+message.getMessage());
//				JSONArray banks = new JSONArray();
//				banks.add("SBI");
//				banks.add("ICICI");
//				banks.add("HDFC");
//				JSONObject obj = new JSONObject();
//				obj.put("banks", banks);
////				requestmessage obj = new requestmessage();
////				obj.setMessage("How is your life guys?");
//			    return obj;
//			}

	@GetMapping("/")
	public ModelAndView home(ModelMap model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName(); //get logged in username
		model.addAttribute("username", name);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("home_page.html");
		WebClient.Builder webClientBuilder = WebClient.builder();
		Mono<String> res = webClientBuilder.build()
				.get()
				.uri("http://localhost:8080/hello")
				.retrieve()
				.bodyToMono(String.class);

		res.subscribe(
				value -> System.out.println(value),
				error -> error.printStackTrace(),
				() -> System.out.println("completed without a value"));

		return modelAndView;
	}

	@PostMapping("/test")
	public ResponseEntity<String> receiveMessage(@RequestBody MyRequestObject myRequestObject) {
		String message = myRequestObject.getMessage();
		System.out.println("Message received" + message);
		// Do something with the message
		return ResponseEntity.ok("Received message: " + message);
	}


	@GetMapping("/signup")
	public ModelAndView createAccount(Model model) throws ParseException {
		// create user object to hold student form data
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("create_account.html");
		User user = new User();
		model.addAttribute("user",user);


		RestTemplate myRest = new RestTemplate();
		HttpServletRequest request1 = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		HttpSession session = request1.getSession(false);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		JSONObject reqBody = new JSONObject();
		reqBody.put("message", "Please send the bank accounts!");
		HttpEntity<String> request = new HttpEntity<String>(reqBody.toString(), headers);
		ResponseEntity<String> respEntity = myRest.postForEntity("http://localhost:8080/bankslist", request, String.class);
		if(respEntity.getStatusCode() == HttpStatusCode.valueOf(200)){

			System.out.println("Response received");
			System.out.println(respEntity.getBody());
			JSONParser parser = new JSONParser();
			JSONObject JSONresp = (JSONObject) parser.parse(respEntity.getBody());
			List<String> banks = (JSONArray) JSONresp.get("banks");
			System.out.println("Banks string is - " + banks);
			model.addAttribute("banks", banks);

		}else{
			System.out.println(respEntity.getBody());
			System.out.println("Error in getting banks!");
		}

		return modelAndView;
	}


//	public ModelAndView createAccount(Model model) throws ParseException {
//		// create user object to hold student form data
//		ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("create_account.html");
//		User user = new User();
//		model.addAttribute("user",user);
//
//
//		RestTemplate myRest = new RestTemplate();
//        HttpServletRequest request1 = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        HttpSession session = request1.getSession(false);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        JSONObject reqBody = new JSONObject();
//        reqBody.put("message", "Please send the bank accounts!");
//        HttpEntity<String> request = new HttpEntity<String>(reqBody.toString(), headers);
//        ResponseEntity<String> respEntity = myRest.postForEntity("http://localhost:8080/UPI/GetBanksList", request, String.class);
//
//        if(respEntity.getStatusCode() == HttpStatusCode.valueOf(200)){
//
//        	System.out.println("Response received");
//        	System.out.println(respEntity.getBody());
//			JSONParser parser = new JSONParser();
//			JSONObject JSONresp = (JSONObject) parser.parse(respEntity.getBody());
//			List<String> banks = (JSONArray) JSONresp.get("banks");
//			System.out.println("Banks string is - " + banks);
//			model.addAttribute("banks", banks);
//
//        }else{
//        	System.out.println(respEntity.getBody());
//        	System.out.println("Error in getting banks!");
//        }
//
//		return modelAndView;
//	}


	@PostMapping("/signup")
	public ModelAndView saveUser(@ModelAttribute("user") User user) throws JsonMappingException, JsonProcessingException{

		RestTemplate myRest = new RestTemplate();
		HttpServletRequest request1 = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		HttpSession session = request1.getSession(false);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

//        JSONObject reqBody = new JSONObject();
//        reqBody.put("message", "Please verify account!");
		RegistrationReqBody reqBody = new RegistrationReqBody();
		reqBody.setAccNumber(user.getAccountId());
		reqBody.setBankName(user.getBankName());
		reqBody.setPhoneNumber(user.getPhone());

		ObjectMapper objectMapper = new ObjectMapper();
		String requestBodyJson = objectMapper.writeValueAsString(reqBody);
		HttpEntity<String> request = new HttpEntity<String>(requestBodyJson, headers);


		ResponseEntity<String> respEntity = myRest.postForEntity("http://localhost:8080/UPI/RegisterAccount", request, String.class);
		if (respEntity.getStatusCode() == HttpStatusCode.valueOf(201)) {
			// Convert the JSON string to a Java object using Jackson
			String responseBody = respEntity.getBody();
			ObjectMapper mapper = new ObjectMapper();
			NPCIAccount account = mapper.readValue(responseBody, NPCIAccount.class);
			System.out.println(account.getUpiId());
			user.setUpiId(account.getUpiId());
			// Use the NPCIAccount object as needed
		} else {
			// Handle the error response
			String errorMessage = respEntity.getHeaders().getFirst("Error");
			System.out.println("Error message: 500" + errorMessage);
		}


		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
//		userService.saveUser(user);
		String saveUserRequestBody = objectMapper.writeValueAsString(user);
		HttpEntity<String> saveUserRequest = new HttpEntity<String>(saveUserRequestBody, headers);
		System.out.println("Message ready");
		ResponseEntity<String> saveUserResp = myRest.postForEntity(upi_server + "/saveUser", saveUserRequest, String.class);
		if(saveUserResp.getStatusCode() == HttpStatusCode.valueOf(200)){
			System.out.println(saveUserResp.getBody());
		} else {
			System.out.println("Unable to save user");
		}
		System.out.println("User is " + user.getFirstName() + " Bank is " + user.getBankName());
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("redirect:/start_page.html");
		return modelAndView;
	}

	@GetMapping("/loggedIn")
	public ModelAndView listUsers(Model model) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("logged_in.html");
		List<User> listUsers = userService.findAllUsers();
		model.addAttribute("listUsers", listUsers);
		return modelAndView;
	}


}


