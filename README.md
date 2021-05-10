# Poller API
Poller application can save,update,delete and get the URL info saved by the User. User can also get the stats. There is a poller running in the background to check the URL status based on the interval configured.
 

## Prerequisite
1. Maven
2. JDK 8
3. Docker
4. Shell
5. Port 8080,5432 should be available

## Build and run locally

Use the package manager [maven](https://maven.apache.org/) to build Poller API.

```bash
mvn clean package
java -jar poller-0.0.1-SNAPSHOT.jar
```

## Build and run with docker
Use the package manager [maven](https://maven.apache.org/) and [docker](https://www.docker.com) to build Poller API

```bash
mvn clean package
docker build -t poller .
docker-compose up
```

## API documentation
Please refer [swagger](http://localhost:8080/swagger-ui.html) for documentation.
