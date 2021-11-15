# Autorizador

## 1.

## 2.

## 3. Compilação & Execução

O projeto está preparado para ser compilado e executado de duas formas diferentes: com uso do JDK e Maven ou 
utilizando apenas Docker. 

### JDK

Caso tenha o JDK 11+ instalado, você pode fazer a compilação do projeto usando Maven. Dessa forma, um arquivo `jar 
será gerado para execução.

#### Compilação com Maven

Caso tenha o Maven 3.8.3 instalado, você pode rodar o seguinte comando, na raiz desse projeto, para compilar o projeto:

`mvn clean package`

#### "No Maven? No problem"

Caso não tenha o Maven instalado, não tem problema, o projeto já vem com um Maven "embutido" (Maven Wrapper). Porém, 
ainda sim é necessário ter o JDK 11+ instalado para esse método. 

Basta rodar o seguinte comando na raiz desse projeto: 

`./mvnw clean package`

#### Running

Se você compilou o projeto utilizando o JDK e Maven, você terá um arquivo `authorizer.java` dentro do diretório 
`target` na raiz do projeto.  

Para executar o programa, basta rodar o seguinte comando, na raiz do projeto,:

`java -jar target/authorizer.java < file`

Onde `file` é o nome completo do arquivo com as operações a serem processadas.

### Docker

Prefere Docker? Eu também. Por isso preparei um Dockerfile para facilitar a compilação e execução do projeto.

Dessa forma, não é preciso ter nem JDK nem Maven instalados. Apenas o Docker é necessário.

#### Compilação

Usando Docker basta rodar o comando abaixo na raiz do projeto, o qual irá criar uma imagem na sua máquina. Nessa 
imagem o projeto terá sido compilado e estará pronto para execução.

`docker image build -t authorizer .`

No comando a cima, estamos dando o nome da imagem de `authorizer`, mas você pode escolher o nome que preferir.

#### Running

Após realizar a compilação do projeto e criação da imagem, basta rodar o comando a seguir para executar o projeto.

`docker container run -i --rm authorizer < file`

Onde `file` é o nome completo do arquivo com as operações a serem processadas. 

Lembrando que, caso tenha dado um nome diferente de `authorizer`, você deve substituir aqui pelo nome que usou.

## 4.