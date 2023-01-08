# builder
FROM maven:3.8.6-eclipse-temurin-17 as builder

WORKDIR /workdir

COPY . /workdir/

RUN mvn package -Dmaven.test.skip=true -B


# runner
FROM eclipse-temurin:17-jdk

ENV VERTICLE_HOME /usr/verticles

EXPOSE 8888

COPY --from=builder /workdir/target/starter-1.0.0-SNAPSHOT.jar $VERTICLE_HOME/

ENV VERTICLE_FILE $VERTICLE_HOME/starter-1.0.0-SNAPSHOT.jar

WORKDIR $VERTICLE_HOME

ENTRYPOINT ["sh", "-c"]

CMD ["exec java -jar $VERTICLE_FILE"]
