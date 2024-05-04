#!/bin/sh

# create a virtual environment to use awslocal
python -m virtualenv .venv
. .venv/bin/activate
pip install -U awscli-local[ver1]

# bring up all the containers
docker compose -f docker-compose.yml up -d

# create and configure s3 buckets
while read bucket; do
    cors_config_path="file://$(pwd)/s3_cors_config.json"
    awslocal s3api create-bucket --bucket "$bucket"
    awslocal s3api put-bucket-cors --bucket "$bucket" --cors-configuration "$cors_config_path"
done <s3_buckets

# clean up the virtual environment
deactivate
rm -r .venv
