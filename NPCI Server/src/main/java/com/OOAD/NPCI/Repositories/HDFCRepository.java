package com.OOAD.NPCI.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.OOAD.NPCI.domain.BankAccount;

public interface HDFCRepository extends JpaRepository<BankAccount, String>{    
}
