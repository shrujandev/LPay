package com.OOAD.NPCI.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class BankAccount {
    @Id
    private String upiId;
    private String accountNumber;
}

