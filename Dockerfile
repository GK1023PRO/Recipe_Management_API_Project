# Build stage
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

# Install bash and curl for Render health checks
RUN apk add --no-cache bash curl

# Create non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Add application
WORKDIR /app
COPY --from=build /app/target/Recipe_Management_API_Project-1.0-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8080

# Add health check
HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=3 \
  CMD curl -f http://localhost:8080/api/health || exit 1

# Set entry point
ENTRYPOINT ["java", "-jar", "app.jar"]