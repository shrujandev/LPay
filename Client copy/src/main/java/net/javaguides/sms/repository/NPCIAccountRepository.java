package net.javaguides.sms.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.javaguides.sms.entity.NPCIAccount;



@Repository
public interface NPCIAccountRepository extends JpaRepository<NPCIAccount, String> {
    NPCIAccount findByPhoneNumber(String phonenumber);
}
