FROM maven:3.9.4-eclipse-temurin-17 AS dev
WORKDIR /app

# Kopiuj najpierw plik pom.xml, pobierz zależności – dzięki temu Docker cache'uje je, jeśli dependencies się nie zmieniły
COPY pom.xml .
RUN mvn dependency:go-offline

# Potem dopiero kopiuj resztę kodu źródłowego (src, avro, ewentualnie inne katalogi)
COPY src ./src
COPY src/main/avro ./src/main/avro

RUN mvn clean package -DskipTests

EXPOSE 8082

# Domyślnie uruchamiaj przez Maven spring-boot:run
ENTRYPOINT ["mvn", "spring-boot:run"]
