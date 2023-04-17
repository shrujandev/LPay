package net.javaguides.sms.controller;

import java.util.List;

import net.javaguides.sms.repository.UserRepository;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
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

import net.javaguides.sms.entity.RegistrationReqBody;

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
    @Autowired
    private UserRepository userRepository;

	public UserController(UserService userService, UserRepository userRepository) {
		super();
		this.userService = userService;
        this.userRepository = userRepository;
	}
	@PostMapping("/getusrcreds")
	public String getUserCredentials(@RequestBody JSONObject request) throws JsonProcessingException {
		String username = (String)request.get("username");
		System.out.println(username);
        User user = userRepository.findByPhone(username);
        ObjectMapper objectMapper = new ObjectMapper();
        String response = objectMapper.writeValueAsString(user);
        return response;
	}

    @PostMapping("/saveUser")
    public String saveUer(@RequestBody String userSaveRequest) throws JsonProcessingException{
        System.out.println("Got request to save user");
        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(userSaveRequest, User.class);
        userService.saveUser(user);
        System.out.println("User saved");
        return "User saved";
    }

	
	
//	@PostMapping("/signup")
//	public ModelAndView saveUser(@ModelAttribute("user") User user) throws JsonMappingException, JsonProcessingException{
//
//		RestTemplate myRest = new RestTemplate();
//        HttpServletRequest request1 = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        HttpSession session = request1.getSession(false);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
////        JSONObject reqBody = new JSONObject();
////        reqBody.put("message", "Please verify account!");
//        RegistrationReqBody reqBody = new RegistrationReqBody();
//        reqBody.setAccNumber(user.getAccountId());
//        reqBody.setBankName(user.getBankName());
//        reqBody.setPhoneNumber(user.getPhone());
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String requestBodyJson = objectMapper.writeValueAsString(reqBody);
//        HttpEntity<String> request = new HttpEntity<String>(requestBodyJson, headers);
//
//
//        ResponseEntity<String> respEntity = myRest.postForEntity("http://localhost:8080/UPI/RegisterAccount", request, String.class);
//        if (respEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {
//            // Convert the JSON string to a Java object using Jackson
//        	String responseBody = respEntity.getBody();
//            ObjectMapper mapper = new ObjectMapper();
//            // Use the NPCIAccount object as needed
//        } else {
//            // Handle the error response
//            String errorMessage = respEntity.getHeaders().getFirst("Error");
//            System.out.println("Error message: 500" + errorMessage);
//        }
//
//
//
//	    userService.saveUser(user);
//		System.out.println("User is " + user.getFirstName() + " Bank is " + user.getBankName());
//	    ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("redirect:/start_page.html");
//	    return modelAndView;
//	}
	

	
	

	
	

}


