#!/bin/bash

# is used to push docker images directly to dockerhub instead through the github actions pipline
mvn clean package -DskipTests
docker build --tag=post-service --force-rm=true .
img_id=$(docker images -q post-service)
docker tag "$img_id" josip97/post-service
docker push josip97/post-service