# API Gateway Service


## Overview

JoyJoin API Gateway service is a crucial component in our microservices architecture, providing a single entry point for all service requests. It uses Spring Cloud Gateway for routing and filtering, and integrates with Eureka for service discovery. The gateway handles routing, security (with JWT validation), and cross-origin requests, making it essential for both functionality and security.

## Features

- Forward HTTP requests from frontend to corresponding backend services
- Validate JWT token to filter out malformed requests
- CORS configurations that enables access from proper clients

## Prerequisites

- Java 11 or higher
- Maven for dependency management and building
- The Registry Service running
- All the microservices running

## Setup

Run the following command:

```shell
mvn spring-boot:run
```

## Configuration

### Routes

Routes are configured in `application.yml` located in `main/resources`.

### CORS

CORS is configured in both `application.yml` and `CorsConfig.java`

### Authorization

Authorization is implemented in `AuthorizationFilter.java`

## Documentation

This project is documented with JavaDoc. To view the javadoc, run the following command:

```shell
mvn javadoc:javadoc
```

and then the site can be found in `target/site/apidocs/`.