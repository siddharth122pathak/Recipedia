# ---- Step 1: Build the app ----
    FROM maven:3.8.7-eclipse-temurin-17 AS build

    # Set working directory
    WORKDIR /app
    
    # Copy the project files (excluding target/)
    COPY pom.xml mvnw ./
    COPY src ./src
    
    # Grant execution permission for Maven wrapper
    RUN chmod +x mvnw
    
    # Build the application
    RUN ./mvnw clean package -DskipTests
    
    # ---- Step 2: Run the app ----
    FROM eclipse-temurin:17-jdk
    
    # Set working directory
    WORKDIR /app
    
    # Copy the built JAR from the first stage
    COPY --from=build /app/target/*.jar app.jar
    
    # Expose application port
    EXPOSE 8080
    
    # Start the application
    CMD ["java", "-jar", "app.jar"]