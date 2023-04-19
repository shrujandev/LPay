package net.javaguides.sms.controller;

//import java.util.ArrayList;
//import java.util.List;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.server.ServerErrorException;


import jakarta.servlet.http.HttpServletResponse;
import net.javaguides.sms.entity.NPCIAccount;
import net.javaguides.sms.entity.RegistrationReqBody;
import net.javaguides.sms.entity.requestmessage;
import net.javaguides.sms.service.NPCIService;
import net.javaguides.sms.service.impl.NPCIServiceImpl.AccountExistsException;
import net.javaguides.sms.service.impl.NPCIServiceImpl.BankServerVerificationException;


@RestController
public class NPCIController {
    
    private static class BankNames{
        private List<String> NamesList;

		public List<String> getNamesList() {
			return NamesList;
		}

		public void setNamesList(List<String> namesList) {
			NamesList = namesList;
		}
        
    }

    private final NPCIService nPCIService;

    @Autowired
    public NPCIController(NPCIService myService){
        this.nPCIService = myService;
    }

    @PostMapping(value = "/UPI/RegisterAccount", consumes = "application/json", produces = "application/json")
    public ResponseEntity<NPCIAccount> registerAccount(
        @RequestBody final RegistrationReqBody reqBody){
            System.out.println("Received" + "Params: "+reqBody.getPhoneNumber()+" "+ reqBody.getAccNumber()+" "+ reqBody.getBankName());

            NPCIAccount result=null;

            try{
                result = nPCIService.registerAccount(reqBody.getPhoneNumber(), reqBody.getAccNumber(), reqBody.getBankName());
            }catch(AccountExistsException er){
                HttpHeaders returnHeaders = new HttpHeaders();
                returnHeaders.setContentType(MediaType.APPLICATION_JSON);
                returnHeaders.add("Error", er.getMessage());
                return new ResponseEntity<NPCIAccount>(null, returnHeaders, HttpStatus.BAD_REQUEST);
            }catch(BankServerVerificationException er){
                HttpHeaders returnHeaders = new HttpHeaders();
                returnHeaders.setContentType(MediaType.APPLICATION_JSON);
                returnHeaders.add("Error", er.getMessage());
                return new ResponseEntity<NPCIAccount>(null, returnHeaders, HttpStatus.BAD_REQUEST);
            }
            
            HttpHeaders returnHeaders = new HttpHeaders();
            returnHeaders.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<NPCIAccount> response = new ResponseEntity<NPCIAccount>(result, returnHeaders, HttpStatus.CREATED);
            return response;
    }

    @PostMapping(
              value = "/GetBanksList", consumes = "application/json", produces = "application/json")
    public JSONObject showmessage(@RequestBody requestmessage message , HttpServletResponse response) {
    	System.out.println("Message received i am in post mapping of npcicontroller: "+message.getMessage());
    	BankNames result = new BankNames();
        result.setNamesList(this.nPCIService.getBanksList());
        JSONArray banks = new JSONArray();
        for (int i=0; i < result.getNamesList().size(); i++) {
                banks.add(result.getNamesList().get(i));
        }
        
        JSONObject obj = new JSONObject();
		obj.put("banks", banks);
		

	
       
		
		
		
		
//		banks.add("SBI");
//		banks.add("ICICI");
//		banks.add("HDFC");
		
//		requestmessage obj = new requestmessage();
//		obj.setMessage("How is your life guys?");
	    return obj;

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
//    @GetMapping(value = "/UPI/GetBanksList")
//    public ResponseEntity<BankNames> GetBanksList(){
//        BankNames result = new BankNames();
//        result.setNamesList(this.nPCIService.getBanksList());
//
//        HttpHeaders returnHeaders = new HttpHeaders();
//        returnHeaders.setContentType(MediaType.APPLICATION_JSON);
//        ResponseEntity<BankNames> response = new ResponseEntity<BankNames>(result, returnHeaders, HttpStatus.OK);
//
//        return response;
//    }
    
    
    
    
    @PostMapping(
			  value = "/bankslist", consumes = "application/json", produces = "application/json")
			public JSONObject showmessage2(@RequestBody requestmessage message , HttpServletResponse response) {
//				response.setHeader("Title", "This is the header");
//				System.out.println("Message received : "+message.getMessage());
//				JSONArray banks = new JSONArray();
//				banks.add("SBI");
//				banks.add("ICICI");
//				banks.add("HDFC");
//				JSONObject obj = new JSONObject();
//				obj.put("banks", banks);
//////				requestmessage obj = new requestmessage();
//////				obj.setMessage("How is your life guys?");
//			    return obj;
//			    
			    
			    
			    
			    System.out.println("Message received i am in post mapping of npcicontroller: "+message.getMessage());
			    response.setHeader("Title", "This is the header");
			    BankNames result = new BankNames();
		        result.setNamesList(this.nPCIService.getBanksList());
		        JSONArray banks = new JSONArray();
		        for (int i=0; i < result.getNamesList().size(); i++) {
		                banks.add(result.getNamesList().get(i));
		        }
		        
		        JSONObject obj = new JSONObject();
				obj.put("banks", banks);
			    return obj;
			}
    
//    
//    @PostMapping(
//			value = "/UPI/Transact", consumes = "application/json", produces = "application/json")
//	public ResponseEntity<MyTransaction> validateTransaction(
//	    @RequestBody final validateTransactionReqBody reqBody){
//	        MyTransaction result;
//	        try{
//	            result = this.nPCIService.validateTransaction(reqBody.getSenderUPI(), reqBody.getSenderBankAcc(), reqBody.getReceiverUPI(), Double.valueOf(reqBody.getAmount()));
//	        }catch(UPIDoesNotExistException er){
//	            return new ResponseEntity<MyTransaction>(null, getErrorHeader(er), HttpStatus.BAD_REQUEST);
//	        }catch(InsufficientBalanceException er){
//	            return new ResponseEntity<MyTransaction>(null, getErrorHeader(er), HttpStatus.BAD_REQUEST);
//	        }
//	
//	        HttpHeaders returnHeaders = new HttpHeaders();
//	        returnHeaders.setContentType(MediaType.APPLICATION_JSON);
//	        ResponseEntity<MyTransaction> response = new ResponseEntity<MyTransaction>(result, returnHeaders, HttpStatus.OK);
//	
//	        return response;
//	    }
//    
//    
//    @PostMapping(value = "/UPI/GetBalance")
//    public ResponseEntity<String> getBalance(
//        @RequestBody final getBalanceReqBody reqBody){
//            Double result;
//            try{
//                result = this.nPCIService.getBalance(reqBody.getUPIId(), reqBody.getAccNumber());
//            }catch(UPIDoesNotExistException er){
//                return new ResponseEntity<String>(null, getErrorHeader(er), HttpStatus.BAD_REQUEST);
//            }catch(ServerErrorException er){
//                return new ResponseEntity<String>(null, getErrorHeader(er), HttpStatus.INTERNAL_SERVER_ERROR);
//            }catch(ResourceAccessException er){
//                return new ResponseEntity<String>(null, getErrorHeader(er), HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//
//            ResponseEntity<String> response = new ResponseEntity<String>(result.toString(), HttpStatus.OK);
//            return response;
//        }

}
