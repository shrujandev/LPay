package net.javaguides.sms.service;

import java.util.List;

public interface StudentService {
	List<Student> getAllStudents();
	
	Student saveStudent(Student student);
	
	Student getStudentId(Long id);
	Student updateStudent(Student student);
	
	void deleteStudentById(Long id);
}
