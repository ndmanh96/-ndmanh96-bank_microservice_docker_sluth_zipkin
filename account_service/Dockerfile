#Start with a base image containing Java runtime
FROM openjdk:8-jdk-alpine

#Information around who maintains the image
MAINTAINER manhcode.com

# Add the application's jar to the container
COPY target/account_service-0.0.1-SNAPSHOT.jar account_service-0.0.1-SNAPSHOT.jar

#execute the application
ENTRYPOINT ["java","-jar","/account_service-0.0.1-SNAPSHOT.jar"]

# build: docker build . -t manhcode/account_service

# run: docker run -p 8000:8000 manhcode/account_service 