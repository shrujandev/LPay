package com.OOAD.NPCI.services;

import java.util.List;

import com.OOAD.NPCI.domain.NPCIAccount;

public interface NPCIService {
    NPCIAccount registerAccount(String phoneNumber, String accountNumber, String bankName);

    List<String> getBanksList();
}



