# Cross Product API management

This application exposes an API with aggregated data which is consumed from different products.


### Requirements

- Java JDK 8
- Gradle 5.4 (provided)

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

The resulting artifact will be generated in the /build/libs folder. Deploy it to an external application server.
