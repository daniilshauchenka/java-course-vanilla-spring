# Blog backend (Spring)
Backend of a blog web application implemented using Spring Framework

# Tech stack:
* Java 21
* Spring Framework 6 (Core, WebMVC, JDBC)
* JDBC (no ORM)
* H2  
* Liquibase
* JUnit 5 + Mockito 
* MockMvc
* Gradle 
* Tomcat

## Build
```bash
./gradlew build
```

## Tests
```bash 
./gradlew test
```

## Run Application (Tomcat)
### Build WAR:
```bash
./gradlew build
```
### Locate WAR file:
``` 
build/libs/*.war
```

### Copy it to:
```
tomcat/webapps/
```
### Start Tomcat:
```
bin/startup.sh
```
### Application will be available at:
```
http://localhost:8080
```