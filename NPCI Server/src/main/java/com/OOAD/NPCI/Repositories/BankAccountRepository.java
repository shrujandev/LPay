package com.OOAD.NPCI.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.OOAD.NPCI.domain.BankAccount;
import com.OOAD.NPCI.domain.BankAccountCompositeKey;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, BankAccountCompositeKey> {
    BankAccount findByUpiIdAndAccNumber(String upiId, String accNumber);
    List<BankAccount>  findByAccNumber(String accNumber);
}
