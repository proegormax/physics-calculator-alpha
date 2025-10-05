# --- Stage 1: build ---
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -e -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -q -DskipTests package

# --- Stage 2: runtime ---
FROM eclipse-temurin:21-jre
WORKDIR /app
# Render задаёт порт через $PORT → пробросим его в Spring
ENV JAVA_OPTS="-Dserver.port=${PORT}"
COPY --from=build /app/target/*SNAPSHOT*.jar app.jar
# если имя файла фиксировано, то лучше COPY /app/target/app.jar app.jar
EXPOSE 8080
CMD ["sh","-c","java $JAVA_OPTS -jar app.jar"]
