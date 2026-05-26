# SKM Backend

Backend van de SKM-applicatie voor het beheer van sportkampioenschappen.
Ontwikkeld in Java met Spring Boot en PostgreSQL.

## Vereisten

- Java JDK 22 of hoger (bij voorkeur Amazon Corretto)
- Apache Maven

Controleer via de terminal:
```bash
java -version
mvn -v
```

## Database configureren

Zorg dat PostgreSQL lokaal draait en maak een database aan:

```sql
CREATE DATABASE skm_db;
```

Pas het bestand `src/main/resources/application.properties` aan:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/skm_db
spring.datasource.username=postgres
spring.datasource.password=jouw_wachtwoord
```

## Applicatie uitvoeren

```bash
mvn spring-boot:run
```
