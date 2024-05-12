#!/bin/bash

if [[ "$OSTYPE" != "linux-gnu"* && "$OSTYPE" != "darwin"* ]]; then
    echo "This script can only be run on Linux or MacOS."
    exit 1
fi

if [[ ! -d .venv ]]; then
    python -m virtualenv .venv
fi

# shellcheck source=.venv/bin/activate
source .venv/bin/activate
pip install -U awscli-local[ver1] # always use the latest possible version to prevent python version mismatch

docker compose -f docker-compose-dev.yml up -d --remove-orphans

while read bucket; do
    cors_config_path="file://$(pwd)/s3_cors_config.json"
    awslocal s3api create-bucket --bucket "$bucket"
    awslocal s3api put-bucket-cors --bucket "$bucket" --cors-configuration "$cors_config_path"
done <s3_buckets

deactivate
rm -rf .venv
