###############################################################################
# Stage 1: Build and Test
FROM maven:3.9.11-eclipse-temurin-24 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the project files
COPY pom.xml .
RUN mvn dependency:go-offline -B -e

COPY src ./src

# Run Maven package tests
RUN mvn clean package -DskipTests=false

###############################################################################
# Stage 2: Create the final runtime image
FROM openjdk:23-jdk

# Set the working directory
WORKDIR /app


# Copy the built JAR from the build stage
# Define the entry point to run the CLI application

COPY --from=build /app/target/traceip-jar-with-dependencies.jar traceip.jar
ENTRYPOINT ["java", "-jar", "traceip.jar"]

# COPY --from=build /app /app
# ENTRYPOINT ["java", "-jar", "target/traceip-jar-with-dependencies.jar"]
