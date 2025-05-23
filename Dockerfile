# ===================
# Build stage
# ===================
FROM maven:3.8.5-eclipse-temurin-17 AS build

WORKDIR /app

# Copy Maven files
COPY pom.xml .
COPY src ./src

# Clean build and generate jar with javadoc
RUN mvn clean \
 && mvn dependency:purge-local-repository -DactTransitively=false -DreResolve=false || true \
 && mvn compile \
 && mvn javadoc:javadoc \
 && mvn package -DskipTests

# ===================
# Runtime stage
# ===================
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Install curl for health checks
RUN apk add --no-cache curl

# Copy the built jar
COPY --from=build /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Health check endpoint
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/api/health || exit 1

# Run the application
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
