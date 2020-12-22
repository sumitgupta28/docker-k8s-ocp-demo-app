package com.ocp.demo.kafka.controller;

import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ocp.demo.kafka.model.ChatMessage;
import com.ocp.demo.kafka.producer.Producer;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(description = "Set of endpoints for Sending Compexta dn Plain type of stream Messages.")
public class StreamController {

	private Producer producer;

	public StreamController(Producer producer) {
		super();
		this.producer = producer;
	}

	// get the message as a complex type via HTTP, publish it to broker using
	// spring cloud stream
	@RequestMapping(value = "/sendMessage/complexType", method = RequestMethod.POST)
	@ApiOperation("Get the message as a complex type via HTTP, publish it to broker using spring cloud stream")
	public String publishMessageComplextType(@RequestBody ChatMessage payload) {
		payload.setTime(System.currentTimeMillis());
		producer.getMysource().output().send(MessageBuilder.withPayload(payload).setHeader("type", "chat").setHeader(MessageHeaders.CONTENT_TYPE, "application/json").build());
		return "success";
	}

	// get the String message via HTTP, publish it to broker using spring cloud
	// stream
	@RequestMapping(value = "/sendMessage/string", method = RequestMethod.POST)
	@ApiOperation("Get the String message via HTTP, publish it to broker using spring cloud stream")
	public String publishMessageString(@RequestBody String payload) {
		// send message to channel
		producer.getMysource().output().send(MessageBuilder.withPayload(payload).setHeader("type", "string").build());
		return "success";
	}
}
