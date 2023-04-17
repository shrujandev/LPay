package net.javaguides.sms.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;



@Entity
@Table(name = "bankserver")
public class BankServerEntity {
    @Id
    @Column(name = "bankname")
    private String bankName;
    @Column(name = "bankurl")
    private String bankURL;
    @Column(name = "bankapikey")
    private String bankAPIKey;
    public BankServerEntity() {
		
	}
	public BankServerEntity(String bankName, String bankURL, String bankAPIKey) {
		super();
		this.bankName = bankName;
		this.bankURL = bankURL;
		this.bankAPIKey = bankAPIKey;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankURL() {
		return bankURL;
	}
	public void setBankURL(String bankURL) {
		this.bankURL = bankURL;
	}
	public String getBankAPIKey() {
		return bankAPIKey;
	}
	public void setBankAPIKey(String bankAPIKey) {
		this.bankAPIKey = bankAPIKey;
	}
    
    
}
