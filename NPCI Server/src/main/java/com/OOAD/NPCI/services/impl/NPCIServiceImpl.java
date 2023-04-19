package com.OOAD.NPCI.services.impl;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerErrorException;

import com.OOAD.NPCI.Repositories.BankAccountRepository;
import com.OOAD.NPCI.Repositories.BankServersRepository;
import com.OOAD.NPCI.Repositories.NPCIAccountRepository;
import com.OOAD.NPCI.Repositories.TransactionRepository;
import com.OOAD.NPCI.domain.BankAccount;
import com.OOAD.NPCI.domain.BankServer;
import com.OOAD.NPCI.domain.BankServerEntity;
import com.OOAD.NPCI.domain.MyTransaction;
import com.OOAD.NPCI.domain.NPCIAccount;
import com.OOAD.NPCI.services.NPCIService;

import net.minidev.json.JSONObject;

@Service
public class NPCIServiceImpl implements NPCIService {
    private final NPCIAccountRepository NPCIRep;
    private final BankAccountRepository BankRep;
    private final BankServersRepository ServerRep;
    private final TransactionRepository TransactionRep;

    @Autowired
    public NPCIServiceImpl(final NPCIAccountRepository NPCIRep, final BankAccountRepository BankRep, final BankServersRepository ServerRep, final TransactionRepository TransactionRep){
        this.NPCIRep = NPCIRep;
        this.BankRep = BankRep;
        this.ServerRep = ServerRep;
        this.TransactionRep = TransactionRep;
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

    public static class AccountDoesNotExistException extends RuntimeException{
        public AccountDoesNotExistException(){
            super("UPI-Id does not exist");
        }
    }

    public static class UPIDoesNotExistException extends RuntimeException{
        public UPIDoesNotExistException(String name){
            super(name + " UPI-Id does not exist");
        }
    }

    public static class InsufficientBalanceException extends RuntimeException{
        public InsufficientBalanceException(){
            super("Account does not have suffecient funds");
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

    private ResponseEntity<String> verifyAccount(BankServer bankServer, String phoneNumber, String accountNumber){
        RestTemplate myRest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject reqBody = new JSONObject();
        reqBody.put("accNumber", accountNumber);
        reqBody.put("phoneNumber", phoneNumber);

        HttpEntity<String> request = new HttpEntity<String>(reqBody.toString(), headers);
        //API Key Implementation
        return myRest.postForEntity(bankServer.getVerifyAccountURL(), request, String.class);
    }

    private ResponseEntity<String> requestBankToSendAmount(BankServer bankServer, MyTransaction transaction){
        RestTemplate myRest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject reqBody = new JSONObject();
        reqBody.put("senderAccNumber", transaction.getSenderBankAcc());
        reqBody.put("receiverAccNumber", transaction.getReceiverBankAcc());
        reqBody.put("amount", transaction.getAmount());

        HttpEntity<String> request = new HttpEntity<String>(reqBody.toString(), headers);
        //API Key Implementation
        return myRest.postForEntity(bankServer.getSendAmountURL(), request, String.class);
    }

    private ResponseEntity<String> requestBankToReceiveAmount(BankServer bankServer, MyTransaction transaction){
        RestTemplate myRest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject reqBody = new JSONObject();
        reqBody.put("senderAccNumber", transaction.getSenderBankAcc());
        reqBody.put("receiverAccNumber", transaction.getReceiverBankAcc());
        reqBody.put("amount", transaction.getAmount());

        HttpEntity<String> request = new HttpEntity<String>(reqBody.toString(), headers);
        //API Key Implementation
        return myRest.postForEntity(bankServer.getReceiveAmountURL(), request, String.class);
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

        ResponseEntity<String> respEntity = verifyAccount(bankServer, phoneNumber, accountNumber);

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

    public BankAccount addBankAccount(String UPIid, String accountNumber, String bankName) throws RuntimeException{
        Optional<NPCIAccount> accounExistsCheck = NPCIRep.findById(UPIid);
        if(!accounExistsCheck.isPresent()){
            throw new AccountDoesNotExistException();
        }

        NPCIAccount givenAcc = accounExistsCheck.get();
        String phoneNumber = givenAcc.getPhoneNumber();

        BankServer bankServer = getBankServer(bankName);
        

        ResponseEntity<String> respEntity = verifyAccount(bankServer, phoneNumber, accountNumber);

        if(respEntity.getStatusCode() == HttpStatusCode.valueOf(200) && respEntity.getBody().equals("Verified")){
            BankAccount newAcc = BankAccount.builder()
            .accNumber(accountNumber)
            .bank(bankName)
            .upiId(UPIid)
            .build();

            BankAccount result = this.BankRep.save(newAcc);

            return result;
        }else{
            throw new BankServerVerificationException();
        }
    }

    public double getBalance(String UPI, String AccNumber) throws RuntimeException{
        BankAccount bankAcc = this.BankRep.findByUpiIdAndAccNumber(UPI, AccNumber);
        if(bankAcc == null){
            throw new UPIDoesNotExistException("Given");
        }

        BankServer bankServer = getBankServer(bankAcc.getBank());

        RestTemplate myRest = new RestTemplateBuilder().setConnectTimeout(Duration.ofMillis(10000)).setReadTimeout(Duration.ofMillis(10000)).build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject reqBody = new JSONObject();
        reqBody.put("accNumber", AccNumber);

        HttpEntity<String> request = new HttpEntity<String>(reqBody.toString(), headers);
        //API Key Implementation
        ResponseEntity<String> respEntity =  myRest.postForEntity(bankServer.getCheckBalanceURL(), request, String.class);
        if(respEntity.getStatusCode() == HttpStatus.OK){
            return Double.valueOf(respEntity.getBody());
        } else {
            throw new ServerErrorException("Bank server error", null);
        }
    }
   
    public MyTransaction validateTransaction(String senderUpi, String senderBankAcc, String receiverUpi, double amount) throws RuntimeException{
        Optional<NPCIAccount> senderNPCI = this.NPCIRep.findById(senderUpi);
        Optional<NPCIAccount> receiverNPCI = this.NPCIRep.findById(receiverUpi);

        if(senderNPCI.isPresent()){
            throw new UPIDoesNotExistException("Sender");
        }else if(receiverNPCI.isPresent()){
            throw new UPIDoesNotExistException("Receiver");
        }

        BankServer senderBankServer;
        BankServer receiverBankServer;

        if(senderBankAcc.equals(senderNPCI.get().getDefaultBankAccNumber())){
            senderBankServer = getBankServer(senderNPCI.get().getDefaultBank());
        }else{
            BankAccount senderAccount = this.BankRep.findByUpiIdAndAccNumber(senderUpi, senderBankAcc);
            senderBankServer = getBankServer(senderAccount.getBank());
        }

        receiverBankServer = getBankServer(receiverNPCI.get().getDefaultBank());

        double senderBalance = this.getBalance(senderUpi, senderBankAcc);
        if(senderBalance<amount){
            throw new InsufficientBalanceException();
        }

        MyTransaction transaction = MyTransaction.builder()
                                    .amount(amount)
                                    .senderUPI(senderUpi)
                                    .senderBankAcc(senderBankAcc)
                                    .receiverUPI(receiverUpi)
                                    .receiverBankAcc(receiverNPCI.get().getDefaultBankAccNumber())
                                    .status("INITIATED")
                                    .build();

        MyTransaction savedTransaction = this.TransactionRep.save(transaction);

        ResponseEntity<String> senderBankResp;
        ResponseEntity<String> receiverBankResp;
        try{              
            senderBankResp = requestBankToSendAmount(senderBankServer, transaction);
            receiverBankResp = requestBankToReceiveAmount(receiverBankServer, transaction);
        }catch(ResourceAccessException er){
            savedTransaction.setStatus("SERVER TIMEOUT");
            return transaction;
        }

        if(senderBankResp.getStatusCode() == HttpStatus.OK && receiverBankResp.getStatusCode() == HttpStatus.OK){
            savedTransaction.setStatus("COMPLETE");
            savedTransaction = this.TransactionRep.save(savedTransaction);
            return savedTransaction;
        }else{
            savedTransaction.setStatus("FAILED");
            savedTransaction = this.TransactionRep.save(savedTransaction);
            return savedTransaction;
        }
    }


    // public ResponseEntity<String> handleReceivedFunds(String senderBankAcc, String receiverBankAcc, String amount) {
    //     List<BankAccount> senderAccount = this.BankRep.findByAccNumber(senderBankAcc);
    //     List<BankAccount> receiverAccount = this.BankRep.findByAccNumber(receiverBankAcc);

    //     if (receiverAccount.isEmpty()) {
    //         throw new UPIDoesNotExistException("Account associated");
    //     }

    // }
    
}
