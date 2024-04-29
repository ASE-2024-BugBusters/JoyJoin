FROM --platform=linux/amd64 openjdk:17
VOLUME /tmp
COPY target/*.jar Api-Gateway.jar
ENTRYPOINT ["java", "-jar","/Api-Gateway.jar"]
