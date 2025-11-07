# syntax=docker/dockerfile:1.6

# ---- Build stage ----------------------------------------------------------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace

# Copy Gradle wrapper and project metadata first for better caching
COPY gradlew ./
COPY gradle ./gradle
COPY settings.gradle build.gradle ./

RUN chmod +x gradlew

# Copy source code last to leverage Docker layer caching
COPY src ./src

# Build the Spring Boot fat jar
RUN ./gradlew bootJar --no-daemon && \
    JAR_FILE=$(find build/libs -maxdepth 1 -type f -name "*-SNAPSHOT.jar" ! -name "*-plain.jar" | head -n 1) && \
    mv "$JAR_FILE" app.jar

# ---- Runtime stage --------------------------------------------------------
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /workspace/app.jar ./app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
