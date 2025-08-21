
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Run Stage
FROM openjdk:17.0.1-jdk-slim
WORKDIR /app
COPY --from=build /app/target/singai_pos_management-1.0.0-SNAPSHOT.jar /app/singai_pos_management-1.0.0-SNAPSHOT.jar
EXPOSE 9001
ENTRYPOINT ["java", "-jar", "/app/singai_pos_management-1.0.0-SNAPSHOT.jar"]
