package net.javaguides.sms.entity;

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

    public MyTransaction() {
		
	}
    public MyTransaction(UUID transactionId, String senderUPI, String senderBankAcc, String receiverUPI,
			String receiverBankAcc, String status, double amount) {
		super();
		TransactionId = transactionId;
		this.senderUPI = senderUPI;
		this.senderBankAcc = senderBankAcc;
		this.receiverUPI = receiverUPI;
		this.receiverBankAcc = receiverBankAcc;
		this.status = status;
		this.amount = amount;
	}

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

	public UUID getTransactionId() {
		return TransactionId;
	}

	public void setTransactionId(UUID transactionId) {
		TransactionId = transactionId;
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
