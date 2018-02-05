# Builder image
FROM maven:3.5-jdk-8 as builder
COPY . /home/root/build/
WORKDIR /home/root/build/
RUN mvn clean package -DskipTests

# Executer image
FROM openjdk:8-jre-alpine
COPY --from=builder /home/root/build/target/*.jar \
/srv/notifications-service/notifications-service.jar
WORKDIR /srv/notifications-service/

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "notifications-service.jar"]
