# ---- Step 1: Build the app ----
    FROM maven:3.8.7-eclipse-temurin-17 AS build

    # Set working directory
    WORKDIR /app
    
    # Copy the project files
    COPY pom.xml ./
    COPY src ./src
    
    # Build the application
    RUN mvn clean package -DskipTests
    
    # ---- Step 2: Run the app ----
    FROM eclipse-temurin:17-jdk
    
    # Set working directory
    WORKDIR /app
    
    # Copy the built JAR from the first stage
    COPY --from=build /app/target/*.jar app.jar
    
    # Expose application port
    EXPOSE 8080
    
    # Start the application
    CMD ["java", "-jar", "app.jar", "--server.port=10000"]    