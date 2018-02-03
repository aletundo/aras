# Builder image
FROM maven:3.5-jdk-8 as builder
COPY . /home/root/build/
WORKDIR /home/root/build/
RUN mvn package -DskipTests

# Executer image
FROM openjdk:8-jre-alpine
COPY --from=builder /home/root/build/target/*.jar \
/srv/eureka-server/eureka-server.jar
WORKDIR /srv/eureka-server/
EXPOSE 8761
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "eureka-server.jar"]
