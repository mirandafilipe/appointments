# Appointments Sesame Service

____

The appointments service is responsible for requesting the
appointments external service(https://us-central1-sesame-care-dev.cloudfunctions.net/sesame_programming_test_api) for reading appointments from doctors and
locations. The main responsibility is transforming the appointments
in an internal API for use by Sesame service API contract applying validation and 
cleaning steps to the data, plus collecting and 
listing errors from the appointment 3rd party provider.

### Rest Endpoint

The service offers the following API to retrieve appointments in the expected
Sesame contract:

``http://localhost:8080/appointments``

The API essentially returns all appointments in the expected format, even 
the ones with errors, eg: negative price. 

I decided to have a separate session in the Response to list the errors, and use the id
of the appointment to make the link. That way the caller may iterate over all 
appointments JSON object and decide what to do, without having to look
into errors right away


### Building & Unit Testing

The service has been build with gradle, therefore a jar can be built after tests with  the 
gradle command:

``./gradlew bootJar``

To run only tests, use:

``./gradlew test``

To build the docker image, use: 

``./gradlew bootBuildImage``

### Running

Make sure your local environment has docker, Java 11, gradle installed

The service can be run by building a docker image and running it right away with the command below, which binds
the running container to the local port 8080

``./gradlew bootBuildImage &&  docker run  -p 8080:8080 -t appointments:0.0.1-SNAPSHOT ``


### Stack 

 - Java 11
 - Spring Boot for development and unit/endpoint testing 
 - Default SLF4J with logback for logging
 - Docker for containerization
 - Spring Actuator for provisioning monitoring and k8s probes 


### Next Steps
  
 - Implement a integration/delivery pipelines, one possible option is circleCI(there are many)
 - Push the image built from this CI pipeline to a container registry
 - Perhaps create a Dockerfile and docker-compose file for local development and tooling  
 - Deploy the container to k8s and run it there, using the k8s probes from the service/k8s
to better manage the running application
 - Using Kubernetes many features for container orchestration will be offered, 
from auto-scaling to alerting, service re-starts, etc
   

### Final note

I have made some
conscious decisions about naming and code organization which I will be happy to talk about. 

PS.: I can already think about some refactoring tasks here and there.  
But, let's iterate :)