package net.javaguides.sms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.javaguides.sms.entity.BankServerEntity;



public interface BankServersRepository extends JpaRepository<BankServerEntity, String>{
    BankServerEntity findBybankName(String BankName);
}
