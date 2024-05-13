# User Service

## Overview

User Service is a comprehensive microservice designed to manage user profiles, interactions, and authentication within
our platform. This service enables operations such as retrieving user details, updating user information, managing
avatar uploads, and handling user relationships. Additionally, it supports user registration and login functionalities.

## Features

- **User Management:** Retrieve and update user profiles using unique identifiers.
- **Relationship Management:** Add or remove follow relationships among users.
- **Authentication:** Register new users and authenticate existing users.

## Prerequisites

Before deploying and running the service, ensure the following prerequisites are met:

- Java 11 or newer
- Maven for dependency management and building
- docker (for testing)

## External Dependencies

### Database

The database is used for persisting application data including user information and relation between users.

Database configurations are specified in the `application.yml` file. By default, the service expects a PostgreSQL
database named `userService` running at port `5436`. The default username and password can be found in the configuration
file.

### Amazon S3 Storage

Amazon S3 is used for storing large files, such as avatars uploaded by users, that are part of the application’s data.

S3 configurations are specified in the `application.yml` file as well. The service will store and retrieve user avatar
data to and from the `avatar` bucket.

Interactions with S3 utilized a stand-alone library named `libs3`, which can be found at `/path/to/JoyJoin/libs3/`.

## Setup

Simply execute the following command:

   ```bash
   mvn spring-boot:run
   ```

## Project Structure

This project is organized into a structured directory layout that facilitates efficient development and deployment workflows. Below is an outline of the key components in this repository:

- **`src/`**: The source code directory contains all the Java code, organized by package and purpose:
  - `main/`: Includes all production code.
    - `java/`: Contains the application code organized into several packages for aspects, configuration, controllers, exceptions, models, DTOs, repositories, security, and services.
    - `resources/`: Houses configuration files like `application.yml` and `application-docker.yml` for different environments.
  - `test/`: Contains all the test cases, primarily focusing on converters and controllers.
- **`dockerBuild.sh`**: A shell script for building Docker images.
- **`Dockerfile`**: Defines the Docker container configuration.
- **`mvnw`**: Maven wrapper script for building the project without needing a pre-installed Maven.
- **`pom.xml`**: Maven project object model file that contains information about the project and configuration details used by Maven to build the project.
- **`qodana.yaml`**: Configuration file for Qodana, a smart code quality platform.
- **`README.md`**: Markdown file containing project information and documentation.

The `src/main/java` directory contains the Java source files of the application, organized into subdirectories representing various components and layers of the application architecture:

- **`com.joyjoin.userservice`**: The base package for the application, which contains several sub-packages:
  - **`aspect`**: Contains AOP-related classes like `LoggingAspect.java` for cross-cutting concerns.
  - **`beanConfig`**: Includes configuration beans such as `AwsConfig.java` for AWS services and `MapperConfig.java` for model mapping configurations.
  - **`controller`**: Holds the controllers and DTOs:
    * **`dto`**: Data Transfer Objects like `GetAvatarUploadUrlResponse.java` and `UpdateUserRequest.java`.
* `UserController.java`: Manages user-related HTTP requests.
  - **`exception`**: Contains exception classes and a global exception handler:
    - Exception classes like `AccountNameAlreadyExistsException.java`, `EmailAlreadyExistsException.java`, and more.
    - `GlobalExceptionHandler.java`: Catches and handles global exceptions.
  - **`model`**: Includes domain models and converters:
    - **`converter`**: Contains converters like `ImageRefConverter.java`.
    - Domain models such as `User.java`, `Address.java`, and other related classes.
  - **`modelDto`**: Houses DTOs for model data interactions.
  - **`packer`**: Contains classes like `ImageAggregator.java` and `UserAggregator.java` for data packaging.
  - **`repository`**: Includes JPA repositories like `UserRepository.java` for database interactions.
  - **`security`**: Manages security configurations and components:
    - **`config`**: Configuration classes for security settings.
    - **`controller`**: Contains security-related controllers.
    - **`filter`**: Includes filters such as `JwtAuthenticationFilter.java` for JWT processing.
    - **`service`**: Services like `AuthService.java` and `JwtService.java` handling authentication and authorization.
  - **`service`**: Service classes like `UserService.java` and `ImageService.java` handle business logics.
  - `UserServiceApplication.java`: The main application class that starts the Spring Boot application.

## API Endpoints

### User Management

- **GET `/api/user/`**: Retrieve all users.
- **PATCH `/api/user/users/{uuid}`**: Update specific user details.
- **GET `/api/user/users/{uuid}/upload_avatar`**: Generate a URL for avatar upload.
- **GET `/api/user/users/{uuids}`**: Retrieve multiple users by their UUIDs.
- **GET `/api/user/by-email`**: Fetch a user by email.
- **PUT `/api/user/users/{followerId}/followee/{followeeId}`**: Add a following relationship.
- **DELETE `/api/user/users/{followerId}/followee/{followeeId}`**: Remove a following relationship.
- **GET `/api/user/users/{uuid}/followee`** and **GET `/api/user/users/{uuid}/follower`**: Retrieve all followees and
  followers of a user respectively.

### Authentication

- **POST `/api/auth/register`**: Register a new user.
- **POST `/api/auth/login`**: Authenticate and log in a user.


## Configuration

All the configuration is in the `application.yml` file in `src/resources` directory.

## Documentation

The user service is documented with JavaDoc. To build the document site, run the following command:

```shell
mvn javadoc:javadoc
```

After that, the document website generated can be found under `target/site/apidocs/`.