#!/bin/bash

# virtualenv
if [[ ! -d .venv ]]; then
    python -m virtualenv .venv
fi

# shellcheck source=.venv/bin/activate
source .venv/bin/activate
pip install -U -r requirements.txt

sudo localstack start -d

while read bucket; do
    cors_config_path="file://$(pwd)/s3_cors_config.json"
    awslocal s3api create-bucket --bucket "$bucket"
    awslocal s3api put-bucket-cors --bucket "$bucket" --cors-configuration "$cors_config_path"
done <s3_buckets