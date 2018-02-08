# CI Pipeline

Simple pipeline which uses docker multistage to build test and create executable JAR.

## Build & Test
Run the following command to build the application:
 
```bash
java-resilient-rest-api $ docker build -t <tag> .
```
docker build -t <tag> Dockerfile
## Local Deploy 

To locally deploy the application run:
```bash
java-resilient-rest-api $ docker run --restart always -p ${LOCAL_PORT}:8080 ${SERVICE}
```
