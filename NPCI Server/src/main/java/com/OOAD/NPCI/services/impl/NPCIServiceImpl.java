package com.OOAD.NPCI.services.impl;
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

import com.OOAD.NPCI.Repositories.BankAccountRepository;
import com.OOAD.NPCI.Repositories.BankServersRepository;
import com.OOAD.NPCI.Repositories.NPCIAccountRepository;
import com.OOAD.NPCI.domain.BankAccount;
import com.OOAD.NPCI.domain.BankServerEntity;
import com.OOAD.NPCI.domain.BankServer;
import com.OOAD.NPCI.domain.NPCIAccount;
import com.OOAD.NPCI.services.NPCIService;

import net.minidev.json.JSONObject;

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
        return BankServer.builder()
        .bankName(bankServ.getBankName())
        .bankAPIKey(bankServ.getBankAPIKey())
        .bankURL(bankServ.getBankURL())
        .build();
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
        //API Key Implementation
        ResponseEntity<String> respEntity = myRest.postForEntity(bankServer.getVerifyAccountURL(), request, String.class);
        if(respEntity.getStatusCode() == HttpStatusCode.valueOf(200) && respEntity.getBody().equals("Verified")){
            NPCIAccount newAcc = NPCIAccount.builder()
            .phoneNumber(phoneNumber)
            .defaultBank(bankName)
            .defaultBankAccNumber(accountNumber)
            .upiId(phoneNumber + "@UPI")
            .build();
            

            NPCIAccount resultAcc = this.NPCIRep.save(newAcc);


            BankAccount newBankAccount = new BankAccount(resultAcc.getUpiId(), resultAcc.getDefaultBankAccNumber(), resultAcc.getDefaultBank());
            this.BankRep.save(newBankAccount);

            return resultAcc;

        }else{
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

        return BankNamesList;
    }



    
}
