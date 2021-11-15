# Autorizador

## 1.

## 2. Frameworks e Bibliotecas

O projeto foi feito utilizando Java como linguagem de programação, JVM como plataforma e Maven como gerenciador de 
dependências e automação de compilação. Nenhum framework complexo foi necessário.

Porém, algumas bibliotecas foram incluídas:
- `org.codehaus.jackson.jackson-mapper-asl`: Única biblioteca utilizada em tempo de execução da aplicação. Essa 
  biblioteca foi utilizada para facilitar a leitura e escrita de JSON na aplicação. Utilizei ela não só pela 
  familiaridade mas também por ser uma biblioteca amplamente utilizada quando desenvolvemos em Java ou Kotlin. É uma 
  dependência compátivel com os módulos do Spring Framework, por exemplo.
- `org.junit.jupiter.junit-jupiter`: o JUnit é um framework de automatização de testes amplamente utilizadno nas 
  linguagens de programação Java e Kotlin.  É utilizado apenas na etapa de testes da aplicação.
- `org.assertj.assertj-core`: O assertj é uma biblioteca para escrevermos "declarações" mais fluentes em testes 
  automatizados. Enquanto com JUnit teríamos uma escrita de assert como `assertEquals(a, b)` com assertj 
  escrevemos `assertThat(a).isEqualsTo(b)`, tornando a escrita do testes muito mais natural. É utilizado apenas na 
  etapa de testes da aplicação.
- `maven-compiler-plugin`: esse é um plugin utilizado pelo Maven para executador a compilação do projeto. É 
  utilizado para a etapa de compilação da aplicação.
- `maven-surefire-plugin`: esse plugin é utilizado para adicionar o jUnit Jupiter no ciclo de vida do Maven, ou seja,
  é necessários para que os testes sejam rodados durante o ciclo de compilação da aplicação. É utilizado apenas para 
  a etapa de testes da aplicação.
- `maven-assembly-plugin`: esse plugin do Maven nos ajuda a criarmos um arquivo `jar` independente e executável. Com 
  eles conseguimos definir a `main class` a ser executada, adicionamos as dependências dentro do pacote e renomeados 
  esse pacote para sua versão final, chamada de `authorizer.jar`. É utilizado apenas na etapa `package` (criação de 
  pacotes) da aplicação.

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