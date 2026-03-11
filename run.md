# Complete Docker Setup Guide for Restaurant Billing System

## Project Structure Analysis
```
restaurant-billing-system/
├── src/                    # Source code directory
├── pom.xml                 # Maven configuration file
├── Dockerfile             # Docker image definition
├── docker-compose.yml     # Multi-container orchestration
├── .env                   # Environment variables
├── application.properties # Spring Boot configuration
├── target/                # Build output directory (will contain JAR)
└── mvnw, mvnw.cmd        # Maven wrapper scripts
```

---

## Step-by-Step Setup Process

### Step 1: Build the Application with Maven
```bash
# Clean previous builds and package the application
mvn clean package

# Or use Maven wrapper (recommended for consistency)
./mvnw clean package  # Linux/Mac
mvnw.cmd clean package  # Windows
```

**Why this step is needed:**
- Creates the executable JAR file in `target/` directory
- The JAR file is required by Docker to run the application
- Without this step, Docker build will fail because there's no JAR to copy

**Common Error:** "PostgreSQL not found" during build
- This error occurs because Maven tries to run tests that require database connection
- **Solution:** Skip tests during build: `mvn clean package -DskipTests`

### Step 2: Fix Dockerfile
The current Dockerfile has an incomplete COPY command. Fix it:

```dockerfile
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY target/billing-system-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9000

ENTRYPOINT ["java","-jar","app.jar"]
```

### Step 3: Verify docker-compose.yml
Current configuration is correct:
```yaml
version: "3.5"
services:
  postgres:
    image: postgres:17
    container_name: postgres-db
    ports:
      - "5432:5432"
    environment:
      POSTGRES_DB: restaurant_db
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: root
    volumes:
      - postgres_data:/var/lib/postgresql/data

  billing-app:
    build: .
    ports:
      - "9000:9000"
    depends_on:
      - postgres
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/restaurant_db
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: root

volumes:
  postgres_data:
```

### Step 4: Run with Docker Compose
```bash
# Start all services
docker-compose up

# Or run in background
docker-compose up -d
```

---

## Why Docker is Required for This Project

### 1. **Database Dependency Elimination**
- **Problem:** Application requires PostgreSQL database installed locally
- **Docker Solution:** Provides PostgreSQL in a container, no local installation needed
- **Benefit:** Any developer can run the project without database setup

### 2. **Environment Consistency**
- **Problem:** "Works on my machine" issues due to different OS, Java versions, etc.
- **Docker Solution:** Same environment everywhere (development, testing, production)
- **Benefit:** Eliminates environment-related bugs

### 3. **Simplified Deployment**
- **Problem:** Complex deployment process with multiple dependencies
- **Docker Solution:** One command to deploy entire application stack
- **Benefit:** Faster, reliable deployments

### 4. **Isolation**
- **Problem:** Multiple projects conflicting on same ports or dependencies
- **Docker Solution:** Each application runs in isolated containers
- **Benefit:** No conflicts between projects

---

## Dockerfile Explanation

### What is Dockerfile?
A Dockerfile is a text file that contains instructions for building a Docker image. It's like a recipe for creating your application's container.

### Why We Create Dockerfile?
1. **Containerization**: Package the application with all its dependencies
2. **Portability**: Run the same container anywhere Docker is installed
3. **Scalability**: Easily create multiple instances of the application

### Line-by-Line Explanation:
```dockerfile
FROM eclipse-temurin:21-jdk-jammy
```
- Uses Eclipse Temurin JDK 21 on Ubuntu Jammy as base image
- Provides Java runtime environment needed for Spring Boot

```dockerfile
WORKDIR /app
```
- Sets working directory inside container to `/app`
- All subsequent commands run from this directory

```dockerfile
COPY target/billing-system-0.0.1-SNAPSHOT.jar app.jar
```
- Copies the built JAR file from host to container
- Renames it to `app.jar` for simplicity

```dockerfile
EXPOSE 9000
```
- Documents that container listens on port 9000
- Doesn't actually publish the port, just documents it

```dockerfile
ENTRYPOINT ["java","-jar","app.jar"]
```
- Sets the command to run when container starts
- Executes the Spring Boot application

---

## docker-compose.yml Explanation

### What is docker-compose.yml?
A YAML file that defines and runs multi-container Docker applications. It's like an orchestra conductor for your containers.

### Why We Create docker-compose.yml?
1. **Multi-Container Management**: Coordinate multiple services (app + database)
2. **Service Dependencies**: Define startup order and connections
3. **Configuration Management**: Centralize all service configurations

### Section-by-Section Explanation:

#### Version and Services
```yaml
version: "3.5"
services:
```
- Specifies Docker Compose version
- Begins services definition

#### PostgreSQL Service
```yaml
postgres:
  image: postgres:17
  container_name: postgres-db
  ports:
    - "5432:5432"
  environment:
    POSTGRES_DB: restaurant_db
    POSTGRES_USER: postgres
    POSTGRES_PASSWORD: root
  volumes:
    - postgres_data:/var/lib/postgresql/data
```
- **image**: Uses official PostgreSQL 17 image
- **container_name**: Gives container a predictable name
- **ports**: Maps host port 5432 to container port 5432
- **environment**: Sets up database credentials
- **volumes**: Persists database data beyond container lifecycle

#### Application Service
```yaml
billing-app:
  build: .
  ports:
    - "9000:9000"
  depends_on:
    - postgres
  environment:
    SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/restaurant_db
    SPRING_DATASOURCE_USERNAME: postgres
    SPRING_DATASOURCE_PASSWORD: root
```
- **build**: Builds image from Dockerfile in current directory
- **ports**: Exposes application on host port 9000
- **depends_on**: Ensures PostgreSQL starts first
- **environment**: Provides database connection details to Spring Boot

#### Volumes
```yaml
volumes:
  postgres_data:
```
- Defines named volume for data persistence
- Database data survives container restarts/recreation

---

## Benefits of Using Docker for This Project

### 1. **Development Benefits**
- **Zero Setup**: New developers can start coding in minutes
- **Consistency**: Same environment for all team members
- **Isolation**: No interference with other projects

### 2. **Operational Benefits**
- **Scalability**: Easy to scale horizontally
- **Monitoring**: Built-in container health checks
- **Backup**: Easy data backup and restore

### 3. **Deployment Benefits**
- **CI/CD Integration**: Perfect for automated pipelines
- **Rollback**: Instant rollback to previous versions
- **Multi-Environment**: Same containers for dev/staging/prod

### 4. **Cost Benefits**
- **Resource Efficiency**: Better resource utilization
- **Reduced Support**: Fewer environment-related issues
- **Faster Onboarding**: New team members productive quickly

---

## Complete Workflow Commands

### Initial Setup
```bash
# 1. Build the application
mvn clean package -DskipTests

# 2. Start all services
docker-compose up -d

# 3. Check status
docker-compose ps

# 4. View logs
docker-compose logs -f
```

### Development Workflow
```bash
# Make code changes
# Rebuild and restart
docker-compose up --build

# Or just restart if no Dockerfile changes
docker-compose restart billing-app
```

### Cleanup
```bash
# Stop services
docker-compose down

# Stop and remove volumes (deletes data)
docker-compose down -v

# Remove all containers and images
docker system prune -a
```

---

## Troubleshooting Guide

### Maven Build Issues
```bash
# If tests fail due to missing database
mvn clean package -DskipTests

# If Maven wrapper doesn't work
chmod +x mvnw  # Linux/Mac
# or use system Maven: mvn clean package
```

### Docker Issues
```bash
# Check if Docker is running
docker --version
docker-compose --version

# Check container status
docker-compose ps

# View detailed logs
docker-compose logs billing-app
docker-compose logs postgres
```

### Port Conflicts
```bash
# Check what's using ports
netstat -tulpn | grep :5432
netstat -tulpn | grep :9000

# Kill processes using ports (Linux/Mac)
sudo kill -9 <PID>
```

### Database Connection Issues
```bash
# Test database connection
docker exec -it postgres-db psql -U postgres -d restaurant_db

# Check if database is ready
docker-compose logs postgres | grep "database system is ready"
```

---

## Next Steps

1. **Fix the Dockerfile** with the correct COPY command
2. **Build the application** using Maven
3. **Run docker-compose up** to start everything
4. **Access the application** at http://localhost:9000
5. **Explore Swagger UI** at http://localhost:9000/swagger-ui

This Docker setup eliminates all local database dependencies and provides a consistent, portable development environment for your Restaurant Billing System.
