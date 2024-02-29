# Use official OpenJDK 17 image as base image
FROM openjdk:17-oracle

LABEL authors="ua.com.javarush.abstract"

# Set the working directory inside the container
WORKDIR /app

# Copy the packaged Spring Boot application JAR file into the container at path /app
COPY target/*.jar /app/app.jar

# Expose the port that your application runs on
EXPOSE 8080

# Command to run the Spring Boot application when the container starts
CMD ["java", "-jar", "app.jar"]
