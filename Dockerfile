# Build Environment
FROM maven:3.5.2-jdk-8-alpine AS build

WORKDIR /build

ADD ./pom.xml /build
ADD ./src /build/src
RUN mvn clean package install

# Runtime Environment
FROM openjdk:8-jre
COPY --from=build /build/target/weather-0.0.1-SNAPSHOT.jar /bin/weather/weather-0.0.1-SNAPSHOT.jar

EXPOSE 8080

CMD ["java", "-jar", "/bin/weather/weather-0.0.1-SNAPSHOT.jar"]