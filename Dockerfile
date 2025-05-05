FROM openjdk:17-jdk-slim
LABEL authors="Zanaad"
WORKDIR /app
COPY target/PingPongApp-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","app.jar"]
