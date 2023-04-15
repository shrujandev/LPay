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
        return this.bankURL + "/verifyAccount";
    }
}