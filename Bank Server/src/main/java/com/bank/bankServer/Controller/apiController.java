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

    @PostMapping(value="/save")
    public String saveAccount(@RequestBody account acc) {
        bankRepo.save(acc);
        return acc.getname();

    }
    
    @PutMapping(value="balance/{id}")
    public String updateAccount(@PathVariable long id,@RequestBody account acc) {
        account updatedAcc = bankRepo.findById(id).get();
        float balance = acc.getBalance();
        updatedAcc.setaccountNo(acc.getaccountNo());
        updatedAcc.setId(acc.getId());
        updatedAcc.setname(acc.getname());
        updatedAcc.setBalance(balance - 1000);
        bankRepo.save(updatedAcc);
        return "success " + updatedAcc.getBalance();
        
    }

    @PutMapping(value="debit/{id}/{amount}")
    public String debit(@PathVariable long id, @PathVariable float amount, @RequestBody account acc) {
        account debitAccount = bankRepo.findById(id).get();
        debitAccount.setaccountNo(acc.getaccountNo());
        debitAccount.setId(acc.getId());
        debitAccount.setname(acc.getname());
        debitAccount.setBalance(debitAccount.getBalance() - amount);
        bankRepo.save(debitAccount);
        return "success";
    }
    
}
