package net.javaguides.sms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;



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

    public NPCIAccount() {

    }
    public NPCIAccount(String upiId, String phoneNumber, String defaultBank, String defaultBankAccNumber) {
        super();
        this.upiId = upiId;
        this.phoneNumber = phoneNumber;
        this.defaultBank = defaultBank;
        this.defaultBankAccNumber = defaultBankAccNumber;
    }
    public String getUpiId() {
        return upiId;
    }
    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getDefaultBank() {
        return defaultBank;
    }
    public void setDefaultBank(String defaultBank) {
        this.defaultBank = defaultBank;
    }
    public String getDefaultBankAccNumber() {
        return defaultBankAccNumber;
    }
    public void setDefaultBankAccNumber(String defaultBankAccNumber) {
        this.defaultBankAccNumber = defaultBankAccNumber;
    }




}