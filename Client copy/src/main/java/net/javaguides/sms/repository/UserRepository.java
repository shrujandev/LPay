package net.javaguides.sms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.javaguides.sms.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{

	@Query("SELECT u FROM User u WHERE u.phone = ?1")
    public User findByPhone(String phone);

}
