FROM maven:latest AS build

WORKDIR /app

COPY . .

RUN mvn clean install -DskipTests

RUN addgroup --system spring && adduser --system --group spring

USER spring

CMD ["mvn", "spring-boot:run"]
