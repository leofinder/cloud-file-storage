# Cloud File Storage

Cloud File Storage is a Java-based web application for managing file storage in the cloud. It provides an intuitive interface for uploading, downloading, and organizing files while utilizing popular storage and database solutions.

## Features

- **File Management**: Upload, download, and organize files in the cloud.
- **Storage Integration**: Uses MinIO for scalable object storage.
- **Database**: PostgreSQL for data persistence.
- **Redis**: Implements caching and session management for improved performance.
- **Docker Support**: Fully containerized using Docker for both development and production environments.
- **Security**: Configured with authentication and session handling for secure operations.
- **Monitoring and Metrics**: Integrated with Prometheus for application monitoring.

## Prerequisites

- Java 17+
- Maven
- Docker and Docker Compose

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/leofinder/cloud-file-storage.git
cd cloud-file-storage
```

### Configuration

#### Environment Variables
- Use the provided `.env.example` file for configuration:
    ```bash
    cp .env.example .env
    ```
- Edit `.env` and replace the placeholders with your specific settings (e.g., database credentials, Redis password, MinIO keys).

### Development Environment

1. Start the services with the development configuration:
    ```bash
    docker compose -f docker-compose.dev.yml up --build
    ```
2. Access the application at [http://localhost:8080](http://localhost:8080).

### Production Environment

1. Build and run the application with the production configuration:
    ```bash
    docker compose -f docker-compose.prod.yml up --build
    ```
2. Access the application at your server's public IP or domain.

## Build and Run Locally (Optional)

1. Build the application using Maven:
    ```bash
    mvn clean install
    ```
2. Run the application:
    ```bash
    java -jar target/cloud-file-storage-0.0.1-SNAPSHOT.jar
    ```

## Technologies and Dependencies

- **Framework**: Spring Boot
- **Storage**: MinIO
- **Database**: PostgreSQL
- **Caching**: Redis
- **Monitoring**: Prometheus
- **Dependencies**:
    - Spring Boot Starter (Web, Data JPA, Security, Validation, Thymeleaf)
    - Flyway for database migrations
    - Testcontainers for integration testing
    - MapStruct for mapping DTOs
    - Lombok for boilerplate code reduction

## Docker Configuration

- **Development**:
    - PostgreSQL, MinIO, Redis services are configured in `docker-compose.dev.yml`.
- **Production**:
    - Includes an application container in addition to the services in `docker-compose.prod.yml`.