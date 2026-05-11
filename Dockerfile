# Render: Docker runtime, Root Directory = backend/inventory/inventory (Dockerfile Path ./Dockerfile).
# Java 25 + Gradle 9.x match build.gradle / wrapper.

FROM gradle:9.4.1-jdk25-alpine AS build
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY src ./src
RUN gradle bootJar --no-daemon \
    && cp "$(find build/libs -maxdepth 1 -name '*.jar' ! -name '*-plain.jar' | head -n1)" /app/application.jar

FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /app/application.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
