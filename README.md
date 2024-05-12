# Access Application
It is possible to use this link: `https://joy-join.vercel.app/` to access the deployed Application.


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

# Swagger API Documentation

To see the Swagger API Documentation, run the service and go to this link: http://localhost:8085/swagger-ui/index.html#/