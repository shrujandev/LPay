package com.OOAD.NPCI.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.OOAD.NPCI.domain.NPCIAccount;

@Repository
public interface NPCIAccountRepository extends JpaRepository<NPCIAccount, String> {
}
