ARG DOCKER_REGISTRY

FROM $DOCKER_REGISTRY/general/docker-images/maven-docker:latest

WORKDIR /backend-example

# Copy source code
ADD . /backend-example

# Build the service
RUN mvn clean -B -T 4 package -DskipTests \
    && mv target/*.jar backend.jar

# Expose port for rest interface
EXPOSE 8333

# Provide entry-point
CMD java -jar backend.jar




