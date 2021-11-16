# Autorizador

## 1. Decisões Técnicas e Arquiteturais

Tentei manter um equilíbrio entre um projeto simples e a capacidade de extensão, buscando inspiração de 
conceitos da `Clean Architecture` e da `Hexagonal Architecture` e aplicação dos princípios `SOLID`.

### 1.1 - Organização em Pacotes

Decidi por seguir por uma separação de pacotes semelhante as APIs e bibliotecas do Java para tirar melhor proveito 
dos recursos de encapsulamento, evitando subpacotes que tornassem o agrupamento mais organizado mas que compretesse 
o encapsulamento já que, no Java, não existe uma hirarquia entre pacotes e subpacotes.    

### 1.2 - Controllers x Presenters 

`AuthorizerApplication` é a classe `main` do projeto Java, onde nela são inicializadas as configurações iniciais e o 
fluxo é entregue para a classe `TerminalAuthorizerExecutor`.

`TerminalAuthorizerExecutor` é uma classe de camada mais externa, responsável por lidar com o terminal. Ela faz a 
leitura e escrita, com ajuda do `ObjectMapper`, que fica responsável pelas traduções de objetos em JSON e vice-versa.

Note que as abstrações no uso de `Scanner` como input e `PrintStream` como output permitem que a leitura e escrita 
possam ser facilmente substituídas por outras implementações como, por exemplo, leitura e escrita em arquivos.

Caso as linhas 23 e 24 da classe `AuthorizerApplication` fossem substituídas por

```
Scanner input = new Scanner(new File(inputFileName));
PrintStream output = new PrintStream(outputFileName);
```

Teríamos uma aplicação com exato mesmo comportamento, mas lendo input de um arquivo com nome `inputFileName` e 
escrevendo em um arquivo de nome `outputFileName`. Nenhuma outra alteração seria necessária.

### 1.3 - Core

A partir da classe `AuthorizerProcessor` temos o que podemos chamar da camada `application` ou `core`. A partir 
dessa camada não temos mais dependências externas, apenas lógicas da aplicação e negócio.

O `AuthorizerProcessor` recebe uma dependência do tipo `AccountRepository`, a qual é uma abstração de um repositório 
de contas, tendo como implementação a classe `AccountRepositoryImpl` que trabalha como um repositório em memória. 
Essa decisão se baseia na oportunidade de uma futura alteração da origem da informações de conta e também para 
isolar a implementação de um repositório que seria considerado um `adapter` ou `gateway` (`Dependency Inversion 
Principle`).

### 1.3.1 - Operações e Strategy Pattern

O `AuthorizerProcessor` trabalha com uma classe `StrategySelector`, a qual é responsável por auxiliar na aplicação 
de um `Strategy Pattern`. Hoje temos dois tipos de operações: criação de conta e criação de transação, porém, com o 
uso desse pattern, facilitamos a extensão da aplicação para novas operações, seguindo os principios `Single 
Responsiblity Principle` e `Open-Closed Principle`.

### 1.3.2 - Violações e Decorator Pattern

As classes `AccountCreationStrategy` e `TransactionCreationStrategy` trabalham com as regras específicas para cada 
tipo de operação (Criação de Conta e Criação de Transação). Como as regras se acumulam uma sobre as outras, foi 
implementando um `Decorator Pattern`, novamente pensando em solucionar o problema atual, de forma simples, mas 
mantendo a possibilidade de extensão, adicionando novas regras com o mínimo de impacto possísvel no código (`Single
Responsiblity Principle` e `Open-Closed Principle`).

### 1.3.3 - Account e Transaction

As classes `Acccount` e `Transaction` são as mais internas da aplicação e tentei mantê-las o mais isoladas e 
encapsuladas possível, de forma que não dependem de nenhuma outra classe e é sempre evitado retorná-las ou 
recebe-las de camada mais externas, sempre preferindo uma tranformação em objetos (`Requests` e `Results`).

### 1.4 Metodologia

A metodologia de desenvolvimento utilizada foi inspirada no TDD, onde os testes eram escritos antes da implementação,
sempre iniciando em uma camada mais externa (do core). Os exemplos de input e output esperados pelo desafio foram de 
grande ajuda nesse sentido.

Após implementação de todas as regras do projeto, este pode ser refatorado, separando-o em camadas e aplicando padrões 
de projeto sem que houvesse qualquer preocupação com o funcionamento da aplicação, já que os testes garantiam que o 
correto funcionamento estava sendo mantido. 

### 1.5 Testes Automatizados

Decidi por dois tipos de testes, que chamarei de "unitários" e "integrados".

Os testes unitários estão na classe `AuthorizerProcessorTest` e estão no nível `core` da aplicação, ou seja, sem 
considerar as camadas externas.

Já a classe `AuthorizerApplicationTests` faz os testes "integrados", simulando o input a partir de arquivos e 
validando o resultado contra um arquivo de output esperado, os quais se encontram na diretório `src/test/resources`.

### 1.6 Performance

Houveram três pontos de atenção em relação à performance da aplicação:

- Lista de transações: embora a lista seja referenciada por sua abstração, decidiu-se por inicializá-la como uma 
  `LinkedList` (lista duplamenta ligada) pensado nas operações de inserções nas extremidades, que são extremamente 
  eficientes nesse tipo de implementação. Como a maioria da leitura é feita pelas pontas, a perda de performance 
  nesse sentido deve ser mínima. Tendo uma maior amostra de dados poderíamos fazer testes alterando a linha 18 da 
  classe `Account`
- Durante a verificação da regra de alta frequência num curto intervalo (método `isHighFrequencySmallInterval` da 
  classe `Account`) dado que sabemos que as transações serão processadas em ordem cronológica, decidiu-se por 
  otimizar a busca olhando apenas os últimos três elementos da lista de transações.
- Durante a verificação de transações duplicadas (método `isDoubledTransaction` da classe `Account`) foi processada 
  a lista inteira para validação da regra, o que pode apresentar um problema de performance para um grande volume de 
  dados. Optou-se por manter a simplicidade da implementação mas algumas alternativas envolvem o uso de `Map` do 
  tipo `HashMap` onde os objetos `Transaction` seriam duplicados para buscas mais eficientes. Poderia-se usar como 
  chave desse `Map` o merchant, merchant e data, merchant e valor ou merchant, valor e data, dependendo do padrão de 
  entrada esperado.

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
  é necessário para que os testes sejam rodados durante o ciclo de compilação da aplicação. É utilizado apenas para 
  a etapa de testes da aplicação.
- `maven-assembly-plugin`: esse plugin do Maven nos ajuda a criarmos um arquivo `jar` independente e executável. Com 
  eles conseguimos definir a `main class` a ser executada, adicionamos as dependências dentro do pacote e renomeados 
  esse pacote para sua versão final, chamada de `authorizer.jar`. É utilizado apenas na etapa `package` (criação de 
  pacotes) da aplicação.

## 3. Compilação & Execução

O projeto está preparado para ser compilado e executado de duas formas diferentes: com uso do JDK e Maven ou 
utilizando apenas Docker. 

### 3.1 - JDK

Caso tenha o JDK 11+ instalado, você pode fazer a compilação do projeto usando Maven. Dessa forma, um arquivo `jar` 
será gerado para execução.

#### 3.1.1 - Compilação com Maven

Caso tenha o Maven 3.8.3 instalado, você pode rodar o seguinte comando, na raiz desse projeto, para compilar o projeto:

`mvn clean package -s settings.xml`

A opção `-s settings.xml` serve para utilizar as configurações de repositório do projeto (padrões) e evitar 
conflitos com configurações que você já possa ter.

#### 3.1.2 - "No Maven? No problem"

Caso não tenha o Maven instalado, não tem problema: o projeto já vem com um Maven "embutido" (Maven Wrapper). Porém, 
ainda sim é necessário ter o JDK 11+ instalado para esse método. 

Basta rodar o seguinte comando na raiz desse projeto: 

`./mvnw clean package -s settings.xml`

A opção `-s settings.xml` serve para utilizar as configurações de repositório do projeto (padrões) e evitar
conflitos com configurações que você já possa ter.

#### 3.1.3 - Running

Se você compilou o projeto utilizando o JDK e Maven, você terá um arquivo `authorizer.jar` dentro do diretório 
`target` na raiz do projeto.  

Para executar o programa, basta rodar o seguinte comando, na raiz do projeto,:

`java -jar target/authorizer.jar < file`

Onde `file` é o nome completo do arquivo com as operações a serem processadas.

### 3.2 - Docker

Prefere Docker? Eu também. Por isso preparei um Dockerfile para facilitar a compilação e execução do projeto.

Dessa forma, não é preciso ter nem JDK nem Maven instalados. Apenas o Docker é necessário.

#### 3.2.1 - Compilação

Usando Docker basta rodar o comando abaixo na raiz do projeto, o qual irá criar uma imagem na sua máquina. Nessa 
imagem o projeto terá sido compilado e estará pronto para execução.

`docker image build -t authorizer .`

No comando anterior, estamos dando o nome da imagem de `authorizer`, mas você pode escolher o nome que preferir.

#### 3.2.2 - Running

Após realizar a compilação do projeto e criação da imagem, basta rodar o comando a seguir para executar o projeto.

`docker container run -i --rm authorizer < file`

Onde `file` é o nome completo do arquivo com as operações a serem processadas. 

Lembrando que, caso tenha dado um nome diferente de `authorizer`, você deve substituir aqui pelo nome que usou.