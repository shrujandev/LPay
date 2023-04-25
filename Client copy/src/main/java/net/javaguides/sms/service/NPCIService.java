package net.javaguides.sms.service;

import java.util.List;

import net.javaguides.sms.entity.NPCIAccount;



public interface NPCIService {
    NPCIAccount registerAccount(String phoneNumber, String accountNumber, String bankName);

    List<String> getBanksList();
}



