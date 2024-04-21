# is used to push docker images directly to dockerhub instead through the github actions pipline
mvn clean package
docker build --tag=registry-service --force-rm=true .
img_id=$(docker images -q registry-service)
docker tag $img_id josip97/registry-service
docker push josip97/registry-service