# AutoScoutCodingChallenge Application

## What has been done

* **Below approach has been taken by me to meet the requirements**
```
  $ As a predominant Java backend developer, I used the Spring Boot application to meet the requirements.
   Rest API endpoints have been created for each requirement(total 4) mentioned.And no UI has been developed to show the report due to time constraint.
   Hence report format would be JSON outputs of the Rest endpoints exposed, see below in the corresponding section to know how to access the appropriate endpoint.
```

## How to build application

* **Build application using Maven**
```
  $ mvn clean install
```

## How to run application

* **Start application using executable**
```
  $ java -jar target/autoscout.codingchallenge-0.0.1-SNAPSHOT.jar
```
* **Start application using maven**
```
  $ mvn spring-boot:run
```


## API endpoints for requirement

* **Average Listing Selling Price per Selling Type**
```
  $ http://localhost:8080/averageSellingPriceBySellerType
   JSON Response
  {"dealer":25037.34,"other":25317.76,"private":26080.48}
  
```
* **Distribution of available cars by make**
```
  $ http://localhost:8080/percentageByMake
  JSON Response
  {"Toyota":16.0,"Mercedes-Benz":16.0,"Renault":14.0,"Audi":14.0,"Mazda":13.0,"VW":10.0,"Fiat":9.0,"BWM":7.0}
```
* **Average price of the 30% most contacted listings**
```
  $ http://localhost:8080/averagePriceOf30MostContacted
  JSON Response
  24638.87
```
* **The Top5 most contacted listings per month**
```
  $ http://localhost:8080/mostContactedListingByMonth/{month}  
  pass month value as (JANUARY,FEBRUARY,MARCH ...)   
  JSON Response for JANUARY
  [["1061","Renault","5641","7000","21"],["1132","Mercedes-Benz","34490","7000","18"],["1077","Mercedes-Benz","8007","4000","17"],["1099","BWM","5914","8500","17"],["1122","Audi","40481","2000","17"]]
  
```


## Running test classes

* **AutoScoutControllerTest.java**
```
  $ Above Java resource file contains integration tests for all the API enpoints using Spring MVC test framework 
    We can run entire file at once or individual test one at a time
```
* **AutoScoutComputeServiceTest.java**
```
  $ Above Java resource file contains unit tests for all the service methods used using Junit and Mockito
    We can run entire file at once or individual test one at a time
```