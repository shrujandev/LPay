package net.javaguides.sms.entity;




public class BankServer {
    private String bankName;
    private String bankURL;
    private String bankAPIKey;

    public String getVerifyAccountURL(){
        return this.bankURL + "/verifyAccount";
    }

    public BankServer() {
		
	}
    public BankServer(String bankName, String bankURL, String bankAPIKey) {
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

	
    
    
}
