package net.javaguides.sms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.javaguides.sms.entity.Student;
//jparepository has two parameters, entity, and the type of the primary key
public interface StudentRepository extends JpaRepository<Student,Long> {

}
