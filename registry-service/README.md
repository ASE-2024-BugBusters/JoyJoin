# Registry Service

Registry Service is a Spring Boot application that serves as an Eureka Server. This service is designed to manage service registration and discovery, enabling microservices to find and communicate with each other seamlessly. The application also logs the registration and renewal of services to assist with monitoring in distributed environments.

## Features
- Eureka Server for service registration and discovery.
- Basic authentication for securing Eureka dashboard and API endpoints.

## Prerequisites
- Java 11 or newer
- Maven for dependency management and building

## Installation

Navigate to the root directory of this project:

```bash
cd path/to/JoyJoin/registry-service
```

Build the project using Maven:

```bash
mvn clean package
```

## Running the Application

Run the Spring Boot application:

```bash
mvn spring-boot:run
```

Open http://localhost:8761 in your web browser to see the Eureka dashboard. Use the credentials defined in the `application.yml` for authentication.

## Configuration

The application is configured through the `application.yml` file located in the `src/main/resources` directory. Here you can set the server port, Eureka client and server settings, and basic auth credentials.
