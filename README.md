# Hire Me Maven Clinic
This application is my submission for the take-home assignment for Maven Clinic.

# Requirements
The requirements for this project can be found in the `projectRequirements.pdf` file of the root.

# The Application
The application is a Spring Boot application which exposes two rest controllers, one for appointments and one for users. 

## Running Locally
### Main Application
In order to run the application locally via Maven, running the command `mvn spring-boot:run` will run the application in its local
profile.

The application will boot up on port `8662`.

### Tests
To run the tests via Maven, run the command `mvn test` will run all tests in the application.

I did not include any tests for Lombok only classes (where it creates the getters and setters) since that library handles
its own testing when being generated. I focused my testing on business logic and anything I created myself.

## Swagger
The application is outfitted with Swagger. Navigating to `http://localhost:8662/swagger-ui/index.html` will take you to
the swagger documentation. This includes the two endpoints, their documentation on requirements, return code explanations,
etc. 

# Reviewers Notes
- In an effort to keep my thoughts tied with the code, so you don't have to flip back and forth, you'll find "Reviewer Note"
  in my javadoc which are my thoughts, assumptions, actions taken on specific classes and methods. I'll include any other
  notes here that don't fit within those javadocs.
- All the REST API documentation is done through Swagger. Using swagger documentation properties, Swagger will show you
  the endpoints, their requirements, what the HTTP response codes translate to, etc.
- User ID, since it's a string, should always be sanitized and escaped since it's user provided data. Since this is a small
  scale example I chose not to do sanitization, but know that if this were a real world application or to be deployed at all,
  I would sanitize any user input. JPA, if we were using a database back end, handles sanitization itself, so it would not
  be a concern at the database level.
- In the application.yaml you'll see I expose several of the actuator endpoints like heapdump and threaddump. These 
  endpoints can expose a good amount of in memory information and as such should always be secured. I included these 
  as enabled as I would for any professional application, but in any normal setting those would be secured, so the 
  endpoints can only be accessed by privileged users. In no circumstances would I allow those to be deployed unprotected.
- API versioning is a controversial topic, some like to do so semantically with v1 in the endpoint, although it goes 
  against rest principles. This version of the application assumes version 1. If there were future increments for new 
  versions, I would do so by controlling routing via a header. The header would allow the client to specify the version 
  of the endpoint they are calling.