# ---------- Build stage ----------
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests

# ---------- Run stage ----------
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# copy jar
COPY --from=build /build/target/*.jar app.jar

# Railway requires dynamic port
ENV PORT=8080

EXPOSE 8080

# run spring boot
ENTRYPOINT ["java","-jar","app.jar"]
