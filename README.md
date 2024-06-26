*NOTE*: please find the GitHub repository at https://github.com/ASE-2024-BugBusters/JoyJoin, where you can also find the wiki, scrum boards and the history pull requests.

# Project Organization
## Introduction
This documentation outlines the adapted Scrum methodology utilized by our team for project management. We have employed Git Scrum boards to organize tasks and facilitate collaboration among team members. Our approach involves dividing the project into three distinct phases, each marked by specific deliverables. This documentation provides an overview of our process and workflow.
## Scrum Board Structure
Our Scrum board is organized around three key phases:
- Initiation: Tasks in this phase involve project setup, initial planning, and establishing the development environment.
- Development: This phase encompasses the core development work, where individual modules are built and integrated into the project.
- Refinement: The final phase focuses on fine-tuning and optimizing the project, addressing any remaining issues, and preparing for deployment.
## Task Lifecycle
- To-Do: Tasks are initially listed as “To-Do” when they are identified and prioritized for implementation.
In Progress: Once a team member begins working on a task, its status is updated to “In Progress” to indicate active development.
- Done: Tasks transition to the “Done” column upon completion and successful testing by the developer. However, it’s important to note that our team, comprising four developers responsible for independent modules, does not explicitly utilize an “In Review/Testing” phase. Instead, developers conduct internal unit testing before pushing their features to the development branch.
- In Review: A different developer has to review the code to check if there are errors that the other may made.
- QA Not OK: In the event of integration errors or failed unit tests, tasks are moved to the “QA Not OK” column. Developers are then responsible for addressing these issues promptly to ensure the quality and integrity of the project.
- Ready for Deployment: In this step after testing and Code review we know that this task is ready to be deployed

## Workflow Overview:
- Initiation Phase: During this phase, tasks primarily involve project setup, planning, and environment configuration. Key deliverables include defining project scope, setting up repositories, and establishing development guidelines.
- Development Phase: The core development work takes place in this phase, with each team member focusing on their respective modules. Tasks are moved from “To-Do” to “In Progress” as developers begin implementing features. Once development is complete, tasks are transitioned to “Done” after successful unit testing.
- Refinement Phase: In the final phase, the project undergoes fine-tuning and optimization. Tasks may involve bug fixes, performance enhancements, and documentation updates. Any issues identified during integration testing are addressed promptly to ensure a stable release.

## Task Scheduling and Project Management
- Tools: Github Projects, have a look at `images/GithubProjects.png`
- Process: Tasks organized into user stories and added to the backlog.
Sprint planning meetings held at the beginning of each sprint.
2 times a week stand-up meetings conducted for progress tracking.
Sprint reviews held at the end of each sprint.
- Timeline Overview: Gantt chart used for visualization.

## Versioning and Git Branching
Version Control: Git

Branching Model: Gitflow

Main Branches:
- main
- development
- Feature Branches: Created for each new feature or user story.
- Pull Requests: Integral part of the workflow for code review and collaboration.
Each feature or bug fix developed on a feature branch.
Code review and testing conducted before merging into the main branch.

# Deployment Frontend
The Frontend is deployed on AWS and can be accessed: http://13.38.253.17:3005/, have a look at `images/FrontendOnAWS.png`
# Deployment Microservices on AWS
1. For each Microservice create an EC2 instance
2. The FRONTEND, REGISTRY-SERVICE and the API-GATEWAY need an elastic IP address, which means they get a fixed public IP address in the VPC. This is required to know where to connect to and where to send the requests on startup of the ther service
3. The other services don't need one. They get a dynamic IP address, but they know where to connect to REGISTRY-SERVICE and the API-GATEWAY
4. The Security-Groups need to be adjusted, the REGISTRY-SERVICE allows only requests from the VPC and the API-GATEWAY allows all request, since it has to be able to communicate with the internet
5. The other MS only accept requests from the ip coming from the API-GATEWAY
6. The MS need to connect to a RDS, so create 3 of them for each MS
7. Since we use s3 Buckets for our image storage, we need to create 3 Buckets for each MS
8. Create IAM Role that allows full access to the Buckets for each MS
9. Create ACCESS_KEY and SECRET_KEY to be able to connect to the s3 Buckets
10. Connect to each MS through the cmd and install docker on it

Have a look at `images/RunningAwsMS.png`
## Run this commands for each MS
### REGISTRY-SERVICE
`docker pull josip97/registry-service`

`docker run -d -p 8761:8761 josip97/registry-service`

### API-GATEWAY
`docker pull josip97/api-gateway`

`docker run -e "eureka.client.serviceUrl.defaultZone=http://CftYQ29IIX76lbg5:q0XdGl8iEygR311T@172.31.44.185:8761/eureka/" -d -p 9191:9191 josip97/api-gateway`

### USER-SERVICE
`docker pull josip97/user-service`

`docker run -e "eureka.client.serviceUrl.defaultZone=http://CftYQ29IIX76lbg5:q0XdGl8iEygR311T@172.31.44.185:8761/eureka/" -e "SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-user.c36ussau4w0r.eu-west-3.rds.amazonaws.com:5436/userService" -e "SPRING_DATASOURCE_USERNAME=postgres" -e "SPRING_DATASOURCE_PASSWORD=aHauYoTm56oG7X6u" -e "s3.ACCESS_KEY=AKIAXYBJKEDVALB7ONOX" -e "s3.SECRET_KEY=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" -e "s3.BUCKET_NAME=joyjoin-avatar" -e "s3.REGION=eu-west-3" -e "s3.ENDPOINT=https://s3.eu-west-3.amazonaws.com/joyjoin-avatar/" --network host josip97/user-service`

### EVENT-SERVICE
`docker pull josip97/event-service`

`docker run -e "eureka.client.serviceUrl.defaultZone=http://CftYQ29IIX76lbg5:q0XdGl8iEygR311T@172.31.44.185:8761/eureka/" -e "SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-event.c36ussau4w0r.eu-west-3.rds.amazonaws.com:5432/eventService" -e "SPRING_DATASOURCE_USERNAME=postgres" -e "SPRING_DATASOURCE_PASSWORD=N9LoFoo3GbkEf9vV" -e "s3.ACCESS_KEY=AKIAXYBJKEDVALB7ONOX" -e "s3.SECRET_KEY=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" -e "s3.BUCKET_NAME=joyjoin-event-img" -e "s3.REGION=eu-west-3" -e "s3.ENDPOINT=https://s3.eu-west-3.amazonaws.com/joyjoin-event-img/" --network host josip97/event-service`

### POST-SERVICE
`docker pull josip97/post-service`

`docker run -e "api.BASE_URL=http://ec2-35-181-200-35.eu-west-3.compute.amazonaws.com:9191/" -e "eureka.client.serviceUrl.defaultZone=http://CftYQ29IIX76lbg5:q0XdGl8iEygR311T@172.31.44.185:8761/eureka/" -e "SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-post.c36ussau4w0r.eu-west-3.rds.amazonaws.com:5432/postService" -e "SPRING_DATASOURCE_USERNAME=postgres" -e "SPRING_DATASOURCE_PASSWORD=bEBaGQk+RLTEBydI" -e "s3.ACCESS_KEY=AKIAXYBJKEDVALB7ONOX" -e "s3.SECRET_KEY=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" -e "s3.BUCKET_NAME=joyjoin-post-img" -e "s3.REGION=eu-west-3" -e "s3.ENDPOINT=https://s3.eu-west-3.amazonaws.com/joyjoin-post-img/" --network host josip97/post-service`

### FRONTEND
`docker pull josip97/frontend`

`docker run -p 3005:3005 josip97/frontend`

# Running the Application Locally

**Warning:** Directly bringing up the services defined in the `docker-compose.yml` is NOT sufficient to have the project
run correctly, please use the shell script named `run.sh` under the root directory of this project.

The application consists of 3 microservices, one frontend service, one registry service, one http gateway and several
infrastructure services such as postgres and s3. To run the application locally, you can simply run `run.sh` locating
under the root of this project. The script will:

- bring up 3 database containers required for microservices,
- bring up localstack service to provide a local S3 simulation,
- set up S3 environment, including creating buckets needed, configure CORS, etc.,
- bring up the registry service, microservices, and the http gateway,
- start a development server that hosts our frontend pages.

If everything went well, you would find the home page at `http://localhost:3005/`. Note that currently the
port used by this application is not yet configurable, so please ensure that the following ports are not occupied before
running this application:

- `3005` for frontend dev server,
- `5434-5436` for postgres containers,
- `4566` and `4510-4559` for localstack and emulated AWS services,
- `8761` for registry service,
- and `9191` for http gateway.

# Development Setup (WIP)

Simply execute `dev_setup.sh`

# Documentation
## Technologies
- Backend - Springboot, Spring Cloud
- Frontend - Vuejs
- Database - PostgreSQL
- Containerization - Docker
- CI/CD - Github Actions and Qodana
- Deployment Frontend - Vercel
- Deployment Backend - AWS
- API Documentation - Swagger

## Architecture
Our Architecture consists of a Registry-Service which is responsible to check which services are available, nothing else.
The Api-Gateway which does the work of a loadbalancer and forwards the requests made to the corresponding MS
A User-Service, Event-Service and a Post-Service, where each of them has its own Postgres DB.
The services communicate through API requests through the Api-Gateway. They don't know anything about each other.
Since we deploy our services on random ports and we have a the Api-Gateway and Registry-Service it is easy to deploy multiple instances of the same Service.
If one goes down or is not available for some reason the Api-Gateway forwards it to the other MS without any problems. 
This makes our Application highly scalable. The other thing that makes our Application scalable are the s3 buckets.
All of our choices are made to make our application easy scalable.
Have a look at the `images/Architecture.png`

## API Documentation
For the API Documentation we used Swagger. To see the documentation you need to run it locally with docker-compose.
Then for each MS you need to get the PORT from the log, since they are dynamically assigned and then go to the page: `http://localhost:{PORT}/swagger-ui/index.html#/`
Replace `{PORT}` with the corresponding port number for the microservice you wish to access. This will direct you to the Swagger UI interface, where you can explore and interact with the API documentation seamlessly.
Have a look at `images/SwaggerExample.png`

# Testing and continuous integration
## Testing approach
During the development process, testing played a crucial role in ensuring the quality and reliability of our code. Our testing strategy evolved over time to adapt to the changing requirements and constraints of the project.
### Early Stages
In the initial stages of setting up the architecture, testing was limited due to the extensive configuration required. At this point, our focus was primarily on establishing a solid foundation for the project.
### Spring Reviews
As development progressed with each sprint, we adopted a thorough review process before merging tasks into the development branch. Each code change underwent a review by a team member to ensure quality and consistency.
### Frontend Testing
As the project expanded and time constraints became more pressing, we relied on frontend testing as a form of initial validation. This allowed us to quickly verify basic functionalities and identify any glaring issues.
### Integration Testing
Recognizing the limitations of frontend testing, we bolstered our testing strategy by implementing integration tests for each endpoint. This ensured comprehensive coverage and helped maintain the reliability of our codebase, especially as the complexity of the project increased.

## Continuous Integration (CI) Flow
Our continuous integration (CI) flow was integral to our development process, providing a seamless way to integrate code changes and ensure their compatibility with the existing codebase. The CI pipeline included the following steps:
### Integration Testing
Integration tests, as mentioned earlier, were performed to validate the interaction between different components of the system, especially at the endpoint level.
### Static Code Analysis
We integrated static code analysis tools, such as Qodana, into our CI pipeline to automatically check our code for potential issues and improvements. This proactive approach enabled us to maintain code quality and catch issues early in the development process. Have a look at `images/QodanaExample.png`

## Quality Check Steps
Quality checks were an integral part of our development workflow, ensuring that our code adhered to established standards and best practices. These steps were incorporated into our CI pipeline and development process:
### Code Reviews
Each code change underwent a thorough review by a team member to ensure adherence to coding standards, maintainability, and readability.
### Documentation
Comprehensive documentation was maintained throughout the development process to provide context, guidelines, and instructions for future reference.
### Best Practices and Conventions
Our development approach is guided by a commitment to adhering to industry best practices and established conventions. We prioritize the following principles:

Design Patterns: We employ well-established design patterns to ensure our codebase is structured, maintainable, and scalable. By leveraging design patterns, we promote code reuse, enhance readability, and facilitate future enhancements.

Architecture: Our architecture is designed with scalability, performance, and maintainability in mind. We follow architectural principles such as separation of concerns, modularity, and scalability to build robust and resilient systems.

Documentation: Comprehensive documentation is an integral part of our development process. We document code, architecture, APIs, and processes to provide clarity, facilitate collaboration, and ensure that knowledge is shared effectively across the team.

RESTful Principles: We adhere to RESTful principles when designing our APIs to promote interoperability, simplicity, and scalability. Our APIs are designed to be resource-oriented, stateless, and cacheable, following standard HTTP methods and status codes.

By embracing these best practices and conventions, we strive to maintain code quality, improve developer productivity, and deliver high-quality software solutions to our clients and stakeholders.

## Consideration During Development
Throughout the development process, we placed a strong emphasis on maintaining code quality, reliability, and scalability. Quality checks were integrated into our workflow from the outset, ensuring that every code change underwent thorough scrutiny before being merged into the main codebase. Additionally, the continuous integration flow facilitated seamless integration of code changes while maintaining the integrity of the overall system. Overall, our approach prioritized quality, collaboration, and accountability, ensuring the delivery of high-quality and reliable software solutions, even under tight deadlines.

