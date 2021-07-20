# International Suite Service

This application exposes an API that returns aggregated data from different MC internal products.

  - [Configuration](#configuration)
    + [Requirements](#requirements)
    + [Installation](#installation)
      - [Compilation](#compilation)
      - [Package and run](#package-and-run)
    + [Database configuration](#database-configuration)
      - [Docker database container preparation](#docker-database-container-preparation)
  * [Database Migration](#database-migration)
    + [P27](#p27)
    + [SAMA](#sama)
    + [Generic migration script](#generic-migration-script)
    + [Maven plugin](#maven-plugin)
- [Post-deployment](#post-deployment)
- [Swagger](#swagger)
- [Release](#release)

## Configuration

### Requirements

- Java JDK 8
- Maven 3.6.3 (provided)
- Apache Tomcat 7+

### Installation

- Download and install Java JDK 8 from the [Java official webpage](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html). 
- Set JAVA_HOME environment variable to point to the previous installation.

#### Compilation

```shell
./mvnw compile
```

#### Package and run 

```shell
./mvnw clean package
```

The resulting artifact will be generated in /target folder. Deploy it to an external application server.

### Database configuration

The main application uses an Oracle database for non-local environments, which is accessed 
using a JNDI data source named `jdbc/ISS_DB`. For local environments, an in-memory h2 database is 
used.

A Flyway Autoconfiguration is used at the moment to run DB migrations, which are placed in 
`src/main/resources/db/migration`. 
They are versioned according to the following format: `V<datetime>__<migration_name>.sql`

#### Docker database container preparation

Make sure the docker can allocate at least 4GB of RAM. Full description of the steps to install database docker container can be found [here](https://github.com/vocalink/cp-jenkins-jobs/tree/docker/oracle).

## Database Migration

### P27

```bash
docker run --rm --network=host \
-v $(pwd)/database/migrations/common:/flyway/sql/common \
-v $(pwd)/database/migrations/data/p27:/flyway/sql/data \
flyway/flyway \
-url=jdbc:oracle:thin:@127.0.0.1:1521/PDB112 \
-user='SYS as SYSDBA' \
-password=Password123 \
-schemas=CPUI_DB_OWNER \
-placeholders.schemaOwner=CPUI_DB_OWNER \
-placeholders.schemaUser=CPUI_DB_USER \
-placeholders.project.version=local \
migrate
```

**Note:** some users should pass -v parameters as absolute paths or use `${PWD}` environment variable 

### SAMA

```shell
docker run --rm --network=host \
-v $(pwd)/database/migrations/common:/flyway/sql/common \
-v $(pwd)/database/migrations/data/sama:/flyway/sql/data \
flyway/flyway \
-url=jdbc:oracle:thin:@127.0.0.1:1521/PDB112 \
-user='SYS as SYSDBA' \
-password=Password123 -schemas=CPUI_DB_OWNER \
-placeholders.schemaOwner=CPUI_DB_OWNER \
-placeholders.schemaUser=CPUI_DB_USER \
-placeholders.project.version=local \
migrate
```

### Generic migration script

The above-mentioned dockerized flyway tool can run by the wrapping script `./local-migrate.sh` 

```shell
./local-migrate.sh <flyway-goal> <scheme>
```

`<flyway-goal>` - flyway plugin goal (e.g. `clean`), default: `migrate`

`<scheme>` - scheme name (`sama` or `p27`), default: `p27`

### Maven plugin

```shell
cd database
./mvnw flyway:info -Piss-db -Dscheme=<sheme>
```

or with mvn with custom parameters

```shell
cd database
./mvnw flyway:info -Piss-db \
-Dscheme=<scheme> \
-Dflyway.url="jdbc:oracle:thin:@localhost:1521/PDB112" \
-Dflyway.user='SYS as SYSDBA' \
-Dflyway.password='Password123' \
-Dflyway.schemas=CPUI_DB_OWNER,CPUI_DB_USER \
-Dflyway.placeholders.schemaOwner=CPUI_DB_OWNER \
-Dflyway.placeholders.schemaUser=CPUI_DB_USER
```

`<sheme>` -  scheme name (`sama` or `p27`)

# Post-deployment

1. [General information](http://localhost:8080/international-suite-service/actuator/info) about the application
2. Health statuses actuator [endpoint](http://localhost:8080/international-suite-service/actuator/health)
3. Contents of the application [log file](http://localhost:8080/international-suite-service/actuator/logfile)
4. Application environment variables actuator [endpoint](http://localhost:8080/international-suite-service/actuator/env)

# Swagger

In order to navigate through the implemented REST APIs specification, please access the below link.
   http://localhost:8080/international-suite-service/swagger-ui/

# Release

The release of this project is managed by [semantic-release](https://github.com/semantic-release/semantic-release) library. The current version is tracked in the pom.xml file

