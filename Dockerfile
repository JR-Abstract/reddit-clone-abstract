FROM maven:3.9.6 AS build

WORKDIR /app

COPY . .

RUN mvn clean install -DskipTests

CMD mvn spring-boot:run
