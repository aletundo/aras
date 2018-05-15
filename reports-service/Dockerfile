# Builder image
FROM maven:3.5-jdk-8-alpine as builder
WORKDIR /home/root/build/reports-service/
COPY pom.xml .
RUN mvn -B -C -T 1C org.apache.maven.plugins:maven-dependency-plugin:3.0.2:go-offline
COPY . .
RUN mvn -B -o -T 1C package -DskipTests

# Executer image
FROM openjdk:8-jre-alpine
ENV LC_ALL=C
ENV LANG=C.UTF-8
RUN apk update && apk --no-cache add bash libstdc++ ca-certificates openssl && \
    wget -q -O /etc/apk/keys/sgerrand.rsa.pub https://raw.githubusercontent.com/sgerrand/alpine-pkg-glibc/master/sgerrand.rsa.pub && \
    apk --no-cache -X https://apkproxy.herokuapp.com/sgerrand/alpine-pkg-glibc add glibc glibc-bin
COPY --from=builder /home/root/build/reports-service/target/*.jar \
/srv/reports-service/reports-service.jar
WORKDIR /srv/reports-service/
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "reports-service.jar"]
