package com.bank.bankServer.Controller;

import java.util.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.*;
import com.bank.bankServer.Repository.BankRepo;
import com.bank.bankServer.Entity.account;

@RestController
public class apiController {

    @Autowired
    private BankRepo bankRepo;


    @GetMapping(value="/")
    public String getPage() {
        return "Welcome";
    }

    @GetMapping(value="/accounts")
    public List<account> getUsers() {
        return bankRepo.findAll();
    }
}
