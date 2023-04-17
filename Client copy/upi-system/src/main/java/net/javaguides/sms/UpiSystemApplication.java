package net.javaguides.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import net.javaguides.sms.entity.Student;
import net.javaguides.sms.repository.StudentRepository;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class UpiSystemApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(UpiSystemApplication.class, args);
	}
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Override
	public void run(String... args) throws Exception{
//		Student student1 = new Student("Ramesh","Pande","Rameshp@gmail.com");
//		studentRepository.save(student1);
//		
//		Student student2 = new Student("Sundhar","Pichai","Sudharp@gmail.com");
//		studentRepository.save(student2);
//		
//		Student student3 = new Student("Sandesh","K","SandeshK@gmail.com");
//		studentRepository.save(student3);
	}

}




