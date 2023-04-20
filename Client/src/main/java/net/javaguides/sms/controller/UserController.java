package net.javaguides.sms.controller;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.boot.Banner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import net.javaguides.sms.entity.User;
import net.javaguides.sms.entity.requestmessage;
import net.javaguides.sms.service.UserService;
//import net.minidev.json.JSONObject;
import org.json.simple.JSONObject;
import reactor.core.publisher.Mono;
import java.lang.String;
import java.util.UUID;

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

class MyTransaction {

	public UUID transactionId;


	public String senderUPI;


	public String senderBankAcc;


	public String receiverUPI;


	public String receiverBankAcc;


	public String status;


	public double amount;

	public MyTransaction() {
	}

	public MyTransaction(UUID transactionId, String senderUPI, String senderBankAcc, String receiverUPI, String receiverBankAcc, String status, double amount) {
		this.transactionId = transactionId;
		this.senderUPI = senderUPI;
		this.senderBankAcc = senderBankAcc;
		this.receiverUPI = receiverUPI;
		this.receiverBankAcc = receiverBankAcc;
		this.status = status;
		this.amount = amount;
	}

	public void setTransactionId(String id){
		this.transactionId = UUID.fromString(id);
	}

	public UUID getTransactionId() {
		return transactionId;
	}


	public String getSenderUPI() {
		return senderUPI;
	}

	public void setSenderUPI(String senderUPI) {
		this.senderUPI = senderUPI;
	}

	public String getSenderBankAcc() {
		return senderBankAcc;
	}

	public void setSenderBankAcc(String senderBankAcc) {
		this.senderBankAcc = senderBankAcc;
	}

	public String getReceiverUPI() {
		return receiverUPI;
	}

	public void setReceiverUPI(String receiverUPI) {
		this.receiverUPI = receiverUPI;
	}

	public String getReceiverBankAcc() {
		return receiverBankAcc;
	}

	public void setReceiverBankAcc(String receiverBankAcc) {
		this.receiverBankAcc = receiverBankAcc;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
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
		User checkUser = User.getCurUserInstance();
		if(checkUser != null){
			modelAndView.setViewName("redirect:/");
			return modelAndView;
		}
		modelAndView.setViewName("start_page.html");

		class LoginDets{
			public String username;
			public String password;

			public LoginDets(){

			}

			public LoginDets(String username, String password) {
				this.username = username;
				this.password = password;
			}

			public String getUsername() {
				return username;
			}

			public void setUsername(String username) {
				this.username = username;
			}

			public String getPassword() {
				return password;
			}

			public void setPassword(String password) {
				this.password = password;
			}
		}
		LoginDets loginDets = new LoginDets();
		model.addAttribute("loginCreds", loginDets);
		return modelAndView;
	}
	@PostMapping("/login")
	public ModelAndView login(@ModelAttribute("username") String username, @ModelAttribute("password") String password) throws JsonProcessingException {
		ModelAndView modelAndView = new ModelAndView();
		JSONObject usrobj = new JSONObject();
		usrobj.put("username", username);

		RestTemplate myRest = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<String>(usrobj.toString(), headers);
		ResponseEntity<String> respEntity = myRest.postForEntity(upi_server + "/getusrcreds", request, String.class);
		if(respEntity.getStatusCode() == HttpStatusCode.valueOf(200)){
			ObjectMapper objectMapper = new ObjectMapper();
			User user = objectMapper.readValue(respEntity.getBody(), User.class);
			if(user == null){
				modelAndView.setViewName("redirect:/login?error");
				return modelAndView;
			}
			if(!user.getPassword().equals(password)){
				modelAndView.setViewName("redirect:/login?error");
				return modelAndView;
			}
			User.authoriseUser();
			User loggedUser = User.getCurUserInstance();
			loggedUser.setId(user.getId());
			loggedUser.setUpiId(user.getUpiId());
			loggedUser.setAccountId(user.getAccountId());
			loggedUser.setPassword(user.getPassword());
			loggedUser.setEmail(user.getEmail());
			loggedUser.setBankName(user.getBankName());
			loggedUser.setPhone(user.getPhone());
			loggedUser.setFirstName(user.getFirstName());
			loggedUser.setLastName(user.getLastName());
			System.out.println("Successfully Logged in!");
			modelAndView.setViewName("redirect:/");
			return modelAndView;
		}
		else{
			System.out.println(respEntity.getStatusCode());
			modelAndView.setViewName("redirect:/login?error");
			return modelAndView;
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
		ModelAndView modelAndView = new ModelAndView();
		User user = User.getCurUserInstance();
		System.out.println("Checking user");
		if(user == null){
			modelAndView.setViewName("redirect:/login");
			return modelAndView;
		}
		String name = "hii";
		model.addAttribute("User", user);

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
		ModelAndView modelAndView = new ModelAndView();
		User checkUser = User.getCurUserInstance();
		if(checkUser != null){
			modelAndView.setViewName("redirect:/");
			return modelAndView;
		}
		// create user object to hold student form data
		modelAndView.setViewName("create_account.html");
		User user = User.getTemporaryUserInstance();
		model.addAttribute("user",user);


		RestTemplate myRest = new RestTemplate();
		HttpServletRequest request1 = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		HttpSession session = request1.getSession(false);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		JSONObject reqBody = new JSONObject();
		reqBody.put("message", "Please send the bank accounts!");
		HttpEntity<String> request = new HttpEntity<String>(reqBody.toString(), headers);
		ResponseEntity<String> respEntity = myRest.postForEntity("http://localhost" +
				":7050/UPI/GetBanksList", request, String.class);
		if(respEntity.getStatusCode() == HttpStatusCode.valueOf(200)){

			System.out.println("Response received");
			System.out.println(respEntity.getBody());
			JSONParser parser = new JSONParser();
			JSONObject JSONresp = (JSONObject) parser.parse(respEntity.getBody());
			List<String> banks = (JSONArray) JSONresp.get("banks");
			System.out.println("Banks string is - " + banks);
			model.addAttribute("banks", banks);

		}else{
			System.out.println(respEntity.getStatusCode());
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


		ResponseEntity<String> respEntity = myRest.postForEntity("http://localhost:7050/UPI/RegisterAccount", request, String.class);
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



		user.setPassword(user.getPassword());
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
		modelAndView.setViewName("redirect:/login");
		User.resetTemporaryUserInstance();
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

	@GetMapping("/logout")
	public ModelAndView logout(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("redirect:/login?logout");
		User.resetCurUserInstance();
		return modelAndView;
	}

	@GetMapping("/demoLogin")
	public ModelAndView demmoLogin(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("login.html");
		return modelAndView;
	}
	private static class GetBalanceReqBody{
		String upiId;
		String AccNumber;

		public GetBalanceReqBody(String upiId, String accNumber) {
			this.upiId = upiId;
			AccNumber = accNumber;
		}

		public GetBalanceReqBody(){}

		public String getUpiId() {
			return upiId;
		}

		public void setUpiId(String upiId) {
			this.upiId = upiId;
		}

		public String getAccNumber() {
			return AccNumber;
		}

		public void setAccNumber(String accNumber) {
			AccNumber = accNumber;
		}
	}

	@PostMapping("/balance")
	public ModelAndView getbalance(Model model) throws ParseException, JsonProcessingException {
		// create user object to hold student form data
		ModelAndView modelAndView = new ModelAndView();
		User user = User.getCurUserInstance();
		System.out.println("Checking user");
		if(user == null){
			modelAndView.setViewName("redirect:/login");
			return modelAndView;
		}

		RestTemplate myRest = new RestTemplate();
		HttpServletRequest request1 = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		HttpSession session = request1.getSession(false);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		GetBalanceReqBody reqBody = new GetBalanceReqBody();
		reqBody.setAccNumber(user.getAccountId());
		reqBody.setUpiId(user.getUpiId());
		ObjectMapper objectMapper1 = new ObjectMapper();
		String requestBody = objectMapper1.writeValueAsString(reqBody);
		HttpEntity<String> request = new HttpEntity<String>(requestBody, headers);
		System.out.println("Sending request to check balance");
		ResponseEntity<String> respEntity = myRest.postForEntity("http://localhost:7050/UPI/GetBalance", request, String.class);
		if(respEntity.getStatusCode() == HttpStatusCode.valueOf(200)){

			System.out.println("Response received");
			System.out.println(respEntity.getBody());
			ObjectMapper objectMapper = new ObjectMapper();
			String responseBody = respEntity.getBody();
			String result = objectMapper.readValue(responseBody, String.class);
			System.out.println("Balance is:" + result);
			model.addAttribute("balance", result);
			modelAndView.setViewName("redirect:/?balance=" + result);
			return modelAndView;

		}else{
			System.out.println(respEntity.getBody());
			System.out.println("Error in getting banks!");
			modelAndView.setViewName("redirect:/?error");
			return modelAndView;
		}


	}

	private static class validateTransactionReqBody{
		String senderUPI;
		String receiverUPI;
		String senderBankAcc;
		String amount;

		public validateTransactionReqBody() {
		}
		public validateTransactionReqBody(String senderUPI, String receiverUPI, String senderBankAcc, String amount) {
			this.senderUPI = senderUPI;
			this.receiverUPI = receiverUPI;
			this.senderBankAcc = senderBankAcc;
			this.amount = amount;
		}

		public String getSenderUPI() {
			return senderUPI;
		}

		public void setSenderUPI(String senderUPI) {
			this.senderUPI = senderUPI;
		}

		public String getReceiverUPI() {
			return receiverUPI;
		}

		public void setReceiverUPI(String receiverUPI) {
			this.receiverUPI = receiverUPI;
		}

		public String getSenderBankAcc() {
			return senderBankAcc;
		}

		public void setSenderBankAcc(String senderBankAcc) {
			this.senderBankAcc = senderBankAcc;
		}

		public String getAmount() {
			return amount;
		}

		public void setAmount(String amount) {
			this.amount = amount;
		}
	}

	@GetMapping("/payment")
	public ModelAndView makePayment(Model model) throws ParseException {
		// create user object to hold student form data
		ModelAndView modelAndView = new ModelAndView();
		System.out.println("In get mapping of payment");
		modelAndView.setViewName("make_payment.html");
		validateTransactionReqBody myreq = new validateTransactionReqBody();
		model.addAttribute("payment",myreq);
		return modelAndView;
	}


	@PostMapping("/paymentProcess")
	public ModelAndView paymentprocess(@ModelAttribute("payment") validateTransactionReqBody reqBody,Model model, RedirectAttributes redirectAttrs) throws JsonMappingException, JsonProcessingException{
		reqBody.setSenderUPI(User.getCurUserInstance().getUpiId());
		reqBody.setSenderBankAcc(User.getCurUserInstance().getAccountId());
		RestTemplate myRest = new RestTemplate();
		HttpServletRequest request1 = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		HttpSession session = request1.getSession(false);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

//        JSONObject reqBody = new JSONObject();
//        reqBody.put("message", "Please verify account!");


		ObjectMapper objectMapper = new ObjectMapper();
		String requestBodyJson = objectMapper.writeValueAsString(reqBody);
		HttpEntity<String> request = new HttpEntity<String>(requestBodyJson, headers);

		System.out.println("sending post request");
		ResponseEntity<MyTransaction> respEntity = myRest.postForEntity("http://localhost:7050/UPI/Transact", request, MyTransaction.class);
		System.out.println("Response from server is " + respEntity.getStatusCode());
		if (respEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {
			// Convert the JSON string to a Java object using Jackson
			if(respEntity.getBody() == null){
				String errorMessage = respEntity.getHeaders().getFirst("Error");
				System.out.println("Error message: 500" + errorMessage);
				ModelAndView modelAndView = new ModelAndView();
				modelAndView.setViewName("redirect:/?balanceError");
				return modelAndView;
			}
			System.out.println("Received response back");
			MyTransaction transaction = respEntity.getBody();
//			ObjectMapper mapper = new ObjectMapper();
//			MyTransaction transaction = mapper.readValue(responseBody, MyTransaction.class);
			System.out.println(transaction.getTransactionId());
			System.out.println(transaction.getAmount());
			System.out.println(transaction.getReceiverUPI());
			ModelAndView modelAndView = new ModelAndView();
			String transactionId = transaction.getTransactionId().toString();
			Double amount = transaction.getAmount();
			String recieverUPI = transaction.getReceiverUPI();
//			model.addAttribute("transactionId", transactionId);
//			model.addAttribute("amount",  amount);
//			model.addAttribute("recieverUPI", recieverUPI);
			model.addAttribute("payment_test","test");
			redirectAttrs.addFlashAttribute("payment", transaction);
//			modelAndView.setViewName("redirect:/?tid="+transactionId+"&am="+amount+"&upi="+recieverUPI);
			modelAndView.setViewName("redirect:/?transaction");

			return modelAndView;
			// Use the NPCIAccount object as needed
		} else {
			// Handle the error response
			String errorMessage = respEntity.getHeaders().getFirst("Error");
			System.out.println("Error message: 500" + errorMessage);
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("redirect:/?error");
			return modelAndView;

		}




	}


}

