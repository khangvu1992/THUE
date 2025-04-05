# Sử dụng OpenJDK 17
FROM openjdk:17
WORKDIR /app

# Copy file JAR từ thư mục target vào container
COPY target/*.jar app.jar

# Expose cổng 8080
EXPOSE 8080

# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]
