# syntax=docker/dockerfile:1

FROM openjdk:17
WORKDIR /app

# Uwaga! Należy korzystać z tego Dockerfile razem z docker-compose.yaml
# Aby postawić tylko backend, zakomentuj te trzy linijki niżej i odkomentuj następne trzy

COPY ./backend/.mvn/ .mvn
COPY ./backend/mvnw/ ./backend/pom.xml ./
COPY ./backend/src/ ./src

#COPY .mvn/ .mvn
#COPY mvnw pom.xml ./
#COPY src/ ./src


RUN sed -i -e 's/\r$//' ./mvnw
RUN ./mvnw dependency:go-offline

EXPOSE 8080

CMD ["./mvnw", "spring-boot:run"]