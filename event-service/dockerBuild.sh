# is used to push docker images directly to dockerhub instead through the github actions pipline
mvn clean package
docker build --tag=event-service --force-rm=true .
img_id=$(docker images -q event-service)
docker tag $img_id josip97/event-service
docker push josip97/event-service