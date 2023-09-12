# Use an official OpenJDK runtime as a parent image
FROM openjdk:18

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container at /app
COPY target/g1-0.0.1-SNAPSHOT.jar /app/

# Expose the port your Spring Boot app will run on (if needed)
EXPOSE 8081

# Define the command to run your application
CMD ["java", "-jar", "g1-0.0.1-SNAPSHOT.jar"]
