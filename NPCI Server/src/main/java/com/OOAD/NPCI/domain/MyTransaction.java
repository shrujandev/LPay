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
    public UUID transactionId;

    @Column(name = "senderupi")
    public String senderUPI;

    @Column(name = "senderbankacc")
    public String senderBankAcc;

    @Column(name = "receiverupi")
    public String receiverUPI;

    @Column(name = "receiverbankacc")
    public String receiverBankAcc;

    @Column(name = "transactionstatus")
    public String status;

    @Column(name = "amount")
    public double amount;

    public void setTransactionId(String id){
        this.transactionId = UUID.fromString(id);
    }
}
