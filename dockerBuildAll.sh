#!/bin/bash

# Directories to build and push the docker image
directories=("api-gateway" "event-service" "post-service" "user-service" "registry-service" "frontend")

# Loop through each directory
for dir in "${directories[@]}"; do
    echo "Building Docker image in $dir directory..."
    cd "$dir" || { echo "Failed to change directory to $dir"; exit 1; }
    ./dockerBuild.sh || { echo "Failed to execute dockerBuild.sh in $dir"; exit 1; }
    cd ..
done

echo "Docker image build completed for all directories."
