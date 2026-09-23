# Catalog API — RESTful Service

![Java CI with Maven and Docker](https://github.com/lucaslsf11/catalog-api/actions/workflows/ci.yml/badge.svg)

API RESTful desenvolvida com **Java 21** e **Spring Boot 3**, estruturada sob o padrão de arquitetura em camadas (*Layered Architecture*) para gerenciamento de catálogo de produtos e categorias, contando com suíte completa de testes automatizados, pipeline de CI/CD e suporte a conteinerização.

---

## Tecnologias e Recursos

- **Java 21** (LTS) & **Spring Boot 3**
- **Spring Data JPA & Hibernate** (Persistência e Mapeamento Objeto-Relacional)
- **H2 In-Memory Database** (Banco relacional em memória com console web ativo para desenvolvimento)
- **PostgreSQL 16** (Banco relacional configurado para execução via container)
- **Bean Validation (Hibernate Validator)** (Validação declarativa de entrada de dados)
- **Java Records** (DTOs imutáveis para transferência de dados de alto desempenho)
- **SpringDoc OpenAPI / Swagger UI** (Documentação interativa e testes de endpoints)
- **Tratamento Global de Exceções** (`@RestControllerAdvice` com respostas semânticas padronizadas)
- **JUnit 5 & Mockito** (Testes unitários de regras de negócio e testes de integração de controladores com MockMvc)
- **Docker & Docker Compose** (Empacotamento multi-stage enxuto e orquestração de banco e API)
- **GitHub Actions** (Esteira de Integração Contínua automatizada para validação de build, testes e imagem Docker)
- **Lombok** (Produtividade e redução de código boilerplate)
- **Apache Maven** (Gerenciamento de dependências e build)

---

## Arquitetura em Camadas

O projeto segue separação de responsabilidades para manter alta coesão e baixo acoplamento:

```text
catalog-api/
├── .github/workflows/   # Pipeline de CI/CD automatizada (GitHub Actions)
├── src/
│   ├── main/java/com/portfolio/catalog/
│   │   ├── controller/  # Endpoints HTTP REST e serialização de respostas
│   │   ├── dto/         # Records de Request/Response e validações
│   │   ├── service/     # Regras de negócio, transações e orquestração
│   │   ├── model/       # Entidades de domínio JPA mapeadas para tabelas relacionais
│   │   ├── repository/  # Interfaces de persistência com Spring Data JPA
│   │   ├── exception/   # Exceções customizadas e interceptador global (@RestControllerAdvice)
│   │   └── config/      # Configurações do OpenAPI/Swagger e carga inicial (DataLoader)
│   └── test/java/com/portfolio/catalog/
│       ├── controller/  # Testes de integração HTTP para endpoints REST via MockMvc
│       └── service/     # Testes unitários com JUnit 5 e Mockito
├── Dockerfile           # Build multi-stage para geração de imagem runtime minimalista
└── docker-compose.yml   # Orquestração do PostgreSQL e Catalog API em containers
```

---

## Como Executar a Aplicação

### Pré-requisitos
- **JDK 21** instalado e configurado nas variáveis de ambiente (`JAVA_HOME`)
- **Git** instalado
- **Docker e Docker Compose** (opcional, caso queira executar o ambiente conteinerizado)
- **Maven** (opcional, pois o projeto já inclui o executável Maven Wrapper)

### 1. Clonar o repositório
```bash
git clone [https://github.com/lucaslsf11/catalog-api.git](https://github.com/lucaslsf11/catalog-api.git)
cd catalog-api
```

### 2. Opção A: Execução local (com H2 Database)
- **Windows (PowerShell / CMD):**
  ```powershell
  .\mvnw.cmd spring-boot:run
  ```
- **Linux / macOS:**
  ```bash
  ./mvnw spring-boot:run
  ```
- **Via IntelliJ IDEA:**
  Navegue até `src/main/java/com/portfolio/catalog/CatalogApiApplication.java` e clique no botão verde de **Run (Play)**.

A aplicação estará acessível em: `http://localhost:8080`

### 3. Opção B: Execução com Docker & PostgreSQL
Para subir a aplicação integrada com banco relacional PostgreSQL conteinerizado:
```bash
docker compose up --build -d
```
Para encerrar os containers:
```bash
docker compose down
```

---

## Testes Automatizados

O projeto conta com suíte de testes com **JUnit 5** e **Mockito**:
- **Camada de Serviço (`src/test/.../service`):** Testes unitários isolados para `CategoryService` e `ProductService`, cobrindo cenários de sucesso, regras de negócio e lançamentos de `ResourceNotFoundException`.
- **Camada de Controlador (`src/test/.../controller`):** Testes via `MockMvc` cobrindo status HTTP (`200`, `201`, `204`, `400`, `404`), serialização de JSON, validação de cabeçalhos de resposta e integração com o interceptador global.

### Executar todos os testes via terminal:
- **Windows:**
  ```powershell
  .\mvnw.cmd clean test
  ```
- **Linux / macOS:**
  ```bash
  ./mvnw clean test
  ```

---

## Documentação da API (Swagger UI)

Com a aplicação em execução, acesse a documentação interativa para consultar esquemas e disparar requisições em tempo real:

**[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)**

### Console do Banco H2 (Modo Local)
- **URL de acesso:** `http://localhost:8080/h2-console`
- **JDBC URL:** `jdbc:h2:mem:catalogdb`
- **User:** `sa`
- **Password:** *(deixar em branco)*

---

## Endpoints Principais

### Categorias (`/api/v1/categories`)
| Método | Endpoint | Descrição | Status Sucesso |
|---|---|---|---|
| `GET` | `/api/v1/categories` | Lista todas as categorias cadastradas | `200 OK` |
| `GET` | `/api/v1/categories/{id}` | Busca os detalhes de uma categoria por ID | `200 OK` |
| `POST` | `/api/v1/categories` | Cadastra uma nova categoria | `201 Created` |
| `PUT` | `/api/v1/categories/{id}` | Atualiza uma categoria existente | `200 OK` |
| `DELETE` | `/api/v1/categories/{id}` | Remove uma categoria | `204 No Content` |

### Produtos (`/api/v1/products`)
| Método | Endpoint | Descrição | Status Sucesso |
|---|---|---|---|
| `GET` | `/api/v1/products` | Lista produtos paginados (`page`, `size`, `sort`) | `200 OK` |
| `GET` | `/api/v1/products/{id}` | Busca os detalhes de um produto por ID | `200 OK` |
| `GET` | `/api/v1/products/category/{categoryId}` | Lista produtos filtrados por categoria | `200 OK` |
| `POST` | `/api/v1/products` | Cadastra um novo produto | `201 Created` |
| `PUT` | `/api/v1/products/{id}` | Atualiza os dados de um produto | `200 OK` |
| `DELETE` | `/api/v1/products/{id}` | Remove um produto | `204 No Content` |