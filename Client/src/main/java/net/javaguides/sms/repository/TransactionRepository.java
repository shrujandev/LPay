package net.javaguides.sms.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.javaguides.sms.entity.MyTransaction;


@Repository
public interface TransactionRepository  extends JpaRepository<MyTransaction, UUID>{
    
}
