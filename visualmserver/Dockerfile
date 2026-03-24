# Stage 1: Build the JAR
FROM maven:3.8-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn package -DskipTests -B

# Stage 2: Run the app
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/target/visualmserver-*.jar app.jar

# Create images directory for uploads
RUN mkdir -p /app/images

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=production"]
