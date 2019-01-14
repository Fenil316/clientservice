clientservice
=


*This is a working application with acceptable assumptions and simplifications.*

### Assumptions
* A client has a unique profile and many finances and can be pulled up using 
* A profile consists of some basic client info.
* Finances are the organizations/firms/businesses that a client wishes to keep accounting for.
* While creating a client, a profile is mandatory while finances are optional.
* All clients must have a unique SSN and Email Id.
* For each finance submitted, there should be a unique Tax Id.

###Technical Details
* This is a springboot web application with RESTful APIs and an in-memory H2 database that will be bootstrapped and populated during application startup.
* The database is relational and spring-data is used for object-relational mapping and to access/manipulate persistence stores.
* The controller and service layer are tested with a 100% code coverage using junit, mockito and spring-test.
* Spring boot maven-plugin is used to package (mvn package) the code and create a 'Fat' jar for a self-sufficient, stand-alone service.
* Data validations are performed using hibernate validators and a customized framework for each bean that inherits 'HasCustomValidations' interface.
* Responses are customised using a wrapper containing:
    * payload: Contains acutal entity/entities,
    * validationErrors: errors encountered during data validations,
    * errors: processing errors
* Each response is of the type application/json and has an appropriate status code.
* Id's for each entities are not included in json responses for security purpose. 

### API Contract
#### To add a client
```
curl -X POST <domain>/clients
Example:

curl -X POST \
  http://localhost:8080/clients \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: 8bc072bc-fcde-e636-a1f3-c4f17e15ce7d' \
  -d '{
            "clientType": "BUSINESS",
            "ssn": "123456781",
            "profile": {
                "firstname": "fenil ",
                "lastname": "shah",
                "address": {
                    "street1": "street1",
                    "street2": "street2",
                    "city": "city",
                    "state": "state",
                    "zip": "72131"
                },
                "dob": "2012-09-17",
                "phone": "123-234-3456",
                "emailId": "fshah1@gmail.com"
            },
            "finance": [
                {
                    "organizationName":"FshahFirms",
                    "organizationAddress": {
                        "street1": "street1",
                        "street2": "street2",
                        "city": "city",
                        "state": "state",
                        "zip": "72131"
                    },
                    "taxId": "12345"
                }
            ]
        }'
 ```
#### To get a particular client's info using their email id
```
curl -X GET \
  http://localhost:8080/clients/fshah1@gmail.com \
  -H 'cache-control: no-cache' \
  -H 'postman-token: aea317cf-819d-f90a-81ae-ded0ac2cd3c6'
```
#### To get all clients
```
curl -X GET \
  http://localhost:8080/clients \
  -H 'cache-control: no-cache' \
  -H 'postman-token: eb663165-95d7-caa0-c8b1-23ecdd39003a'
```
#### To get clients by client type
```
curl -X GET \
  'http://localhost:8080/clients?type=BUSINESS' \
  -H 'cache-control: no-cache' \
  -H 'postman-token: 31515e23-b07a-67ae-6d66-7ae358e3b2d3'
```