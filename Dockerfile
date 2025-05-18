FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/Recipe_Management_API_Project-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

# Build stage
FROM maven:3.8.5-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests && ls -l target

# Runtime stage
FROM eclipse-temurin:17-jdk-alpine
COPY --from=build /app/target/Recipe_Management_API_Project-1.0-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
