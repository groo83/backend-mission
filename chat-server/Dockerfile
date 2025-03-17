# Build
FROM gradle:8.5-jdk21 AS build
WORKDIR /app

# 소스 코드 복사
COPY . .

# Gradle 빌드
RUN gradle clean build -x test

# Run
FROM eclipse-temurin:21-jre
WORKDIR /app

# JAR 파일 복사
COPY build/libs/*.jar app.jar

# 빌드 결과물 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 컨테이너에서 사용할 포트 설정
EXPOSE 8080

# 컨테이너 실행 시 Java 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]