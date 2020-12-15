package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/api")
public class HelloService {

	@RequestMapping(method=RequestMethod.GET,path="/hello")
	public String hello(){
		return "hello OCP";
	}
}
