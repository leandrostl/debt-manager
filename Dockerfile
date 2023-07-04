FROM openjdk:11-jre

COPY build/libs/debt-manager-0.0.1-SNAPSHOT.jar /app.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=test", "-jar", "/app.jar"]
EXPOSE 8080
