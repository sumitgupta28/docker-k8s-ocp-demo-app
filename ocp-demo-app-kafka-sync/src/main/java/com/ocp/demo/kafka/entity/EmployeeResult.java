package com.ocp.demo.kafka.entity;

public class EmployeeResult {

	private String name;
	private String sal;

	public EmployeeResult(String name, String sal) {
		super();
		this.name = name;
		this.sal = sal;
	}

	public EmployeeResult() {
		super();

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSal() {
		return sal;
	}

	public void setSal(String sal) {
		this.sal = sal;
	}

	@Override
	public String toString() {
		return "EmployeeResult [name=" + name + ", sal=" + sal + "]";
	}

}
