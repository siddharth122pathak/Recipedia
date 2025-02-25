# Use an official OpenJDK image from DockerHub
FROM eclipse-temurin:17-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven wrapper and project files
COPY mvnw pom.xml ./
COPY src ./src

# Grant execution permission for the Maven wrapper
RUN chmod +x mvnw

# Build the application inside the container
RUN ./mvnw clean package -DskipTests

# Copy the built JAR file
COPY target/*.jar app.jar

# Expose the application port (Change if needed)
EXPOSE 8080

# Start the application
CMD ["java", "-jar", "app.jar"]