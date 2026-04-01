# Giai đoạn 1: Build
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Giai đoạn 2: Chạy
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
# Copy file .war hoặc .jar (Dùng dấu * cho chắc chắn)
COPY --from=build /app/target/*.war app.war || COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
# Chạy app (thử cả 2 đuôi file)
ENTRYPOINT ["java", "-jar", "app.war"]