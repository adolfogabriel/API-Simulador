# Imagem base do JDK 17
FROM eclipse-temurin:17-jdk as build

# Diretório de trabalho
WORKDIR /app

# Copia o JAR da aplicação (ajuste conforme o seu build)
COPY target/*.jar app.jar

# Expor a porta configurada
EXPOSE 8080

# Executa a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]