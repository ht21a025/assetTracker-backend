FROM eclipse-temurin:21-jdk

WORKDIR /app
COPY . /app

RUN ./gradlew build

CMD ["java", "-jar", "build/libs/assettracker-0.0.1-SNAPSHOT.jar"]