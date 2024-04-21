# is used to push docker images directly to dockerhub instead through the github actions pipline
mvn clean package
docker build --tag=api-gateway --force-rm=true .
img_id=$(docker images -q api-gateway)
docker tag $img_id josip97/api-gateway
docker push josip97/api-gateway