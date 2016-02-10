# web-server

This REST api exposes electoral data (wards, constituencies and electors) to the web-client.

## Dependencies

- Java 8+
- PostgreSQL
- Maven 3+

## Run the application

The component is bundled as a fat jar (including dependencies) and can be run as follows:

```
java -jar target/web-server-0.0.1-SNAPSHOT.jar
```

## PAF api calls

The following calls are made into PAF. PAF provides voter and address data

```
/v1/wards/{code}/streets

/v1/wards/town/{townName}/streets/{streetName}
```