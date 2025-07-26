FROM ubuntu:latest
LABEL authors="Z00GHMG"

ENTRYPOINT ["top", "-b"]

# Stage 1: Build the application
FROM eclipse-temurin:17-jdk-alpine as build

WORKDIR /app

# Copy Gradle wrapper and build files
COPY gradlew build.gradle settings.gradle /app/
COPY gradle /app/gradle

# Copy source code
COPY src /app/src

# Give permission and build
RUN chmod +x gradlew && ./gradlew clean build -x test

# Stage 2: Create a minimal runtime image
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy built jar from previous stage
COPY --from=build /app/build/libs/*.jar app.jar

# Create directory for file uploads
RUN mkdir -p /app/uploads

# Expose application port (default Spring Boot port)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
