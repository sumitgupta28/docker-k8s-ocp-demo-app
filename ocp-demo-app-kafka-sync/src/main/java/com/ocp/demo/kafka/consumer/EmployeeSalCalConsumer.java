package com.ocp.demo.kafka.consumer;

import java.io.IOException;
import java.util.Random;
import java.util.logging.Logger;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.ocp.demo.kafka.entity.Employee;
import com.ocp.demo.kafka.entity.EmployeeResult;

@Service
public class EmployeeSalCalConsumer {

	private static final Logger logger = Logger.getLogger(EmployeeSalCalConsumer.class.getName());

	@KafkaListener(topics = "${kafka.reuest.topic}", groupId = "${kafka.group.id}")
	public EmployeeResult consume(Employee employee) throws IOException {
		logger.info(String.format("Consumed message -> %s", employee.toString()));
		String calSal = String.valueOf((new Random()).nextInt(50000)) + "$";
		EmployeeResult employeeResult = new EmployeeResult(employee.getFirstName() + " " + employee.getLastName(),
				calSal);
		logger.info(String.format("Returned message -> %s", employeeResult.toString()));

		return employeeResult;

	}
}