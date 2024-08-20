# OpenJDK 11 이미지를 베이스 이미지로 사용
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /apps

COPY ./build/libs/*.jar /apps/escape-reservation.jar

# 어플리케이션 포트 설정 (예: 8080)
EXPOSE 8080

# 어플리케이션 실행
CMD ["java", "-jar", "/apps/escape-reservation.jar"]