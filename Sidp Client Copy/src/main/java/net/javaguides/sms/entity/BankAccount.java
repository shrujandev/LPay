package net.javaguides.sms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;



@Entity
@IdClass(BankAccountCompositeKey.class)
@Table(name = "bankaccount", schema = "npci")
public class BankAccount {
    @Id
    @Column(name = "upiid")
    private String upiId;
    @Id
    @Column(name = "accnumber")
    private String accNumber;
    @Column(name = "bank")
    private String bank;
    
    
    public BankAccount() {
		
	}
    public BankAccount(String upiId, String accNumber, String bank) {
		super();
		this.upiId = upiId;
		this.accNumber = accNumber;
		this.bank = bank;
	}
	public String getUpiId() {
		return upiId;
	}
	public void setUpiId(String upiId) {
		this.upiId = upiId;
	}
	public String getAccNumber() {
		return accNumber;
	}
	public void setAccNumber(String accNumber) {
		this.accNumber = accNumber;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	
    
    
}