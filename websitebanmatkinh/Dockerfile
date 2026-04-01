# --- Giai đoạn 1: Build dự án ---
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# --- Giai đoạn 2: Chạy ứng dụng ---
# SỬA DÒNG NÀY ĐỂ KHÔNG BỊ LỖI NOT FOUND
FROM container-registry.oracle.com/java/jdk:25
WORKDIR /app

# Copy file .war (hoặc .jar tùy project của bạn)
COPY --from=build /app/target/*.war app.war

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.war"]