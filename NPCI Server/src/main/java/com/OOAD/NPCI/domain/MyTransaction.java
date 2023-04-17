package com.OOAD.NPCI.domain;

import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Transaction")
public class MyTransaction {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "transactionid")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID TransactionId;

    @Column(name = "senderupi")
    private String senderUPI;

    @Column(name = "senderbankacc")
    private String senderBankAcc;

    @Column(name = "receiverupi")
    private String receiverUPI;

    @Column(name = "receiverbankacc")
    private String receiverBankAcc;

    @Column(name = "transactionstatus")
    private String status;

    @Column(name = "amount")
    private double amount;
}
