FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/Recipe_Management_API_Project-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

# Use Maven to build the JAR
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY src ./src
COPY pom.xml .
RUN mvn clean package

# Final image
FROM openjdk:17
COPY --from=build /app/target/Recipe_Management_API_Project-1.0-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]