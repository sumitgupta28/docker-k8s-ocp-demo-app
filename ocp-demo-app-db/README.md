# OCP Demo APP


*	this is simple application with curd api's for employee model.

	```
	  {
	    "emailId": "string",
	    "firstName": "string",
	    "id": 0,
	    "lastName": "string"
	  }
	```
*	Here are the list of api's 

![API's](images/emp-service-apis.JPG) 


*	This application is using **postgresql as database**.



# Build docker image and run

## Docker Compose

Compose is a tool for defining and running multi-container Docker applications. With Compose, you use a YAML file to configure your application’s services. 
Then, with a single command, you create and start all the services from your configuration. To learn more about all the features of Compose, see the list of features.
Compose works in all environments: production, staging, development, testing, as well as CI workflows. You can learn more about each case in Common Use Cases.
Using Compose is basically a three-step process:


1.  Define your app’s environment with a Dockerfile so it can be reproduced anywhere.
2.	Define the services that make up your app in docker-compose.yml so they can be run together in an isolated environment.
3.	Run docker-compose up and Compose starts and runs your entire app


Compose has commands for managing the whole lifecycle of your application:

*	Start, stop, and rebuild services
*	View the status of running services
*	Stream the log output of running services
*	Run a one-off command on a service

Make sure you are at folder /ocp-demo-app/ocp-demo-app-db and run below command

*   **build images**, this command will use the **DockerFile** to first create spring boot jar then build the image 
* 	This command will use the default file name **docker-compose.yml** 
* 	Here is the content of docker-compose.yml

	`
	version: '3.3'
	services:
	  postgresql:
	    image: 'postgres:13.1-alpine' 
	    container_name: postgresql
	    environment:
	     - POSTGRES_DB=sample_database
	     - POSTGRES_USER=postgresql_user
	     - POSTGRES_PASSWORD=postgresql_password
	  
	  ocp-demo-app-db:
	    build: .
	    container_name: ocp-demo-app-db
	    ports:
	      - 8080:8080
	    depends_on:
	      - postgresql
	    environment:
	      - POSTGRESQL_DB_NAME=sample_database
	      - POSTGRESQL_USER=postgresql_user
	      - POSTGRESQL_PASSWORD=postgresql_password
      `


*	Now lets **create/build** image for ocp-demo-app-db

	$ docker-compose build
	
	
* 	this command will create image for ocp-demo-app-db and you can verify this with docker images comamnd

	$ docker images
	
	
![Docker-compose build](images/compose-build.JPG) 
	
*   **run the application**, use below command to run the application


	$ docker-compose up -d

* 	This will also create a default network

![Docker-compose build](images/compose-up.JPG) 	


* 	here are the process,containers & images in use by docker-compose

![Docker-compose build](images/compose-all.JPG) 	


# Kubernetes


*	build image and push to docker hub	

*	use below commands to create image and tag and push to docker hub

*	go to folder /ocp-demo-app/ocp-demo-app-db

*	build image with required tag

	$ docker build . -t sumitgupta28/ocp-demo-app-db
	
*	this will create docker image with tag	**sumitgupta28/ocp-demo-app-db:latest**

*	docker login
	
	$ docker login

*	docker push

	$ docker push sumitgupta28/ocp-demo-app-db
	
	
![Docker Push](images/docker-push.JPG) 

![Docker Push](images/docker-push-1.JPG) 
	

