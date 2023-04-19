package com.OOAD.NPCI.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@IdClass(BankAccountCompositeKey.class)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "bankaccount")
public class BankAccount {
    @Id
    @Column(name = "upiid")
    private String upiId;
    @Id
    @Column(name = "accnumber")
    private String accNumber;
    @Column(name = "bank")
    private String bank;
}