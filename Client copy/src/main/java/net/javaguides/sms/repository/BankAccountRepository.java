package net.javaguides.sms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.javaguides.sms.entity.BankAccount;
import net.javaguides.sms.entity.BankAccountCompositeKey;



@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, BankAccountCompositeKey> {
}
