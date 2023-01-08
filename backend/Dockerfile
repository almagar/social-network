#FROM openjdk:17
#ADD target/social-network-spring-boot.jar social-network-spring-boot.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "social-network-spring-boot.jar"]

FROM maven:3.8.5-openjdk-17 as builder

WORKDIR /backend
COPY . .
RUN mvn -Dmaven.test.skip=true clean package

#CMD mvn spring-boot:run -Dspring-boot.run.profiles=production

FROM eclipse-temurin:17-jre-alpine
WORKDIR /backend
COPY --from=builder /backend/target/social-network-0.0.1-SNAPSHOT.jar .

ENTRYPOINT ["java", "-jar", "social-network-0.0.1-SNAPSHOT.jar"]
