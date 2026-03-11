# Docker Setup Guide for Restaurant Billing System


## Overview
This guide provides step-by-step instructions to containerize and run the Restaurant Billing System using Docker and Docker Compose.

---

## 1. Dockerfile
# Dockerfile - Defines how to build the application container image
FROM eclipse-temurin:17-jdk-jammy  # Base image: OpenJDK 17 on Ubuntu Jammy

WORKDIR /app  # Set working directory inside container

COPY target/billing-system-0.0.1-SNAPSHOT.jar app.jar  # Copy built JAR file to container

EXPOSE 9000  # Expose port 9000 for the Spring Boot application

ENTRYPOINT ["java","-jar","app.jar"]  # Command to run the application when container starts

---

## 2. Build Docker Image
# Build the Docker image for the billing system application
# This creates a container image from the Dockerfile
docker build -t billing-system .

# Explanation:
# - docker build: Command to build Docker image
# - -t billing-system: Tag the image with name "billing-system"
# - .: Build context (current directory containing Dockerfile)

---

## 3. Run Individual Container (Optional)
# Run the application container without database
# Note: This won't work without database connection
docker run -p 9000:9000 billing-system

# Explanation:
# - docker run: Command to run a container
# - -p 9000:9000: Map host port 9000 to container port 9000
# - billing-system: Image name to run

---

## 4. docker-compose.yml
version: "3.5"  # Docker Compose version specification

services:  # Define services that make up the application stack

  postgres:
    image: postgres:17  # Use PostgreSQL version 17
    container_name: postgres-db  # Custom container name
    ports:
      - "5433:5432"  # Map host port 5433 to container port 5432
    environment:
      POSTGRES_DB: restaurant_db  # Database name
      POSTGRES_USER: postgres  # Database username
      POSTGRES_PASSWORD: root  # Database password
    volumes:
      - postgres_data:/var/lib/postgresql/data  # Persist database data

  billing-app:
    build: .  # Build image from Dockerfile in current directory
    ports:
      - "9000:9000"  # Map host port 9000 to container port 9000
    depends_on:
      - postgres  # Wait for postgres service to start first
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/restaurant_db  # Database connection URL
      SPRING_DATASOURCE_USERNAME: postgres  # Database username
      SPRING_DATASOURCE_PASSWORD: root  # Database password

volumes:
  postgres_data:  # Named volume for data persistence

---

## 5. Run with Docker Compose
# Start all services defined in docker-compose.yml
docker-compose up

# For running in background (detached mode):
docker-compose up -d

# Explanation:
# - docker-compose up: Start all services
# - -d flag: Run in detached mode (background)
# - This will:
#   1. Start PostgreSQL container with persistent data
#   2. Build and start the application container
#   3. Connect the application to the database
#   4. Map ports for external access

---

## 6. Additional Useful Commands

### Stop Services
# Stop all running services
docker-compose down

# Stop and remove volumes (deletes database data)
docker-compose down -v

### View Logs
# View logs for all services
docker-compose logs

# View logs for specific service
docker-compose logs billing-app
docker-compose logs postgres

# Follow logs in real-time
docker-compose logs -f

### Rebuild Application
# Force rebuild of application image
docker-compose up --build

# Build without starting
docker-compose build

### Database Access
# Connect to PostgreSQL database
docker exec -it postgres-db psql -U postgres -d restaurant_db

---

## 7. Application URLs
Once running, access the application at:
- **Main Application**: http://localhost:9000
- **Swagger UI**: http://localhost:9000/swagger-ui
- **API Docs**: http://localhost:9000/api-docs

---

## 8. Prerequisites
Ensure you have:
1. Docker installed and running
2. Docker Compose installed
3. Maven build completed (target/billing-system-0.0.1-SNAPSHOT.jar exists)

---

## 9. Troubleshooting

### Port Conflicts
If port 5433 or 9000 are already in use, modify the port mappings in docker-compose.yml:
```yaml
ports:
  - "5434:5432"  # Use different host port for PostgreSQL
  - "9001:9000"  # Use different host port for application
```

### Database Connection Issues
- Ensure PostgreSQL container is healthy: `docker-compose ps`
- Check application logs: `docker-compose logs billing-app`
- Verify database credentials match application.properties

### Build Issues
- Ensure Maven build completed successfully: `mvn clean package`
- Check if target/billing-system-0.0.1-SNAPSHOT.jar exists
- Verify Docker daemon is running
