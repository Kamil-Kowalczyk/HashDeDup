FROM maven:3.9.4-eclipse-temurin-21-alpine AS builder

WORKDIR /build

COPY pom.xml .
COPY src ./src
RUN mvn clean package

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app
COPY --from=builder /build/target/*-jar-with-dependencies.jar java-worker.jar
CMD ["java", "-jar", "java-worker.jar"]