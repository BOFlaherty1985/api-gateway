FROM openjdk:11
MAINTAINER Benjamin OFlaherty
COPY /build/libs/api-gateway-0.0.1-SNAPSHOT.jar api-gateway.jar
ENTRYPOINT ["java","-jar","/api-gateway.jar"]
