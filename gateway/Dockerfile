# Builder image
FROM maven:3.5-jdk-8-alpine as builder
WORKDIR /home/root/build/gateway/
COPY pom.xml .
RUN mvn -B -C -T 1C org.apache.maven.plugins:maven-dependency-plugin:3.0.2:go-offline
COPY . .
RUN mvn -B -o -T 1C package -DskipTests

# Executer image
FROM openjdk:8-jre-alpine
COPY --from=builder /home/root/build/gateway/target/*.jar \
/srv/gateway/gateway.jar
WORKDIR /srv/gateway/
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "gateway.jar"]
