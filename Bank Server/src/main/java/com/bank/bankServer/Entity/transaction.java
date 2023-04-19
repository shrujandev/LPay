package com.bank.bankServer.Entity;

import javax.annotation.processing.Generated;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "transaction")
@Data
public class transaction{
    @Id
    @Column
    private String transactionId;


    @Column
    private Long debitAccount;
    
    @Column
    private Long creditAccount;


    @Column
    private float amount;


    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }


    public Long getDebitaccount() {
        return debitAccount;
    }

    public void setDebitaccount(Long Debitaccount) {
        this.debitAccount = Debitaccount;
    }

    public Long getCreditaccount() {
        return creditAccount;
    }

    public void setCreditaccount(Long creditaccount) {
        this.creditAccount = creditaccount;
    }


    public float getAmount() {
        return amount;
    }

    public void setamount(float amount) {
        this.amount = amount;
    }


}