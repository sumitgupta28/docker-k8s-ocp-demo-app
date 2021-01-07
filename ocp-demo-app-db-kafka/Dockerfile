FROM maven:3.6.0-jdk-8-slim AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package

FROM openjdk:8-jre
COPY --from=build /usr/src/app/target/ocp-demo-app-db-kafka*SNAPSHOT.jar /usr/app/ocp-demo-app-db-kafka.jar 
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/app/ocp-demo-app-db-kafka.jar"]


