package com.bank.bankServer.Repository;

import org.springframework.data.jpa.repository.*;
import com.bank.bankServer.Entity.*;
import org.springframework.stereotype.*;
import java.util.*;
@Repository
public interface BankRepo extends JpaRepository<account,Long>{
   
}
