# Usar una imagen base de Java
#Usar la imagen base de OpenJDK 17
FROM openjdk:17

# Copiar el código fuente de la aplicación al contenedor
COPY . /app

# Establecer el directorio de trabajo
WORKDIR /app

# Construir el JAR sin ejecutar pruebas
WORKDIR /app/arriendatufinca
RUN ./mvnw clean install -DskipTests

# Ejecutar la aplicación cuando se inicie el contenedor
CMD ["java", "-jar", "target/javeriana-0.0.1-SNAPSHOT.jar"]