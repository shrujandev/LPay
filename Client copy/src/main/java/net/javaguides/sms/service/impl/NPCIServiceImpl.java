package net.javaguides.sms.service.impl;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import net.javaguides.sms.entity.BankAccount;
import net.javaguides.sms.entity.BankServer;
import net.javaguides.sms.entity.BankServerEntity;
import net.javaguides.sms.entity.NPCIAccount;
import net.javaguides.sms.repository.BankAccountRepository;
import net.javaguides.sms.repository.BankServersRepository;
import net.javaguides.sms.repository.NPCIAccountRepository;
import net.javaguides.sms.service.NPCIService;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



@Service
public class NPCIServiceImpl implements NPCIService {
    private final NPCIAccountRepository NPCIRep;
    private final BankAccountRepository BankRep;
    private final BankServersRepository ServerRep;

    @Autowired
    public NPCIServiceImpl(final NPCIAccountRepository NPCIRep, final BankAccountRepository BankRep, final BankServersRepository ServerRep){
        this.NPCIRep = NPCIRep;
        this.BankRep = BankRep;
        this.ServerRep = ServerRep;
       
    }
 
    //Custom errors
    public static class AccountExistsException extends RuntimeException{
        public AccountExistsException(){
            super("Account associated with phone number already exists");
        }
    }

    public static class BankServerVerificationException extends RuntimeException{
        public BankServerVerificationException(){
            super("Bank server could not verify UPI Connection request");
        }
    }

    //helper methods
    private BankServer getBankServer(String bankName){
        BankServerEntity resultVal = ServerRep.findBybankName(bankName);
        if(resultVal == null){
            return null;
        }else{
            return BankServerEntityToBankAccount(resultVal);
        }
    }

    private BankServer BankServerEntityToBankAccount(BankServerEntity bankServ){
        return new BankServer(bankServ.getBankName(),bankServ.getBankURL(),bankServ.getBankAPIKey());
        
    }

//methods

    //register
    public NPCIAccount registerAccount(String phoneNumber, String accountNumber, String bankName) throws RuntimeException{
        
        NPCIAccount accounExistsCheck = NPCIRep.findByPhoneNumber(phoneNumber);
        if(accounExistsCheck != null){
            throw new AccountExistsException();
        }

        System.out.println(bankName);
        BankServer bankServer = getBankServer(bankName);

        RestTemplate myRest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject reqBody = new JSONObject();
        reqBody.put("accNumber", accountNumber);
        reqBody.put("phoneNumber", phoneNumber);

        HttpEntity<String> request = new HttpEntity<String>(reqBody.toString(), headers);
        String url = bankServer.getVerifyAccountURL();
        System.out.println(url);
        ResponseEntity<String> respEntity = myRest.postForEntity(url, request, String.class);
        if(respEntity.getStatusCode() == HttpStatusCode.valueOf(200)){
        	
        	System.out.println("Response received");
        	System.out.println(respEntity.getBody());
			JSONParser parser = new JSONParser();
			try {
		        // code that may throw a ParseException
				JSONObject JSONresp = (JSONObject) parser.parse(respEntity.getBody());
				System.out.println(JSONresp.get("Authorization"));
				if(JSONresp.get("Authorization").equals("Verified")) {
					System.out.println("Account is verified.");
					NPCIAccount newAcc = new NPCIAccount(phoneNumber + "@UPI",phoneNumber,bankName,accountNumber);
		            NPCIAccount resultAcc = this.NPCIRep.save(newAcc);
		            BankAccount newBankAccount = new BankAccount(resultAcc.getUpiId(), resultAcc.getDefaultBankAccNumber(), resultAcc.getDefaultBank());
		            this.BankRep.save(newBankAccount);
		            return resultAcc;
				}
				else {
					throw new BankServerVerificationException();
				}
		    } catch (ParseException e) {
		        // handle the exception here
		    	System.out.println(e);
		    	throw new BankServerVerificationException();
		    }
        }
        else {
        	throw new BankServerVerificationException();
        }
	}
        

    //get all available banks
    public List<String> getBanksList(){
    	
        List<BankServerEntity> serversList = this.ServerRep.findAll();
        ArrayList<String> BankNamesList = new ArrayList<>();
        for(BankServerEntity s: serversList){
            BankNamesList.add(s.getBankName());
        }
        System.out.println("Was able to retrieve list from bank");

        return BankNamesList;
    }



    
}
