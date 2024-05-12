#!/bin/bash

# is used to push docker images directly to dockerhub instead through the github actions pipline
mvn clean package -DskipTests
docker build --tag=user-service --force-rm=true .
img_id=$(docker images -q user-service)
docker tag "$img_id" josip97/user-service
docker push josip97/user-service