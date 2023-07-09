FROM ubuntu:latest AS build
RUN apt-get update
RUN apt-get install openjdk-11-jdk -y
COPY . .
RUN ./gradlew build

FROM openjdk:11-jdk-slim
EXPOSE 8081
COPY --from=build /build/libs/demo-1.jar app.jar

ENTRYPOINT ["java" , "-jar " , "app.jar"]