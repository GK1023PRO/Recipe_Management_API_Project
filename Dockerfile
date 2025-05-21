# Dockerfile - Optimized for Render
FROM maven:3.8.5-eclipse-temurin-17 AS build
WORKDIR /app

# Copy dependencies first to leverage Docker cache
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage with smaller base image
FROM eclipse-temurin:17-jre-alpine

# Working directory
WORKDIR /app

# Copy JAR from build stage
COPY --from=build /app/target/Recipe_Management_API_Project-1.0-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8080

# Define entry point with production profile
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]