# Build stage
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
# Limit memory for Render Free Tier (512MB) and use dynamic PORT
ENTRYPOINT ["java", "-Xmx384m", "-Xms128m", "-Dserver.port=${PORT}", "-jar", "app.jar"]
