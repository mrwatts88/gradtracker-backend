FROM maven:3-jdk-8-alpine

WORKDIR /backend-example

COPY target/*.jar backend.jar

# expose port for rest interface
EXPOSE 8333

# provide entry-point
CMD java -jar backend.jar




