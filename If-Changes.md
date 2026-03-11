If You Change Java Code Only


Example changes:
Controller
Service
Repository
Entity
Business logic


Step 1 — Rebuild the JAR
Run:
mvn clean package -DskipTests
This creates a new JAR in:
target/billing-system-0.0.1-SNAPSHOT.jar

Step 2 — Rebuild Docker Image
Since the JAR changed, Docker must rebuild the image:

docker compose up --build

or

docker-compose up --build

This will:
Build a new image
Replace the running container
Start the updated application



2️⃣ If You Change Only application.properties or .env

Example:
DB config
email config
port

You only need to restart containers:
docker compose restart
or
docker compose up -d

No need to rebuild image.



3️⃣ If You Change Dockerfile

Example:
Base image
Java version
COPY path
You must rebuild the image:

docker compose up --build



4️⃣ If You Change docker-compose.yml
Example:
Ports
Environment variables
Volumes
Run:

docker compose down
docker compose up --build



5️⃣ Fast Development Workflow (Recommended)
Instead of typing many commands, use:
docker compose up --build -d
This will:
rebuild image if needed
restart containers
run in background


6️⃣ Check Running Containers
docker ps
Example output:
postgres-db
restaurant-billing-system-billing-app-1


7️⃣ See Logs
Very useful when debugging:

docker compose logs -f billing-app

or

docker compose logs -f postgres


8️⃣ If Container Is Stuck or Broken

Clean everything:

docker compose down
docker compose up --build


9️⃣ Full Workflow After Code Change

Typical developer workflow:

# rebuild jar
mvn clean package -DskipTests

# rebuild docker image and run
docker compose up --build -d

# check logs
docker compose logs -f



🔟 Pro Developer Trick (Faster Builds)
Instead of full rebuild:

docker compose restart billing-app

But only works if JAR inside container is unchanged.

🚀 Recommended Professional Workflow
mvn clean package -DskipTests
docker compose up --build -d
docker compose logs -f

