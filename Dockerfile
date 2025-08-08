# Use the official Gradle image with JDK 20 for building the application
FROM gradle:8.4-jdk20 AS build

# Set the working directory
WORKDIR /app

# Copy the project files
COPY . .

# Build the project to generate the JAR file
RUN gradle build --no-daemon

# Use the official OpenJDK 20 image for running the application
FROM openjdk:20-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the port the application runs on
EXPOSE 8080

# Set the entry point to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
