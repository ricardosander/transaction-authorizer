FROM openjdk

RUN mkdir /home/authorizer

COPY . /home/authorizer

WORKDIR /home/authorizer

RUN ./mvnw clean package -s settings.xml

CMD java -jar target/authorizer.jar