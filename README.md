# Fullstack Java Project

## Yassine Amzil (3AONC)

## Folder structure

- Readme.md
- _architecture_: this folder contains documentation regarding the architecture of your system.
- `docker-compose.yml` : to start the backend (starts all microservices)
- _backend-java_: contains microservices written in java
- _demo-artifacts_: contains images, files, etc that are useful for demo purposes.
- _frontend-web_: contains the Angular webclient

Each folder contains its own specific `.gitignore` file.  
**:warning: complete these files asap, so you don't litter your repository with binary build artifacts!**

## How to setup and run this application

Follow these steps to run the application:

1. **Set Up the Frontend**:
   - Navigate to the `frontend-web` directory.
   - Run the following command to install the necessary dependencies:
     ```bash
     npm install
     ```

2. **Start the Backend with Docker Compose**:
   - In the project root directory, run the following command to start all microservices:
     ```bash
     docker-compose up
     ```
   - To remove the Docker Compose file's data and volumes, use the following command:
     ```bash
     docker-compose down -v
     ```

3. **Start the Services in Order**:
   - After setting up Docker Compose, manually start the following applications in this order:
     1. **ConfigServiceApplication**
     2. **DiscoveryServiceApplication**
     3. **GatewayServiceApplication**
     4. The remaining services:
        - **PostServiceApplication**
        - **ReviewServiceApplication**
        - **CommentServiceApplication**
        - **NotificationServiceApplication**


<!-- :heavy_check_mark:_(COMMENT) Add setup instructions and provide some direction to run the whole  application: frontend to backend._ -->
