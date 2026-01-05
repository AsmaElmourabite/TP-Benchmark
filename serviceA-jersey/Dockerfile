FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
COPY --from=build /app/target/jmx_prometheus_javaagent.jar jmx_prometheus_javaagent.jar
COPY jmx-config.yml jmx-config.yml
EXPOSE 8080 8081
ENTRYPOINT ["java", "-javaagent:jmx_prometheus_javaagent.jar=8081:jmx-config.yml", "-jar", "app.jar"]

