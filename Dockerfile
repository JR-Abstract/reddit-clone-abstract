FROM maven:3.9.6 AS build

WORKDIR /app

COPY . .

RUN mvn clean install -DskipTests

RUN addgroup --system spring && adduser --system --group spring

USER spring

CMD ["mvn", "spring-boot:run"]
