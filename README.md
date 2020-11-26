# Cross Product International Suite Service

This application exposes an API with aggregated data which is consumed from different products.


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

### Release

The release of this project is managed by the [standard-version](https://github.com/conventional-changelog/standard-version) library. The current version is tracked in the ```version.txt``` file.
