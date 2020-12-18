package com.fsd.employee.kafka.consumer;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

	private static final Logger LOGGER = LogManager.getLogger(KafkaConsumer.class);

	@KafkaListener(topics = "mytopic", groupId = "group_id")
	public void consume(String message) throws IOException {
		LOGGER.info(String.format("Consumed message -> %s", message));
	}
}