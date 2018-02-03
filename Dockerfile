# Build Environment
FROM maven:3.5.2-jdk-8-alpine AS build

WORKDIR /build

ADD ./pom.xml /build
RUN mvn install

ADD ./src /build/src
RUN mvn clean
RUN mvn package

# Runtime Environment
FROM openjdk:8-jre
COPY --from=build /build/target/demo-0.0.1-SNAPSHOT.jar /bin/demo/demo-0.0.1-SNAPSHOT.jar

EXPOSE 8080

CMD ["java", "-jar", "/bin/demo/demo-0.0.1-SNAPSHOT.jar"]