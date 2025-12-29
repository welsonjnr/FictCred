# FictCred

## Descrição
FictCred é uma aplicação Spring Boot desenvolvida para gerenciar clientes e propostas de crédito. Permite operações CRUD para clientes e criação/listagem de propostas de crédito associadas a clientes específicos.

## Tecnologias Utilizadas
- **Java 21**
- **Spring Boot 3.5.9**
- **Maven** (para gerenciamento de dependências e build)
- **Banco de Dados**: H2 (desenvolvimento) e PostgreSQL (produção)
- **Documentação da API**: OpenAPI/Swagger
- **Containerização**: Docker e Docker Compose

## Pré-requisitos
Antes de executar a aplicação, certifique-se de ter instalado:
- **Java 21** ou superior
- **Maven 3.6+** (ou use o wrapper Maven incluído no projeto)
- **Docker** e **Docker Compose** (opcional, para execução com PostgreSQL)

## Como Executar a Aplicação Localmente

### Opção 1: Execução Simples com Maven (Banco H2 em Memória)
Esta opção é ideal para desenvolvimento rápido, utilizando o banco H2 em memória.

1. **Clone o repositório** (se aplicável) e navegue até a pasta do projeto.

2. **Execute a aplicação**:
   ```bash
   ./mvnw spring-boot:run
   ```
   Ou, se preferir usar Maven diretamente:
   ```bash
   mvn spring-boot:run
   ```

3. **Verifique se a aplicação está rodando**:
   - Acesse: http://localhost:8080
   - A aplicação estará disponível na porta 8080.

### Opção 2: Execução com Docker Compose (Banco PostgreSQL)
Esta opção utiliza Docker Compose para subir a aplicação junto com um banco PostgreSQL.

1. **Certifique-se de que o Docker e Docker Compose estão instalados**.

2. **Execute o comando**:
   ```bash
   docker-compose up --build
   ```

3. **Aguarde a inicialização**:
   - O Docker Compose irá construir a imagem da aplicação e iniciar os containers.
   - O banco PostgreSQL será iniciado primeiro, e a aplicação aguardará até que o banco esteja saudável.

4. **Verifique se a aplicação está rodando**:
   - Acesse: http://localhost:8080
   - O banco PostgreSQL estará disponível na porta 5432 do host.

### Parar a Aplicação
- Para parar a execução com Docker Compose:
  ```bash
  docker-compose down
  ```

## Documentação da API
A aplicação utiliza OpenAPI/Swagger para documentação interativa da API.

- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

Na interface do Swagger, você pode visualizar todos os endpoints disponíveis, testar as requisições e ver os esquemas de dados.

## Como Fazer Requisições com Postman

### Importando a Collection
1. **Abra o Postman**.

2. **Importe a collection**:
   - Clique em "Import" no canto superior esquerdo.
   - Selecione "File" e escolha o arquivo `FictCred_Postman_Collection.json` localizado na raiz do projeto.
   - A collection "FictCred API Collection" será importada com todas as requisições pré-configuradas.

3. **Configure as variáveis**:
   - Na collection, há variáveis pré-definidas:
     - `baseUrl`: http://localhost:8080 (já configurada)
     - `clienteId`: Será preenchida automaticamente após criar um cliente
     - `propostaId`: Será preenchida automaticamente após criar uma proposta

### Estrutura da Collection
A collection está organizada em duas pastas principais:

#### 1. Cliente
- **Criar Cliente** (POST): Cria um novo cliente. Exemplo de body:
  ```json
  {
    "nome": "João Silva",
    "cpf": "12345678901",
    "rendaMensal": 5000.00
  }
  ```
  - Após a criação, o `clienteId` é automaticamente salvo para uso nas outras requisições.

- **Buscar Cliente por ID** (GET): Busca um cliente específico usando o ID salvo.

- **Listar Todos os Clientes** (GET): Lista todos os clientes cadastrados.

- **Atualizar Cliente** (PUT): Atualiza os dados de um cliente existente.

- **Remover Cliente** (DELETE): Remove um cliente do sistema.

#### 2. Proposta de Crédito
- **Criar Proposta de Crédito** (POST): Cria uma nova proposta para um cliente. Exemplo de body:
  ```json
  {
    "valorSolicitado": 10000.00,
    "numeroParcelas": 12
  }
  ```
  - Usa o `clienteId` salvo anteriormente.
  - Após a criação, o `propostaId` é automaticamente salvo.

- **Buscar Proposta por ID** (GET): Busca uma proposta específica usando o ID salvo.

- **Listar Propostas por Cliente** (GET): Lista todas as propostas de um cliente específico.

### Como Usar a Collection
1. **Inicie sempre criando um cliente** para obter um `clienteId`.

2. **Execute as operações de cliente** conforme necessário.

3. **Crie propostas de crédito** associadas ao cliente criado.

4. **Teste as operações de proposta** usando os IDs salvos automaticamente.

### Dicas para Uso do Postman
- As variáveis `clienteId` e `propostaId` são atualizadas automaticamente pelos scripts de teste nas requisições de criação.
- Verifique o console do Postman para ver logs de criação de IDs.
- Todas as requisições usam o Content-Type `application/json` quando necessário.
- A base URL está configurada como `http://localhost:8080`, mas pode ser alterada se necessário.

## Endpoints da API
A API segue o padrão REST e está versionada (v1). Base URL: `http://localhost:8080/fictcred/v1/api/`

### Cliente
- `POST /cliente` - Criar cliente
- `GET /cliente` - Listar todos os clientes
- `GET /cliente/{id}` - Buscar cliente por ID
- `PUT /cliente/{id}` - Atualizar cliente
- `DELETE /cliente/{id}` - Remover cliente

### Proposta de Crédito
- `POST /proposta-cliente/{clienteId}` - Criar proposta para um cliente
- `GET /proposta-cliente/{id}` - Buscar proposta por ID
- `GET /proposta-cliente/cliente/{clienteId}` - Listar propostas de um cliente

## Configurações de Banco de Dados
- **Desenvolvimento (padrão)**: H2 em memória (dados não persistidos)
- **Produção (Docker Compose)**: PostgreSQL em container

As configurações podem ser alteradas nos arquivos `application.properties`, `application-dev.properties` e `application-prod.properties`.

## Suporte
Para dúvidas ou problemas, consulte a documentação da API via Swagger ou verifique os logs da aplicação.
