# Start with Eclipse Temurin 21 (OpenJDK) on Alpine Linux
FROM eclipse-temurin:21-jdk-alpine

# Create a directory inside the container for our app
WORKDIR /app

# Copy the JAR from our Gradle build (found in build/libs/ after 'gradlew build')
COPY build/libs/product-service-0.0.1-SNAPSHOT.jar product-service.jar

# Expose port 8080 (Spring Boot default)
EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java", "-jar", "product-service.jar"]
