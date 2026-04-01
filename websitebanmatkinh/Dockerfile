# --- Giai đoạn 1: Build (Dùng Oracle JDK 25) ---
FROM container-registry.oracle.com/java/jdk:25 AS build
WORKDIR /app

# Cài đặt Maven
RUN microdnf install -y tar gzip curl
RUN curl -O https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz \
    && tar -xzf apache-maven-3.9.6-bin.tar.gz -C /opt \
    && ln -s /opt/apache-maven-3.9.6/bin/mvn /usr/bin/mvn

# Copy và Build
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# --- Giai đoạn 2: Chạy ứng dụng ---
FROM container-registry.oracle.com/java/jdk:25
WORKDIR /app

# SỬA LỖI TẠI ĐÂY: Dùng dấu * để copy bất kể là .jar hay .war mà không bị lỗi cú pháp
COPY --from=build /app/target/*.?ar /app/app.run

EXPOSE 8080

# Chạy file bất kể tên gốc là gì
ENTRYPOINT ["java", "-jar", "/app/app.run"]