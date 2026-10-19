
FROM maven:3.9-eclipse-temurin-25

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

EXPOSE 8080

CMD ["sh", "-c", "mvn jetty:run -Djetty.http.port=${PORT:-8080}"]
