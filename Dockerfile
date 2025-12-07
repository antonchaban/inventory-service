# --- STAGE 1: Build ---
# ЗМІНА ТУТ: Прибираємо "-alpine", використовуємо повноцінний Linux (Debian/Ubuntu based)
# Це дозволить запустити protoc компілятор без помилок.
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# Копіюємо конфіг і качаємо залежності (кешування шарів)
COPY pom.xml .
# Завантажуємо всі залежності (включно з плагінами), щоб пришвидшити наступні збірки
RUN mvn dependency:go-offline

# Копіюємо вихідний код
COPY src ./src

# Збираємо додаток.
# -DskipTests пропускає тести, бо в контейнері може не бути доступної БД
RUN mvn clean package -DskipTests

# --- STAGE 2: Runtime ---
# Для запуску ми все ще можемо використовувати легкий Alpine,
# бо JAR файл - це байт-код, йому байдуже на тип Linux.
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Копіюємо зібраний JAR з попереднього етапу
COPY --from=builder /app/target/*.jar app.jar

# Порти, які слухає додаток
EXPOSE 8080 9090

ENTRYPOINT ["java", "-jar", "app.jar"]