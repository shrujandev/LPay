package com.OOAD.NPCI.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class NPCIAccount {
    @Id
    private String upiId;
    private int phoneNumber;
    private String defaultBank;
    private String defaultBankAccNumber;
}

