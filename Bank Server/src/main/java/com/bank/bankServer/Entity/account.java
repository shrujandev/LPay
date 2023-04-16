package com.bank.bankServer.Entity;

import javax.annotation.processing.Generated;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "account_test2")
@Data
public class account {
   
    @Column
    private Long accountNo;
   
    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;
    
    

    @Column
    private float Balance;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }


    public Long getaccountNo() {
        return accountNo;
    }

    public void setaccountNo(Long accountNo) {
        this.accountNo = accountNo;
    }


    public float getBalance() {
        return Balance;
    }

    public void setBalance(float Balance) {
        this.Balance = Balance;
    }


}