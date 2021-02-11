package com.ocp.demo.kafka.controller;

import java.time.Duration;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
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
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeSalCalcController.class);


	private ReplyingKafkaTemplate<String, Employee, EmployeeResult> replyingKafkaTemplate;

	@Value("${kafka.request.topic}")
	private String requestTopic;

	@Value("${kafka.reply.topic}")
	private String replyTopic;

	@Autowired
	EmployeeSalCalcController(ReplyingKafkaTemplate<String, Employee, EmployeeResult> replyingKafkaTemplate) {
		this.replyingKafkaTemplate = replyingKafkaTemplate;
	}

	@RequestMapping(method = RequestMethod.POST, path = "/calSal", produces = "application/json", consumes = "application/json")
	@ApiOperation("Calculate Employee Sal and return")
	public ResponseEntity<EmployeeResult> calSal(@RequestBody Employee employee)
			throws InterruptedException, ExecutionException {

		// create producer record
		ProducerRecord<String, Employee> record = new ProducerRecord<String, Employee>(requestTopic, employee);
		// set reply topic in header
		record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, replyTopic.getBytes()));

		RequestReplyFuture<String, Employee, EmployeeResult> sendAndReceive = replyingKafkaTemplate.sendAndReceive(record,
				Duration.ofSeconds(10));

		// confirm if producer produced successfully
		SendResult<String, Employee> sendResult = sendAndReceive.getSendFuture().get();
		
		//print all headers
		sendResult.getProducerRecord().headers().forEach(header -> LOGGER.debug(header.key() + ":" + header.value().toString()));
		
		// get consumer record
		ConsumerRecord<String, EmployeeResult> consumerRecord = sendAndReceive.get();

		return new ResponseEntity<>(consumerRecord.value(), HttpStatus.OK);

	}
}