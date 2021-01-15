# OCP Demo Applications

This repository contains sample application to demonstrate how an application can be build/deployed on various platforms 
- 	**Docker**
- 	**Kubernates**
- 	**Openshift**


## **Technologies**
*	Java 8
*	Spring boot & Spring Cloud
*	Postgresql
*	Zookeeper & Kafka
*	Docker [[Docker Play Ground](https://labs.play-with-docker.com/ "")] 
*	Kubernates [[Kubernetes Play Ground](https://www.katacoda.com/courses/kubernetes/ "")] 
*	Openshift [[Openshift45 Play Ground](https://learn.openshift.com/playgrounds/openshift45/ "")] 


## **Applications**

- [Simple Rest Service](/ocp-demo-app/README.md) 
	
	- How to deploy the /ocp-demo-app [ a simple rest service] using Docker and Kubernetes. 

- [Simple Rest Service on OCP with S2I-Source](/ocp-demo-app/README-ocp.md) 
	
	- How to deploy the /ocp-demo-app [ a simple rest service] on OCP using S2I[Source] build Strategy 

- [Simple Rest Service on OCP with DockerFile & Docker Image](/ocp-demo-app/README-ocp-docker.md) 
	
	- How to deploy the /ocp-demo-app [ a simple rest service] on OCP using DockerFile & Docker Image build Strategy 


- [CURD Rest Service with Postgresql](/ocp-demo-app-db/README.md)

	- How to deploy the /ocp-demo-app-db [ a simple rest service with Postgresql] using 
		-	docker , 
		- 	docker-compose and 
		- 	Kubernetes StatefulSets


- [Simple Rest Service with Kakfa Topic](/ocp-demo-app-kafka/README.md)

	- How to deploy the /ocp-demo-app-kafka [ a simple rest service with kafka topic] using 
		-	docker , 
		- 	docker-compose and 
		- 	Kubernetes

- [Simple Rest Service with Kakfa Stream](/ocp-demo-app-kafka-stream/README.md)

	- How to deploy the /ocp-demo-app-kafka-stream [ a simple rest service with kafka stream] using 
		-	docker , 
		- 	docker-compose and 
		- 	Kubernetes

- [Simple Rest Service implementing Request - Response Pattern with Kakfa](/ocp-demo-app-kafka-sync/README.md)

	- How to deploy the /ocp-demo-app-kafka-sync [ Simple Rest Service implementing Request - Response Pattern with Kakfa] using 
		-	docker , 
		- 	docker-compose and 
		- 	Kubernetes
