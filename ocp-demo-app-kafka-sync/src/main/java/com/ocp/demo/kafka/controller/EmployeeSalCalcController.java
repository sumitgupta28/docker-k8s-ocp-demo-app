package com.ocp.demo.kafka.controller;

import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ocp.demo.kafka.entity.Employee;
import com.ocp.demo.kafka.entity.EmployeeResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/emp")
@Api(description = "Set of endpoints for Retrieving employee Details.")
public class EmployeeSalCalcController {

	private ReplyingKafkaTemplate<String, Employee, EmployeeResult> replyingKafkaTemplate;

	@Value("${kafka.request.topic}")
	private String requestTopic;

	@Autowired
	EmployeeSalCalcController(ReplyingKafkaTemplate<String, Employee, EmployeeResult> replyingKafkaTemplate) {
		this.replyingKafkaTemplate = replyingKafkaTemplate;
	}

	@RequestMapping(method = RequestMethod.POST, path = "/calSal", produces = "application/json", consumes = "application/json")
	@ApiOperation("Calculate Employee Sal and return")
	public ResponseEntity<EmployeeResult> calSal(@RequestBody Employee employee)
			throws InterruptedException, ExecutionException {

		ProducerRecord<String, Employee> record = new ProducerRecord<>(requestTopic, null, "STD001", employee);
		RequestReplyFuture<String, Employee, EmployeeResult> future = replyingKafkaTemplate.sendAndReceive(record);
		ConsumerRecord<String, EmployeeResult> response = future.get();
		return new ResponseEntity<>(response.value(), HttpStatus.OK);

	}
}