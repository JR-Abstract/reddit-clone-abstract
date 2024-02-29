FROM maven:latest AS build

WORKDIR /app

COPY . .

RUN mvn clean install

CMD mvn spring-boot:run
