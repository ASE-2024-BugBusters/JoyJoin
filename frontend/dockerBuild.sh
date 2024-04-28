#!/bin/bash

# is used to push docker images directly to dockerhub instead through the github actions pipline
docker build --tag=frontend --force-rm=true .
img_id=$(docker images -q frontend)
docker tag "$img_id" josip97/frontend
docker push josip97/frontend