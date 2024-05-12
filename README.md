# Deployment Frontend
The Frontend is deployed on vercel and can be accessed through this link: `https://joy-join.vercel.app/`

# Deployment Microservices on AWS
1. For each Microservice create an EC2 instance
2. The REGISTRY-SERVICE and the API-GATEWAY need an elastic IP address, which means they get a fixed private IP address in the VPC. This is required to know where to connect to and where to send the requests on startup of the ther service
3. The other services don't need one. They get a dynamic IP address, but they know where to connect to REGISTRY-SERVICE and the API-GATEWAY
4. The Security-Groups need to be adjusted, the REGISTRY-SERVICE allows only requests from the VPC and the API-GATEWAY allows all request, since it has to be able to communicate with the internet
5. The other MS only accept requests from the ip coming from the API-GATEWAY
6. The MS need to connect to a RDS, so create 3 of them for each MS
7. Since we use s3 Buckets for our image storage, we need to create 3 Buckets for each MS
8. Create IAM Role that allows full access to the Buckets for each MS
9. Create ACCESS_KEY and SECRET_KEY to be able to connect to the s3 Buckets
10. Connect to each MS through the cmd and install docker on it

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
