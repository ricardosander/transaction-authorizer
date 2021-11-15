# transaction-authorizer

## Build & Running

### JDK

#### Maven
`mvn clean package`
`java -jar target/authorizer.java < file`

### No Maven? No probleam
`./mvnw clean package`
`java -jar target/authorizer.java < file`


## Docker

### Build
`docker image build -t authorizer .`

### Running
`docker container run -i --rm authorizer < file`