package net.javaguides.sms.controller;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



import jakarta.servlet.http.HttpServletResponse;

import net.javaguides.sms.entity.NPCIAccount;
import net.javaguides.sms.entity.requestmessage;
import net.javaguides.sms.service.NPCIService;
import net.javaguides.sms.service.impl.NPCIServiceImpl.AccountExistsException;
import net.javaguides.sms.service.impl.NPCIServiceImpl.BankServerVerificationException;


@RestController
public class BankController {
	 @PostMapping(
             value = "/{bankurl}/verifyAccount", consumes = "application/json", produces = "application/json")
   public JSONObject showmessage(@PathVariable String bankurl,@RequestBody requestmessage message , HttpServletResponse response) {
       
       JSONObject obj = new JSONObject();
		obj.put("Authorization","Verified");
		response.setHeader("Title", "This is the header");
		System.out.println("Message received : "+message.getMessage());
		return obj;
  
  }

}
