package com.ocp.demo.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

import com.ocp.demo.kafka.entity.Employee;
import com.ocp.demo.kafka.entity.EmployeeResult;

@Configuration
public class KafkaConfig {

	@Value("${kafka.group.id}")
	private String groupId;

	@Value("${kafka.reply.topic}")
	private String replyTopic;

	@Bean
	public ReplyingKafkaTemplate<String, Employee, EmployeeResult> replyingKafkaTemplate(
			ProducerFactory<String, Employee> pf,
			ConcurrentKafkaListenerContainerFactory<String, EmployeeResult> factory) {

		ConcurrentMessageListenerContainer<String, EmployeeResult> replyContainer = factory.createContainer(replyTopic);
		replyContainer.getContainerProperties().setMissingTopicsFatal(false);
		replyContainer.getContainerProperties().setGroupId(groupId);
		return new ReplyingKafkaTemplate<>(pf, replyContainer);

	}

	@Bean
	public KafkaTemplate<String, EmployeeResult> replyTemplate(ProducerFactory<String, EmployeeResult> pf,
			ConcurrentKafkaListenerContainerFactory<String, EmployeeResult> factory) {
		KafkaTemplate<String, EmployeeResult> kafkaTemplate = new KafkaTemplate<>(pf);
		factory.getContainerProperties().setMissingTopicsFatal(false);
		factory.setReplyTemplate(kafkaTemplate);
		return kafkaTemplate;
	}
}
