# Use a Windows-based Docker image with OpenJDK installed
FROM openjdk:11-jdk-windowsservercore AS build

# Set the working directory
WORKDIR /app

# Copy the Gradle wrapper files
COPY gradlew .
COPY gradle gradle

# Copy the build configuration files
COPY settings.gradle .
COPY build.gradle .

# Copy the project source code
COPY src src

# Download and cache the Gradle distribution
RUN .\gradlew --version

# Build the project
RUN .\gradlew build

# Use a Windows-based Docker image with OpenJDK installed
FROM openjdk:11-jdk-windowsservercore

# Set the working directory
WORKDIR /app

# Copy the JAR file from the previous build stage
COPY --from=build /app/build/libs/demo-1.jar app.jar

# Expose the desired port (if needed)
EXPOSE 8081

# Set the entry point command
ENTRYPOINT ["java", "-jar", "app.jar"]