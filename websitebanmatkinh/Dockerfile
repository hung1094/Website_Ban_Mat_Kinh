# --- Giai đoạn 1: Build dự án ---
# Sử dụng image Maven có hỗ trợ JDK mới nhất để build
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy file cấu hình maven và mã nguồn
COPY pom.xml .
COPY src ./src

# Build dự án tạo file WAR (Skip test để build nhanh hơn trên Render)
RUN mvn clean package -DskipTests

# --- Giai đoạn 2: Chạy ứng dụng ---
# Sử dụng OpenJDK 25 (Bản slim để giảm dung lượng image)
FROM openjdk:25-slim
WORKDIR /app

# Vì packaging là war, Maven sẽ tạo file trong thư mục target/
# Chúng ta copy file .war vào và đổi tên thành app.war để dễ quản lý
COPY --from=build /app/target/*.war app.war

# Mở port 8080
EXPOSE 8080

# Chạy ứng dụng Spring Boot trực tiếp từ file WAR
ENTRYPOINT ["java", "-jar", "app.war"]