FROM ubuntu:latest AS build
RUN apt-get update
RUN apt-get install openjdk-11-jdk -y
WORKDIR /app
COPY . .
RUN ./gradlew bootJar --no-daemon

FROM openjdk:11-jdk-slim
EXPOSE 8081
COPY --from=build /app/build/libs/demo-1.jar app.jar

ENTRYPOINT ["java" , "-jar " , "app.jar"]