# --- Giai đoạn 1: Build dự án ---
FROM maven:3.9.9-amazoncorretto-25-al2023 AS build
WORKDIR /app

# Copy mã nguồn
COPY pom.xml .
COPY src ./src

# Build tạo file (Hỗ trợ Java 25 trực tiếp)
RUN mvn clean package -DskipTests

# --- Giai đoạn 2: Chạy ứng dụng ---
FROM amazoncorretto:25-al2023-headless
WORKDIR /app

# Copy file từ bước build sang (Dùng dấu * cho linh hoạt .jar/.war)
COPY --from=build /app/target/*.?ar /app/app.run

EXPOSE 8080

# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "/app/app.run"]