# Builder image
FROM maven:3.5-jdk-8-alpine as builder
WORKDIR /home/root/build/projects-service/
COPY pom.xml .
RUN mvn -B -C -T 1C org.apache.maven.plugins:maven-dependency-plugin:3.0.2:go-offline
COPY . .
RUN mvn -B -o -T 1C package -DskipTests

# Executer image
FROM openjdk:8-jre-alpine
ENV LC_ALL=C
ENV LANG=C.UTF-8

RUN apk update && apk add tzdata
RUN cp /usr/share/zoneinfo/Europe/Rome /etc/localtime
RUN echo "Europe/Rome" >  /etc/timezone
RUN apk del tzdata

RUN apk update && apk --no-cache add bash libstdc++ ca-certificates openssl && \
    wget -q -O /etc/apk/keys/sgerrand.rsa.pub https://raw.githubusercontent.com/sgerrand/alpine-pkg-glibc/master/sgerrand.rsa.pub && \
    apk --no-cache -X https://apkproxy.herokuapp.com/sgerrand/alpine-pkg-glibc add glibc glibc-bin
COPY --from=builder /home/root/build/projects-service/target/*.jar \
/srv/projects-service/projects-service.jar
WORKDIR /srv/projects-service/
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-XX:+PrintFlagsFinal", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-jar", "projects-service.jar"]
