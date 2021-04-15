package com.example.demo.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.http.ResponseEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;

@RestController

@Api(description = "Set of endpoints for OCP Demo APP.")
public class HelloService {

	
	@RequestMapping(method = RequestMethod.GET, path = "")
	@ApiOperation(value = "Running hostname", notes = "Running hostname")
	// @formatter:off
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Some Sample Message"),
			@ApiResponse(code = 404, message = "Some Sample Message"),
			@ApiResponse(code = 400, message = "Some Sample Message"),
			@ApiResponse(code = 500, message = "Some Sample Message") })
	// @formatter:on
	public ResponseEntity<String> defaultService() throws UnknownHostException {
		return ResponseEntity.ok().body("Response from HostName : "+InetAddress.getLocalHost().getHostName());
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/api/health")
	@ApiOperation(value = "Return 200 Healthy Response", notes = "Return 200 Healthy Response")
	// @formatter:off
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Some Sample Message"),
			@ApiResponse(code = 404, message = "Some Sample Message"),
			@ApiResponse(code = 400, message = "Some Sample Message"),
			@ApiResponse(code = 500, message = "Some Sample Message") })
	// @formatter:on
	public ResponseEntity<String> health() {
		return ResponseEntity.ok().body("App is Healthy");
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/api/hello")
	@ApiOperation(value = "Return Hello OCP", notes = "Return Hello OCP")
	// @formatter:off
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Some Sample Message"),
			@ApiResponse(code = 404, message = "Some Sample Message"),
			@ApiResponse(code = 400, message = "Some Sample Message"),
			@ApiResponse(code = 500, message = "Some Sample Message") })
	// @formatter:on
	public String hello() {
		return "hello OCP";
	}

	@RequestMapping(method = RequestMethod.GET, path = "/api/hello/{name}")
	@ApiOperation(value = "Return Hello + Name", notes = "Return Hello + Name")
	// @formatter:off
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Some Sample Message"),
			@ApiResponse(code = 404, message = "Some Sample Message", 
			responseHeaders = {
					@ResponseHeader(name = "Privacy-Declaration"), 
					@ResponseHeader(name = "X-Request-ID"),
					@ResponseHeader(name = "X-Correlation-ID"), 
					@ResponseHeader(name = "API-Version"),
					@ResponseHeader(name = "Backend-Version") }),
			@ApiResponse(code = 400, message = "Some Sample Message"),
			@ApiResponse(code = 500, message = "Some Sample Message") })
	// @formatter:on

	public String helloName(@ApiParam("name.") @PathVariable(value = "name") String name) {
		return "hello " + name;
	}
}
