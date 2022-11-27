#FROM openjdk:17
#ADD target/social-network-spring-boot.jar social-network-spring-boot.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "social-network-spring-boot.jar"]

FROM maven:3.8.5-openjdk-17

WORKDIR /backend
COPY . .
RUN mvn -Dmaven.test.skip=true clean package

CMD mvn spring-boot:run -Dspring-boot.run.profiles=production
