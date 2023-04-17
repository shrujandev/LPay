package com.OOAD.NPCI.services;

import java.util.List;

import com.OOAD.NPCI.domain.BankAccount;
import com.OOAD.NPCI.domain.MyTransaction;
import com.OOAD.NPCI.domain.NPCIAccount;

public interface NPCIService {
    NPCIAccount registerAccount(String phoneNumber, String accountNumber, String bankName);

    List<String> getBanksList();

    BankAccount addBankAccount(String UPIid, String accountNumber, String bankName);

    double getBalance(String UPI, String AccNumber);

    MyTransaction validateTransaction(String senderUpi, String senderBankAcc, String receiverUpi, double amount);

    
}



