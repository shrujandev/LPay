package net.javaguides.sms.repository;

import net.javaguides.sms.entity.MyTransaction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
import java.util.*;
@Repository
public interface MyTransactionRepository extends JpaRepository<MyTransaction,Long>{
}
