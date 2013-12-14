#SNP4J

A blocking implementation of the SNP protocol to talk with [Snarl](http://snarl.fullphat.net/).

#Environment
The following build environment is required to build this project:

* `maven-3.0.5`, `java-1.6`

#Build
To build the project:

	mvn clean verify

#Release
To release the project:

	mvn release:prepare release:perform -B

