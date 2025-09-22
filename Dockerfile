FROM openjdk:17-jdk-alpine
LABEL authors="AyselS"

EXPOSE 8080

COPY target/CloudService-0.0.1-SNAPSHOT.jar cloudservice.jar

CMD ["java", "-jar", "cloudservice.jar"]