# === Etapa 1: Build do Backend com Maven + JDK 21 ===
FROM maven:3.9.4-eclipse-temurin-21 AS backend-build

WORKDIR /app/backend

# Copia pom.xml e src do backend
COPY backend/pom.xml ./
COPY backend/src ./src

# Build do backend, pulando testes
RUN mvn clean package -DskipTests

# === Etapa 2: Imagem final para execução ===
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copia o JAR final
COPY --from=backend-build /app/backend/target/*.jar app.jar

# Expõe a porta do Render (usaremos variável PORT)
ENV PORT=8080
EXPOSE ${PORT}

# Comando para rodar a aplicação, pegando porta do Render
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT} -jar app.jar"]
