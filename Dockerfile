# Build stage
FROM maven:3.8.5-eclipse-temurin-17 AS build
WORKDIR /app

# Copy Maven files
COPY pom.xml .
COPY src ./src

# Clean and compile
RUN mvn clean compile

# Generate JavaDoc first
RUN mvn javadoc:javadoc

# Copy JavaDoc to resources directory
RUN mkdir -p src/main/resources/static/OOPDocumentationJavaDoc
RUN cp -r target/site/apidocs/* src/main/resources/static/OOPDocumentationJavaDoc/ || true

# Build the application
RUN mvn package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the built JAR
COPY --from=build /app/target/Recipe_Management_API_Project-1.0-SNAPSHOT.jar /app.jar

# Expose port
EXPOSE 8080

# Start the application
ENTRYPOINT ["java", "-jar", "/app.jar"]