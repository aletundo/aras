# Builder image
FROM maven:3.5-jdk-8-alpine as builder
WORKDIR /home/root/build/config-server/
COPY pom.xml .
RUN mvn -B -C -T 1C org.apache.maven.plugins:maven-dependency-plugin:3.0.2:go-offline
COPY . .
RUN mvn -B -o -T 1C package -DskipTests

# Executer image
FROM openjdk:8-jre-alpine

RUN apk update && apk add tzdata
RUN cp /usr/share/zoneinfo/Europe/Rome /etc/localtime
RUN echo "Europe/Rome" >  /etc/timezone
RUN apk del tzdata

COPY --from=builder /home/root/build/config-server/target/*.jar \
/srv/config-server/config-server.jar
WORKDIR /srv/config-server/
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-XX:+PrintFlagsFinal", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-jar", "config-server.jar"]
