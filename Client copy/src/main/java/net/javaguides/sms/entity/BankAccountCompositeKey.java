package net.javaguides.sms.entity;

import java.io.Serializable;




public class BankAccountCompositeKey implements Serializable{
    
    private String upiId;
    private String accNumber;
    
    public BankAccountCompositeKey() {
		
	}
	public BankAccountCompositeKey(String upiId, String accNumber) {
		super();
		this.upiId = upiId;
		this.accNumber = accNumber;
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
    
}
