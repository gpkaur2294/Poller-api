FROM openjdk:8-jdk-alpine
EXPOSE 8080
RUN mkdir -p /app/
ADD target/poller*.jar /app/poller.jar
ENTRYPOINT ["java", "-jar", "/app/poller.jar"]