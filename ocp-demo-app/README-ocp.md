## Deploying ocp-demo-app into OpenShift Container

*	Ocp Container can deploy application in various way like S2I [Source to Image] , Docker File and Docker Image. 

### S2I [Source to Image] 

![](https://www.admin-magazine.com/var/ezflow_site/storage/images/archive/2018/47/automatic-build-and-deploy-with-openshift-and-gitlab-ci/figure-7/155512-1-eng-US/Figure-7_large.png) 

*	Login as Developer	
	
	$ oc login -u developer -p developer

######output 

	Login successful.
	
	You don't have any projects. You can try to create a new project, by running
	
	    oc new-project <projectname>
	    
	    
*	Create a new Project	
	  
	$ oc new-project ocp-demo

######output 

	Now using project "ocp-demo" on server "https://openshift:6443".
	
	You can add applications to this project with the 'new-app' command. For example, try:
	
	    oc new-app ruby~https://github.com/sclorg/ruby-ex.git
	
	to build a new example application in Ruby. Or use kubectl to deploy a simple Kubernetes application:
	
	    kubectl create deployment hello-node --image=gcr.io/hello-minikube-zero-install/hello-node	  
	  
*	Lets deploy simple ocp-demo-app 

	$ oc new-app java:8~https://github.com/sumitgupta28/ocp-demo-app.git --context-dir=/ocp-demo-app --strategy=source	
	
Here 
1.	**[java:8]** is Builder Image Version
2.	**[https://github.com/sumitgupta28/ocp-demo-app.git]** is git hub url
3.	**[--context-dir=/ocp-demo-app]** subfolder which needs to build and deploy 	
4.	**[ --strategy=source]** build Strategy source to image
	

######Output

	--> Found image 46b0b4c (6 months old) in image stream "openshift/java" under tag "8" for "java:8"
	
	    Java Applications
	    -----------------
	    Platform for building and running plain Java applications (fat-jar and flat classpath)
	
	    Tags: builder, java
	
	    * A source build using source code from https://github.com/sumitgupta28/ocp-demo-app.git will be created
	      * The resulting image will be pushed to image stream tag "ocp-demo-app:latest"
	      * Use 'oc start-build' to trigger a new build
	
	--> Creating resources ...
	    imagestream.image.openshift.io "ocp-demo-app" created
	    buildconfig.build.openshift.io "ocp-demo-app" created
	    deployment.apps "ocp-demo-app" created
	    service "ocp-demo-app" created
	--> Success
	    Build scheduled, use 'oc logs -f bc/ocp-demo-app' to track its progress.
	    Application is not exposed. You can expose services to the outside world by executing one or more of the commands below:
	     'oc expose svc/ocp-demo-app'
	    Run 'oc status' to view your app.


*	**oc status** - Show overview of current project resources 
  
	 $ oc status

######Output

	In project ocp-demo on server https://openshift:6443
	
	svc/ocp-demo-app - 172.25.97.124 ports 8080, 8443, 8778
	  deployment/ocp-demo-app deploys istag/ocp-demo-app:latest <-
	    bc/ocp-demo-app source builds https://github.com/sumitgupta28/ocp-demo-app.git on openshift/java:8
	    deployment #2 running for 4 minutes - 1 pod
	    deployment #1 deployed 6 minutes ago
	
	
	1 info identified, use 'oc status --suggest' to see details.
	
	
	
*	**oc get all** - Lets see what all resource got created post this deployment
		

	$ oc get all
	NAME                                READY   STATUS      RESTARTS   AGE
	pod/ocp-demo-app-1-build            0/1     Completed   0          14m
	pod/ocp-demo-app-7478f8c9cb-5v46m   1/1     Running     0          12m
		
	NAME                   TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)                      AGE
	service/ocp-demo-app   ClusterIP   172.25.97.124   <none>        8080/TCP,8443/TCP,8778/TCP   14m
		
	NAME                           READY   UP-TO-DATE   AVAILABLE   AGE
	deployment.apps/ocp-demo-app   1/1     1            1           14m
		
	NAME                                      DESIRED   CURRENT   READY   AGE
	replicaset.apps/ocp-demo-app-649f4c6cbc   0         0         0       14m
	replicaset.apps/ocp-demo-app-7478f8c9cb   1         1         1       12m
		
	NAME                                          TYPE     FROM   LATEST
	buildconfig.build.openshift.io/ocp-demo-app   Source   Git    1
		
	NAME                                      TYPE     FROM          STATUS     STARTED          DURATION
	build.build.openshift.io/ocp-demo-app-1   Source   Git@0f4226a   Complete   14 minutes ago   1m19s
		
	NAME                                          IMAGE REPOSITORY                                                                TAGS     UPDATED
	imagestream.image.openshift.io/ocp-demo-app   default-route-openshift-image-registry.apps-crc.testing/ocp-demo/ocp-demo-app   latest   12 minutes ago
	
	
![](images/ocp-all.jpg "") 

#### Lets Understand these

*	**buildconfig.build.openshift.io/ocp-demo-app** - This a build configuration to build a docker image from source.  
	
*	**pod/ocp-demo-app-1-build** - Pod to build the source to image

*	**imagestream.image.openshift.io/ocp-demo-app** - image stream to store the ocp-demo-app image

* 	**deployment.apps/ocp-demo-app** - deployment to deploy the ocp-demo-app image

* 	**replicaset.apps/ocp-demo-app** - replica set for deployment

* 	**pod/ocp-demo-app-7478f8c9cb-5v46m** - Pod running the ocp-demo-app 

* 	**service/ocp-demo-app** - ocp-demo-app demo app service


#### final step, lets expose service. 

	$ oc expose service ocp-demo-app
	route.route.openshift.io/ocp-demo-app exposed
	
	
This will create a route 
	
	$ oc get routes.route.openshift.io
	NAME           HOST/PORT                                                                PATH   SERVICES       PORT       TERMINATION   WILDCARD
	ocp-demo-app   ocp-demo-app-ocp-demo.2886795273-80-host19nc.environments.katacoda.com          ocp-demo-app   8080-tcp                 None
	
	
Now test application 	
	
	$ curl http://ocp-demo-app-ocp-demo.2886795273-80-host19nc.environments.katacoda.com/api/hello
	hello OCP

	$ curl http://ocp-demo-app-ocp-demo.2886795273-80-host19nc.environments.katacoda.com/api/hello/Sumit
	hello Sumit$