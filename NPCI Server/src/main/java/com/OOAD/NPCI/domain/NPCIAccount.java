package com.OOAD.NPCI.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class NPCIAccount {
    @Id
    @Column(name = "upiid")
    private String upiId;
    @Column(name = "phonenumber")
    private String phoneNumber;
    @Column(name = "defaultbank")
    private String defaultBank;
    @Column(name = "defaultbankaccnumber")
    private String defaultBankAccNumber;
}

