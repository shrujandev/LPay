package net.javaguides.sms.service;

import java.util.List;

import net.javaguides.sms.entity.MyTransaction;
import net.javaguides.sms.entity.NPCIAccount;



public interface NPCIService {
    NPCIAccount registerAccount(String phoneNumber, String accountNumber, String bankName);

    List<String> getBanksList();
    
    double getBalance(String UPI, String AccNumber);

    MyTransaction validateTransaction(String senderUpi, String senderBankAcc, String receiverUpi, double amount);
}



