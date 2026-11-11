
FROM maven:3.9-eclipse-temurin-26 AS build

WORKDIR /workspace

COPY pom.xml ./
COPY src ./src

RUN mvn -B -DskipTests package

FROM eclipse-temurin:26-jre

WORKDIR /app

COPY --from=build /workspace/target/retech-api-0.1.0-SNAPSHOT.jar app.jar

EXPOSE 10000

ENTRYPOINT ["java", "-jar", "app.jar"]
