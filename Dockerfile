FROM openjdk:18

WORKDIR /app

COPY target/g1-0.0.1-SNAPSHOT.jar /app/

EXPOSE 8081

CMD ["java", "-jar", "g1-0.0.1-SNAPSHOT.jar"]
