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

### Database Migration

```
docker run --rm --network host -v /${PWD}/database:/flyway/sql flyway/flyway -url=jdbc:oracle:thin:@127.0.0.1:1521/PDB112 -user='SYS as SYSDBA' -password=Password123 -schemas=CPUI_DB_OWNER -placeholders.schemaOwner=CPUI_DB_OWNER  -placeholders.schemaUser=CPUI_DB_USER -placeholders.project.version=local-version migrate
```

or

```
# migrate goal will run if no parameter specified

./local-migrate.sh <flyway-goal>
```

### Release

The release of this project is managed by [semantic-release](https://github.com/semantic-release/semantic-release) library. The current version is tracked in the pom.xml file
