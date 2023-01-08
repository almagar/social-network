FROM maven:3.8.5-openjdk-17 as builder

WORKDIR /backend
COPY . .
RUN mvn -Dspring-boot.run.profiles=production -Dmaven.test.skip=true clean package

FROM eclipse-temurin:17-jre-alpine
WORKDIR /backend
COPY --from=builder /backend/target/social-network-0.0.1-SNAPSHOT.jar .

ENTRYPOINT ["java", "-jar", "-Dspring-boot.run.profiles=production", "social-network-0.0.1-SNAPSHOT.jar"]
