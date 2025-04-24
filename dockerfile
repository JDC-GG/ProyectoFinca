# Etapa de build
FROM maven:3.8.5-openjdk-17-slim AS build

WORKDIR /app
COPY . .

RUN mvn clean package -DskipTests

# Etapa final: solo copiamos el jar ya construido
FROM openjdk:17-slim

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

CMD ["java", "-jar", "app.jar"]
