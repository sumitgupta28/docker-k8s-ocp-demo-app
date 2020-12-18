package com.fsd.employee.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fsd.employee.entity.Employee;
import com.fsd.employee.entity.repository.EmployeeRepository;
import com.fsd.employee.exception.ResourceNotFoundException;
import com.fsd.employee.kafka.producer.KafkaProducer;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/v1")
@Api(description = "Set of endpoints for Creating, Retrieving, Updating and Deleting of Employees.")
public class EmployeeController {

	private static final Logger LOGGER = LogManager.getLogger(EmployeeController.class);

	private EmployeeRepository employeeRepository;

	private KafkaProducer kafkaProducer;

	@Autowired
	EmployeeController(KafkaProducer kafkaProducer, EmployeeRepository employeeRepository) {
		this.kafkaProducer = kafkaProducer;
		this.employeeRepository = employeeRepository;
	}

	@RequestMapping(method = RequestMethod.GET, path = "/employees", produces = "application/json")
	@ApiOperation("Returns list of all Employees in the system.")
	public List<Employee> getAllEmployees() {
		List<Employee> employees = employeeRepository.findAll();
		kafkaProducer.sendMessage("getAllEmployees");
		LOGGER.info(" All the Employees");
		return employees;
	}

	@RequestMapping(method = RequestMethod.GET, path = "/employees/{employeeId}", produces = "application/json")
	@ApiOperation("Returns a specific Employee by their identifier. 404 if does not exist.")
	public ResponseEntity<Employee> getEmployeeById(
			@ApiParam("Id of the employee to be obtained. Cannot be empty.") @PathVariable(value = "employeeId") Integer employeeId)
			throws ResourceNotFoundException {
		Employee employee = findEmployeeById(employeeId);
		LOGGER.info(" Employee {} by Employee Id {} ", employee, employeeId);
		kafkaProducer.sendMessage("getEmployeeById "+ employeeId);
		return ResponseEntity.ok().body(employee);
	}

	@RequestMapping(method = RequestMethod.POST, path = "/employees", produces = "application/json")
	@ApiOperation("Creates a new Employee.")
	public Employee createEmployee(@Valid @RequestBody Employee employee) {
		LOGGER.info(" Creating new Employee {}", employee);
		kafkaProducer.sendMessage("createEmployee "+ employee.toString());

		return employeeRepository.save(employee);
	}

	@RequestMapping(method = RequestMethod.PUT, path = "/employees/{employeeId}")
	@ApiOperation("Update Employee by Id. 404 if does not exist.")
	public ResponseEntity<Employee> updateEmployee(@PathVariable(value = "employeeId") Integer employeeId,
			@Valid @RequestBody Employee employeeDetails) throws ResourceNotFoundException {
		Employee employee = findEmployeeById(employeeId);
		employee.setEmailId(employeeDetails.getEmailId());
		employee.setLastName(employeeDetails.getLastName());
		employee.setFirstName(employeeDetails.getFirstName());
		LOGGER.info(" Update Employee {}", employee);
		kafkaProducer.sendMessage("updateEmployee "+ "{employeeId - "+employeeId +"} --"+ employee.toString());

		final Employee updatedEmployee = employeeRepository.save(employee);
		return ResponseEntity.ok(updatedEmployee);
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "/employees/{id}")
	@ApiOperation("Deletes a employee from the system. 404 if the employee identifier is not found.")
	public Map<String, Boolean> deleteEmployee(
			@ApiParam("Id of the Employee to be deleted. Cannot be empty.") @PathVariable(value = "id") Integer employeeId)
			throws ResourceNotFoundException {
		Employee employee = findEmployeeById(employeeId);
		LOGGER.info(" Delete Employee {} by EmployeeId", employee, employeeId);
		kafkaProducer.sendMessage("deleteEmployee "+ "{employeeId - "+employeeId +"} --"+ employee.toString());

		employeeRepository.deleteById(employee.getId());
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

	private Employee findEmployeeById(Integer employeeId) throws ResourceNotFoundException {
		Employee employee = employeeRepository.findById(employeeId).orElseThrow(
				() -> new ResourceNotFoundException("Employee not found for this employeeId :: " + employeeId));

		return employee;
	}

}
