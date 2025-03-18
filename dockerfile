# Usar una imagen base de Java
FROM openjdk:17-jdk-alpine

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo JAR de la aplicación
COPY target/javeriana-0.0.1-SNAPSHOT.jar app.jar

# Puerto expuesto
EXPOSE 8081

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"] 