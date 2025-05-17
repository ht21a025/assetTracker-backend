# Java 21 の JDK をベースに使用
FROM eclipse-temurin:21-jdk

# 作業ディレクトリを作成
WORKDIR /app

# プロジェクト全体をコピー
COPY . .

# gradlew に実行権限を付与
RUN chmod +x ./gradlew

# ✅ Gradle ビルド（テストをスキップ）
RUN ./gradlew build -x test

# Spring Boot アプリの起動
CMD ["java", "-jar", "build/libs/assettracker-0.0.1-SNAPSHOT.jar"]
