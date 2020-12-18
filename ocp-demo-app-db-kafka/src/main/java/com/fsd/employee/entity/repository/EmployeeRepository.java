package com.fsd.employee.entity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fsd.employee.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

	Optional<Employee> findById(Integer id);

	void deleteById(Integer id);
}
