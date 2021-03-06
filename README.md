# Spring Camel JMS to DB Demo
This is a demo of using Apache Camel in Spring Boot to create an integration that extracts an XML message from 
a JMS queue, converts it into an object and places it in the database.

## Addidional info
[Oracle® Fusion Middleware Developing JAXB Applications Using Oracle TopLink](https://docs.oracle.com/middleware/1212/toplink/TLJAX/toc.htm)

## Directory layout
The project has [Standard Maven Directory Layout](https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html).

## Build from Source

### Before You Start
To build you will need:
1) Installed and configured [Git](https://help.github.com/en/github/getting-started-with-github/set-up-git);
2) Installed and configured [Java SE Development Kit 8](https://www.oracle.com/technetwork/java/javase/downloads/index.html); 
or later. Be sure that your ```JAVA_HOME``` environment variable points to 
the ```jdk1.8.0``` folder extracted from the JDK download
3) [Installed](https://maven.apache.org/install.html) and configured [Maven](https://maven.apache.org/download.cgi)

### Get the Source Code
```shell script
git clone git@github.com:mefmor/spring-camel-jms-to-db-demo.git
cd spring-camel-jms-to-db-demo
```

### Build from the Command Line
To compile, test, and build all jars, distribution zips, and docs use:
```shell script
./mvn build
```

## Source Code Style
The wiki pages [Code Style](https://github.com/spring-projects/spring-framework/wiki/Code-Style) 
and [IntelliJ IDEA Editor Settings](https://github.com/spring-projects/spring-framework/wiki/IntelliJ-IDEA-Editor-Settings) 
define the source file coding standards we use along with some IDEA editor settings we customize.

