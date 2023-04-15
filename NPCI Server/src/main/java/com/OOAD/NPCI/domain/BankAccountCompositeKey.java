package com.OOAD.NPCI.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountCompositeKey implements Serializable{
    
    private String upiId;
    private String accNumber;
}
