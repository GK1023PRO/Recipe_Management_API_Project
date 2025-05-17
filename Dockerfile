FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/Recipe_Management_API_Project-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
