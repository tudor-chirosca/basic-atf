# International Suite Service

This application exposes an API that returns aggregated data from different MC internal products.

### Configuration

#### Database

The main application uses an Oracle database for non-local environments, which is accessed 
using a JNDI Datasource named jdbc/ISS_DB. For local environments, an in-memory h2 database is 
used.

A Flyway Autoconfiguration is used at the moment to run DB migrations, which are placed in 
src/main/resources/db/migration. 
They are versioned according to the following format: V<datetime>__<migration_name>.sql


### Requirements

- Java JDK 8
- Maven 3.6.3 (provided)
- Apache Tomcat 7+

### Installation

- Download and install Java JDK 8 from the [Java official webpage](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html). 
- Set JAVA_HOME environment variable to point to the previous installation.

### Compilation

```
./mvnw compile
```

### Package and run 

```
./mvnw clean package
```

The resulting artifact will be generated in /target folder. Deploy it to an external application server.

### Docker Database preparation

- [Docker steps find here](https://github.com/vocalink/cp-jenkins-jobs/tree/docker/oracle)

### Database Migration

```
docker run --rm --network host -v /${PWD}/database:/flyway/sql flyway/flyway -url=jdbc:oracle:thin:@127.0.0.1:1521/PDB112 -user='SYS as SYSDBA' -password=Password123 -schemas=CPUI_DB_OWNER -placeholders.schemaOwner=CPUI_DB_OWNER  -placeholders.schemaUser=CPUI_DB_USER -placeholders.project.version=local-version migrate
```

or

```
# migrate goal will run if no parameter specified

./local-migrate.sh <flyway-goal>
```

or with mvn for local docker db

```
cd database
./mvnw flyway:info -Piss-db,local-env
```

or with mvn with custom parameters

```
cd database
./mvnw flyway:info -Piss-db -Dflyway.url="jdbc:oracle:thin:@localhost:1521/PDB112" -Dflyway.user='SYS as SYSDBA' -Dflyway.password='Password123' -Dflyway.schemas=CPUI_DB_OWNER,CPUI_DB_USER -Dflyway.placeholders.schemaOwner=CPUI_DB_OWNER -Dflyway.placeholders.schemaUser=CPUI_DB_USER
```

### Release

The release of this project is managed by [semantic-release](https://github.com/semantic-release/semantic-release) library. The current version is tracked in the pom.xml file
