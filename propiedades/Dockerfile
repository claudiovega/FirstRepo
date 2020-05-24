
# For Java 8, try this
# FROM openjdk:8-jdk-alpine


# For Java 11, try this
FROM adoptopenjdk/openjdk11:alpine-jre
ENV TZ Chile/Continental
# Refer to Maven build -> finalName
ARG JAR_FILE=target/app-proceso-propiedades.jar

# cd /opt/app
WORKDIR /opt/app

# cp target/spring-boot-web.jar /opt/app/app.jar
COPY ${JAR_FILE} propiedades.jar

# java -jar /opt/app/app.jar
ENTRYPOINT ["java","-jar","propiedades.jar"]

VOLUME create container-data




