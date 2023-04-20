package net.javaguides.sms.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "transaction")
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

//    public void setTransactionId(String id){
//        this.transactionId = UUID.fromString(id);
//    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public String getSenderUPI() {
        return senderUPI;
    }

    public void setSenderUPI(String senderUPI) {
        this.senderUPI = senderUPI;
    }

    public String getSenderBankAcc() {
        return senderBankAcc;
    }

    public void setSenderBankAcc(String senderBankAcc) {
        this.senderBankAcc = senderBankAcc;
    }

    public String getReceiverUPI() {
        return receiverUPI;
    }

    public void setReceiverUPI(String receiverUPI) {
        this.receiverUPI = receiverUPI;
    }

    public String getReceiverBankAcc() {
        return receiverBankAcc;
    }

    public void setReceiverBankAcc(String receiverBankAcc) {
        this.receiverBankAcc = receiverBankAcc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}

