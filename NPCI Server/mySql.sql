CREATE DATABASE npci;

USE NPCI;


CREATE TABLE BankServer(
	bankName VARCHAR(10) PRIMARY KEY,
    bankURL VARCHAR(50) NOT NULL,
    bankAPIKey VARCHAR(50)
);

CREATE TABLE NPCIAccount(
	upiId VARCHAR(30) PRIMARY KEY,
	phoneNumber VARCHAR(13) NOT NULL,
    defaultBank VARCHAR(10),
    defaultBankAccNumber VARCHAR(30),
    
    FOREIGN KEY (defaultBank) REFERENCES BankServer(bankName)
);

CREATE TABLE BankAccount(
	upiId VARCHAR(30) PRIMARY KEY,
    AccNumber VARCHAR(30) NOT NULL,
    bank VARCHAR(10) NOT NULL,
    
    FOREIGN KEY (upiId) REFERENCES NPCIAccount (upiId),
	FOREIGN KEY (bank) REFERENCES BankServer(bankName)

);

CREATE TABLE Transaction(
	TransactionId VARCHAR(36) PRIMARY KEY,
    senderUPI VARCHAR(30) NOT NULL,
    receiverUPI VARCHAR(30) NOT NULL,
    senderBankAcc VARCHAR(30),
    receiverBankAcc VARCHAR(30),
    TransactionStatus VARCHAR(10),
    amount double
);



