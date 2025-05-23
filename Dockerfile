# Build stage
FROM maven:3.8.5-eclipse-temurin-17 AS build

WORKDIR /app

# Copy Maven files
COPY pom.xml .
COPY src ./src

# Force complete clean build
RUN mvn clean -U
RUN mvn dependency:purge-local-repository -DactTransitively=false -DreResolve=false || true
RUN mvn compile -U
RUN mvn javadoc:javadoc -U
RUN mvn package -DskipTests -U

# Runtime stage
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Install curl for health checks
RUN apk add --no-cache curl

# Copy the packaged jar
COPY --from=build /app/target/*.jar app.jar

# Create static directory and copy JavaDoc if it exists
RUN mkdir -p /app/static/OOPDocumentationJavaDoc
RUN cp -r /app/target/classes/static/OOPDocumentationJavaDoc/. /app/static/OOPDocumentationJavaDoc/ 2>/dev/null || true

# Set proper permissions
RUN chmod 755 /app/static && chmod -R 644 /app/static/* || true

# Expose port
EXPOSE 10000

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/api/health || exit 1

# Run the application
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]