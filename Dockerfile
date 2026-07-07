FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/*.jar spring-dsl-flow.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "spring-dsl-flow.jar"]