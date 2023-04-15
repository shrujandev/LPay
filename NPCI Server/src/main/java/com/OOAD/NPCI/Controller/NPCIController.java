package com.OOAD.NPCI.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.OOAD.NPCI.domain.NPCIAccount;
import com.OOAD.NPCI.services.NPCIService;
import com.OOAD.NPCI.services.impl.NPCIServiceImpl.AccountExistsException;
import com.OOAD.NPCI.services.impl.NPCIServiceImpl.BankServerVerificationException;

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
        String AccNumber;
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
    public ResponseEntity<BankNames> GetBanksList(){
        BankNames result = new BankNames();
        result.setNamesList(this.nPCIService.getBanksList());

        HttpHeaders returnHeaders = new HttpHeaders();
        returnHeaders.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<BankNames> response = new ResponseEntity<BankNames>(result, returnHeaders, HttpStatus.OK);

        return response;
    }

}
