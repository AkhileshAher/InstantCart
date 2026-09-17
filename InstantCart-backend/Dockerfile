# ---------- Build Stage ----------
FROM eclipse-temurin:24-jdk AS build

WORKDIR /app

# Copy Maven files first
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make Maven wrapper executable
RUN chmod +x mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src src

# Build application
RUN ./mvnw clean package -DskipTests


# ---------- Runtime Stage ----------
FROM eclipse-temurin:24-jre

WORKDIR /app

# Copy generated JAR
COPY --from=build /app/target/*.jar app.jar

EXPOSE 9091

ENTRYPOINT ["java", "-jar", "app.jar"]