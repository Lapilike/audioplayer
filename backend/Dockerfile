FROM maven:latest AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17

ENV SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/audio
ENV DB_USERNAME=postgres
ENV DB_PASSWORD=24032023

WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]