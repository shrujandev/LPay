package com.bank.bankServer.Response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {
    public static ResponseEntity<Object> generateResponse(String message, HttpStatus status, Object responseObj) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("message", message);
        map.put("status", status.value());
        map.put("data", responseObj);

        return new ResponseEntity<Object>(map, status);
    }
    
    public static ResponseEntity<Object> generateBalance(String account,float balance,HttpStatus status) {
        Map<String, Object> map = new HashMap<String, Object>();
            map.put("account", account);
            map.put("balance", balance);
            map.put("status", status.value());
            

            return new ResponseEntity<Object>(map,status);
    }
}