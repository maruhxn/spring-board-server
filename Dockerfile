# Use the official OpenJDK base image
FROM openjdk:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container at /app
COPY build/libs/board-server-*.jar /app/app.jar

RUN mkdir -p /app/upload

# Set the Spring profile to "prod"
ENV SPRING_PROFILES_ACTIVE=prod

# Expose the port that the application will run on
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]
