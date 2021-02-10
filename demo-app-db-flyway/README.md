# Demo APP with flyway


*	This is simple application with curd api's for employee model.

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
* 	we will use fly-way as db migration tool.



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

Make sure you are at folder /demo-app-db-flyway and run below command

*   **build images**, this command will use the **DockerFile** to first create spring boot jar then build the image 
* 	This command will use the default file name **docker-compose.yml** 
* 	Here is the content of docker-compose.yml

	version: '3.3'
	services:
	  postgresql:
	    image: 'postgres:13.1-alpine' 
	    container_name: postgresql
	    environment:
	     - POSTGRES_DB=sample_database
	     - POSTGRES_USER=postgresql_user
	     - POSTGRES_PASSWORD=postgresql_password
	  
	  demo-app-db-fly-way:
	    build: .
	    container_name: demo-app-db-fly-way
	    ports:
	      - 8080:8080
	    depends_on:
	      - postgresql
	    environment:
	      - POSTGRESQL_DB_NAME=sample_database
	      - POSTGRESQL_USER=postgresql_user
	      - POSTGRESQL_PASSWORD=postgresql_password
	      - POSTGRESQL_SERVICE_NAME=postgresql
	      - POSTGRESQL_PORT=5432
		


*	Now lets **create/build** image for demo-app-db-flyway

		$ docker-compose build
	
	
* 	this command will create image for demo-app-db-flyway and you can verify this with docker images comamnd


		$ docker images
		REPOSITORY                                   TAG                                              IMAGE ID            CREATED             SIZE
		demo-app-db-flyway_demo-app-db-fly-way       latest                                           ab11d85d4436        3 minutes ago       316MB
	
*   **run the application**, use below command to run the application


		$ docker-compose ps
		Name   Command   State   Ports
		------------------------------
		
		$ docker-compose up -d
		Creating postgresql ... done
		Creating demo-app-db-fly-way ... done
		
		$ docker-compose ps
		       Name                      Command               State           Ports
		-------------------------------------------------------------------------------------
		demo-app-db-fly-way   java -jar /usr/app/demo-ap ...   Up      0.0.0.0:8080->8080/tcp
		postgresql            docker-entrypoint.sh postgres    Up      5432/tcp
			
		$ docker images
		REPOSITORY                                   TAG                                              IMAGE ID            CREATED             SIZE
		demo-app-db-flyway_demo-app-db-fly-way       latest                                           ab11d85d4436        8 minutes ago       316MB
		postgres                                     13.1-alpine                                      8c6053d81a45        12 days ago         159MB


# Kubernetes


*	build image and push to docker hub	

*	use below commands to create image and tag and push to docker hub

*	go to folder /demo-app-db-flyway

*	build image with required tag

	$ docker build . -t sumitgupta28/demo-app-db-flyway
	
*	this will create docker image with tag	**sumitgupta28/demo-app-db-flyway:latest**

*	docker login
	
	$ docker login

*	docker push

	$ docker push sumitgupta28/demo-app-db-flyway
	
	
![Docker Push](images/docker-push.JPG) 

![Docker Push](images/docker-push-1.JPG) 



*	go to folder **/ocp-demo-app/demo-app-db-flyway/kubernetes** and run below command

	```
	$ kubectl apply -f .
	deployment.apps/demo-app-db-flyway created
	service/demo-app-db-flyway created
	configmap/postgres-configuration created
	service/postgresql created
	statefulset.apps/postgres-statefulset created
	persistentvolume/postgres-pv created
	persistentvolumeclaim/postgres-pv-claim created
	```

![ocp-demo-app](images/demo-app-db-flyway.png) 


*	This command will execute all the yml files under the kubernetes folder and create below kubernetes objects 

	```
	$ kubectl get all
	NAME                                   READY   STATUS    RESTARTS   AGE
	pod/demo-app-db-flyway-776db59bf4-845dx   1/1     Running   0          24m
	pod/demo-app-db-flyway-776db59bf4-gmvr8   1/1     Running   0          24m
	pod/demo-app-db-flyway-776db59bf4-ltnrh   1/1     Running   0          24m
	pod/postgres-statefulset-0             1/1     Running   0          26m
	
	NAME                      TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)          AGE
	service/demo-app-db-flyway   NodePort    10.110.127.32   <none>        8080:30080/TCP   14m
	service/postgresql        NodePort    10.98.240.12    <none>        5432:32760/TCP   26m
	
	NAME                              READY   UP-TO-DATE   AVAILABLE   AGE
	deployment.apps/demo-app-db-flyway   3/3     3            3           24m
	
	NAME                                         DESIRED   CURRENT   READY   AGE
	replicaset.apps/demo-app-db-flyway-776db59bf4   3         3         3       24m
	
	NAME                                    READY   AGE
	statefulset.apps/postgres-statefulset   1/1     26m

	```

*	**persistentvolume/postgres-pv** - this is a Persistent Volume for postgres database

*	**persistentvolumeclaim/postgres-pv-claim** - this is a Persistent Volume Claim for postgres-pv postgres database

*	**configmap/postgres-configuration** - this is a config map for possgres sql credentials  

*	**statefulset.apps/postgres-statefulset** - This is pod for postgres sql 

*	**service/postgresql** - postgre-sql exposed as postgresql service name

* 	**deployment.apps/demo-app-db-flyway** - This is deployment for **sumitgupta28/demo-app-db-flyway:latest** image with 3 replicas

*	**service/demo-app-db-flyway** - This is a service for demo-app-db-flyway deployemnt.

*	application can be accessible as http://<<hostname>>:30080/swagger-ui.html

![emp-service-demo.JPG](images/emp-service-demo.JPG) 


*	Can verify the swagger json a http://<<hostname>>:30080/v2/api-docs

	```
		{
	   "swagger":"2.0",
	   "info":{
	      "description":"OCP Demo Application",
	      "version":"1.0.0",
	      "title":"OCP Demo Application",
	      "contact":{
	         "name":"Sumit Gupta",
	         "url":"www.ocp-learning-fake.net",
	         "email":"sumitgupta28@gmail.com"
	      },
	      "license":{
	         
	      }
	   },
	   "host":"2886795294-30080-elsy05.environments.katacoda.com",
	   "basePath":"/",
	   "tags":[
	      {
	         "name":"employee-controller",
	         "description":"Set of endpoints for Creating, Retrieving, Updating and Deleting of Employees."
	      }
	   ],
	   "paths":{
	      "/api/v1/employees":{
	         "get":{
	            "tags":[
	               "employee-controller"
	            ],
	            "summary":"Returns list of all Employees in the system.",
	            "operationId":"getAllEmployeesUsingGET",
	            "consumes":[
	               "application/json"
	            ],
	            "produces":[
	               "application/json"
	            ],
	            "responses":{
	               "200":{
	                  "description":"OK",
	                  "schema":{
	                     "type":"array",
	                     "items":{
	                        "$ref":"#/definitions/Employee"
	                     }
	                  }
	               },
	               "401":{
	                  "description":"Unauthorized"
	               },
	               "403":{
	                  "description":"Forbidden"
	               },
	               "404":{
	                  "description":"Not Found"
	               }
	            }
	         },
	         "post":{
	            "tags":[
	               "employee-controller"
	            ],
	            "summary":"Creates a new Employee.",
	            "operationId":"createEmployeeUsingPOST",
	            "consumes":[
	               "application/json"
	            ],
	            "produces":[
	               "application/json"
	            ],
	            "parameters":[
	               {
	                  "in":"body",
	                  "name":"employee",
	                  "description":"employee",
	                  "required":true,
	                  "schema":{
	                     "$ref":"#/definitions/Employee"
	                  }
	               }
	            ],
	            "responses":{
	               "200":{
	                  "description":"OK",
	                  "schema":{
	                     "$ref":"#/definitions/Employee"
	                  }
	               },
	               "201":{
	                  "description":"Created"
	               },
	               "401":{
	                  "description":"Unauthorized"
	               },
	               "403":{
	                  "description":"Forbidden"
	               },
	               "404":{
	                  "description":"Not Found"
	               }
	            }
	         }
	      },
	      "/api/v1/employees/{employeeId}":{
	         "get":{
	            "tags":[
	               "employee-controller"
	            ],
	            "summary":"Returns a specific Employee by their identifier. 404 if does not exist.",
	            "operationId":"getEmployeeByIdUsingGET",
	            "consumes":[
	               "application/json"
	            ],
	            "produces":[
	               "application/json"
	            ],
	            "parameters":[
	               {
	                  "name":"employeeId",
	                  "in":"path",
	                  "description":"Id of the employee to be obtained. Cannot be empty.",
	                  "required":false,
	                  "type":"integer",
	                  "format":"int32"
	               }
	            ],
	            "responses":{
	               "200":{
	                  "description":"OK",
	                  "schema":{
	                     "$ref":"#/definitions/Employee"
	                  }
	               },
	               "401":{
	                  "description":"Unauthorized"
	               },
	               "403":{
	                  "description":"Forbidden"
	               },
	               "404":{
	                  "description":"Not Found"
	               }
	            }
	         },
	         "put":{
	            "tags":[
	               "employee-controller"
	            ],
	            "summary":"updateEmployee",
	            "operationId":"updateEmployeeUsingPUT",
	            "consumes":[
	               "application/json"
	            ],
	            "produces":[
	               "*/*"
	            ],
	            "parameters":[
	               {
	                  "name":"employeeId",
	                  "in":"path",
	                  "description":"employeeId",
	                  "required":true,
	                  "type":"integer",
	                  "format":"int32"
	               },
	               {
	                  "in":"body",
	                  "name":"employeeDetails",
	                  "description":"employeeDetails",
	                  "required":true,
	                  "schema":{
	                     "$ref":"#/definitions/Employee"
	                  }
	               }
	            ],
	            "responses":{
	               "200":{
	                  "description":"OK",
	                  "schema":{
	                     "$ref":"#/definitions/Employee"
	                  }
	               },
	               "201":{
	                  "description":"Created"
	               },
	               "401":{
	                  "description":"Unauthorized"
	               },
	               "403":{
	                  "description":"Forbidden"
	               },
	               "404":{
	                  "description":"Not Found"
	               }
	            }
	         }
	      },
	      "/api/v1/employees/{id}":{
	         "delete":{
	            "tags":[
	               "employee-controller"
	            ],
	            "summary":"Deletes a employee from the system. 404 if the employee identifier is not found.",
	            "operationId":"deleteEmployeeUsingDELETE",
	            "consumes":[
	               "application/json"
	            ],
	            "produces":[
	               "*/*"
	            ],
	            "parameters":[
	               {
	                  "name":"id",
	                  "in":"path",
	                  "description":"Id of the Employee to be deleted. Cannot be empty.",
	                  "required":false,
	                  "type":"integer",
	                  "format":"int32"
	               }
	            ],
	            "responses":{
	               "200":{
	                  "description":"OK",
	                  "schema":{
	                     "type":"object",
	                     "additionalProperties":{
	                        "type":"boolean"
	                     }
	                  }
	               },
	               "401":{
	                  "description":"Unauthorized"
	               },
	               "204":{
	                  "description":"No Content"
	               },
	               "403":{
	                  "description":"Forbidden"
	               }
	            }
	         }
	      }
	   },
	   "definitions":{
	      "Employee":{
	         "type":"object",
	         "properties":{
	            "emailId":{
	               "type":"string"
	            },
	            "firstName":{
	               "type":"string"
	            },
	            "id":{
	               "type":"integer",
	               "format":"int32"
	            },
	            "lastName":{
	               "type":"string"
	            }
	         }
	      }
	   }
	}
	```
