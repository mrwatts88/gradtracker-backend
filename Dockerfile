FROM maven:3.6.0-jdk-11

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




