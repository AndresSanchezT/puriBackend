# ========= BUILD =========
FROM eclipse-temurin:21-jdk AS build

WORKDIR /build

# 1️⃣ Copiamos solo lo necesario para cachear dependencias
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN ./mvnw dependency:go-offline

# 2️⃣ Copiamos el código
COPY src src

# 3️⃣ Construimos el jar
RUN ./mvnw clean package -DskipTests


# ========= RUNTIME =========
FROM eclipse-temurin:21-jre-ubi10-minimal

# ✅ Instalar fuentes necesarias para JasperReports
RUN microdnf install -y \
    fontconfig \
    dejavu-sans-fonts \
    dejavu-serif-fonts \
    dejavu-sans-mono-fonts \
    && microdnf clean all

WORKDIR /app

COPY --from=build /build/target/SISTEMASPURI-0.0.1-SNAPSHOT.jar app.jar

ENV PORT 8087
EXPOSE $PORT

ENTRYPOINT ["java", "-Djava.awt.headless=true", "-jar", "app.jar"]
