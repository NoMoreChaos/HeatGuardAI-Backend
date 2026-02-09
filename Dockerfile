# 1단계: 빌드 스테이지
FROM azul/zulu-openjdk:25-latest AS builder
WORKDIR /app

COPY . .
RUN ./gradlew clean build -x test || apt update && apt install -y gradle && gradle clean build -x test

# 2단계: 실행 스테이지
FROM eclipse-temurin:25-jre
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
