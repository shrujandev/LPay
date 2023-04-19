package com.bank.bankServer.Entity;

import javax.annotation.processing.Generated;

import jakarta.persistence.*;
import lombok.*;


@Data
public class accBal {
    private String account;

    public String getaccount() {
        return account;
    }

    public void setaccount(String account) {
        this.account = account;
    }


}
