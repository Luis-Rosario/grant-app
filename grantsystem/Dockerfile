FROM openjdk:11-jre-slim

WORKDIR /app
COPY ./target/grants-0.0.1-SNAPSHOT.jar /app

EXPOSE 8080

CMD ["java", "-jar", "grants-0.0.1-SNAPSHOT.jar"]
