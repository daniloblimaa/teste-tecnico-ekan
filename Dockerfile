FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml ./
COPY src ./src

RUN mvn -B -DskipTests package --no-transfer-progress


FROM eclipse-temurin:17-jre-alpine
WORKDIR /app


COPY --from=build /app/target/*.jar app.jar


RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

EXPOSE 8080


ENV JAVA_OPTS="-Xms256m -Xmx512m"

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]

