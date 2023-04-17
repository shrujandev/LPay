package net.javaguides.sms.entity;


public class RegistrationReqBody{
    String phoneNumber;
    String bankName;
    String AccNumber;
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getAccNumber() {
		return AccNumber;
	}
	public void setAccNumber(String accNumber) {
		AccNumber = accNumber;
	}
    
}