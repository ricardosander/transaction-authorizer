# transaction-authorizer

## Build & Running

### JDK

#### Maven
`mvn clean package`
`java -jar target/authorizer.java < file`

### No Maven? No problem
`./mvnw clean package`
`java -jar target/authorizer.java < file`

## Docker

### Build
`docker image build -t authorizer .`

### Running
`docker container run -i --rm authorizer < file`