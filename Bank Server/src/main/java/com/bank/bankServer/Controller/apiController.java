package com.bank.bankServer.Controller;

import java.util.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.*;
import com.bank.bankServer.Repository.BankRepo;
import com.bank.bankServer.Repository.TransactionRepo;
import com.bank.bankServer.Entity.account;
import com.bank.bankServer.Entity.accBal;
import com.bank.bankServer.Entity.transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.bank.bankServer.Response.ResponseHandler;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.*;




@RestController
public class apiController {

    @Data
    @NoArgsConstructor
    private static class phAccount {

        public String accNumber;
        public String phoneNumber;

    }
    

    @Data
    @NoArgsConstructor
    private static class balAccount {
        
        public String accNumber;
    }

    @Autowired
    private BankRepo bankRepo;

    // @Autowired
    // private TransactionRepo tranRepo;

    @GetMapping(value="/")
    public String getPage() {
        return "Welcome";
    }

    @GetMapping(value="/accounts")
    public List<account> getUsers() {
        return bankRepo.findAll();
    }

    @PostMapping(value="/save")
    public String saveAccount(@RequestBody account acc) {
        bankRepo.save(acc);
        return acc.getname();

    }

    @PostMapping(value = "verify")
    public ResponseEntity<String> verify(@RequestBody phAccount acc) {
        try{
            account findAccount = bankRepo.findById(Long.parseLong(acc.getAccNumber())).get();
            System.out.println(acc.getAccNumber());
            if (findAccount != null) {
                System.out.println("first return");
                return new ResponseEntity<String>("Verified", HttpStatus.OK);
            }
            else {
                
                System.out.println("second return");
                return new ResponseEntity<String>("Invalid", HttpStatus.OK);
            }
        } catch (Exception e) {
            System.out.println("error"+e);
            return new ResponseEntity<String>("Invalid", HttpStatus.OK);
        }
    }
    
    @PostMapping(value="/checkBalance")
    public  ResponseEntity<String> balance(@RequestBody balAccount bal) {
        String id = bal.getAccNumber();
        System.out.println(id);
        account updatedAcc = bankRepo.findById(Long.parseLong(id)).get();
        return new ResponseEntity<String>(String.valueOf(updatedAcc.getBalance()), HttpStatus.OK);

    }

    @GetMapping(value="balanceCheck/{id}/{amount}")
    public ResponseEntity<Object> updateAccount(@PathVariable String id,@PathVariable float amount) {
        try{
            account checkAcc = bankRepo.findById(Long.parseLong(id)).get();
            float balance = checkAcc.getBalance();
            if(balance >= amount){
                return ResponseHandler.generateResponse("true",HttpStatus.OK,checkAcc);
            }
            else{
                return ResponseHandler.generateResponse("false",HttpStatus.OK,checkAcc);
            }

        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
        }

    }
    

    // @PostMapping(value = "/entry")
    // public
    



    @PutMapping(value="debit/{id}/{amount}")
    public String debit(@PathVariable String id, @PathVariable float amount, @RequestBody account acc) {
        account debitAccount = bankRepo.findById(Long.parseLong(id)).get();
        debitAccount.setaccountNo(acc.getaccountNo());
        debitAccount.setId(acc.getId());
        debitAccount.setname(acc.getname());
        debitAccount.setBalance(debitAccount.getBalance() - amount);
        bankRepo.save(debitAccount);
        return "success";
    }

    @PutMapping(value="credit/{id}/{amount}")
    public String credit(@PathVariable String id, @PathVariable float amount, @RequestBody account acc) {
        account debitAccount = bankRepo.findById(Long.parseLong(id)).get();
        debitAccount.setaccountNo(acc.getaccountNo());
        debitAccount.setId(acc.getId());
        debitAccount.setname(acc.getname());
        debitAccount.setBalance(debitAccount.getBalance() + amount);
        bankRepo.save(debitAccount);
        return "success";
    }
    
}
