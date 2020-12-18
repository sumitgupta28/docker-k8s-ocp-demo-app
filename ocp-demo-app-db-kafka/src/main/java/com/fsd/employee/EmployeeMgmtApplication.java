package com.fsd.employee;

import java.util.stream.Stream;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fsd.employee.entity.Employee;
import com.fsd.employee.entity.repository.EmployeeRepository;

@SpringBootApplication
public class EmployeeMgmtApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeMgmtApplication.class, args);
	}

	@Bean
	ApplicationRunner init(EmployeeRepository repository) {

		String[][] data = { { "Sumit", "Gupta", "sumit@mail.com" }, { "User 1", "LastName 1", "user1@mail.com" },
				{ "User 2", "LastName 1", "user2@mail.com" } };
		return args -> {
			Stream.of(data).forEach(array -> {
				Employee kayak = new Employee(array[0], array[1], array[2]);
				repository.save(kayak);
			});
			repository.findAll().forEach(System.out::println);
		};
	}

}
