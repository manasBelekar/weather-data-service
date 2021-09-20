# weather-data-service
We have weather data service that provides a bunch of information.

# Project structure 
```bash
.
│   .classpath
│   .gitignore
│   .project
│   Dockerfile
│   Makefile
│   nginx.conf
│   pom.xml
│   README.md
│
├───.settings
│       org.eclipse.core.resources.prefs
│       org.eclipse.jdt.core.prefs
│       org.eclipse.m2e.core.prefs
│       org.springframework.ide.eclipse.prefs
│
├───config
│   ├───dev
│   │       kustomization.yaml
│   │       namespace.yaml
│   │       values.yaml
│   │
│   ├───local
│   │       kustomization.yaml
│   │       namespace.yaml
│   │       values.yaml
│   │
│   ├───preview
│   │       kustomization.yaml
│   │       namespace.yaml
│   │       values.yaml
│   │
│   └───prod
│           kustomization.yaml
│           namespace.yaml
│           values.yaml
│
├───helm
│   └───project
│       │   .helmignore
│       │   Chart.lock
│       │   Chart.yaml
│       │   values.yaml
│       │
│       ├───charts
│       │       oauth2-proxy-5.0.4.tgz
│       │       redis-10.7.13.tgz
│       │
│       └───templates
│               app.tpl
│               ingress.tpl
│               _capabilities.tpl
│               _helpers.tpl
│
└───src
    ├───main
    │   ├───java
    │   │   └───com
    │   │       └───tenera
    │   │           ├───common
    │   │           │       ControllerAdvisor.java
    │   │           │       WeatherUtility.java
    │   │           │
    │   │           ├───controller
    │   │           │       WeatherController.java
    │   │           │
    │   │           ├───dao
    │   │           │       WeatherDao.java
    │   │           │
    │   │           ├───exception
    │   │           │       InvalidCityException.java
    │   │           │
    │   │           ├───mapper
    │   │           │       ObjectMapper.java
    │   │           │       WeatherBeanToHIstoricalResponseMapper.java
    │   │           │       WeatherBeanToWeatherResponseMapper.java
    │   │           │       WeatherDataRowMapper.java
    │   │           │
    │   │           ├───model
    │   │           │       HistoricalResponse.java
    │   │           │       WeatherConditionEnum.java
    │   │           │       WeatherDataBean.java
    │   │           │       WeatherResponse.java
    │   │           │
    │   │           ├───service
    │   │           │       WeatherService.java
    │   │           │
    │   │           └───task
    │   │                   AppConfiguration.java
    │   │                   Application.java
    │   │
    │   └───resources
    │           application.properties
    │           banner.txt
    │           data.sql
    │
    └───test
        ├───java
        │   └───com
        │       └───tenera
        │           ├───common
        │           │       ControllerAdvisorTest.java
        │           │       WeatherUtilityTest.java
        │           │
        │           ├───controller
        │           │       WeatherControllerTest.java
        │           │
        │           ├───dao
        │           │       WeatherDaoTest.java
        │           │
        │           ├───service
        │           │       WeatherServiceTest.java
        │           │
        │           └───task
        │                   ApplicationTest.java
        │
        └───resources
```

# Infrastructure template
```
-> Config : carries required files specific to per enviroment deployment.
-> helm/project : carries template to deploy application to kubernetes cluster provided details .
-> Dockerfile : creating of image with respect to project.
-> Makefile : local deployment and testing 
        ```make build``` at root directory create a build ready for kuberenetes
        ```make deploy``` proceeding 'make build' deployed application to the specific context pointed at the moment.
```

# Information

### Libraries

#### Web container to serve the app 

   - tomcat
   
#### In-memory database 

   - H2
   
#### Unit test

   - JUnit 4
   - Mockito
   
# Instructions

### Test

    $ mvn test
    
### Build

    $ mvn install

### Run Locally

    $ java -jar weather-data-service-0.0.1-SNAPSHOT.jar
    
   
### Swagger documentation available at (locally)
   
   http://localhost:8000/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/
   
   ```sh
   sample data added for City 'Berlin' is prepopulated for historical API calculation
   ```
   
### Server (locally)
   
   The Application starts a tomcat server on localhost port 8080 with some sample data
   
- http://localhost:8000/api/v1/weather/history?location=Berlin
  for historical data
  
- http://localhost:8000/api/v1/weather/current?location=Berlin
  for current weather condition

### Http Status
- 200 OK
- 400 Bad Request 
- 404 Not Found
- 500 Internal Server Error 

### Docker
    -Build (to be executed at project root level)
    docker build -t weather-data-service .
    
    -run
    docker run -it weather-data-service -p 8080:8080

#### More information

 - Externalized the openweather url, api key and unit (for easy configuration w/o code change) in application.properties file.
 - Swagger enabled for APIs for easy documentation and testing
 - code having coverage of ~80% at the time of deployment
 - used inmemory H2 database, for storing the data. could be changed to file based DB or any other DB as well.
 - As weather data is not getting updated multiple times for same minute, modeled weatherData to have city_id and timestamp combination to be unique key in DB.
 - repeated record won't be stored to honor the unique constraint.
 - wrote DockerFile which actually build and deploy the our service to Docker.
 - Refer insfrastructure template for multi-environment deployment

# References 
https://spring.io/guides/topicals/spring-boot-docker/

