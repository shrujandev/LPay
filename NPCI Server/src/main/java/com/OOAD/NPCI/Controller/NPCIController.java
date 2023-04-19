package com.OOAD.NPCI.Controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.catalina.connector.Response;
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

import com.OOAD.NPCI.domain.BankAccount;
import com.OOAD.NPCI.domain.MyTransaction;
import com.OOAD.NPCI.domain.NPCIAccount;
import com.OOAD.NPCI.services.NPCIService;
import com.OOAD.NPCI.services.impl.NPCIServiceImpl.AccountDoesNotExistException;
import com.OOAD.NPCI.services.impl.NPCIServiceImpl.AccountExistsException;
import com.OOAD.NPCI.services.impl.NPCIServiceImpl.BankServerVerificationException;
import com.OOAD.NPCI.services.impl.NPCIServiceImpl.InsufficientBalanceException;
import com.OOAD.NPCI.services.impl.NPCIServiceImpl.UPIDoesNotExistException;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@RestController
public class NPCIController {
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class RegistrationReqBody{
        String phoneNumber;
        String bankName;
        String accNumber;
    }

    @Data
    @NoArgsConstructor
    private static class requestmessage{
        private String message;
    }

    @Data
    @NoArgsConstructor
    private static class BankNames{
        private List<String> NamesList;
    }

    @Data
    @NoArgsConstructor
    private static class AddBankAccountReqBody{
        private String upiId;
        private String bankName;
        private String AccNumber;
    }

    @Data
    @NoArgsConstructor
    private static class GetBalanceReqBody{
        String upiId;
        String AccNumber;
    }

    @Data
    @NoArgsConstructor
    private static class ValidateTransactionReqBody{
        String senderUPI;
        String receiverUPI;
        String senderBankAcc;
        String amount;
    }

    @Data
    @NoArgsConstructor
    private static class ReceivedFundsReqBody{
        
        String senderBankAcc;
        String receiverBankAcc;
        String amount;
        String transactionId;
        
    }

    private static HttpHeaders getErrorHeader(Throwable er){
        HttpHeaders returnval = new HttpHeaders();
        returnval.set("Error", er.getMessage());
        return returnval;
    }

    private final NPCIService nPCIService;

    @Autowired
    public NPCIController(NPCIService myService){
        this.nPCIService = myService;
    }

    @PostMapping(path = "/UPI/RegisterAccount")
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
              value = "/greeting", consumes = "application/json", produces = "application/json")
            public requestmessage showmessage(@RequestBody requestmessage message , HttpServletResponse response) {
                response.setHeader("Title", "This is the header");
                System.out.println("Message received : "+message.getMessage());
                requestmessage obj = new requestmessage();
                obj.setMessage("How is your life guys?");
                return obj;
            }

    @GetMapping(value = "/UPI/GetBanksList")
    public ResponseEntity<BankNames> getBanksList(){
        BankNames result = new BankNames();
        result.setNamesList(this.nPCIService.getBanksList());

        HttpHeaders returnHeaders = new HttpHeaders();
        returnHeaders.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<BankNames> response = new ResponseEntity<BankNames>(result, returnHeaders, HttpStatus.OK);

        return response;
    }

    @PostMapping(value = "/UPI/AddBankAccount")
    public ResponseEntity<BankAccount> addBankAccount(
        @RequestBody final AddBankAccountReqBody reqBody){
            System.out.println("addBankAccount" + "Params: "+reqBody.getUpiId()+" "+ reqBody.getAccNumber()+" "+ reqBody.getBankName());
            BankAccount result;
            
           try{
                result = this.nPCIService.addBankAccount(reqBody.getUpiId(), reqBody.getAccNumber(), reqBody.getBankName());
           }catch(AccountDoesNotExistException er){
                HttpHeaders returnHeaders = new HttpHeaders();
                returnHeaders.setContentType(MediaType.APPLICATION_JSON);
                returnHeaders.add("Error", er.getMessage());
                return new ResponseEntity<BankAccount>(null, returnHeaders, HttpStatus.BAD_REQUEST);
           }catch(BankServerVerificationException er){
                HttpHeaders returnHeaders = new HttpHeaders();
                returnHeaders.setContentType(MediaType.APPLICATION_JSON);
                returnHeaders.add("Error", er.getMessage());
                return new ResponseEntity<BankAccount>(null, returnHeaders, HttpStatus.BAD_REQUEST);
            }

            HttpHeaders returnHeaders = new HttpHeaders();
            returnHeaders.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<BankAccount> response = new ResponseEntity<BankAccount>(result, returnHeaders, HttpStatus.CREATED);

            return response;

        }

    @PostMapping(value = "/UPI/GetBalance")
    public ResponseEntity<String> getBalance(
        @RequestBody final GetBalanceReqBody reqBody){
            Double result;
            try{
                result = this.nPCIService.getBalance(reqBody.getUpiId(), reqBody.getAccNumber());
            }catch(UPIDoesNotExistException er){
                return new ResponseEntity<String>(null, getErrorHeader(er), HttpStatus.BAD_REQUEST);
            }catch(ServerErrorException er){
                return new ResponseEntity<String>(null, getErrorHeader(er), HttpStatus.INTERNAL_SERVER_ERROR);
            }catch(ResourceAccessException er){
                return new ResponseEntity<String>(null, getErrorHeader(er), HttpStatus.INTERNAL_SERVER_ERROR);
            }

            ResponseEntity<String> response = new ResponseEntity<String>(result.toString(), HttpStatus.OK);
            return response;
        }

    @PostMapping(value = "/UPI/Transact")
    public ResponseEntity<MyTransaction> validateTransaction(
        @RequestBody final ValidateTransactionReqBody reqBody){
            MyTransaction result;
            try{
                result = this.nPCIService.validateTransaction(reqBody.getSenderUPI(), reqBody.getSenderBankAcc(), reqBody.getReceiverUPI(), Double.valueOf(reqBody.getAmount()));
            }catch(UPIDoesNotExistException er){
                return new ResponseEntity<MyTransaction>(null, getErrorHeader(er), HttpStatus.BAD_REQUEST);
            }catch(InsufficientBalanceException er){
                return new ResponseEntity<MyTransaction>(null, getErrorHeader(er), HttpStatus.BAD_REQUEST);
            }

            HttpHeaders returnHeaders = new HttpHeaders();
            returnHeaders.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<MyTransaction> response = new ResponseEntity<MyTransaction>(result, returnHeaders, HttpStatus.OK);

            return response;
        }
    
    @PostMapping(value = "/Bank/ReceivedFunds")
    public ResponseEntity<String> handleReceivedFunds(
        @RequestBody final ReceivedFundsReqBody reqBody){
            System.out.println("entered");
            this.nPCIService.handleReceivedFunds(reqBody.getTransactionId(), reqBody.getSenderBankAcc(), reqBody.getReceiverBankAcc(), reqBody.getAmount());
            return new ResponseEntity<String>("Acknowldeged", HttpStatus.OK);
        }
    
}
