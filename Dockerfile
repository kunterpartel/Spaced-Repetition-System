# Use the official Gradle image to build the application
FROM gradle:8.0-jdk17 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the Gradle wrapper and build scripts
COPY gradle /app/gradle
COPY gradlew /app/
COPY /card-service/build.gradle.kts /app/
COPY /card-service/settings.gradle.kts /app/

# Download Gradle dependencies
RUN ./gradlew dependencies

# Copy the source code
COPY /card-service/src /app/src

# Build the application
RUN ./gradlew build --no-daemon

# Use the official OpenJDK image to run the application
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the builder stage
COPY --from=builder /app/build/libs/*.jar /app/app.jar

# Expose the port that the application will run on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
