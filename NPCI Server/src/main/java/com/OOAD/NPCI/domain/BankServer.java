package com.OOAD.NPCI.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankServer {
    private String bankName;
    private String bankURL;
    private String bankAPIKey;

    public String getVerifyAccountURL(){
        return this.bankURL + "/verify";
    }

    public String getCheckBalanceURL(){
        return this.bankURL + "/checkBalance";
    }

    public String getSendAmountURL(){
        return this.bankURL + "/debit";
    }

    public String getReceiveAmountURL(){
        return this.bankURL + "/credit";
    }
}
