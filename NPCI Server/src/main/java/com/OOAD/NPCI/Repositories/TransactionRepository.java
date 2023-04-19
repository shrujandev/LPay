package com.OOAD.NPCI.Repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.OOAD.NPCI.domain.MyTransaction;

@Repository
public interface TransactionRepository  extends JpaRepository<MyTransaction, UUID>{
    MyTransaction findByTransactionId(String transactionId);
}
