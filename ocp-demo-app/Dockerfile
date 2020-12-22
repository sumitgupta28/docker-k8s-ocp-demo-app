FROM maven:3.6.0-jdk-8-slim AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package

FROM openjdk-8-rhel8:1.2
COPY --from=build /usr/src/app/target/ocp-demo-app*SNAPSHOT.jar /usr/app/ocp-demo-app.jar 
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/app/ocp-demo-app.jar"]


