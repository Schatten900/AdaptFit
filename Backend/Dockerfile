# =====================
# BUILD STAGE
# =====================
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

# Copia apenas o necessário primeiro (cache)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

# Agora copia o código
COPY src src

RUN ./mvnw clean package -DskipTests

# =====================
# RUNTIME STAGE
# =====================
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
