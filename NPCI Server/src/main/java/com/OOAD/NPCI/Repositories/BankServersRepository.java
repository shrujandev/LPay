package com.OOAD.NPCI.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.OOAD.NPCI.domain.BankServerEntity;

public interface BankServersRepository extends JpaRepository<BankServerEntity, String>{
    BankServerEntity findBybankName(String BankName);
}
