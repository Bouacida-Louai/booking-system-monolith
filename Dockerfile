# ── Stage 1: Build ────────────────────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests -B

# ── Stage 2: Run ──────────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S booking && adduser -S booking -G booking

COPY --from=builder /app/target/*.jar app.jar

RUN chown booking:booking app.jar

USER booking

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]