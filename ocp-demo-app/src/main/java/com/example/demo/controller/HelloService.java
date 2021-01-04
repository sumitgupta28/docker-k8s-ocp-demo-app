package com.example.demo.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(path = "/api")
@Api(description = "Set of endpoints for OCP Demo APP.")

public class HelloService {

	@RequestMapping(method = RequestMethod.GET, path = "/hello")
	@ApiOperation("Return Hello OCP")
	public String hello() {
		return "hello OCP";
	}

	@RequestMapping(method = RequestMethod.GET, path = "/hello/{name}")
	@ApiOperation("Return Hello + Name")
	public String getEmployeeById(@ApiParam("name.") @PathVariable(value = "name") Integer name) {
		return "hello " + name;
	}
}
