# Use official Eclipse Temurin Java 17 image
FROM eclipse-temurin:17-jdk-alpine AS build

# Set working directory
WORKDIR /app

# Copy Maven wrapper, pom.xml, and source
COPY pom.xml .
COPY src ./src

# Install Maven
RUN apk add --no-cache maven && mvn clean package -DskipTests

# ---- Runtime Stage ----
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the JAR from build stage
COPY --from=build /app/target/doctor-website-0.0.1-SNAPSHOT.jar app.jar

# Expose port (Render uses PORT env var)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
